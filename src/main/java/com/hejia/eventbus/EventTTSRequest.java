package com.hejia.eventbus;

/*
说明	    语音合成服务操作请求
参数说明	key：值为“SPEECHTTS”表示开始语音合成
        arg1：传入的字符串
        arg2：预留
发布者	有语音合成需要界面
订阅者	SpeechTTSService
*/
public class EventTTSRequest {
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

    public EventTTSRequest(String key, String arg1, String arg2) {

        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
