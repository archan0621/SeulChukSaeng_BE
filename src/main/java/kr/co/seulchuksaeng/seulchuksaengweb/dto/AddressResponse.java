package kr.co.seulchuksaeng.seulchuksaengweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponse {
    @Getter
    private String status;
    @Getter
    private Meta meta;
    private List<Address> addresses;
    @Getter
    private String errorMessage;

    public AddressResponse() {
    }

    // Getter와 Setter 메서드들

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @JsonProperty("addresses")
    public List<Address> getAddresses() {
        return addresses;
    }

    @JsonProperty("addresses")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private int totalCount;
        private int page;
        private int count;

        // Getter와 Setter 메서드들
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        private String roadAddress;
        private String jibunAddress;
        private String englishAddress;
        private List<AddressElement> addressElements;
        private String x;
        private String y;
        private double distance;

        // Getter와 Setter 메서드들
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressElement {
        private List<String> types;
        private String longName;
        private String shortName;
        private String code;

        // Getter와 Setter 메서드들
    }
}