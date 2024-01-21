package kr.co.seulchuksaeng.seulchuksaengweb.exception.member;

public class EventNotStartException extends MemberException {
    public EventNotStartException() {
        super("경기 시작 한시간 전부터 출석이 가능합니다.");
    }
}
