package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import jakarta.persistence.EntityManager;
import kr.co.seulchuksaeng.seulchuksaengweb.annotation.LogExecutionTime;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberListInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager entityManager;

    //회원 저장
    public void save (Member member) {
        entityManager.persist(member);
    }

    public Member findMemberById(String id){
        try {
            return entityManager.createQuery("select m from Member m where m.id = :id", Member.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
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
