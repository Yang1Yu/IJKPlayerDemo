package com.hejia.entity;

public class DCDCStatus {
	private String workStatus = "";
	private float outA = 0;
	private float outV = 0;
	private int  conyrolT = 0;
	private String allLineStatue = "";
	private String hardStatus = "";
	private String TStatus = "";
	private String intAStatus = "";
	private String intVStatus = "";
	private String outVStatus = "";
	private int busbarV = 0;
	private int lifecycle = 0;
	
	
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(int workStatus) {
		switch (workStatus) {
			case 0:
				this.workStatus = "就绪";
				break;
			case 1:
				this.workStatus = "运行";
				break;
			case 2:
				this.workStatus = "故障";
				break;
		default:
			break;
		}
	}
	public float getOutA() {
		return outA;
	}
	public void setOutA(int outA) {
		this.outA = (float) (outA*0.5);
	}
	public float getOutV() {
		return outV;
	}
	public void setOutV(int outV) {
		this.outV = (float) (outV*0.5);
	}
	public int getConyrolT() {
		return conyrolT;
	}
	public void setConyrolT(int conyrolT) {
		this.conyrolT = conyrolT-40;
	}
	public String getAllLineStatue() {
		return allLineStatue;
	}
	public void setAllLineStatue(int allLineStatue) {
		switch (allLineStatue) {
			case 0:
				this.allLineStatue = "正常";
				break;
			case 1:
				this.allLineStatue = "故障";
				break;
		default:
			break;
		}
	}
	public String getHardStatus() {
		return hardStatus;
	}
	public void setHardStatus(int hardStatus) {
		switch (hardStatus) {
			case 0:
				this.hardStatus = "正常";
				break;
			case 1:
				this.hardStatus = "故障";
				break;
		default:
			break;
		}
	}
	public String getTStatus() {
		return TStatus;
	}
	public void setTStatus(int tStatus) {
		switch (tStatus) {
			case 0:
				this.TStatus = "正常";
				break;
			case 1:
				this.TStatus = "过温";
				break;
		default:
			break;
		}
	}
	public String getIntAStatus() {
		return intAStatus;
	}
	public void setIntAStatus(int intAStatus) {
		switch (intAStatus) {
			case 0:
				this.intAStatus = "正常";
				break;
			case 1:
				this.intAStatus = "过流";
				break;
		default:
			break;
		}
	}
	public String getIntVStatus() {
		return intVStatus;
	}
	public void setIntVStatus(int intVStatus1,int intVStatus2) {

		switch (intVStatus1) {
			case 0:
				this.intVStatus = "正常";
				break;
			case 1:
				this.intVStatus = "过压";
				break;
		default:
			break;
		}
		switch (intVStatus2) {
			case 0:
//			this.intVStatus = "正常";
				break;
			case 1:
				this.intVStatus = "欠压";
				break;
		default:
			break;
		}
	}

	public String getOutVStatus() {
		return outVStatus;
	}
	public void setOutVStatus(int outVStatus1,int outVStatus2) {
		switch (outVStatus1) {
			case 0:
				this.outVStatus = "正常";
				break;
			case 1:
				this.outVStatus = "过压";
				break;
		default:
			break;
		}
		switch (outVStatus2) {
			case 0:
//			this.outVStatus = "正常";
				break;
			case 1:
				this.outVStatus = "欠压";
				break;
		default:
			break;
		}
	}

	public int getBusbarV() {
		return busbarV;
	}
	public void setBusbarV(int busbarV) {
		this.busbarV = busbarV;
	}
	public int getLifecycle() {
		return lifecycle;
	}
	public void setLifecycle(int lifecycle) {
		this.lifecycle = lifecycle;
	}
	
	
}
