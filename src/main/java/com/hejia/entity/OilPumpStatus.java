package com.hejia.entity;

public class OilPumpStatus {

	
	private float outV = 0;
	private float outA = 0;
	private String workStatus = "";
	private String allLineStatue = "";
	private String TStatus = "";	
	private String intAStatus = "";
	private String intVStatus = "";
	private String phaseStatus = "";
	private int lifecycle = 0;
	
	
	public float getOutV() {
		return outV;
	}
	public void setOutV(int outV) {
		this.outV = (float) (outV*0.1);
	}
	public float getOutA() {
		return outA;
	}
	public void setOutA(int outA) {
		this.outA = (float) (outA*0.1);
	}
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
	public String getAllLineStatue() {
		return allLineStatue;
	}
	public void setAllLineStatue(int allLineStatue) {
		switch (allLineStatue) {
			case 0:
				this.allLineStatue = "正常";
				break;
			case 1:
				this.allLineStatue = "过温";
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
	public void setIntVStatus(int intVStatus) {
		switch (intVStatus) {
			case 0:
				this.intVStatus = "正常";
				break;
			case 1:
				this.intVStatus = "过压";
				break;
		default:
			break;
		}
	}
	public String getPhaseStatus() {
		return phaseStatus;
	}
	public void setPhaseStatus(int phaseStatus1,int phaseStatus2) {
		switch (phaseStatus1) {
			case 0:
				this.phaseStatus = "正常";
				break;
			case 1:
				this.phaseStatus = "短相";
				break;
		default:
			break;
		}
		switch (phaseStatus2) {
		case 0:
//			this.intVStatus = "正常";
			break;
			case 1:
				this.phaseStatus = "缺相";
				break;
		default:
			break;
		}
	}

	public int getLifecycle() {
		return lifecycle;
	}
	public void setLifecycle(int lifecycle) {
		this.lifecycle = lifecycle;
	}
	


}
