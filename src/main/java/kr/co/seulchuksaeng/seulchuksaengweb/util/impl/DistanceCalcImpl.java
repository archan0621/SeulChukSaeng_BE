package kr.co.seulchuksaeng.seulchuksaengweb.util.impl;

import kr.co.seulchuksaeng.seulchuksaengweb.util.DistanceCalc;
import org.springframework.stereotype.Component;

@Component
public class DistanceCalcImpl implements DistanceCalc {
    private static final double EARTH_RADIUS = 6371000; // 지구 반지름 (미터)

    @Override
    public Double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 두 지점 사이의 위도 차이와 경도 차이 계산
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine 공식을 이용한 거리 계산
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // 미터 단위로 반환
    }

}
