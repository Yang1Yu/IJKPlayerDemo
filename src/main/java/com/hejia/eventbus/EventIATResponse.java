package com.hejia.eventbus;

/*
说明  	响应语音合成服务操作(反馈)
参数说明  key：主键（和request对应）
        values：返回值
        key：值为“IATSTRING”时表示初始化语音拼写对象
        values：无意义
        key：值为“IATOVER”时表示初始化语音拼写对象
        values：无意义
发布者	SpeechIATActivity
订阅者	MAIN模块
*/
public class EventIATResponse {
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

    public EventIATResponse(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
