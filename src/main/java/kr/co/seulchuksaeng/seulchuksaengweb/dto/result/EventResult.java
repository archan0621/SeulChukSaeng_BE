package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventReadInnerResult;
import kr.co.seulchuksaeng.seulchuksaengweb.dto.result.innerResult.EventShowcaseInnerResult;
import lombok.Getter;

import java.util.List;

public record EventResult() {

    public record Create(String result, String message) {}

    public record Read(String result, String message, EventReadInnerResult readResult) {}

    public record Update(String result, String message) {}

    public record Remove(String result, String message) {}

    public record Showcase(String result, String message, List<EventShowcaseInnerResult> eventShowcase, int showcaseCount) {}
}