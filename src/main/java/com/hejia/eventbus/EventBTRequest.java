package com.hejia.eventbus;

import java.util.Objects;

/*
说明	    蓝牙服务进行操作请求
参数说明	key：主键
        key：值为“BTCONN”时表示有设备与蓝牙相连
        arg1：连接设备名
        arg2：无意义
        key：值为“BTDISCONN”时表示断开蓝牙相连
        arg1：是否弹出pop pop nopop
        arg2：无意义
        key：值为“PB”时表示电话簿
        arg1：接收到的电话簿中姓名
        arg2：接收到的电话簿中号码
        key：值为“HISTORY”时表示通话记录
        arg1：接收到的通话记录中姓名
        arg2：接收到的通话记录中号码
        key：值为“BTMUSICINFO”时表示有蓝牙音乐相关信息传入
        arg1：蓝牙音乐歌曲名
        arg2：蓝牙音乐歌手名
        arg3: 蓝牙音乐状态
        arg4：蓝牙音乐总时长
        key：值为“HFCCIN”时表示有电话呼出
        arg1：拨号状态码（3.5表示拨号中，2表示正在通话中，9表示其他）
        arg2：呼入或呼出号码
        key：值为“CID”时表示有电话呼入
        arg1：拨号状态码（3.5表示拨号中，2表示正在通话中，9表示其他）
        arg2：呼入或呼出号码
        key：值为“HFCCINCUT”时表示呼出电话挂断
        arg1：无意义
        arg2：无意义
        key：值为“CIDCUT”时表示呼入电话挂断
        arg1：无意义
        arg2：无意义
        key：值为“CLOSE”时表示关闭蓝牙音乐
        arg1：无意义
        arg2：无意义
        key：主键，值为“SSPPIN”时表示首次配对
        ssppin为传入的配对码
        arg1：配对码
        arg2：无意义
        key：值为“BTMUSICPOS”时开始接收蓝牙音乐播放位置
        arg1：蓝牙音乐当前播放状态
        arg2：蓝牙音乐当前播放位置
        key：值为“BTMUSICSTATUS”时表示蓝牙音乐状态有变化
        arg1：蓝牙音乐状态码
        （0停止1正在播放2暂停）
        arg2：无意义
        §key：值为“NOLIMITS”时表示无电话簿权限
        §arg1：接收到反馈
        §arg2：无意义
发布者	BluetoothService
订阅者	MainActivity
        蓝牙模块
*/
public class EventBTRequest {
    private String key;
    private String arg1;
    private Object arg2;
    private String arg3;
    private Object arg4;

    public EventBTRequest(String key, String arg1, Object arg2) {
        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    public Object getArg4() {
        return arg4;
    }

    public void setArg4(Object arg4) {
        this.arg4 = arg4;
    }

    public EventBTRequest(String key, String arg1, Object arg2, String arg3, Object arg4) {

        this.key = key;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
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
