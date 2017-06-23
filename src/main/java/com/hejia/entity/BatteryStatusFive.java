package com.hejia.entity;

public class BatteryStatusFive {

	private int cellMaxV=0;           	//电芯最高电压
	private int cellMinV=0;                 //电芯最低电压
	private int cellAverV=0;              //电芯平均电压
	private String maxVCellCSCNum = "";           		  //最高电压电芯所在CSC编号
	private String maxVCellCSCLoc = "";           	//最高电压电芯所在CSC内位置
	private String minVCellCSCNum = "";                 //最低电压电芯所在CSC编号
	private String minVCellCSCLoc = "";              //最低电压电芯所在CSC内位置
	

	public int getCellMaxV() {
		return cellMaxV;
	}
	public void setCellMaxV(int cellMaxV) {
		this.cellMaxV = cellMaxV;
	}
	public int getCellMinV() {
		return cellMinV;
	}
	public void setCellMinV(int cellMinV) {
		this.cellMinV = cellMinV;
	}
	public int getCellAverV() {
		return cellAverV;
	}
	public void setCellAverV(int cellAverV) {
		this.cellAverV = cellAverV;
	}
	public String getMaxVCellCSCNum() {
		return maxVCellCSCNum;
	}
	public void setMaxVCellCSCNum(int maxVCellCSCNum) {
		String[] strArray = {"CSC1" ,"CSC2" ,"CSC3","CSC4","CSC5","CSC6","CSC7","CSC8","CSC9","CSC10","CSC11","CSC12" 
				,"CSC13","CSC14","CSC15" ,"CSC16","CSC17","CSC18" };
		this.maxVCellCSCNum = strArray[maxVCellCSCNum-1];
	}
	public String getMaxVCellCSCLoc() {
		return maxVCellCSCLoc;
	}
	public void setMaxVCellCSCLoc(int maxVCellCSCLoc) {
		String[] strArray = {"Temp point 1","Temp point 2","Temp point 3","Temp point 4","Temp point 5","Temp point 6"};
		this.maxVCellCSCLoc = strArray[maxVCellCSCLoc-1];
	}
	public String getMinVCellCSCNum() {
		return minVCellCSCNum;
	}
	public void setMinVCellCSCNum(int minVCellCSCNum) {
		String[] strArray = {"CSC1" ,"CSC2" ,"CSC3","CSC4","CSC5","CSC6","CSC7","CSC8","CSC9","CSC10","CSC11","CSC12" 
				,"CSC13","CSC14","CSC15" ,"CSC16","CSC17","CSC18" };
		this.minVCellCSCNum = strArray[minVCellCSCNum-1];
	}
	public String getMinVCellCSCLoc() {
		return minVCellCSCLoc;
	}
	public void setMinVCellCSCLoc(int minVCellCSCLoc) {
		String[] strArray = {"Temp point 1","Temp point 2","Temp point 3","Temp point 4","Temp point 5","Temp point 6"};
		this.minVCellCSCLoc = strArray[minVCellCSCLoc-1];
	}
	
	
	

}
