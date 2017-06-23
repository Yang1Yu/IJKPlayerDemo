package com.hejia.serialport;

import com.hejia.serialport.*;

public class SendDataAnalysis {

	//根据参数获取要发送的Android版本的完整语句
	public byte[] processSendAboutData(int sysMainVers,int sysSonVers,int sysReviseVers,int applyMainVers,int applySonVers,int applyReviseVers)
	{
		byte[] data = new byte[17];
		byte[] data0 = {0x7e,0x30,0x00,0x00,0x00,8};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[13];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] = (byte) sysMainVers;
		xy = getByteFromInt(sysSonVers); 
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = (byte) sysReviseVers;
		data[10] = (byte) applyMainVers;
		xy = getByteFromInt(applySonVers); 
		data[11] = xy[0];
		data[12] = xy[1];
		data[13] = (byte) applyReviseVers;
		for(int i = 0;i<13;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[14] = xy[0];
		data[15] = xy[1];
		data[16] = 0x7f;
		return data;
	}


	//根据参数获取要发送的心跳计数器的完整语句
	public byte[] processSendHeartbeatData(int heatbeat)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x00,0x01,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] = (byte) heatbeat;

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}


	//根据参数获取要发送的温度信息的完整语句
	public byte[] processSendTemperatureData(int cpuTemper)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x00,0x02,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] = (byte) cpuTemper;

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}

	//根据参数获取要发送的工作模式的完整语句
	public byte[] processSendWorkmodeData(int mode)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x00,0x03,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) ((byte)mode<<6);

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}

	//根据参数获取要发送的LCD背光控制的完整语句
	public byte[] processSendLcdControlData(int enable,int bright)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x01,0x00,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) (((byte)enable<<6)+((byte)bright<<4));

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}

	//根据参数获取要发送的功放控制的完整语句
	public byte[] processSendPowerAmplifierData(int ifSilence,int ifEnable)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x01,0x01,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) (((byte)ifSilence<<6)+((byte)ifEnable<<4));

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}

	//根据参数获取要发送的空调控制的完整语句
	public byte[] processSendAirConditionData(int workMode,int circulMode,int airOutMode, int mainAirSpeedAdjust,int mainTemperAdjust,
			 int viceAirSpeedAdjust,int viceTemperAdjust,int behindGlassHeat,int saveEnergyMode,int airPurificationMode)
	{
		byte[] data=new byte[13];
		byte[] data0 = {0x7e,0x30,0x00,0x03,0x02,4};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[9];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) (((byte)workMode<<6)+((byte)circulMode<<4)+airOutMode);
		data[7] = (byte) (((byte)mainAirSpeedAdjust<<4)+((byte)(mainTemperAdjust-15)));
		data[8] = (byte) (((byte)viceAirSpeedAdjust<<4)+((byte)(viceTemperAdjust-15)));
		data[9] = (byte) (((byte)behindGlassHeat<<7)+((byte)saveEnergyMode)<<6+((byte)airPurificationMode)<<5);
		for(int i = 0;i<9;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[10] = xy[0];
		data[11] = xy[1];
		data[12] = 0x7f;
		return data;
	}

	//根据参数获取要发送的灯光控制的完整语句
	public byte[] processLightConditionData(int courtesyLamp,int roomLamp,int driverLamp, int eleCompartmentLamp)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x03,0x04,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) (((byte)courtesyLamp<<7)+((byte)roomLamp<<6)+((byte)driverLamp<<5)+((byte)eleCompartmentLamp<<4));

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}


	//根据参数获取要发送的电视控制的完整语句
	public byte[] processTVConditionData(int TVSwitch,int overturnSwitch)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x03,0x05,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) (((byte)TVSwitch<<7)+((byte)overturnSwitch<<6));

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}


	//根据参数获取要发送的风扇控制的完整语句
	public byte[] processAirFanConditionData(int frontAirFan,int behindAirFan,int driverAirFan)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x03,0x05,1};	
		byte[] xy = new byte[2];
		byte[] datacheck = new byte[6];
		
		for(int i = 0;i<6;i++)
		{
			data[i]=data0[i];
		}
		data[6] =(byte) (((byte)frontAirFan<<7)+((byte)behindAirFan<<6)+((byte)driverAirFan<<6));

		for(int i = 0;i<6;i++)
		{
			datacheck[i]=data[i+1];
		}
		int checkSum = 	com.hejia.serialport.CRC16.calcCrc16(datacheck);
		xy = getByteFromInt(checkSum);
		data[7] = xy[0];
		data[8] = xy[1];
		data[9] = 0x7f;
		return data;
	}

	//根据参数获取要发送的电子路牌的完整语句
	public byte[] processElectroniSignsConditionData(int frontAirFan,int behindAirFan,int driverAirFan)
	{
		byte[] data=new byte[10];
		byte[] data0 = {0x7e,0x30,0x00,0x03,0x07,1};	

		return data;
	}


	//将校验整形转为两个字节
	private byte[] getByteFromInt(int value)
	{
		byte[] data = new byte[2];
		data[0] = (byte)((value & 0xff00)>>8);		
		 data[1] = (byte)(value & 0x00ff);
		 return data;
	}
}
