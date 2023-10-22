package kr.co.seulchuksaeng.seulchuksaengweb.controller;

import kr.co.seulchuksaeng.seulchuksaengweb.annotation.AdminAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.UserAuthorize;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.*;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.*;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    private final MemberRepository memberRepository;

    @AdminAuthorize
    @PostMapping("/create")
    public EventResult.Create createEvent(@RequestBody EventForm.Create eventCreateForm, @AuthenticationPrincipal User user) {
        log.info("경기 생성 요청이 발생하였습니다 - 요청자 : {}, 경기 제목 : {}", user.getUsername(), eventCreateForm.title());
        try {
            eventService.create(eventCreateForm, user.getUsername());
            log.info("경기 생성에 성공하였습니다 - 요청자 : {}, 경기 제목 : {}", user.getUsername(), eventCreateForm.title());
            return new EventResult.Create("success", "경기 생성에 성공하였습니다");
        } catch (Exception e) { //특정 예외가 예상되지 않지만 불안하니까 일단 예외처리, 추후 테스트 단계에서 수정예정
            log.info("경기 생성에 실패하였습니다 - 요청자 : {}, 경기 제목 : {},실패 사유 : {}", user.getUsername(), eventCreateForm.title(), e.getMessage());
            return new EventResult.Create("fail", e.getMessage());
        }
    }

    @UserAuthorize
    @LogExecutionTime
    @PostMapping("/list")
    public EventResult.Showcase listEvent(@AuthenticationPrincipal User user) {
        log.info("경기 목록 조회 요청이 발생하였습니다 - 요청자 : {}", user.getUsername());
        try {
            if (user.getAuthorities().toString().contains("ADMIN")) { // 관리자인 경우 처리
                log.info("관리자가 경기 목록을 조회하였습니다 - 요청자 : {}", user.getUsername());
                return eventService.list();
            } else if (user.getAuthorities().toString().contains("USER")) { // 사용자인 경우 처리
                Member member = memberRepository.findMemberById(user.getUsername());
                log.info("사용자가 경기 목록을 조회하였습니다 - 요청자 : {}", user.getUsername());
                return eventService.list(member.getGender());
            }
        } catch (NoEventException e) { // 조회할 경기가 없는 경우
            log.info("경기 목록 조회에 실패하였습니다 - 요청자 : {}, 실패 사유 : {}", user.getUsername(), e.getMessage());
            return new EventResult.Showcase("fail", e.getMessage(), null, 0);
        }

        log.info("경기 목록 조회에 실패하였습니다 - 요청자 : {}, 실패 사유 : 알 수 없음", user.getUsername());
        return new EventResult.Showcase("fail", "조회에 오류가 발생하였습니다.", null, 0);
    }

    @UserAuthorize
    @LogExecutionTime
    @PostMapping("/read")
    public EventResult.Read readEvent(@RequestBody EventForm.Read eventReadForm, @AuthenticationPrincipal User user) {
        log.info("경기 조회 요청이 발생하였습니다 - 요청자 : {}, 경기 고유번호 : {}", user.getUsername(), eventReadForm.eventId());
        try {
            EventReadInnerResult result = eventService.read(eventReadForm.eventId());
            log.info("경기 조회에 성공하였습니다 - 요청자 : {}, 경기 고유번호 : {}", user.getUsername(), eventReadForm.eventId());
            return new EventResult.Read("success", "경기 조회에 성공하였습니다", result);
        } catch (NoEventException e) {
            log.info("경기 조회에 실패하였습니다 - 요청자 : {}, 경기 고유번호 : {}, 실패 사유 : {}", user.getUsername(), eventReadForm.eventId(), e.getMessage());
            return new EventResult.Read("fail", e.getMessage(), null);
        }
        
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/update")
    public EventResult.Update updateEvent(@RequestBody EventForm.Update form, @AuthenticationPrincipal User user) {
        log.info("경기 내용 수정 요청이 발생하였습니다 - 요청자 : {}, 경기 제목 : {}", user.getUsername(), form.title());
        try {
            eventService.update(form);
            log.info("경기 내용 수정에 성공하였습니다 - 요청자 : {}, 경기 제목 : {}", user.getUsername(), form.title());
            return new EventResult.Update("success", "경기 내용 수정에 성공하였습니다");
        } catch (NoEventException e) {
            log.info("경기 내용 수정에 실패하였습니다 - 요청자 : {}, 경기 제목 : {}, 실패 사유 : {}", user.getUsername(), form.title(), e.getMessage());
            return new EventResult.Update("fail", e.getMessage());
        }
    }

    @AdminAuthorize
    @LogExecutionTime
    @PostMapping("/remove")
    public EventResult.Remove removeEvent(@RequestBody EventForm.Remove form, @AuthenticationPrincipal User user) {
        log.info("경기 삭제 요청이 발생하였습니다 - 요청자 : {}, 경기 고유번호 : {}", user.getUsername(), form.eventId());
        try {
            eventService.remove(form.eventId());
            log.info("경기 삭제에 성공하였습니다 - 요청자 : {}, 경기 고유번호 : {}", user.getUsername(), form.eventId());
            return new EventResult.Remove("success", "경기 삭제에 성공하였습니다");
        } catch (NoEventException e) {
            log.info("경기 삭제에 실패하였습니다 - 요청자 : {}, 경기 고유번호 : {}, 실패 사유 : {}", user.getUsername(), form.eventId(), e.getMessage());
            return new EventResult.Remove("fail", e.getMessage());
        }
    }

}
