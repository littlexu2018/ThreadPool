package com.xuhh.threadpool.test;

public class Request {
    private String method;
    private Integer param;
    private String servieName;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getParam() {
        return param;
    }

    public void setParam(Integer param) {
        this.param = param;
    }

    public String getServieName() {
        return servieName;
    }

    public void setServieName(String servieName) {
        this.servieName = servieName;
    }
}
