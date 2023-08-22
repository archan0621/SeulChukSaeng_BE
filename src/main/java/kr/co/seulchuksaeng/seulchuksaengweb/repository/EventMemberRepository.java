package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import jakarta.persistence.EntityManager;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.MemberEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventMemberRepository {

    private final EntityManager entityManager;

    public void save(MemberEvent memberEvent) {
        entityManager.persist(memberEvent);
    }

    public List<Member> getAllMemberList(Event event) {
        List<MemberEvent> resultList = entityManager.createQuery("select me from MemberEvent me where me.event = :event", MemberEvent.class)
                .setParameter("event", event)
                .getResultList();

        List<Member> members = new ArrayList<>();
        for (MemberEvent memberEvent : resultList) {
            members.add(memberEvent.getMember());
        }

        return members;
    }

    // event와 member를 파라미터로 받아서 event에 member가 포함되어 있는지 확인
    public boolean isMemberInEvent(Event event, Member member) {
        List<MemberEvent> resultList = entityManager.createQuery("select me from MemberEvent me where me.event = :event and me.member = :member", MemberEvent.class)
                .setParameter("event", event)
                .setParameter("member", member)
                .getResultList();

        return !resultList.isEmpty();
    }

    // 해당 경기에 참여하지 않는 인원을 조회한다.
    public List<Member> getNonParticipatingMembers(Event event) {
        return entityManager.createQuery(
                        "SELECT m FROM Member m LEFT JOIN MemberEvent me ON m = me.member AND me.event = :event WHERE me.event IS NULL", Member.class)
                .setParameter("event", event)
                .getResultList();
    }

    // 해당 경기에서 인원을 삭제한다.
    public void deleteMemberFromEvent(Member member, Event event) {
        entityManager.createQuery("DELETE FROM MemberEvent me WHERE me.member = :member AND me.event = :event")
                .setParameter("member", member)
                .setParameter("event", event)
                .executeUpdate();
    }


}