package kr.co.seulchuksaeng.seulchuksaengweb.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

}
