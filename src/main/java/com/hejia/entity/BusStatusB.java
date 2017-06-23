package com.hejia.entity;

public class BusStatusB {

    private int busSpeed = 0;	//车速----1LSB = 1/256 km/h, 偏移：0
    private int orderTorque = 0;   //指令扭矩----1LSB = 1Nm，偏移：0
    private int socStatus = 0;	//SOC状态----1LSB = 1%，偏移：0
    private int systemError = 0;//系统故障----0:无故障，1~255：故障码
	private int more = 0;
	
	
    public int getBusSpeed() {
        return busSpeed;
    }
    public void setBusSpeed(int busSpeed) {
        this.busSpeed = busSpeed;
    }
    public int getOrderTorque() {
        return orderTorque;
    }
    public void setOrderTorque(int orderTorque) {
        this.orderTorque = orderTorque;
    }
    public int getSocStatus() {
        return socStatus;
    }
    public void setSocStatus(int socStatus) {
        this.socStatus = socStatus;
    }
    public int getSystemError() {
        return systemError;
    }
    public void setSystemError(int systemError) {
        this.systemError = systemError;
    }


}
