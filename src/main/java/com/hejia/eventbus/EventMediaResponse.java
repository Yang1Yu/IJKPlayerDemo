package com.hejia.eventbus;

/**
 * Created by yining on 2017/3/16.
 */
/*
说明	    响应多媒体服务操作请求（反馈）
参数说明	key：主键（和request对应）
        values：返回值
        key：值为“MEDIAPLAY”表示播放状态
        values：传播放状态
        （play pause stop playmp3）
        key：值为“SEEKPOS”表示seekbar中进度
        values：传进度条中选中的位置
发布者	MediaService
订阅者	多媒体模块
*/
public class EventMediaResponse {
    private String key;
    private String values;
    private Object obj;

    public EventMediaResponse(String key, String values, Object obj) {
        this.key = key;
        this.values = values;
        this.obj = obj;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValues() {
        return values;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setValues(String values) {
        this.values = values;
    }

}
