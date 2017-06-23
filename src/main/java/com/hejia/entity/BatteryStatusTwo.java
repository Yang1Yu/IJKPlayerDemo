package com.hejia.entity;

import java.text.DecimalFormat;

public class BatteryStatusTwo {

	private float soc = 0;           	     			//soc
	private float soh = 0;               			  //soh
	private float allA=0;            			  //总电流
	private float maxChargeA=0;           		  //最大允许充电电流
	private float maxDischargeA=0;           		  //最大允许放电电流
	public float getSoc() {
		return soc;
	}
	public void setSoc(int soc) {
		this.soc = (float) (soc*0.4);
	}
	public float getSoh() {
		return soh;
	}
	public void setSoh(int soh) {
		this.soh = (float) (soh*0.4);
	}
	public float getAllA() {
		return allA;
	}
	public void setAllA(int allA) {
		this.allA = (float) (allA*0.1-1000);
	}
	public float getMaxChargeA() {
		return maxChargeA;
	}
	public void setMaxChargeA(int maxChargeA) {
		this.maxChargeA = (float) (maxChargeA*0.1);
	}
	public float getMaxDischargeA() {
		return maxDischargeA;
	}
	public void setMaxDischargeA(int maxDischargeA) {
		this.maxDischargeA = (float) (maxDischargeA*0.1);
	}



}
