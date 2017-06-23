package com.hejia.entity;

public class BatteryBagInfor {

	private String batteryType = "";              //电池类型
	private int batteryCapacity  = 0;           		  //电池包额定容量
	private float platformVoltage = 0;                 //单体平台电压
	private float minVoltage = 0;           		  //单体最低电压
	private float maxVoltage = 0;                 //单体最高电压
    private int more = 0;
	public String getBatteryType() {
		return batteryType;
	}
	public void setBatteryType(int batteryType) {
		switch (batteryType) {
			case 1:
				this.batteryType = "铅酸电池";
				break;
			case 2:
				this.batteryType = "镍氢电池";
				break;
			case 3:
				this.batteryType = "磷酸铁锂电池";
				break;
			case 4:
				this.batteryType = "锰酸锂电池";
				break;
			case 5:
				this.batteryType = "钴酸锂电池";
				break;
			case 6:
				this.batteryType = "三元材料电池";
				break;
			case 7:
				this.batteryType = "聚合物锂离子电池";
				break;
			case 8:
				this.batteryType = "钛酸锂电池";
				break;
			default:
				break;
		}
	}
	public int getBatteryCapacity() {
		return batteryCapacity;
	}
	public void setBatteryCapacity(int batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}
	public float getPlatformVoltage() {
		return platformVoltage;
	}
	public void setPlatformVoltage(int platformVoltage) {	
		this.platformVoltage = (float) (platformVoltage*0.1);
	}
	public float getMinVoltage() {
		return minVoltage;
	}
	public void setMinVoltage(int minVoltage) {	
		this.minVoltage = (float) (minVoltage*0.1);
	}
	public float getMaxVoltage() {
		return maxVoltage;
	}
	public void setMaxVoltage(int maxVoltage) {	
		this.maxVoltage = (float) (maxVoltage*0.1);
	}
	public int getMore() {
		return more;
	}
	public void setMore(int more) {
		this.more = more;
	}
    
    
    
    
}
