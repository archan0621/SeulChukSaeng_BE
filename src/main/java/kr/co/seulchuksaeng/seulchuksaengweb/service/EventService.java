package kr.co.seulchuksaeng.seulchuksaengweb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.NoResultException;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.AddressResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.LocationResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.NetworkException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NotValidAddressException;
import kr.co.seulchuksaeng.seulchuksaengweb.httpProvider.NaverMapApiProvider;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final NaverMapApiProvider naverMapApiProvider;

    @Transactional
    public void create(EventForm.Create ef, String id) {

        LocationResponse locationResponse = naverMapApiProvider.checkLocation(ef.location());//주소가 유효한지 확인

        Member member = memberRepository.findMemberById(id);

        Event event = new Event(ef.title(), ef.location(), ef.description(), locationResponse.getY(), locationResponse.getX(), ef.gender(), ef.startTime(), ef.endTime(), ef.money(), member);

        eventRepository.save(event);

    }

    public EventResult.Showcase list() { // 관리자 전용 서비스
        try {
            List<EventShowcaseInnerResult> eventShowcaseInnerResultList = eventRepository.findEventList().stream()
                    .map(event -> new EventShowcaseInnerResult(event.getEventId(), event.getTitle()))
                    .collect(Collectors.toList());

            return new EventResult.Showcase("success", "경기 목록 조회에 성공하였습니다", eventShowcaseInnerResultList, eventShowcaseInnerResultList.size());
        } catch (Exception e) {
            throw new NoEventException();
        }
    }

    public EventResult.Showcase list(Gender gender) { // 일반 유저 서비스
        try {
            List<EventShowcaseInnerResult> eventShowcaseInnerResultList = eventRepository.findEventList(gender).stream()
                    .map(event -> new EventShowcaseInnerResult(event.getEventId(), event.getTitle()))
                    .collect(Collectors.toList());

            return new EventResult.Showcase("success", "경기 목록 조회에 성공하였습니다", eventShowcaseInnerResultList, eventShowcaseInnerResultList.size());
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
    public void update(EventForm.Update form) {
        Event event = eventRepository.findEventById(Long.valueOf(form.getEventId()));
        if(!event.getLocation().equals(form.getLocation())) { //만약 주소가 변경되었다면
            LocationResponse locationResponse = naverMapApiProvider.checkLocation(form.getLocation());//주소가 유효한지 확인
            event.update(form.getTitle(), form.getLocation(), form.getDescription(), form.getGender(), form.getStartTime(), form.getEndTime(), form.getMoney());
            event.updateLocation(locationResponse.getY(), locationResponse.getX());
            return;
        }
        event.update(form.getTitle(), form.getLocation(), form.getDescription(), form.getGender(), form.getStartTime(), form.getEndTime(), form.getMoney());
    }

    @Transactional
    public void remove(String eventId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        eventRepository.delete(event);
    }

}
