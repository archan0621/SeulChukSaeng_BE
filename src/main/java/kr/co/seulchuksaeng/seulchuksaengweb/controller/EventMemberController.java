package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.annotation.AdminAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventMemberAddForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventMemberListForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberAddResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberListResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.AlreadyMemberInEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import kr.co.seulchuksaeng.seulchuksaengweb.service.EventMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public EventMemberListResult memberNoList(@RequestBody EventMemberListForm form) {
        try {
            ArrayList<EventMemberListInnerResult> innerResults = eventMemberService.notPlayingMemberList(form.getEventId());
            return new EventMemberListResult("success", "경기 미참여 인원을 조회하였습니다.", innerResults, innerResults.size());
        } catch (NoEventException e) {
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @PostMapping("/memberList")
    public EventMemberListResult memberList(@RequestBody EventMemberListForm form) { //SS301 해당 경기에 참여 인원 조회
        try {
            List<Member> members = eventMemberService.playingMemberList(form.getEventId());
            ArrayList<EventMemberListInnerResult> innerResult = new ArrayList<>();
            for (Member member : members) {
                innerResult.add(new EventMemberListInnerResult(member.getId(), member.getName()));
            }
            return new EventMemberListResult("success", "경기 참여 인원을 조회하였습니다.", innerResult, innerResult.size());
        } catch (NoEventException e) {
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @PostMapping("/memberAdd")
    public EventMemberAddResult memberAdd(@RequestBody EventMemberAddForm form) { //SS302 해당 경기에 참여 인원 추가.
        try {
            eventMemberService.addMember(form.getEventId(), form.getMemberId());
            return new EventMemberAddResult("success", "회원을 경기에 추가하였습니다.");
        } catch (AlreadyMemberInEventException | UserNotFoundException | NoEventException e) {
            return new EventMemberAddResult("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @PostMapping("/memberRemove") //SS304 해당 경기에 참여 인원 삭제
    public EventMemberAddResult memberRemove(@RequestBody EventMemberAddForm form) {
        try {
            eventMemberService.removeMember(form.getEventId(), form.getMemberId());
            return new EventMemberAddResult("success", "회원을 경기에서 삭제하였습니다.");
        } catch (UserNotFoundException | NoEventException e) {
            return new EventMemberAddResult("fail", e.getMessage());
        }
    }

}
