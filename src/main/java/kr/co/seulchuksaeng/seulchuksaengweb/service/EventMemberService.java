package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.*;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.AlreadyMemberInEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NoEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventMemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventMemberService {

    private final EventMemberRepository eventMemberRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    public ArrayList<EventMemberListInnerResult> notPlayingMemberList(String eventId) {

        ArrayList<EventMemberListInnerResult> list = new ArrayList<>();

        Event event = eventRepository.findEventById(Long.valueOf(eventId));

        eventMemberRepository.getNonParticipatingMembers(event).forEach(member -> {
            list.add(new EventMemberListInnerResult(member.getId(), member.getName()));
        });

        return list;
    }

    @Transactional
    public List<EventMemberListInnerResult> playingMemberList(String eventId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        List<Member> allMemberList = eventMemberRepository.getAllMemberList(event);

        return allMemberList.stream()
                .map(member -> new EventMemberListInnerResult(member.getId(), member.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMember(String eventId, String memberId) {
            Member member = memberRepository.findMemberById(memberId);
            Event event = eventRepository.findEventById(Long.valueOf(eventId));
            validateDuplicateMemberInEvent(event, member);
            MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);
            eventMemberRepository.save(memberEvent);
    }

    @Transactional
    public void removeMember(String eventId, String memberId) {
        Member member = memberRepository.findMemberById(memberId);
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        eventMemberRepository.deleteMemberFromEvent(member, event);
    }

    private void validateDuplicateMemberInEvent(Event event, Member member) {
        boolean memberInEvent = eventMemberRepository.isMemberInEvent(event, member);
        if (memberInEvent) { //회원이 경기에 이미 참여하고 있을 경우
            throw new AlreadyMemberInEventException();
        }
    }

    @Transactional
    public void attendMember(String eventId, String memberId) {
        Member member = memberRepository.findMemberById(memberId);
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        MemberEvent memberEvent = eventMemberRepository.findMemberEventByMemberAndEvent(member, event);
        memberEvent.attend();
    }

}
