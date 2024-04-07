package thisisexercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thisisexercise.exercise.domain.Member;
import thisisexercise.exercise.domain.MemberRole;
import thisisexercise.exercise.domain.Role;
import thisisexercise.exercise.handler.CustomExceptionCode;
import thisisexercise.exercise.handler.CustomValidationException;
import thisisexercise.exercise.repository.MemberRepository;
import thisisexercise.exercise.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    public boolean checkId(String email) {

        return memberRepository.existsByEmail(email);

    }

    @Transactional
    public Member save(Member member) {
        Role roleUser = roleRepository.findByRole("ROLE_USER").orElseThrow(() -> new CustomValidationException(CustomExceptionCode.LOGIN_FAIL));

        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setRole(roleUser);

        member.addMemberRole(memberRole);
        return memberRepository.save(member);

    }

    public Optional<Member> findByEmail(String memberEmail) {
        return memberRepository.findByEmail(memberEmail);
    }
}
