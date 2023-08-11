package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventCreateForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventShowcaseResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<EventShowcaseInnerResult> eventShowcaseInnerResultList = new ArrayList<>();

        try {
            List<Event> eventList = eventRepository.findEventList();
            for (Event event : eventList) {
                eventShowcaseInnerResultList.add(new EventShowcaseInnerResult(event.getEventId(), event.getTitle()));
            }
            return new EventShowcaseResult("success", "경기 목록 조회에 성공하였습니다", eventShowcaseInnerResultList, eventShowcaseInnerResultList.size());
        } catch (Exception e) {
            throw new NoEventException();
        }

    }

    public EventShowcaseResult list(Gender gender) { // 일반 유저 서비스
        ArrayList<EventShowcaseInnerResult> eventShowcaseInnerResultList = new ArrayList<>();

        try {
            List<Event> eventList = eventRepository.findEventList(gender);
            for (Event event : eventList) {
                eventShowcaseInnerResultList.add(new EventShowcaseInnerResult(event.getEventId(), event.getTitle()));
            }
            return new EventShowcaseResult("success", "경기 목록 조회에 성공하였습니다", eventShowcaseInnerResultList, eventShowcaseInnerResultList.size());
        } catch (Exception e) {
            throw new NoEventException();
        }
    }
}
