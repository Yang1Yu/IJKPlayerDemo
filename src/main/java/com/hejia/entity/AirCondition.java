package com.hejia.entity;

public class AirCondition {


    private int workMode = 0;               //工作模式----0:关闭，1:制冷，2:制热，3:自动
    private int circulMode = 0;            //循环模式----0:无效，1:内循环，2:外循环
    private int airOutMode = 0;                  //出风方式----0:无效，1:正面，2:正面及脚部，3:脚部，4:脚部及挡风玻璃，5:前挡风玻璃
    private int mainAirSpeedAdjust = 0;      //主风速调节----0：关闭  15：最大风量
    private int mainTemperAdjust = 0;        //主温度调节---1LSB = 1℃, 偏移：15℃
    private int viceAirSpeedAdjust = 0;      //副风速调节----0：关闭  15：最大风量
    private int viceTemperAdjust = 0;        //副温度调节----1LSB = 1℃, 偏移：15℃
    private int behindGlassHeat = 0;         //后挡风玻璃加热----0：关闭，1：打开
    private int saveEnergyMode = 0;          //节能模式----0：关闭，1：打开
    private int airPurificationMode = 0;     //空气净化----0：关闭，1：打开
    private int more = 0;
    
    
	public int getWorkMode() {
        return workMode;
    }
    public void setWorkMode(int workMode) {
        this.workMode = workMode;
    }
    public int getCirculMode() {
        return circulMode;
    }
    public void setCirculMode(int circulMode) {
        this.circulMode = circulMode;
    }
    public int getAirOutMode() {
        return airOutMode;
    }
    public void setAirOutMode(int airOutMode) {
        this.airOutMode = airOutMode;
    }
    public int getMainAirSpeedAdjust() {
        return mainAirSpeedAdjust;
    }
    public void setMainAirSpeedAdjust(int mainAirSpeedAdjust) {
        this.mainAirSpeedAdjust = mainAirSpeedAdjust;
    }
    public int getMainTemperAdjust() {
        return mainTemperAdjust;
    }
    public void setMainTemperAdjust(int mainTemperAdjust) {
        this.mainTemperAdjust = mainTemperAdjust;
    }
    public int getViceAirSpeedAdjust() {
        return viceAirSpeedAdjust;
    }
    public void setViceAirSpeedAdjust(int viceAirSpeedAdjust) {
        this.viceAirSpeedAdjust = viceAirSpeedAdjust;
    }
    public int getViceTemperAdjust() {
        return viceTemperAdjust;
    }
    public void setViceTemperAdjust(int viceTemperAdjust) {
        this.viceTemperAdjust = viceTemperAdjust;
    }
    public int getBehindGlassHeat() {
        return behindGlassHeat;
    }
    public void setBehindGlassHeat(int behindGlassHeat) {
        this.behindGlassHeat = behindGlassHeat;
    }
    public int getSaveEnergyMode() {
        return saveEnergyMode;
    }
    public void setSaveEnergyMode(int saveEnergyMode) {
        this.saveEnergyMode = saveEnergyMode;
    }
    public int getAirPurificationMode() {
        return airPurificationMode;
    }
    public void setAirPurificationMode(int airPurificationMode) {
        this.airPurificationMode = airPurificationMode;
    }
    public int getMore() {
        return more;
    }
    public void setMore(int more) {
        this.more = more;
    }


}
