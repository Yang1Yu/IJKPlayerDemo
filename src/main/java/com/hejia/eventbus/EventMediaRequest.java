package com.hejia.eventbus;

/*
说明	    多媒体服务操作请求
构造函数	EventMediaRequest
        （String key，String values）
        参数说明	key：主键
        key：值为“MEDIASELECT”表示提醒选中音乐
        values：传默认值，无作用
        key：值为“CLOSEMEDIAMUSIC”表示关闭多媒体音乐
        values：传默认值，无作用
        key：值为“CLOSEMEDIAVIDEO”表示关闭多媒体视频
        values：传默认值，无作用
        key：值为“PAUSE”表示暂停多媒体
        values：传默认值，无作用
        key：值为“MEDIAPOS”表示播放进度
        values：传播放器中具体播放位置
        key：值为“MEDIALOOP”表示多媒体播放循环方式
        values：传播放模式
        （PLAY_ORDER、PLAY_RADOM、PLAY_ONE）
        key：值为“MEDIADELETE”表示删除歌曲
        values：传默认值，无作用
        key：值为“MEDIAADD”表示添加歌曲
        values：传默认值，无作用
        key：值为“MEDIANULL”表示无音乐
        values：传默认值，无作用
        key：值为“MEDIAERR”表示多媒体异常处理
        values：传异常字符串
        key：值为“LISTENER”表示点击列表监听
        values：传默认值，无作用
        key：值为“USB”表示监听U盘相关
        values：UNMOUNTED表示U盘拔出
                MOUNTED表示U盘插入
        key：值为“NOFILE”表示此文件不存在
        values：传默认值，无作用

发布者	多媒体模块
订阅者	MediaService
*/
public class EventMediaRequest {
    private String key;
    private String values;
    private Object obj;

    public EventMediaRequest(String key, String values, Object obj) {
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
