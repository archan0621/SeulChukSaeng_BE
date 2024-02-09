package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.domain.UserRole;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberDetailInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.MemberListInnerResult;
import lombok.Getter;

import java.util.List;

public record MemberResult() {

    public record Join(String result, String message) {}

    public record Login(String result, String message, String token) {}

    public record Remove(String result, String message) {}

    public record GetUserName(String result, String message, UserRole userRole, Long memberId) {}

    public record MemberList(String result, String message, List<MemberListInnerResult> memberList) {}

    public record Detail(String result, String message, MemberDetailInnerResult.MemberInfo memberInfo, MemberDetailInnerResult.rate rate, List<MemberDetailInnerResult.joinedGame> joinedGame) {}

}
