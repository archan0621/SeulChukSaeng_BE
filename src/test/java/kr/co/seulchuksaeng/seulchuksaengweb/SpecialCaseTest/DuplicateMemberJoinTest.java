package kr.co.seulchuksaeng.seulchuksaengweb.SpecialCaseTest;

import kr.co.seulchuksaeng.seulchuksaengweb.controller.MemberController;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.form.MemberForm;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.member.ExistMemberException;
import kr.co.seulchuksaeng.seulchuksaengweb.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
public class DuplicateMemberJoinTest {

    @Autowired
    private MemberController memberController;

    @Test
    @DisplayName("동시에 같은 id를 가진 회원가입 요청")
    public void duplicateMemberJoin() throws InterruptedException {
        //given
        int threadNum = 2;
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        String randomId = UUID.randomUUID().toString(); // 성능 테스트 환경과 똑같이 랜덤 아이디 생성
        MemberForm.Join joinBody1 = new MemberForm.Join(randomId, "testpassword", "abcName1", "010-0000-0000", Gender.MALE, "1234");
        MemberForm.Join joinBody2 = new MemberForm.Join(randomId, "testpassword", "abcName2", "010-1111-1111", Gender.MALE, "1234");
        //when
        Future<?> submit1 = service.submit(() -> {
            memberController.join(joinBody1);
        });

        Future<?> submit2 = service.submit(() -> {
            memberController.join(joinBody2);
        });

        //then
        service.shutdown();

        boolean exceptionOccurred = false;

        try {
            submit1.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ExistMemberException) {
                exceptionOccurred = true;
            }
        }

        try {
            submit2.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ExistMemberException) {
                exceptionOccurred = true;
            }
        }

        assertTrue(exceptionOccurred, "적어도 둘 중의 한개의 요청에선 회원이 존재한다는 예외가 반환되어야 한다.");
        service.awaitTermination(30, TimeUnit.SECONDS);
    }
}
