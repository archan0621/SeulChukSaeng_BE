package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.annotation.AdminAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventAttendForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventMemberAddForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventMemberListForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventAttendResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberAddResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberListResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.AlreadyMemberInEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.AlreadyAttendException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.NoEventMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import kr.co.seulchuksaeng.seulchuksaengweb.service.EventMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventMemberController {

    private final EventMemberService eventMemberService;

    @AdminAuthorize
    @PostMapping("/memberNoList") //SS303 해당 경기에 참여하지 않는 인원을 조회한다.
    public EventMemberListResult memberNoList(@RequestBody EventMemberListForm form, @AuthenticationPrincipal User user) {
        try {
            ArrayList<EventMemberListInnerResult> innerResults = eventMemberService.notPlayingMemberList(form.getEventId());
            log.info("경기 미참여 인원을 조회하였습니다 : {}, {}", user.getUsername(), form.getEventId());
            return new EventMemberListResult("success", "경기 미참여 인원을 조회하였습니다.", innerResults, innerResults.size());
        } catch (NoEventException e) {
            log.info("경기 미참여 인원 조회에 실패하였습니다 : {}, {}", user.getUsername(), form.getEventId());
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @UserAuthorize
    @PostMapping("/memberList")
    public EventMemberListResult memberList(@RequestBody EventMemberListForm form, @AuthenticationPrincipal User user) { //SS301 해당 경기에 참여 인원 조회
        try {
            List<Member> members = eventMemberService.playingMemberList(form.getEventId());
            ArrayList<EventMemberListInnerResult> innerResult = new ArrayList<>();
            for (Member member : members) {
                innerResult.add(new EventMemberListInnerResult(member.getId(), member.getName()));
            }
            log.info("경기 참여 인원을 조회하였습니다 : {}, {}", user.getUsername(), form.getEventId());
            return new EventMemberListResult("success", "경기 참여 인원을 조회하였습니다.", innerResult, innerResult.size());
        } catch (NoEventException e) {
            log.info("경기 참여 인원 조회에 실패하였습니다 : {}, {}, {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @PostMapping("/memberAdd")
    public EventMemberAddResult memberAdd(@RequestBody EventMemberAddForm form, @AuthenticationPrincipal User user) { //SS302 해당 경기에 참여 인원 추가.
        try {
            eventMemberService.addMember(form.getEventId(), form.getMemberId());
            log.info("경기에 회원을 추가하였습니다 : {}, {}, {}", user.getUsername(), form.getEventId(), form.getMemberId());
            return new EventMemberAddResult("success", "회원을 경기에 추가하였습니다.");
        } catch (AlreadyMemberInEventException | UserNotFoundException | NoEventException e) {
            log.info("경기에 회원 추가에 실패하였습니다 : {}, {}, {}, {}", user.getUsername(), form.getEventId(), form.getMemberId(), e.getMessage());
            return new EventMemberAddResult("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @PostMapping("/memberRemove") //SS304 해당 경기에 참여 인원 삭제
    public EventMemberAddResult memberRemove(@RequestBody EventMemberAddForm form, @AuthenticationPrincipal User user) {
        try {
            eventMemberService.removeMember(form.getEventId(), form.getMemberId());
            log.info("경기에서 회원을 삭제하였습니다 : {}, {}, {}", user.getUsername(), form.getEventId(), form.getMemberId());
            return new EventMemberAddResult("success", "회원을 경기에서 삭제하였습니다.");
        } catch (UserNotFoundException | NoEventException e) {
            log.info("경기에서 회원 삭제에 실패하였습니다 : {}, {}, {}, {}", user.getUsername(), form.getEventId(), form.getMemberId(), e.getMessage());
            return new EventMemberAddResult("fail", e.getMessage());
        }
    }

    @UserAuthorize
    @PostMapping("/memberAttend")
    public EventAttendResult memberAttend(@RequestBody EventAttendForm form, @AuthenticationPrincipal User user) { //SS305 해당 경기에 참여 인원 출석
        try {
            eventMemberService.attendMember(form.getEventId(), user.getUsername());
            log.info("경기에 회원을 출석처리하였습니다 : {}, {}", user.getUsername(), form.getEventId());
            return new EventAttendResult("success", "회원을 경기에 출석처리하였습니다.");
        } catch (UserNotFoundException | NoEventException | AlreadyAttendException | NoEventMemberException e) {
            log.info("경기에 회원 출석처리에 실패하였습니다 : {}, {}, {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventAttendResult("fail", e.getMessage());
        }
    }

}