package kr.co.seulchuksaeng.seulchuksaengweb.httpProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.seulchuksaeng.seulchuksaengweb.httpProvider.resp.AddressResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.httpProvider.resp.LocationResponse;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.NetworkException;
import kr.co.seulchuksaeng.seulchuksaengweb.exception.event.NotValidAddressException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NaverMapApiProvider {

    @Value("${mapApi.Secret}")
    private String mapApiSecret;

    @Value("${mapApi.Id}")
    private String mapApiId;

    public WebClient getConnection(String url) {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", mapApiId)
                .defaultHeader("X-NCP-APIGW-API-KEY", mapApiSecret)
                .build();
    }

    @Cacheable(value = "locationCache", key = "#location")
    public LocationResponse checkLocation(String location) {

        WebClient webClient = getConnection("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode");

        Mono<ResponseEntity<String>> responseMono = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", location)
                        .build())
                .retrieve()
                .toEntity(String.class);

        // 응답 처리
        ResponseEntity<String> responseEntity = responseMono.block();
        if (responseEntity == null) {
            throw new NetworkException();
        }

        HttpStatusCode httpStatus = responseEntity.getStatusCode();
        if (httpStatus != HttpStatus.OK) {
            throw new NetworkException();
        }

        String responseBody = responseEntity.getBody();
        if (responseBody != null && !responseBody.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();

            AddressResponse addressResponse = null;
            try {
                addressResponse = objectMapper.readValue(responseBody, AddressResponse.class);
            } catch (JsonProcessingException e) {
                throw new NetworkException();
            }

            if (addressResponse.getMeta().getTotalCount() == 0 || addressResponse.getMeta().getTotalCount() > 1) {
                throw new NotValidAddressException();
            }

            Double x = Double.valueOf(addressResponse.getAddresses().get(0).getX());
            Double y = Double.valueOf(addressResponse.getAddresses().get(0).getY());

            return new LocationResponse(x, y);
        } else {
            throw new NotValidAddressException();
        }
    }

}
