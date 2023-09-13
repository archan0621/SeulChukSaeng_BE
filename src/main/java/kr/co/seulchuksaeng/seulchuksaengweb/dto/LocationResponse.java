package kr.co.seulchuksaeng.seulchuksaengweb.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse {

    private Double x;
    private Double y;

    public LocationResponse(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
