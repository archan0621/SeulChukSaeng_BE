package kr.co.seulchuksaeng.seulchuksaengweb.service;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.*;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventMemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.AlreadyMemberInEventException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.FarFromLocationException;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberEventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.util.DistanceCalc;
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
public class MemberEventService {

    private final MemberEventRepository memberEventRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final DistanceCalc distanceCalc;

    public ArrayList<EventMemberListInnerResult> notPlayingMemberList(String eventId) {

        ArrayList<EventMemberListInnerResult> list = new ArrayList<>();

        Event event = eventRepository.findEventById(Long.valueOf(eventId));

        memberEventRepository.getNonParticipatingMembers(event).forEach(member -> {
            list.add(new EventMemberListInnerResult(member.getId(), member.getName(), null, null));
        });

        return list;
    }

    @Transactional
    public List<EventMemberListInnerResult> playingMemberList(String eventId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        List<MemberEvent> allMemberList = memberEventRepository.getAllMemberList(event);

        return allMemberList.stream()
                .map(memberEvent -> new EventMemberListInnerResult(memberEvent.getMember().getId(), memberEvent.getMember().getName(), memberEvent.getAttend(), memberEvent.getPurchased()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMember(String eventId, String memberId) {
            Member member = memberRepository.findMemberById(memberId);
            Event event = eventRepository.findEventById(Long.valueOf(eventId));
            validateDuplicateMemberInEvent(event, member);
            MemberEvent memberEvent = new MemberEvent(member, event, Attendance.ABSENT, PurchaseStatus.NOT_PURCHASED);
            memberEventRepository.save(memberEvent);
    }

    @Transactional
    public void removeMember(String eventId, String memberId) {
        Member member = memberRepository.findMemberById(memberId);
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        memberEventRepository.deleteMemberFromEvent(member, event);
    }

    private void validateDuplicateMemberInEvent(Event event, Member member) {
        boolean memberInEvent = memberEventRepository.isMemberInEvent(event, member);
        if (memberInEvent) { //회원이 경기에 이미 참여하고 있을 경우
            throw new AlreadyMemberInEventException();
        }
    }

    @Transactional
    public void attendMember(String eventId, String memberId, Double latitude, Double longitude) {

        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        Member member = memberRepository.findMemberById(memberId);
        MemberEvent memberEvent = memberEventRepository.findMemberEventByMemberAndEvent(member, event);

        if (distanceCalc.calculateDistance(event.getLatitude(), event.getLongitude(), latitude, longitude) > 400) {
            throw new FarFromLocationException();
        }

        Attendance attend = memberEvent.attend();

//        if (attend.equals(Attendance.LATE)) {
//            log.info("{} 님이 지각하셨습니다, 경고 +1 처리됩니다.", member.getId());
//            member.giveWarnPoint();
//        }

    }

    @Transactional
    public void purchaseRequest(String eventId, String username) {
        Member member = memberRepository.findMemberById(username);
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        MemberEvent memberEvent = memberEventRepository.findMemberEventByMemberAndEvent(member, event);
        memberEvent.purchaseRequest();
    }

    @Transactional
    public List<EventMemberListInnerResult> purchaseRequestList(String eventId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        return memberEventRepository.getPurchaseRequestList(event).stream()
                .map(member -> new EventMemberListInnerResult(member.getId(), member.getName(),null, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public void purchaseCheck(String eventId, String memberId) {
        Member member = memberRepository.findMemberById(memberId);
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        MemberEvent memberEvent = memberEventRepository.findMemberEventByMemberAndEvent(member, event);
        memberEvent.purchaseCheck();
    }

    public MemberEvent getMemberEvent(String eventId, Long memberId) {
        Member member = memberRepository.findMemberByPK(memberId);
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        return memberEventRepository.findMemberEventByMemberAndEvent(member, event);
    }

    @Transactional
    public void forceAttend(String eventId, Long memberId) {
        Event event = eventRepository.findEventById(Long.valueOf(eventId));
        Member member = memberRepository.findMemberByPK(memberId);
        MemberEvent memberEvent = memberEventRepository.findMemberEventByMemberAndEvent(member, event);
        memberEvent.forceAttend();
    }
}
