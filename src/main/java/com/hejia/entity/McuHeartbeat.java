package com.hejia.entity;

public class McuHeartbeat {
    private int heartbeat = 0;	//心跳计数 ----0 ~ 255

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }
	
}
