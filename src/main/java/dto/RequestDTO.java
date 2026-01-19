package dto;

import java.util.Map;

public class RequestDTO {

    private String method;
    private Map<String, Integer> queryString;
    private Map<String, Object> body;

    public RequestDTO(String method,
                      Map<String, Integer> queryString,
                      Map<String, Object> body) {
        this.method = method;
        this.queryString = queryString;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Integer> getQueryString() {
        return queryString;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}
