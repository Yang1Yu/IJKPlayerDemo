package com.hejia.eventbus;

/*
说明	    语音识别服务操作请求
参数说明
        key：值为“IATINIT”时表示初始化语音拼写对象
        arg1：无意义
        arg2：Context 上下文
发布者	main模块
订阅者	语音识别模块
*/
public class EventIATRequest {
    private String key;
    private String arg1;
    private Object arg2;

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

    public Object getArg2() {
        return arg2;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }

    public EventIATRequest(String key, String arg1, Object arg2) {

        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
