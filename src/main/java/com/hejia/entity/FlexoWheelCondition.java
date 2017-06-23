package com.hejia.entity;

public class FlexoWheelCondition {
    private int voiceAdjust = 0;//音量调节    00：不做动作  01：音量降低  10：音量增加  11：静音
    private int musicAdjust = 0;//多媒体上一曲下一曲   00：不做动作  01：上一曲      10：下一曲
    private int radioAdjust = 0;//收音机上下调频   00:不做动作  01：向上调频  10：向下调频
    private int btCallTelephone = 0;//蓝牙接听电话 00:不做动作 01：接听  10挂断


    public int getVoiceAdjust() {
        return voiceAdjust;
    }
    public void setVoiceAdjust(int voiceAdjust) {
        this.voiceAdjust = voiceAdjust;
    }
    public int getMusicAdjust() {
        return musicAdjust;
    }
    public void setMusicAdjust(int musicAdjust) {
        this.musicAdjust = musicAdjust;
    }
    public int getRadioAdjust() {
        return radioAdjust;
    }
    public void setRadioAdjust(int radioAdjust) {
        this.radioAdjust = radioAdjust;
    }
    public int getBtCallTelephone() {
        return btCallTelephone;
    }
    public void setBtCallTelephone(int btCallTelephone) {
        this.btCallTelephone = btCallTelephone;
    }

}
