package com.taotao.common.httpclient;

/**
 * httpclient请求结果接收对象
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月10日 下午3:41:00
 * we can not wait,we can not stop!
 */
public class HttpResult {

    private Integer code;

    private String body;
    
    public HttpResult() {
        
    }
    public HttpResult(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
