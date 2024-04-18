package kr.co.seulchuksaeng.seulchuksaengweb.scheduler;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.PurchaseStatus;
import kr.co.seulchuksaeng.seulchuksaengweb.httpProvider.DiscordWebhookSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PenaltyGrantTask {

    Runnable grantTask = () -> {
        System.out.println("Hello me");
    };

    private final EventRepository eventRepository;

    private final MemberEventRepository memberEventRepository;

    private final MemberRepository memberRepository;

    public final DiscordWebhookSender discordWebhookSender;

    @Transactional
    public void grantTask(Event event) {
        List<MemberEvent> allMemberList = memberEventRepository.getAllMemberList(event);

        StringBuilder result = new StringBuilder();
        result.append("경기 참여 멤버 목록\n\n");

        for (MemberEvent me : allMemberList) {

            if (me.getAttend().equals(Attendance.ABSENT)) {
                me.getMember().giveWarnPoint(3);
                result.append(me.getMember().getName()).append(" 무단결석 - 경고 +1점\n");
                continue;
            }

            if (me.getAttend().equals(Attendance.LATE)) {
                me.getMember().giveWarnPoint();
                result.append(me.getMember().getName()).append(" 지각 - 주의 +1점\n");
            }

            if(me.getPurchased().equals(PurchaseStatus.NOT_PURCHASED)) {
                me.getMember().giveWarnPoint();
                result.append(me.getMember().getName()).append(" 회비 미납부 - 주의 +1점\n");
            }
        }

        discordWebhookSender.sendLog(event.getTitle() + " 경고 정산 결과", result.toString());


    }
}