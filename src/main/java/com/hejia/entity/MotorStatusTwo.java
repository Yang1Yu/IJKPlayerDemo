package com.hejia.entity;

public class MotorStatusTwo {

	private int motorTemper = 0;               //电机温度
	private int controllerTemper = 0;            //控制器温度
	private int controllerVoltage = 0;              //控制器电压
	private int driveTorque = 0;             //驱动扭矩限制
	private int electTorque = 0;                 //发电扭矩限制
    
    
    public int getMotorTemper() {
		return motorTemper;
	}
	public void setMotorTemper(int motorTemper) {
		this.motorTemper = motorTemper-40;
	}
	public int getControllerTemper() {
		return controllerTemper;
	}
	public void setControllerTemper(int controllerTemper) {
		this.controllerTemper = controllerTemper-40;
	}
	public int getControllerVoltage() {
		return controllerVoltage;
	}
	public void setControllerVoltage(int controllerVoltage) {
		this.controllerVoltage = controllerVoltage;
	}
	public int getDriveTorque() {
		return driveTorque;
	}
	public void setDriveTorque(int driveTorque) {
		this.driveTorque = driveTorque;
	}
	public int getElectTorque() {
		return electTorque;
	}
	public void setElectTorque(int electTorque) {
		this.electTorque = electTorque;
	}

}
