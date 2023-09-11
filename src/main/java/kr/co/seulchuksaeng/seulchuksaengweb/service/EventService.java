package kr.co.seulchuksaeng.seulchuksaengweb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.NoResultException;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.AddressResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.EventForm;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.EventResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.NetworkException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NotValidAddressException;
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

    @Value("${mapApi.Secret}")
    private String mapApiSecret;

    @Value("${mapApi.Id}")
    private String mapApiId;

    @Transactional
    public void create(EventForm.Create ef, String id) {

        checkLocation(ef);

        Member member = memberRepository.findMemberById(id);

        Event event = new Event(ef.getTitle(), ef.getLocation(), ef.getDescription(), ef.getGender(), ef.getStartTime(), ef.getEndTime(), ef.getMoney(), member);

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
        event.update(form.getTitle(), form.getLocation(), form.getDescription(), form.getGender(), form.getStartTime(), form.getEndTime(), form.getMoney());
    }

    @Transactional
    public void remove(String eventId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        eventRepository.delete(event);
    }

    public void checkLocation(EventForm.Create ef) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", mapApiId)
                .defaultHeader("X-NCP-APIGW-API-KEY", mapApiSecret)
                .build();

        Mono<ResponseEntity<String>> responseMono = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", ef.getLocation())
                        .build())
                .retrieve()
                .toEntity(String.class);

        // 응답 처리
        ResponseEntity<String> responseEntity = responseMono.block();
        if (responseEntity == null) {
            throw new NetworkException();
        }

        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        if (httpStatus != HttpStatus.OK) {
            throw new NetworkException();
        }

        String responseBody = responseEntity.getBody();
        if (responseBody != null && !responseBody.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                AddressResponse addressResponse = objectMapper.readValue(responseBody, AddressResponse.class);

                if (addressResponse.getMeta().getTotalCount() == 0) {
                    throw new NotValidAddressException();
                }

            } catch (Exception e) {
                throw new NetworkException();
            }
        } else {
            throw new NotValidAddressException();
        }
    }


}
