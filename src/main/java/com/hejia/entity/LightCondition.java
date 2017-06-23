package com.hejia.entity;

public class LightCondition {

    private int courtesyLamp = 0;    //踏步灯   0：关闭  1：打开
    private int roomLamp = 0;    //室内灯   0：关闭  1：打开
    private int driverLamp = 0;    //司机灯  0：关闭  1：打开
    private int eleCompartmentLamp = 0;   //电气仓灯   0：关闭  1：打开
    
    
    public int getCourtesyLamp() {
        return courtesyLamp;
    }
    public void setCourtesyLamp(int courtesyLamp) {
        this.courtesyLamp = courtesyLamp;
    }
    public int getRoomLamp() {
        return roomLamp;
    }
    public void setRoomLamp(int roomLamp) {
        this.roomLamp = roomLamp;
    }
    public int getDriverLamp() {
        return driverLamp;
    }
    public void setDriverLamp(int driverLamp) {
        this.driverLamp = driverLamp;
    }
    public int getEleCompartmentLamp() {
        return eleCompartmentLamp;
    }
    public void setEleCompartmentLamp(int eleCompartmentLamp) {
        this.eleCompartmentLamp = eleCompartmentLamp;
    }

}
