package com.hejia.eventbus;

import java.util.Objects;

/*
说明	    地图服务操作请求
参数说明	key：主键
        key：值为“LOCATION”时表示定位
        arg1：无意义
        arg2：AMapLocation型定位数据
        key：值为“SPEED”时表示速度
        arg1：无意义
        arg2：float型车速数据
发布者	MapService
订阅者	地图模块
*/
public class EventMapRequest {
    private String key;
    private String arg1;
    private Object arg2;

    public EventMapRequest(String key, String arg1, Object arg2) {
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

    public Object getArg2() {
        return arg2;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }
}
