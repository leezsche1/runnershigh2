package thisisexercise.exercise.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thisisexercise.exercise.domain.Agree;
import thisisexercise.exercise.domain.Member;
import thisisexercise.exercise.domain.MemberRole;
import thisisexercise.exercise.dto.*;
import thisisexercise.exercise.handler.CommonResDTO;
import thisisexercise.exercise.handler.CustomExceptionCode;
import thisisexercise.exercise.handler.CustomValidationException;
import thisisexercise.exercise.handler.CustomValidationException2;
import thisisexercise.exercise.security.util.JwtTokenizer;
import thisisexercise.exercise.service.MemberService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberService memberService;
    private final JwtTokenizer jwtTokenizer;
    private final RedisTemplate redisTemplate;
    private final DefaultMessageService defaultMessageService;

    private final static Duration contactDuration = Duration.ofMinutes(3);
    private final static Duration refreshDuration = Duration.ofDays(7);


    @GetMapping("/")
    public String home(){
        return "hi";
    }

    @PostMapping("/idCheck")
    public ResponseEntity idCheck(@RequestBody @Valid MemberIdCheckDTO memberIdCheckDTO, BindingResult bindingResult) throws CustomValidationException {

        if (memberService.checkId(memberIdCheckDTO.getEmail())) {


            return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "사용가능합니다.", null));
        } else {
            throw new CustomValidationException(CustomExceptionCode.EXISTING_ID);
        }
    }


    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberJoinDTO memberJoinDTO, BindingResult result) throws CustomValidationException {

        if (result.hasErrors()) {
            throw new CustomValidationException(CustomExceptionCode.NULL_FAIL);
        }

        if (memberService.checkId(memberJoinDTO.getMemberEmail())) {

            throw new CustomValidationException(CustomExceptionCode.EXISTING_ID);
        }
        AgreeDTO agreeDTO = memberJoinDTO.getAgreeDTO();

        Agree agree = new Agree(agreeDTO.getFirstSelAgree(), agreeDTO.getSecondSelAgree(), agreeDTO.getThirdSelAgree());

        Member member = new Member();
        member.setEmail(memberJoinDTO.getMemberEmail());
        member.setName(memberJoinDTO.getMemberNm());
        member.setMemberPwd(bCryptPasswordEncoder.encode(memberJoinDTO.getMemberPwd()));
        member.setMemberPhone(memberJoinDTO.getMemberPhone());
        member.setBirth(memberJoinDTO.getBirth());
        member.setAgree(agree);


        Member save = memberService.save(member);

        List<MemberRole> memberRoles = save.getMemberRoles();
        String role = "";
        for (MemberRole memberRole : memberRoles) {
            String roleName = memberRole.getRoleName();
            role += roleName;
        }
        MemberJoinResDTO memberJoinResDTO =
                new MemberJoinResDTO(save.getId(), save.getEmail(), save.getMemberPwd(), role, save.getMemberPhone());
        CommonResDTO<MemberJoinResDTO> commonResDTO = new CommonResDTO<>("1", "회원가입 성공", memberJoinResDTO);
        return ResponseEntity.status(HttpStatus.OK).body(commonResDTO);

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDTO memberLoginDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new CustomValidationException(CustomExceptionCode.NULL_FAIL);
        }
        Optional<Member> findMember = memberService.findByEmail(memberLoginDTO.getMemberEmail());
        if (findMember.isEmpty()) {
            throw new CustomValidationException(CustomExceptionCode.LOGIN_FAIL);
        }
        Member member = findMember.get();

        if (!bCryptPasswordEncoder.matches(memberLoginDTO.getMemberPwd(), member.getMemberPwd())) {
            throw new CustomValidationException(CustomExceptionCode.LOGIN_FAIL);
        }

        List<String> roles = member.getMemberRoles().stream().map(MemberRole::getRoleName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getEmail(), member.getName(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getEmail(), member.getName(), roles);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(member.getId() + member.getEmail(), refreshToken, refreshDuration);

        MemberLoginResponseDTO memberLoginResponseDTO = new MemberLoginResponseDTO();
        memberLoginResponseDTO.setAccessToken(accessToken);
        memberLoginResponseDTO.setRefreshToken(refreshToken);
        memberLoginResponseDTO.setMemberEmail(member.getEmail());
        memberLoginResponseDTO.setId(member.getId());
        memberLoginResponseDTO.setName(member.getName());

        CommonResDTO commonResDTO = new CommonResDTO<>("1", "로그인 성공", memberLoginResponseDTO);

        return ResponseEntity.status(HttpStatus.OK).body(commonResDTO);

    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody @Valid MemberLogoutDTO memberLogoutDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(CustomExceptionCode.NULL_FAIL);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String savedToken = (String) valueOperations.get(memberLogoutDTO.getMemberId() + memberLogoutDTO.getMemberEmail());
        if (savedToken.equals(memberLogoutDTO.getRefreshToken())) {
            redisTemplate.delete(memberLogoutDTO.getMemberId() + memberLogoutDTO.getMemberEmail());
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO<>("1", "로그아웃 성공", null));
        } else {
            throw new CustomValidationException(CustomExceptionCode.REFRESH_FAIL);
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String savedToken = (String) valueOperations.get(refreshTokenDTO.getMemberId() + refreshTokenDTO.getMemberEmail());
        if (savedToken.equals(refreshTokenDTO.getRefreshToken())) {

            Claims claims = jwtTokenizer.parseRefreshToken(refreshTokenDTO.getRefreshToken());
            Long userId = Long.valueOf((Integer) claims.get("userId"));
            List roles = (List) claims.get("roles");
            String email = claims.getSubject();
            String name = (String)claims.get("name");

            String accessToken = jwtTokenizer.createAccessToken(userId, email, name, roles);

            MemberLoginResponseDTO memberLoginResponseDTO = new MemberLoginResponseDTO();
            memberLoginResponseDTO.setAccessToken(accessToken);
            memberLoginResponseDTO.setId(userId);
            memberLoginResponseDTO.setMemberEmail(email);
            memberLoginResponseDTO.setName(name);
            memberLoginResponseDTO.setRefreshToken(refreshTokenDTO.getRefreshToken());

            CommonResDTO<MemberLoginResponseDTO> commonResDTO = new CommonResDTO<>("1", "refreshToken 재발급 성공", memberLoginResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(commonResDTO);

        } else {
            throw new CustomValidationException(CustomExceptionCode.REFRESH_FAIL);
        }


    }


    @PostMapping("/auth/sms")
    public ResponseEntity phoneP(@RequestBody @Valid PhoneNumberDTO number, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(CustomExceptionCode.PHONE_NUMBER_FAIL);
        }

        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        String phoneNumber = number.getPhoneNumber();
//        int numbering = Integer.parseInt(phoneNumber);


        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01065512471");
        message.setTo(phoneNumber);
        message.setText("다음 인증번호를 입력해주세요" + numStr);

        SingleMessageSentResponse response = defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(phoneNumber, numStr, contactDuration);

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO("1", "인증번호 발급 성공", null));
    }

    @PostMapping("/auth/smsCheck")
    public ResponseEntity smsCheck(@RequestBody @Valid SmsCheckDTO smsCheckDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(CustomExceptionCode.AUTH_NUMBER_FAIL);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();

        String authNumber1 = (String) valueOperations.get(smsCheckDTO.getPhoneNumber());
        if (authNumber1.equals(smsCheckDTO.getAuthNumber())) {

            return ResponseEntity.status(HttpStatus.OK).body(new CommonResDTO("1", " 인증번호 일치", null));
        } else {
            throw new CustomValidationException(CustomExceptionCode.AUTH_NUMBER_FAIL);
        }

    }




}
