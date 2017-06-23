package com.hejia.eventbus;

/*
说明	    音源转换操作请求
参数说明	key：主键，值为“AUDIO”时表示进行音源切换
        values：要切换的音源（
        audioBtDialCall表示切换至蓝牙电话、audioMediaOn表示切换至多媒体、
        audioRadioOff表示关闭收音机切回之前音源、
        audioRadioOn表示切至收音机、
        audioBtMusic表示切至蓝牙音乐、audioBtDialCut表示蓝牙电话挂断切回之前音源）
发布者	需进行音源转换界面
订阅者	MainActivity
*/
public class EventAudioRequest {
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

    public EventAudioRequest(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
