package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepository {

    private final EntityManager entityManager;

    //회원 저장
    public void save (Member member) {
        entityManager.persist(member);
    }

    public Member findMemberById(String id) {
        List<Member> results = entityManager.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();
        if(results.isEmpty()) {
            throw new UserNotFoundException();
        }
        return results.get(0);
    }

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public boolean existsById(String id) {
        Member member = entityManager.createQuery("SELECT m FROM Member m WHERE m.id = :id", Member.class)
                .setParameter("id", id)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList().stream().findFirst().orElse(null);
        return member != null;
    }

    public Member findMemberByPK(Long id) {
        try {
            return entityManager.createQuery("select m from Member m where m.memberId = :id", Member.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    public String findNameById(String id) {
        return entityManager.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getSingleResult().getName();
    }

    public List<Member> findAllMember() {
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
