package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.*;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.NoEventMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberEventRepository {

    private final EntityManager entityManager;

    public void save(MemberEvent memberEvent) {
        entityManager.persist(memberEvent);
    }

    public List<MemberEvent> getAllMemberList(Event event) {
        return entityManager.createQuery("select me from MemberEvent me where me.event = :event", MemberEvent.class)
                .setParameter("event", event)
                .getResultList();
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
                        "SELECT m FROM Member m LEFT JOIN MemberEvent me ON m = me.member AND me.event = :event WHERE me.event IS NULL AND m.gender = :gender", Member.class)
                .setParameter("event", event)
                .setParameter("gender", event.getGender()) // gender 파라미터 추가
                .getResultList();
    }

    // 해당 경기에서 인원을 삭제한다.
    public void deleteMemberFromEvent(Member member, Event event) {
        entityManager.createQuery("DELETE FROM MemberEvent me WHERE me.member = :member AND me.event = :event")
                .setParameter("member", member)
                .setParameter("event", event)
                .executeUpdate();
    }


    public MemberEvent findMemberEventByMemberAndEvent(Member member, Event event) {
        try {
            return entityManager.createQuery("select me from MemberEvent me where me.member = :member and me.event = :event", MemberEvent.class)
                    .setParameter("member", member)
                    .setParameter("event", event)
                    .getSingleResult();
        } catch (Exception e) {
            throw new NoEventMemberException();
        }
    }

    public List<Member> getPurchaseRequestList(Event event) {
        return entityManager.createQuery("select me.member from MemberEvent me where me.event = :event and me.purchased = :purchased", Member.class)
                .setParameter("event", event)
                .setParameter("purchased", PurchaseStatus.WAITING)
                .getResultList();
    }

    public List<MemberEvent> getMemberJoinedEvent(Member member) {
        return entityManager.createQuery("select me from MemberEvent me where me.member = :member", MemberEvent.class)
                .setParameter("member", member)
                .getResultList();
    }

}
