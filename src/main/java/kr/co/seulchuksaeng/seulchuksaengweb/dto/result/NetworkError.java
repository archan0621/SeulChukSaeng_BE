package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

public record NetworkError(String result, String message) {
    @Override
    public String toString() {
        return "{\"result\":\"" + result + "\",\"message\":\"" + message + "\"}";
    }
}
