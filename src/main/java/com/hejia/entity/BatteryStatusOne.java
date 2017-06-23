package com.hejia.entity;

public class BatteryStatusOne {

	private int more1 = 0;              //预留
	private int more2 = 0;           		  //预留
	private String currentStatus = "" ;                 //当前状态
	private String relayStatus = "";              //继电器状态
	private String balancedState = "";           		  //均衡状态
	private int more3 = 0;                 //预留
	private String electLineStatus = "";              //充电线连接状态
	private String electMode = "";           		  //充电模式
	private String electStatus = "";                 //充电故障
	private String troubleGrade = "";              //故障等级
	private int more = 0;           		  //预留
	private String currentTrouble = "";                 //当前故障
	public int getMore1() {
		return more1;
	}
	public void setMore1(int more1) {
		this.more1 = more1;
	}
	public int getMore2() {
		return more2;
	}
	public void setMore2(int more2) {
		this.more2 = more2;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(int currentStatus) {
		switch (currentStatus) {
			case 0:
				this.currentStatus = "高压上电";
				break;
			case 1:
				this.currentStatus = "预充";
				break;
			case 2:
				this.currentStatus = "高压闭合";
				break;
			case 3:
				this.currentStatus = "高压上电失败";
				break;
		default:
			break;
		}
	}
	public String getRelayStatus() {
		return relayStatus;
	}
	public void setRelayStatus(int relayStatus) {
		switch (relayStatus) {
			case 1:
				this.relayStatus = "断开";
				break;
			case 2:
				this.relayStatus = "吸合";
				break;
		default:
			break;
		}
	}
	public String getBalancedState() {
		return balancedState;
	}
	public void setBalancedState(int balancedState) {
		switch (balancedState) {
			case 1:
				this.balancedState = "均衡结束";
				break;
			case 2:
				this.balancedState = "正在均衡";
				break;
		default:
			break;
		}
	}
	public int getMore3() {
		return more3;
	}
	public void setMore3(int more3) {
		this.more3 = more3;
	}
	public String getElectLineStatus() {
		return electLineStatus;
	}
	public void setElectLineStatus(int electLineStatus) {
		switch (electLineStatus) {
			case 1:
				this.electLineStatus = "未连接";
				break;
			case 2:
				this.electLineStatus = "已连接";
				break;
		default:
			break;
		}
	}
	public String getElectMode() {
		return electMode;
	}
	public void setElectMode(int electMode) {
		switch (electMode) {
			case 1:
				this.electMode = "直流充电";
				break;
			case 2:
				this.electMode = "交流充电";
				break;
		default:
			break;
		}
	}
	public String getElectStatus() {
		return electStatus;
	}
	public void setElectStatus(int electStatus) {
		switch (electStatus) {
			case 0:
				this.electStatus = "充电准备就绪";
				break;
			case 1:
				this.electStatus = "充电中";
				break;
			case 2:
				this.electStatus = "充电结束";
				break;
			case 3:
				this.electStatus = "充电机故障";
				break;
		default:
			break;
		}
	}
	public String getTroubleGrade() {
		return troubleGrade;
	}
	public void setTroubleGrade(int troubleGrade) {
		switch (troubleGrade) {
			case 0:
				this.troubleGrade = "无故障";
				break;
			case 1:
				this.troubleGrade = "严重故障，切断输出";
				break;
			case 2:
				this.troubleGrade = "轻微故障，降功率50%";
				break;
		default:
			break;
		}
	}
	public int getMore() {
		return more;
	}
	public void setMore(int more) {
		this.more = more;
	}
	public String getCurrentTrouble() {
		return currentTrouble;
	}
	public void setCurrentTrouble(int currentTrouble) {
		String[] strArray = {"Cell voltage too high 1","Cell voltage too high 2","Cell voltage too low 1" 
				,"Cell voltage too low 2","Cell temperature too high 1","Cell temperature too high 2","Cell temperature too low 1" 
				,"Cell temperature too low 2","Pack voltage too high 1","Pack voltage too high 2","Pack voltage too low 1" 
				,"Pack voltage too low 2","Charge current too high 1","Charge current too high 2","Discharge current too high 1" 
				,"Discharge current too high 2","SOC too high 1","SOC too high 2" ,"SOC too low 1","SOC too low2","Pack delt voltage too large 1" 
				,"Pack delt voltage too large 2" ,"Insulation resistance too low 1","Insulation resistance too low 2" 
				,"Cell delt voltage too large 1","Cell delt voltage too large 2","Cell delt temperature too large 1","Cell delt temperature too large 2" 
				,"BMS local CAN communication fault","AL Msd Fuse Open","Signal invalid"};
		switch (currentTrouble) {
		case 1:
			this.currentTrouble = strArray[0];
			break;
		case 2:
			this.currentTrouble = strArray[1];
			break;
		case 4:
			this.currentTrouble = strArray[2];
			break;
		case 5:
			this.currentTrouble = strArray[3];
			break;
		case 7:
			this.currentTrouble = strArray[4];
			break;
		case 8:
			this.currentTrouble = strArray[5];
			break;
		case 10:
			this.currentTrouble = strArray[6];
			break;
		case 11:
			this.currentTrouble = strArray[7];
			break;
		case 13:
			this.currentTrouble = strArray[8];
			break;
		case 14:
			this.currentTrouble = strArray[9];
			break;
		case 16:
			this.currentTrouble = strArray[10];
			break;
		case 17:
			this.currentTrouble = strArray[11];
			break;
		case 25:
			this.currentTrouble = strArray[12];
			break;
		case 26:
			this.currentTrouble = strArray[13];
			break;
		case 28:
			this.currentTrouble = strArray[14];
			break;
		case 29:
			this.currentTrouble = strArray[15];
			break;
		case 31:
			this.currentTrouble = strArray[16];
			break;
		case 32:
			this.currentTrouble = strArray[17];
			break;
		case 34:
			this.currentTrouble = strArray[18];
			break;
		case 35:
			this.currentTrouble = strArray[19];
			break;
		case 37:
			this.currentTrouble = strArray[20];
			break;
		case 38:
			this.currentTrouble = strArray[21];
			break;
		case 40:
			this.currentTrouble = strArray[22];
			break;
		case 41:
			this.currentTrouble = strArray[23];
			break;
		case 43:
			this.currentTrouble = strArray[24];
			break;
		case 44:
			this.currentTrouble = strArray[25];
			break;
		case 49:
			this.currentTrouble = strArray[26];
			break;
		case 50:
			this.currentTrouble = strArray[27];
			break;
		case 85:
			this.currentTrouble = strArray[28];
			break;
		case 87:
			this.currentTrouble = strArray[29];
			break;
		case 255:
			this.currentTrouble = strArray[30];
			break;
		default:
			break;
		}
	}
    
    

}
