package com.hejia.eventbus;

/*
说明	    远端操作请求（车队管理、上传数据）
参数说明	key：主键
        arg1：传入的值
        arg2：预留
发布者	§MotorcadeService
订阅者
*/
public class EventRemoteRequest {
    private String key;
    private String arg1;
    private String arg2;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public EventRemoteRequest(String key, String arg1, String arg2) {

        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
