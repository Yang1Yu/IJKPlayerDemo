package com.hejia.eventbus;

/*
说明	    响应远端操作请求（反馈）
参数说明	key：主键（和request对应）
        values：返回值
发布者
订阅者	§MotorcadeService
*/
public class EventRemoteResponse {
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

    public EventRemoteResponse(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
