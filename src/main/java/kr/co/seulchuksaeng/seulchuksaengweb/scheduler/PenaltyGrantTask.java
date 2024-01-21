package kr.co.seulchuksaeng.seulchuksaengweb.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Attendance;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.MemberEvent;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberEventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.MemberRepository;

import java.util.List;

/*
    Author : 박종하
    Date   : 2024-01-08
    Desc   : 회원 경고 관리 작업
*/

@Component
@RequiredArgsConstructor
public class PenaltyGrantTask {


    Runnable grantTask = () -> {
        System.out.println("Hello me");
    };

    private final EventRepository eventRepository;

    private final MemberEventRepository memberEventRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void grantTask(Event event) {
        List<MemberEvent> allMemberList = memberEventRepository.getAllMemberList(event);

        for (MemberEvent me : allMemberList) {

            if (me.getAttend().equals(Attendance.LATE)) {
                me.getMember().giveWarnPoint();
                return;
            }

            //TODO : 지각 패널티 규정 뭔가 이상한거 같아서 다시 얘기하고 구현하기로 함
        }
    }

}
