package soda.groovy.web.resp

import groovy.json.JsonOutput

import java.nio.charset.StandardCharsets

class JsonResponse extends BaseResponse {

    private Object body

    JsonResponse(int httpCode, Object data) {
        super(httpCode, "application/json; charset=utf-8")
        this.body = data
    }

    @Override
    byte[] getBody() {
        return JsonOutput.toJson(body).getBytes(StandardCharsets.UTF_8)
    }

    void setData(Object data) {
        body = data
    }

}
