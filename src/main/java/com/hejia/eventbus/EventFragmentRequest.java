package com.hejia.eventbus;

/*
说明  	Fragment间进行操作交互操作请求
参数说明
        key：主键
        fm：被操作界面
        values：传递的值
发布者	需要进行Fragment间交互的界面（界面隐藏显示等）
订阅者	MainActivity
*/
public class EventFragmentRequest {
    private String key;
    private String fm;
    private String values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public EventFragmentRequest(String key, String fm, String values) {

        this.key = key;
        this.fm = fm;
        this.values = values;
    }
}
