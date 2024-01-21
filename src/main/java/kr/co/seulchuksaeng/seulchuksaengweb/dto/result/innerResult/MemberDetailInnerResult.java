package kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.Gender;

public record MemberDetailInnerResult() {

    public record MemberInfo(Long id, String name, Gender gender, String phone, int warnPoint) {}

    public record rate(int totalGame, int joinedGame, Long attendedGame, Long lateGame, Long absentGame) {}

    public record joinedGame(Long eventId, String eventTitle){}

}
