package kr.co.seulchuksaeng.seulchuksaengweb.scheduler;
import kr.co.seulchuksaeng.seulchuksaengweb.domain.Event;
import kr.co.seulchuksaeng.seulchuksaengweb.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;

/*
    Author : 박종하
    Date   : 2024-01-08
    Desc   : 회원 경고 관리 스케줄러
*/

@Component
@RequiredArgsConstructor
@Slf4j
public class PenaltyScheduler {

    private final EventRepository eventRepository;
    private final PenaltyGrantTask penaltyGrantTask;

    @Scheduled(cron = "0 0 0 ? * SUN")
    public void checkTodaysEvent() {
        LocalDate today = LocalDate.now();
        List<Event> byEndTime = eventRepository.findByEndTime(today);

        if (byEndTime.isEmpty()) {
            log.info("오늘은 경기가 없는 주입니다~! 경고 스케줄러를 따로 생성하지 않겠습니다!");
        } else {
            for (Event i : byEndTime) {
                createPenaltyScheduler(i.getEndTime(), i);
            }
        }
    }


    public void createPenaltyScheduler(LocalDateTime executionTime, Event event) {
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler();

        String cronExpression = String.format("0 %d %d %d %d *",
                executionTime.getMinute(),
                executionTime.getHour(),
                executionTime.getDayOfMonth(),
                executionTime.getMonthValue());

        CronTrigger trigger = new CronTrigger(cronExpression);
        taskScheduler.schedule(() -> penaltyGrantTask.grantTask(event), trigger);

    }

}
