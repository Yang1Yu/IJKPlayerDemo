package com.hejia.entity;

public class BatteryStatusFour {

	private int cellMaxT=0;           	//电芯最高温度
	private int cellMinT=0;                 //电芯最低温度
	private int cellAverT=0;              //电芯平均温度
	private String maxTCellCSCNum = "";           		  //最高温度电芯所在CSC编号
	private String maxTCellCSCLoc = "";           	//最高温度电芯所在CSC内位置
	private String minTCellCSCNum = "";                 //最低温度电芯所在CSC编号
	private String minTCellCSCLoc = "";              //最低温度电芯所在CSC内位置
	
	
	public int getCellMaxT() {
		return cellMaxT;
	}
	public void setCellMaxT(int cellMaxT) {
		this.cellMaxT = cellMaxT-50;
	}
	public int getCellMinT() {
		return cellMinT;
	}
	public void setCellMinT(int cellMinT) {
		this.cellMinT = cellMinT-50;
	}
	public int getCellAverT() {
		return cellAverT;
	}
	public void setCellAverT(int cellAverT) {
		this.cellAverT = cellAverT-50;
	}
	public String getMaxTCellCSCNum() {
		return maxTCellCSCNum;
	}
	public void setMaxTCellCSCNum(int maxTCellCSCNum) {

		String[] strArray = {"CSC1" ,"CSC2" ,"CSC3","CSC4","CSC5","CSC6","CSC7","CSC8","CSC9","CSC10","CSC11","CSC12" 
				,"CSC13","CSC14","CSC15" ,"CSC16","CSC17","CSC18" };
		this.maxTCellCSCNum = strArray[maxTCellCSCNum-1];
	}
	public String getMaxTCellCSCLoc() {
		return maxTCellCSCLoc;
	}
	public void setMaxTCellCSCLoc(int maxTCellCSCLoc) {
		
		String[] strArray = {"Temp point 1","Temp point 2","Temp point 3","Temp point 4","Temp point 5","Temp point 6"};
		this.maxTCellCSCLoc = strArray[maxTCellCSCLoc-1];
		
	}
	public String getMinTCellCSCNum() {
		return minTCellCSCNum;
	}
	public void setMinTCellCSCNum(int minTCellCSCNum) {
		String[] strArray = {"CSC1" ,"CSC2" ,"CSC3","CSC4","CSC5","CSC6","CSC7","CSC8","CSC9","CSC10","CSC11","CSC12" 
				,"CSC13","CSC14","CSC15" ,"CSC16","CSC17","CSC18" };
		this.minTCellCSCNum = strArray[minTCellCSCNum-1];
	}
	public String getMinTCellCSCLoc() {
		return minTCellCSCLoc;
	}
	public void setMinTCellCSCLoc(int minTCellCSCLoc) {
		
		String[] strArray = {"Temp point 1","Temp point 2","Temp point 3","Temp point 4","Temp point 5","Temp point 6"};
		this.minTCellCSCLoc = strArray[minTCellCSCLoc-1];
		
	}
	
	
	
	
	

}
