package com.hejia.entity;

public class BusStatusA {

    private int elecMagRotate = 0;	//电机转速----电机转速值，1LSB = 0.5RPM, 偏移: -10000
    private int engineRotate = 0;   //发动机转速----发动机转速值，1LSB = 0.5RPM, 偏移: -10000
    private int gearInfor = 0;	//挡位信息----0: 倒挡, 1: 空挡, 2:自动前进档, 3~10: 手动前进档1~8, 11: 驻车档, 12~15:无效
    private int doorStatus = 0;//钥匙门状态----0：OFF，1：ACC，2：ON，3：START
    private int more = 0;
    private int brakePlace = 0;	//刹车踏板位置----1LSB = 0.4, 偏移：0
    private int ebdPlace = 0;	//	制动踏板位置----1LSB = 0.4, 偏移：0
	
    public int getElecMagRotate() {
        return elecMagRotate;
    }
    public void setElecMagRotate(int elecMagRotate) {
        this.elecMagRotate = elecMagRotate;
    }
    public int getEngineRotate() {
        return engineRotate;
    }
    public void setEngineRotate(int engineRotate) {
        this.engineRotate = engineRotate;
    }
    public int getGearInfor() {
        return gearInfor;
    }
    public void setGearInfor(int gearInfor) {
        this.gearInfor = gearInfor;
    }
    public int getDoorStatus() {
        return doorStatus;
    }
    public void setDoorStatus(int doorStatus) {
        this.doorStatus = doorStatus;
    }
    public int getBrakePlace() {
        return brakePlace;
    }
    public void setBrakePlace(int brakePlace) {
        this.brakePlace = brakePlace;
    }
    public int getEbdPlace() {
        return ebdPlace;
    }
    public void setEbdPlace(int ebdPlace) {
        this.ebdPlace = ebdPlace;
    }


}
