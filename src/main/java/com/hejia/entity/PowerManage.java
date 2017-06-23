package com.hejia.entity;

public class PowerManage {


    private int outPutVolt12 = 0;   //12V传感器电源输出----0:关闭,1:使能,2:故障,3:无效
    private int outPutVolt5 = 0;   //5V传感器电源输出----0:关闭,1:使能,2:故障,3:无效
    private int outPutVolt = 0; //核心板电源输出----0:关闭,1:使能,2:故障,3:无效
    private int outPutLcd = 0;  //LCD背光电源输出----0:关闭,1:使能,2:故障,3:无效
    private int brightnessLcd = 0;  //LCD背光亮度----0: 关闭(0%) 15:最亮(100%)
    private int more1 = 0;  //预留
    private int amplPowerOut = 0;   //  功放电源输出----0:关闭,1:使能
    private int amplSilenOut = 0;   //  功放静音输出----0:关闭,1:使能
    private int more2 = 0;  //  预留
    private int  heatDiss1 = 0; //  散热风扇1输出----0: 关闭(0%)15:全速(100%)
    private int heatDiss2 = 0;  //  散热风扇2输出:----0: 关闭(0%)15:全速(100%)
    
	public int getOutPutVolt12() {
        return outPutVolt12;
    }
    public void setOutPutVolt12(int outPutVolt12) {
        this.outPutVolt12 = outPutVolt12;
    }
    public int getOutPutVolt5() {
        return outPutVolt5;
    }
    public void setOutPutVolt5(int outPutVolt5) {
        this.outPutVolt5 = outPutVolt5;
    }
    public int getOutPutVolt() {
        return outPutVolt;
    }
    public void setOutPutVolt(int outPutVolt) {
        this.outPutVolt = outPutVolt;
    }
    public int getOutPutLcd() {
        return outPutLcd;
    }
    public void setOutPutLcd(int outPutLcd) {
        this.outPutLcd = outPutLcd;
    }
    public int getBrightnessLcd() {
        return brightnessLcd;
    }
    public void setBrightnessLcd(int brightnessLcd) {
        this.brightnessLcd = brightnessLcd;
    }
    public int getAmplPowerOut() {
        return amplPowerOut;
    }
    public void setAmplPowerOut(int amplPowerOut) {
        this.amplPowerOut = amplPowerOut;
    }
    public int getAmplSilenOut() {
        return amplSilenOut;
    }
    public void setAmplSilenOut(int amplSilenOut) {
        this.amplSilenOut = amplSilenOut;
    }
    public int getHeatDiss1() {
        return heatDiss1;
    }
    public void setHeatDiss1(int heatDiss1) {
        this.heatDiss1 = heatDiss1;
    }
    public int getHeatDiss2() {
        return heatDiss2;
    }
    public void setHeatDiss2(int heatDiss2) {
        this.heatDiss2 = heatDiss2;
    }


}
