package com.hejia.eventbus;

/*
说明    程序进行初始化或正常退出销毁等操作
参数说明
        key：值为“INIT”时初始化各个界面
        key：值为“DESTORY”时释放各个界面资源
        key: 值为“INITMAIN”时为初始化主界面
发布者	MainActivity
订阅者	各个模块
*/
public class EventSystem {
    private String key;

    public EventSystem(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
