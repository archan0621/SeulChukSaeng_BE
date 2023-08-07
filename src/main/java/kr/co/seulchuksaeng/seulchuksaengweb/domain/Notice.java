package kr.co.seulchuksaeng.seulchuksaengweb.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity @Getter
public class Notice {

    @Id @GeneratedValue
    @Column(name = "notice_id")
    private String noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
