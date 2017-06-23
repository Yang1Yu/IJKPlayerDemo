package com.hejia.entity;

public class MotorStatusOne {

	private int rote = 0;               //电机转速
	private int torque = 0;            //电机扭矩
	private int electricity = 0;       //母线电流
	private String troubleGrade = "";      //故障等级
	private String workMode = "";        //工作模式
    
    public int getRote() {
		return rote;
	}
	public void setRote(int rote) {
		this.rote = rote-6000;
	}
	public int getTorque() {
		return torque;
	}
	public void setTorque(int torque) {
		this.torque = torque-3200;
	}
	public int getElectricity() {
		return electricity;
	}
	public void setElectricity(int electricity) {
		this.electricity = electricity-1000;
	}
	public String getTroubleGrade() {
		return troubleGrade;
	}
	public void setTroubleGrade(int troubleGrade) {
		switch (troubleGrade) {
			case 0:
				this.troubleGrade = "无故障";
				break;
			case 2:
				this.troubleGrade = "警告限功率";
				break;
			case 3:
				this.troubleGrade = "故障关断电机";
				break;
			case 4:
				this.troubleGrade = "严重故障停车";
				break;
			default:
			break;
		}
	}
	public String getWorkMode() {
		return workMode;
	}
	public void setWorkMode(int workMode) {
		switch (workMode) {
			case 0:
				this.workMode = "就绪";
				break;
			case 1:
				this.workMode = "使能";
				break;
			case 2:
				this.workMode = "高压上电";
				break;
			case 3:
				this.workMode = "错误";
				break;

			default:
			break;
		}
	}


    
    



}
