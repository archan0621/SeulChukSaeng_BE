package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import jakarta.persistence.EntityManager;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager entityManager;

    //회원 저장
    public void save (Member member) {
        entityManager.persist(member);
    }

    //회원 id로 조회
    public List<Member> findMemberListById(String id){
        return entityManager.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();
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

    public String findNameById(String id) {
        return entityManager.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", id)
                .getSingleResult().getName();
    }

}
