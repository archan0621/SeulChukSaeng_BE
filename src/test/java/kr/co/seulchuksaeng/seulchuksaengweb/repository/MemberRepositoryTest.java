package kr.co.seulchuksaeng.seulchuksaengweb.repository;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Member;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("회원 레포지토리 테스트")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 테스트 및 검색 테스트")
    public void saveAndFindById() {
        // given
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);

        // when
        memberRepository.save(member);

        // then
        Member savedMember = memberRepository.findMemberById("testId");
        assertNotNull(savedMember);
        assertEquals("testId", savedMember.getId());
        assertEquals("testName", savedMember.getName());
    }
    
    @Test
    @DisplayName("회원 이름 검색 테스트")
    public void findNameById() throws Exception {
        //given
        Member member = new Member("testId", "testPw", "testName", "010-0000-0000", Gender.MALE, UserRole.USER, "testSalt", 0);
        //when
        memberRepository.save(member);
        //then
        String result = memberRepository.findNameById("testId");
        assertEquals(result, "testName");
    }
}