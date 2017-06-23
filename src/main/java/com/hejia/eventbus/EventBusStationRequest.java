package com.hejia.eventbus;

/*
说明      公交报站操作请求
参数说明	key：主键
        arg1：传入的值
        arg2：预留
发布者	§BusStationService
订阅者
*/
public class EventBusStationRequest {
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

    public EventBusStationRequest(String key, String arg1, Object arg2) {

        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
