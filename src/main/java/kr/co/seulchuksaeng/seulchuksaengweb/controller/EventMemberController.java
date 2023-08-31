package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.annotation.AdminAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventAttendForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventMemberForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventMemberListForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberAddResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventMemberListResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.AlreadyMemberInEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.*;
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
        log.info("경기 미참여 인원 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
        try {
            ArrayList<EventMemberListInnerResult> innerResults = eventMemberService.notPlayingMemberList(form.getEventId());
            log.info("경기 미참여 인원 조회에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
            return new EventMemberListResult("success", "경기 미참여 인원을 조회하였습니다.", innerResults, innerResults.size());
        } catch (NoEventException e) {
            log.info("경기 미참여 인원 조회에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @UserAuthorize
    @PostMapping("/memberList")
    public EventMemberListResult memberList(@RequestBody EventMemberListForm form, @AuthenticationPrincipal User user) { //SS301 해당 경기에 참여 인원 조회
        log.info("경기 참여 인원 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
        try {
            List<EventMemberListInnerResult> innerResult = eventMemberService.playingMemberList(form.getEventId());
            log.info("경기 참여 인원 조회에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
            return new EventMemberListResult("success", "경기 참여 인원을 조회하였습니다.", innerResult, innerResult.size());
        } catch (NoEventException e) {
            log.info("경기 참여 인원 조회에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @PostMapping("/memberAdd")
    public EventMemberAddResult memberAdd(@RequestBody EventMemberForm.basic form, @AuthenticationPrincipal User user) { //SS302 해당 경기에 참여 인원 추가.
        log.info("경기에 회원 추가 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.getEventId(), form.getMemberId());
        try {
            eventMemberService.addMember(form.getEventId(), form.getMemberId());
            log.info("경기에 회원을 추가하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.getEventId(), form.getMemberId());
            return new EventMemberAddResult("success", "회원을 경기에 추가하였습니다.");
        } catch (AlreadyMemberInEventException | UserNotFoundException | NoEventException e) {
            log.info("경기에 회원 추가에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), form.getMemberId(), e.getMessage());
            return new EventMemberAddResult("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @PostMapping("/memberRemove") //SS304 해당 경기에 참여 인원 삭제
    public EventMemberAddResult memberRemove(@RequestBody EventMemberForm.basic form, @AuthenticationPrincipal User user) {
        log.info("경기에서 회원 삭제 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.getEventId(), form.getMemberId());
        try {
            eventMemberService.removeMember(form.getEventId(), form.getMemberId());
            log.info("경기에서 회원을 삭제하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.getEventId(), form.getMemberId());
            return new EventMemberAddResult("success", "회원을 경기에서 삭제하였습니다.");
        } catch (UserNotFoundException | NoEventException e) {
            log.info("경기에서 회원 삭제에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), form.getMemberId(), e.getMessage());
            return new EventMemberAddResult("fail", e.getMessage());
        }
    }

    @UserAuthorize
    @PostMapping("/memberAttend") //SS305 해당 경기에 참여 인원 출석
    public EventMemberResult memberAttend(@RequestBody EventAttendForm form, @AuthenticationPrincipal User user) {
        log.info("경기에 회원 출석처리 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
        try {
            eventMemberService.attendMember(form.getEventId(), user.getUsername());
            log.info("경기에 회원 출석처리에 성공하였습니다. - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
            return new EventMemberResult("success", "회원을 경기에 출석처리하였습니다.");
        } catch (UserNotFoundException | NoEventException | AlreadyAttendException | NoEventMemberException e) {
            log.info("경기에 회원 출석처리에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberResult("fail", e.getMessage());
        }
    }

    @UserAuthorize
    @PostMapping("/memberPurchaseReq") //SS306 해당 경기 활동비 납부 확인을 요청한다.
    public EventMemberResult.basic memberPurchaseRequest(@RequestBody EventMemberForm.basic form, @AuthenticationPrincipal User user) {
        log.info("경기 활동비 납부 확인 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
        try {
            log.info("경기 활동비 납부 확인 요청에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
            eventMemberService.purchaseRequest(form.getEventId(), user.getUsername());
            return new EventMemberResult.basic("success", "경기 활동비 납부 확인 요청에 성공하였습니다.");
        } catch (UserNotFoundException | NoEventException | NoEventMemberException | AlreadyPurchasedException |
                 WatingPurchaseException e) {
            log.info("경기 활동비 납부 확인 요청에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberResult.basic("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @PostMapping("/memberPurchaseList") //SS307 활동비 납부 확인 요청 목록을 가져온다.
    public EventMemberListResult memberPurchaseList(@RequestBody EventMemberForm.basic form, @AuthenticationPrincipal User user) {
        log.info("경기 활동비 납부 확인 요청 목록 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
        try {
            List<EventMemberListInnerResult> innerResults = eventMemberService.purchaseRequestList(form.getEventId());
            log.info("경기 활동비 납부 확인 요청 목록 조회에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
            return new EventMemberListResult("success", "경기 활동비 납부 확인 요청 목록을 조회하였습니다.", innerResults, innerResults.size());
        } catch (NoEventException e) {
            log.info("경기 활동비 납부 확인 요청 목록 조회에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberListResult("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @PostMapping("/memberPurchaseCheck") //SS308 활동비 납부 확인
    public EventMemberResult.basic memberPurchaseCheck(@RequestBody EventMemberForm.basic form, @AuthenticationPrincipal User user) {
        log.info("경기 활동비 납부 확인 요청이 발생하였습니다 - 요청자 : {}, 납부 확인 요청자 : {},경기 고유 번호 : {}", user.getUsername(), form.getMemberId(), form.getEventId());
        try {
            eventMemberService.purchaseCheck(form.getEventId(), form.getMemberId());
            log.info("경기 활동비 납부 확인에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.getEventId());
            return new EventMemberResult.basic("success", "경기 활동비 납부 확인에 성공하였습니다.");
        } catch (UserNotFoundException | NoEventException | NoEventMemberException | AlreadyPurchasedException e) {
            log.info("경기 활동비 납부 확인에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.getEventId(), e.getMessage());
            return new EventMemberResult.basic("fail", e.getMessage());
        }
    }

}