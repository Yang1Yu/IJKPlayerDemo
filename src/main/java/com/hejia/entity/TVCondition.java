package com.hejia.entity;

public class TVCondition {
    private int TVSwitch = 0; //电视开关      0：关闭   1：打开
    private int  overturnSwitch = 0;  //翻转装置     0：关闭   1：打开
    
    
    public int getTVSwitch() {
        return TVSwitch;
    }
    public void setTVSwitch(int tVSwitch) {
        TVSwitch = tVSwitch;
    }
    public int getOverturnSwitch() {
        return overturnSwitch;
    }
    public void setOverturnSwitch(int overturnSwitch) {
        this.overturnSwitch = overturnSwitch;
    }

}
