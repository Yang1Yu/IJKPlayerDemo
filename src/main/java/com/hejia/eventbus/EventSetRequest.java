package com.hejia.eventbus;

/*
说明	    程序进行设置操作请求
参数说明	key：主键
        key：值为“CHANGE”时表更改时间
        arg1：要更改的时间
        key：值为“UPDATE”时表更新时间
        arg1：要更新的时间
        key：值为“UPGRADE”时表示开始升级
        arg1：无意义
        arg2：预留
发布者	设置模块
订阅者	SetService
*/
public class EventSetRequest {
    private String key;
    private String arg1;
    private String arg2;

    public EventSetRequest(String key, String arg1, String arg2) {
        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

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
}
