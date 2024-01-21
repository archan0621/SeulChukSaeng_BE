package kr.co.seulchuksaeng.seulchuksaengweb.exception.event;

public class ParticipantGenderChangeException extends EventException {
    public ParticipantGenderChangeException() { super("경기에 참여한 인원이 있어서 성별을 변경할 수 없습니다! "); }
}
