package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.*;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.MemberForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberDetailInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.ExistMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UnverifiedJoinException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.WrongPasswordException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventMemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.security.Crypto;
import kr.co.seulchuksaeng.seulchuksaengweb.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final EventMemberRepository eventMemberRepository;
    private final Crypto crypto;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long join(MemberForm.Join joinForm) {
        //중복 회원 검증
        validateDuplicateMember(joinForm);

        //비밀번호 암호화
        String salt = crypto.getSalt();
        String saltPassword = crypto.saltPassword(joinForm.password(), salt);

        // 암호화한 정보로 회원 저장
        Member member = new Member(joinForm.id(), saltPassword, joinForm.name(), joinForm.phone(), joinForm.gender(), UserRole.USER, salt, 0);
        memberRepository.save(member);

        return member.getMemberId();
    }

    public String login(MemberForm.Login loginForm) {
        try {
            //회원 존재 여부 확인
            Member member = memberRepository.findMemberById(loginForm.loginId());

            //회원이 있다면 비밀번호 암호화
            String saltPassword = crypto.saltPassword(loginForm.password(), member.getSalt());

            //비밀번호 일치 여부 확인
            if(!Objects.equals(saltPassword, member.getPassword())) {
                throw new WrongPasswordException();
            }

            // 회원 존재 및 비밀번호 일치 확인시 JWT 토큰 발급
            return tokenProvider.createToken(String.format("%s:%s", member.getId(), member.getRole()));
        } catch (IndexOutOfBoundsException e) {
            throw new UserNotFoundException();
        }
    }

    public List<MemberListInnerResult> getMemberList() {
        return memberRepository.findAllMember()
                .stream()
                .map(result -> new MemberListInnerResult(
                        result.getMemberId(),
                        result.getName(),
                        result.getGender(),
                        result.getPhone(),
                        result.getWarnPoint()
                ))
                .collect(Collectors.toList());
    }

    public MemberDetailInnerResult.MemberInfo getMemberDetail(Member member) {
        return new MemberDetailInnerResult.MemberInfo(member.getMemberId(), member.getName(), member.getGender(), member.getPhone(), member.getWarnPoint());
    }

    public MemberDetailInnerResult.rate getMemberRate(Member member) {
        List<Event> eventList = eventRepository.findEventList(member.getGender());
        List<MemberEvent> memberJoinedEvent = eventMemberRepository.getMemberJoinedEvent(member);
        Map<Attendance, Long> attendCounting = memberJoinedEvent.stream().collect(Collectors.groupingBy(MemberEvent::getAttend, Collectors.counting()));

        return new MemberDetailInnerResult.rate(eventList.size(), memberJoinedEvent.size(), attendCounting.get(Attendance.ATTEND), attendCounting.get(Attendance.LATE), attendCounting.get(Attendance.ABSENT));
    }

    @Transactional
    public List<MemberDetailInnerResult.joinedGame> getMemberJoinedGame(Member member) {
         return eventMemberRepository.getMemberJoinedEvent(member).stream().map(result -> new MemberDetailInnerResult.joinedGame(result.getEvent().getEventId(), result.getEvent().getTitle())).collect(Collectors.toList());
    }

    private void validateDuplicateMember(MemberForm.Join joinForm) {
        //EXCEPTION
        try {
            Member member = memberRepository.findMemberById(joinForm.id());
            throw new ExistMemberException();
        } catch (UserNotFoundException ignored) { } //UserNotFoundException이 정상인 상황
    }

    public void verifiedMemberJoin(MemberForm.Join joinForm, String verifyCode) {
                //회원 인증코드 검증
                String verifyCodeUser = joinForm.verifyCode();
        if (!Objects.equals(verifyCode, verifyCodeUser)) {
            log.info("현재 인증코드 : {}, 입력받은 인증코드 : {} - 회원가입 거부", verifyCode, verifyCodeUser);
            throw new UnverifiedJoinException();
        }
        log.info("현재 인증코드 : {}, 입력받은 인증코드 : {} - 회원가입 승인", verifyCode, verifyCodeUser);
    }
}
