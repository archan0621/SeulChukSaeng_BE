package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.dto.form.JoinForm;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

    public void verifiedMemberJoin(JoinForm joinForm, String verifyCode) {
        String verifyCodeUser = joinForm.getVerifyCode();
        if (!Objects.equals(verifyCode, verifyCodeUser)) {
            throw new IllegalStateException("인증된 회원이 아닙니다. 회장에게 회원코드를 요청해주세요.");
        }
    }
}
