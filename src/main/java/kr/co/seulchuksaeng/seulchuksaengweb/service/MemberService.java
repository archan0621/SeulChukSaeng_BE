package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(JoinForm joinForm) {
        validateDuplicateMember(joinForm);
        Member member = new Member(joinForm.getId(), joinForm.getPassword(), joinForm.getName(), joinForm.getPhone(), joinForm.getGender(), UserRole.USER);
        memberRepository.save(member);
        return member.getMemberId();
    }

    private void validateDuplicateMember(JoinForm joinForm) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(joinForm.getId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
