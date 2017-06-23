package com.hejia.entity;

public class MotorTrouble {
	
	private int troubleCode = 0;             
    private int more = 0;           
    private String badlyGrade = "";                 //严重等级
    
	public int getTroubleCode() {
		return troubleCode;
	}
	public void setTroubleCode(int troubleCode) {
		this.troubleCode = troubleCode;
	}
	public int getMore() {
		return more;
	}
	public void setMore(int more) {
		this.more = more;
	}
	public String getBadlyGrade() {
		return badlyGrade;
	}
	public void setBadlyGrade(int badlyGrade) {
		switch (badlyGrade) {
			case 0:
				this.badlyGrade = "无故障";
				break;
			case 1:
				this.badlyGrade = "警告";
				break;
			case 5:
				this.badlyGrade = "故障软关断";
				break;
			case 6:
				this.badlyGrade = "严重故障";
				break;

		default:
			break;
		}
	}
    

    
    
	
    

}
