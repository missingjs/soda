package soda.web.resp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResponse extends BaseResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Object body;

    public JsonResponse(int httpCode, Object data) {
        super(httpCode, "application/json; charset=utf-8");
        body = data;
    }

    public JsonResponse(int httpCode) {
        this(httpCode, null);
    }

    @Override
    public byte[] getBody() {
        try {
            return objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setData(Object data) {
        this.body = data;
    }
    
}
