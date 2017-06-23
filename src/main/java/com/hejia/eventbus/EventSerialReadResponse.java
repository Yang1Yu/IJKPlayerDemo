package com.hejia.eventbus;

/*
说明  	响应串口服务操作请求（反馈）
参数说明	key：主键（和request对应）
        values：返回值
发布者	MainActivity
        云诊断模块
订阅者	SerialReadService
*/
public class EventSerialReadResponse {
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

    public EventSerialReadResponse(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
