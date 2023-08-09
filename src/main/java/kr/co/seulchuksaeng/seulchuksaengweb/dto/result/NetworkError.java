package kr.co.seulchuksaeng.seulchuksaengweb.dto.result;

public class NetworkError {
    private String result;

    private String message;

    public NetworkError(String result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{\"result\":\"" + result + "\",\"message\":\"" + message + "\"}";
    }
}
