package com.hejia.eventbus;

/*
说明	    响应收音机操作
构造函数	EventRadioRequest
        （String key，String arg1，String arg2 ）
        key：值为“RADIOON”表示打开收音机
        arg1：传默认值，无作用
        arg2：传默认值，无作用
        key：值为“RADIOOFF”表示关闭收音机
        arg1：传默认值，无作用
        arg2：传默认值，无作用
        key：值为“RESETFREQ”表示开机根据数据库数据设置当前播放的频道
        arg1：传默认值，无作用
        arg2：传默认值，无作用
发布者	有收音机相关操作需要界面
订阅者	FragmentRadio
*/
public class EventRadioRequest {
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

    public EventRadioRequest(String key, String arg1, String arg2) {

        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
