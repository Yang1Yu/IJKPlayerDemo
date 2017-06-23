package com.hejia.eventbus;

/*
说明  	串口服务相关操作请求（读：多功能方向盘）
参数说明	key：主键
        arg1：传入的值
        key：值为“AIRCONDITION”表示空调状态
        arg1：传默认值，无作用
        key：值为“AIRFANCONDITION”表示气扇状态
        arg1：传默认值，无作用
        key：值为“AIRPUMPSTATUS”表示气泵状态
        arg1：传默认值，无作用
        key：值为“BATTERYBAGINFOR”表示电池包信息
        arg1：传默认值，无作用
        key：值为“BATTERYSTATUSFIVE”表示电池状态信息5
        arg1：传默认值，无作用
        key：值为“BATTERYSTATUSFOUR”表示电池状态信息4
        arg1：传默认值，无作用
        key：值为“BATTERYSTATUSONE”表示电池状态信息1
        arg1：传默认值，无作用
        key：值为“BATTERYSTATUSTHREE”表示电池状态信息3
        arg1：传默认值，无作用
        key：值为“BATTERYSTATUSTWO”表示电池状态信息2
        arg1：传默认值，无作用
        key：值为“BUSSTATUSA”表示车身状态A
        arg1：传默认值，无作用
        key：值为“BUSSTATUSB”表示车身状态B
        arg1：传默认值，无作用
        key：值为“DCDCSTATUS”表示DCDC状态
        arg1：传默认值，无作用
        key：值为“FLEXOWHEELCONDITION”表示多功能方向盘
        arg1：传默认值，无作用
        key：值为“LIGHTCONDITION”表示灯光状态
        values：传默认值，无作用
        key：值为“MCUHEARTBEAT”表示MCU心跳计数器
        arg1：传默认值，无作用
        key：值为“MCUVERSIONS”表示气MCU版本信息
        arg1：传默认值，无作用
        key：值为“MOTORSTATUSONE”表示电机状态报文1
        arg1：传默认值，无作用
        key：值为“MOTORSTATUSTWO”表示电机状态报文2
        arg1：传默认值，无作用
        key：值为“MOTORSTATUSTROUBLE”表示电机故障报文
        arg1：传默认值，无作用
        key：值为“NUMIOSTATUS”表示数字IO状态
        arg1：传默认值，无作用
        key：值为“OILPUMPSTATUS”表示油泵状态
        arg1：传默认值，无作用
        key：值为“POWERMANAGE”表示电源管理状态
        arg1：传默认值，无作用
        key：值为“SIMULIOSTATUSA”表示模拟IO状态A
        arg1：传默认值，无作用
        key：值为“SIMULIOSTATUSB”表示模拟IO状态B
        arg1：传默认值，无作用
        key：值为“TVCONDITION”表示电视开关/翻转折叠状态
        arg1：传默认值，无作用

发布者	SerialReadService
订阅者	MainActivity
        云诊断模块
*/
public class EventSerialReadRequest {
    private String key;
    private String arg1;

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

    public EventSerialReadRequest(String key, String arg1) {

        this.key = key;
        this.arg1 = arg1;
    }
}
