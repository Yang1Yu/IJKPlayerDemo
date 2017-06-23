package com.hejia.eventbus;

/*
说明  	响应语音合成服务操作(反馈)
参数说明	key：主键（和request对应）
        values：返回值
发布者	SpeechTTSService
订阅者	进行语音合成操作请求界面
*/
public class EventTTSResponse {
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

    public EventTTSResponse(String key, String values) {

        this.key = key;
        this.values = values;
    }
}
