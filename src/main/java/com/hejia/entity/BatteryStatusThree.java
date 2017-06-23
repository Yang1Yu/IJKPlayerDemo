package com.hejia.entity;

public class BatteryStatusThree {

	private int posiInsR=0;           	//正极绝缘电阻
	private int negaInsR=0;                 //负极绝缘电阻
	private float batteryHV=0;              //电池端高压
	private float genHV=0;           		  //母线端高压




	public int getPosiInsR() {
		return posiInsR;
	}
	public void setPosiInsR(int posiInsR) {
		this.posiInsR = posiInsR;
	}
	public int getNegaInsR() {
		return negaInsR;
	}
	public void setNegaInsR(int negaInsR) {
		this.negaInsR = negaInsR;
	}
	public float getBatteryHV() {
		return batteryHV;
	}
	public void setBatteryHV(int batteryHV) {	
		this.batteryHV = (float) (batteryHV*0.1);
	}
	public float getGenHV() {
		return genHV;
	}
	public void setGenHV(int genHV) {	
		this.genHV = (float) (genHV*0.1);
	}
    
    

}
