package kr.co.seulchuksaeng.seulchuksaengweb.service;

import jakarta.persistence.NoResultException;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventCreateForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventUpdateForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventShowcaseResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    @Transactional
    public void create(EventCreateForm ef, String id) {

        Member member = memberRepository.findMemberById(id);

        Event event = new Event(ef.getTitle(), ef.getLocation(), ef.getDescription(), ef.getGender(), ef.getStartTime(), ef.getEndTime(), ef.getMoney(), member);

        eventRepository.save(event);

    }

    public EventShowcaseResult list() { // 관리자 전용 서비스
        try {
            List<EventShowcaseInnerResult> eventShowcaseInnerResultList = eventRepository.findEventList().stream()
                    .map(event -> new EventShowcaseInnerResult(event.getEventId(), event.getTitle()))
                    .collect(Collectors.toList());

            return new EventShowcaseResult("success", "경기 목록 조회에 성공하였습니다", eventShowcaseInnerResultList, eventShowcaseInnerResultList.size());
        } catch (Exception e) {
            throw new NoEventException();
        }
    }

    public EventShowcaseResult list(Gender gender) { // 일반 유저 서비스
        try {
            List<EventShowcaseInnerResult> eventShowcaseInnerResultList = eventRepository.findEventList(gender).stream()
                    .map(event -> new EventShowcaseInnerResult(event.getEventId(), event.getTitle()))
                    .collect(Collectors.toList());

            return new EventShowcaseResult("success", "경기 목록 조회에 성공하였습니다", eventShowcaseInnerResultList, eventShowcaseInnerResultList.size());
        } catch (Exception e) {
            throw new NoEventException();
        }
    }

    public EventReadInnerResult read(Long eventId) {
        try {
            Event event = eventRepository.findEventById(eventId);
            return EventReadInnerResult.builder()
                    .eventId(event.getEventId())
                    .title(event.getTitle())
                    .location(event.getLocation())
                    .startTime(event.getStartTime())
                    .endTime(event.getEndTime())
                    .gender(event.getGender())
                    .money(event.getMoney())
                    .description(event.getDescription())
                    .build();
        } catch (NoResultException e) {
            throw new NoEventException();
        }
    }

    @Transactional
    public void update(EventUpdateForm form) {
        Event event = eventRepository.findEventById(Long.valueOf(form.getEventId()));
        event.update(form.getTitle(), form.getLocation(), form.getDescription(), form.getGender(), form.getStartTime(), form.getEndTime(), form.getMoney());
    }

    @Transactional
    public void remove(String eventId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        eventRepository.delete(event);
    }

}
