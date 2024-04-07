package thisisexercise.exercise.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thisisexercise.exercise.domain.Member;
import thisisexercise.exercise.domain.MemberRole;
import thisisexercise.exercise.domain.Role;
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
        Optional<Role> roleOpt = roleRepository.findByRole("ROLE_USER");
        Role role = roleOpt.get();

        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setRole(role);

        member.addMemberRole(memberRole);
        return memberRepository.save(member);

    }

    public Optional<Member> findByEmail(String memberEmail) {
        return memberRepository.findByEmail(memberEmail);
    }
}
