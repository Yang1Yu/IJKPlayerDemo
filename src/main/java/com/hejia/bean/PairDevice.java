package com.hejia.bean;

public class PairDevice {
	private String pairDeviceBd;
	private String pairDeviceName;
	private String pairDeviceIndex;

	public PairDevice(String pairDeviceBd, String pairDeviceName) {
		this.pairDeviceBd = pairDeviceBd;
		this.pairDeviceName = pairDeviceName;
	}

	public String getPairDeviceBd() {
		return pairDeviceBd;
	}

	public void setPairDeviceBd(String pairDeviceBd) {
		this.pairDeviceBd = pairDeviceBd;
	}

	public String getPairDeviceName() {
		return pairDeviceName;
	}

	public void setPairDeviceName(String pairDeviceName) {
		this.pairDeviceName = pairDeviceName;
	}

	public String getPairDeviceIndex() {
		return pairDeviceIndex;
	}

	public void setPairDeviceIndex(String pairDeviceIndex) {
		this.pairDeviceIndex = pairDeviceIndex;
	}

}
