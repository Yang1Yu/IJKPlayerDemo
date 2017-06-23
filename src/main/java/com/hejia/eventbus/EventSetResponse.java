package com.hejia.eventbus;

/*
说明	    响应设置程序操作请求（反馈）
参数说明	key：主键（和request对应）
        values：返回值
发布者	SetService
订阅者	设置模块
        MainActivity
*/
public class EventSetResponse {
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

    public EventSetResponse(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
