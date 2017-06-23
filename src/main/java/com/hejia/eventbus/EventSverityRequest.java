package com.hejia.eventbus;

/*
说明    最高优先级操作请求（车辆报警）
参数说明	key：主键
        arg1：传入的值
        arg2：预留
发布者	SerialReadService
订阅者	MainActivity
*/
public class EventSverityRequest {
    private String key;
    private String values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public EventSverityRequest(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
