package com.hejia.eventbus;

/*
说明	    主界面响应Fragment操作请求
参数说明
        key:主键
        values:返回值
发布者	MainActivity
订阅者	对MainActivity发送请求的界面
*/
public class EventFragmentResponse {
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

    public EventFragmentResponse(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
