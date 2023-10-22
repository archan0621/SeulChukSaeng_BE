package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.annotation.AdminAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.MemberEventForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.MemberEventResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.AlreadyMemberInEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.EventAlreadyEndedException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.*;
import kr.co.seulchuksaeng.seulchuksaengweb.service.MemberEventService;
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
public class MemberEventController {

    private final MemberEventService memberEventService;

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberNoList") //SS303 해당 경기에 참여하지 않는 인원을 조회한다.
    public MemberEventResult.Lists memberNoList(@RequestBody MemberEventForm.List form, @AuthenticationPrincipal User user) {
        log.info("경기 미참여 인원 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
        try {
            ArrayList<EventMemberListInnerResult> innerResults = memberEventService.notPlayingMemberList(form.eventId());
            log.info("경기 미참여 인원 조회에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
            return new MemberEventResult.Lists("success", "경기 미참여 인원을 조회하였습니다.", innerResults, innerResults.size());
        } catch (NoEventException e) {
            log.info("경기 미참여 인원 조회에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.Lists("fail", e.getMessage(), null, 0);
        }
    }

    @UserAuthorize
    @LogExecutionTime
    @PostMapping("/memberList")
    public MemberEventResult.Lists memberList(@RequestBody MemberEventForm.List form, @AuthenticationPrincipal User user) { //SS301 해당 경기에 참여 인원 조회
        log.info("경기 참여 인원 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
        try {
            List<EventMemberListInnerResult> innerResult = memberEventService.playingMemberList(form.eventId());
            log.info("경기 참여 인원 조회에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
            return new MemberEventResult.Lists("success", "경기 참여 인원을 조회하였습니다.", innerResult, innerResult.size());
        } catch (NoEventException e) {
            log.info("경기 참여 인원 조회에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.Lists("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberAdd")
    public MemberEventResult.Add memberAdd(@RequestBody MemberEventForm.Add form, @AuthenticationPrincipal User user) { //SS302 해당 경기에 참여 인원 추가.
        log.info("경기에 회원 추가 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.eventId(), form.memberId());
        try {
            memberEventService.addMember(form.eventId(), form.memberId());
            log.info("경기에 회원을 추가하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.eventId(), form.memberId());
            return new MemberEventResult.Add("success", "회원을 경기에 추가하였습니다.");
        } catch (AlreadyMemberInEventException | UserNotFoundException | NoEventException e) {
            log.info("경기에 회원 추가에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), form.memberId(), e.getMessage());
            return new MemberEventResult.Add("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberRemove") //SS304 해당 경기에 참여 인원 삭제
    public MemberEventResult.Remove memberRemove(@RequestBody MemberEventForm.Remove form, @AuthenticationPrincipal User user) {
        log.info("경기에서 회원 삭제 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.eventId(), form.memberId());
        try {
            memberEventService.removeMember(form.eventId(), form.memberId());
            log.info("경기에서 회원을 삭제하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}", user.getUsername(), form.eventId(), form.memberId());
            return new MemberEventResult.Remove("success", "회원을 경기에서 삭제하였습니다.");
        } catch (UserNotFoundException | NoEventException e) {
            log.info("경기에서 회원 삭제에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 회원 아이디 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), form.memberId(), e.getMessage());
            return new MemberEventResult.Remove("fail", e.getMessage());
        }
    }

    @UserAuthorize
    @LogExecutionTime
    @PostMapping("/memberAttend") //SS305 해당 경기에 참여 인원 출석
    public MemberEventResult.Attend memberAttend(@RequestBody MemberEventForm.Attend form, @AuthenticationPrincipal User user) {
        log.info("경기에 회원 출석처리 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
        try {
            memberEventService.attendMember(form.eventId(), user.getUsername(), Double.valueOf(form.lat()), Double.valueOf(form.lng()));
            log.info("경기에 회원 출석처리에 성공하였습니다. - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
            return new MemberEventResult.Attend("success", "회원을 경기에 출석처리하였습니다.");
        } catch (UserNotFoundException | NoEventException | AlreadyAttendException | NoEventMemberException | EventNotStartException | FarFromLocationException |
                 EventAlreadyEndedException e) {
            log.info("경기에 회원 출석처리에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.Attend("fail", e.getMessage());
        } catch (NullPointerException e) {
            log.info("경기에 회원 출석처리에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.Attend("fail", "위치 정보를 가져오는데 실패하였습니다.");
        }
    }

    @UserAuthorize
    @LogExecutionTime
    @PostMapping("/memberPurchaseReq") //SS306 해당 경기 활동비 납부 확인을 요청한다.
    public MemberEventResult.PurchaseReq memberPurchaseRequest(@RequestBody MemberEventForm.PurchaseReq form, @AuthenticationPrincipal User user) {
        log.info("경기 활동비 납부 확인 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
        try {
            log.info("경기 활동비 납부 확인 요청에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
            memberEventService.purchaseRequest(form.eventId(), user.getUsername());
            return new MemberEventResult.PurchaseReq("success", "경기 활동비 납부 확인 요청에 성공하였습니다.");
        } catch (UserNotFoundException | NoEventException | NoEventMemberException | AlreadyPurchasedException |
                 WatingPurchaseException e) {
            log.info("경기 활동비 납부 확인 요청에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.PurchaseReq("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberPurchaseList") //SS307 활동비 납부 확인 요청 목록을 가져온다.
    public MemberEventResult.PurchaseList memberPurchaseList(@RequestBody MemberEventForm.PurchaseList form, @AuthenticationPrincipal User user) {
        log.info("경기 활동비 납부 확인 요청 목록 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
        try {
            List<EventMemberListInnerResult> innerResults = memberEventService.purchaseRequestList(form.eventId());
            log.info("경기 활동비 납부 확인 요청 목록 조회에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
            return new MemberEventResult.PurchaseList("success", "경기 활동비 납부 확인 요청 목록을 조회하였습니다.", innerResults, innerResults.size());
        } catch (NoEventException e) {
            log.info("경기 활동비 납부 확인 요청 목록 조회에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.PurchaseList("fail", e.getMessage(), null, 0);
        }
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/memberPurchaseCheck") //SS308 활동비 납부 확인
    public MemberEventResult.PurchaseCheck memberPurchaseCheck(@RequestBody MemberEventForm.PurchaseCheck form, @AuthenticationPrincipal User user) {
        log.info("경기 활동비 납부 확인 요청이 발생하였습니다 - 요청자 : {}, 납부 확인 요청자 : {},경기 고유 번호 : {}", user.getUsername(), form.memberId(), form.eventId());
        try {
            memberEventService.purchaseCheck(form.eventId(), form.memberId());
            log.info("경기 활동비 납부 확인에 성공하였습니다 - 요청자 : {}, 경기 고유 번호 : {}", user.getUsername(), form.eventId());
            return new MemberEventResult.PurchaseCheck("success", "경기 활동비 납부 확인에 성공하였습니다.");
        } catch (UserNotFoundException | NoEventException | NoEventMemberException | AlreadyPurchasedException e) {
            log.info("경기 활동비 납부 확인에 실패하였습니다 - 요청자 : {}, 경기 고유 번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new MemberEventResult.PurchaseCheck("fail", e.getMessage());
        }
    }

}