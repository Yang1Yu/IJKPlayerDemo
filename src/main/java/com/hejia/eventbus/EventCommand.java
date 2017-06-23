package com.hejia.eventbus;


/*
说明	    MainActivity主动向其他界面发送命令
        （即无请求时直接进行非响应操作）
参数说明	key：主键，不同模块主键不同
        （cloud navi moni media radio bt set all）
        arg1：传递的值
        arg2：传递的值
        key：值为“SUBCOMMAND”表示获取当前界面操作的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“CLOUDCOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“NAVICOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“MONICOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“MEDIACOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“RADIOCOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“BTCOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“SETCOMMAND”表示获取该界面相关的语音命令
        arg1：传语音识别的整条语句
        arg2：传默认值，无作用
        key：值为“SURFACEVIEW”表示surfaceview相关
        arg1：要操作的界面 NaviMap Moni
        arg2：隐藏或显示 HIDE SHOW
        key：值为“NOWFRAGMENT”表示当前所处界面
        arg1：当前所处界面
        arg2：默认值“default”
发布者	MainActivity
订阅者	各个模块及服务
*/
public class EventCommand {
    private String key;
    private String arg1;
    private Object arg2;

    public EventCommand(String key, String arg1, Object arg2) {
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
