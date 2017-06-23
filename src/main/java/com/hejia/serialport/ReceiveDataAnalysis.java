package com.hejia.serialport;

import android.os.Handler;
import android.util.Log;

import com.hejia.entity.AirCondition;
import com.hejia.entity.AirFanCondition;
import com.hejia.entity.AirPumpStatus;
import com.hejia.entity.BatteryBagInfor;
import com.hejia.entity.BatteryStatusFive;
import com.hejia.entity.BatteryStatusFour;
import com.hejia.entity.BatteryStatusOne;
import com.hejia.entity.BatteryStatusThree;
import com.hejia.entity.BatteryStatusTwo;
import com.hejia.entity.BusStatusA;
import com.hejia.entity.BusStatusB;
import com.hejia.entity.DCDCStatus;
import com.hejia.entity.FlexoWheelCondition;
import com.hejia.entity.LightCondition;
import com.hejia.entity.McuHeartbeat;
import com.hejia.entity.McuVersions;
import com.hejia.entity.MotorStatusOne;
import com.hejia.entity.MotorStatusTwo;
import com.hejia.entity.MotorTrouble;
import com.hejia.entity.NumIOStatus;
import com.hejia.entity.OilPumpStatus;
import com.hejia.entity.PowerManage;
import com.hejia.entity.SimulIOStatusA;
import com.hejia.entity.SimulIOStatusB;
import com.hejia.entity.TVCondition;
import com.hejia.eventbus.EventSerialReadRequest;

import de.greenrobot.event.EventBus;

public class ReceiveDataAnalysis {

	public McuVersions mMcuVersions;
	public McuHeartbeat mMcuHeartbeat;
	public PowerManage mPowerManage;
	public NumIOStatus mNumIOStatus;
	public SimulIOStatusA mSimulIOStatusA;
	public SimulIOStatusB mSimulIOStatusB;
	public BusStatusA mBusStatusA;
	public BusStatusB mBusStatusB;
	public AirCondition mAirCondition;
	public FlexoWheelCondition mFlexoWheelCondition;
	public TVCondition mTVCondition;
	public LightCondition mLightCondition;
	public AirFanCondition mAirFanCondition;

	//新界面添加
	public MotorStatusOne mMotorStatusOne;
	public MotorStatusTwo mMotorStatusTwo;
	public MotorTrouble mMotorTrouble;
	public BatteryBagInfor mBatteryBagInfor;
	public BatteryStatusOne mBatteryStatusOne;
	public BatteryStatusTwo mBatteryStatusTwo;
	public BatteryStatusThree mBatteryStatusThree;
	public BatteryStatusFour mBatteryStatusFour;
	public BatteryStatusFive mBatteryStatusFive;
	public DCDCStatus mDCDCStatus;
	public OilPumpStatus mOilPumpStatus;
	public AirPumpStatus mAirPumpStatus;

	private com.hejia.serialport.DataConvert mDataConvert;

	private static ReceiveDataAnalysis mReceiveDataAnalysis = null;
	private Handler mhandler;
	byte[] databyte;//用于缓存单条语句的字节数组
	int[] data;


	//单例模式，将接收处理的信息可面向整个工程，无需重新创建实例
	public static ReceiveDataAnalysis getInstance(){
		if(mReceiveDataAnalysis == null)
		{
			mReceiveDataAnalysis = new ReceiveDataAnalysis();
		}
		return mReceiveDataAnalysis;
	}


	private ReceiveDataAnalysis()
	{
		mMcuVersions = new McuVersions();
		mMcuHeartbeat = new McuHeartbeat();
		mPowerManage = new PowerManage();
		mNumIOStatus = new NumIOStatus();
		mSimulIOStatusA = new SimulIOStatusA();
		mSimulIOStatusB = new SimulIOStatusB();
		mBusStatusA = new BusStatusA();
		mBusStatusB = new BusStatusB();
		mAirCondition = new AirCondition();
		mFlexoWheelCondition = new FlexoWheelCondition();
		mLightCondition = new LightCondition();
		mTVCondition = new TVCondition();
		mAirFanCondition = new AirFanCondition();
		mDataConvert = new com.hejia.serialport.DataConvert();

		mMotorStatusOne = new MotorStatusOne();
		mMotorStatusTwo = new MotorStatusTwo();
		mMotorTrouble = new MotorTrouble();
		mBatteryBagInfor = new BatteryBagInfor();
		mBatteryStatusOne = new BatteryStatusOne();
		mBatteryStatusTwo = new BatteryStatusTwo();
		mBatteryStatusThree = new BatteryStatusThree();
		mBatteryStatusFour = new BatteryStatusFour();
		mBatteryStatusFive = new BatteryStatusFive();
		mDCDCStatus = new DCDCStatus();
		mOilPumpStatus = new OilPumpStatus();
		mAirPumpStatus = new AirPumpStatus();

	}

	//判断单条buffer语句属于那一条信息，分发到对应的语句进行解析
	public boolean proceData(String str)
	{
		boolean value = true;
		String orderStr;
		databyte = mDataConvert.getByteArray(str);//将字符串转换字节数组
		value = judgeCheckSum(str);//判断该条语句的校验值是否正确，正确继续解析，错误返回
		data = new int[databyte.length];
		for(int i = 0;i<data.length;i++)
		{
			data[i] = databyte[i]&0xff;
		}
		if(!value)
		{
			return false;
		}

		orderStr= str.substring(2, 10);

		switch(orderStr){
			case "20000000"://MCU版本信息
				proceMcuVersions();
				EventBus.getDefault().post(
						new EventSerialReadRequest("MCUVERSIONS","default"));
				break;
			case "20000001"://MCU心跳计数器
				proceMcuHeartbeat();
				EventBus.getDefault().post(
						new EventSerialReadRequest("MCUHEARTBEAT","default"));
				break;
			case "20000100"://电源管理状态
				procePowerManager();
				EventBus.getDefault().post(
						new EventSerialReadRequest("POWERMANAGE","default"));
				break;
			case "20000200"://数字IO状态
				proceNumIOStatus();
				EventBus.getDefault().post(
						new EventSerialReadRequest("NUMIOSTATUS","default"));
				break;
			case "20000201"://模拟IO状态A
				proceSimulIOStatusA();
				EventBus.getDefault().post(
						new EventSerialReadRequest("SIMULIOSTATUSA","default"));
				break;
			case "20000202"://模拟IO状态B
				proceSimulIOStatusB();
				EventBus.getDefault().post(
						new EventSerialReadRequest("SIMULIOSTATUSB","default"));
				break;
			case "20000300"://车身状态A
				proceBusStatusA();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BUSSTATUSA","default"));
				break;
			case "20000301"://车身状态B
				proceBusStatusB();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BUSSTATUSB","default"));
				break;
			case "20000302"://空调状态语句
				proceFlexoWheelCondition();
				EventBus.getDefault().post(
						new EventSerialReadRequest("AIRCONDITION","default"));
				break;
			case "20000303"://多功能方向盘语句
				proceAirCondition();
				EventBus.getDefault().post(
						new EventSerialReadRequest("FLEXOWHEELCONDITION","default"));
				break;
			case "20000304"://灯光状态语句
				proceLightCondition();
				EventBus.getDefault().post(
						new EventSerialReadRequest("LIGHTCONDITION","default"));
				break;
			case "20000305"://电视开关状态语句
				proceTVCondition();
				EventBus.getDefault().post(
						new EventSerialReadRequest("TVCONDITION","default"));
				break;
			case "20000306"://风扇状态语句
				proceAirFanCondition();
				EventBus.getDefault().post(
						new EventSerialReadRequest("AIRFANCONDITION","default"));
				break;
			/***********新界面语句*************/
			case "18ff41f0"://电机状态报文1
				processMotorStatusOne();
				EventBus.getDefault().post(
						new EventSerialReadRequest("MOTORSTATUSONE","default"));
				break;
			case "18ff51f0"://电机状态报文2
				processMotorStatusTwo();
				EventBus.getDefault().post(
						new EventSerialReadRequest("MOTORSTATUSTWO","default"));
				break;
			case "18ff81f0"://电机故障报文
				processMotorTrouble();
				EventBus.getDefault().post(
						new EventSerialReadRequest("MOTORSTATUSTROUBLE","default"));
				break;
			case "18e6eff3"://电池包信息1
				processBatteryBagInfor();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BATTERYBAGINFOR","default"));
				break;
			case "1881eff3"://电池状态信息1
				processBatteryStatusOne();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BATTERYSTATUSONE","default"));
				break;
			case "1882eff3"://电池状态信息2
				processBatteryStatusTwo();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BATTERYSTATUSTWO","default"));
				break;
			case "1883eff3"://电池状态信息3
				processBatteryStatusThree();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BATTERYSTATUSTHREE","default"));
				break;
			case "1884eff3"://电池状态信息4
				processBatteryStatusFour();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BATTERYSTATUSFOUR","default"));
				break;
			case "1885eff3"://电池状态信息5
				processBatteryStatusFive();
				EventBus.getDefault().post(
						new EventSerialReadRequest("BATTERYSTATUSFIVE","default"));
				break;
			case "0cf602a1"://DCDC状态
				processDCDCSatus();
				EventBus.getDefault().post(
						new EventSerialReadRequest("DCDCSTATUS","default"));
				break;
			case "0cf601a0"://油泵状态
				processOilPump();
				EventBus.getDefault().post(
						new EventSerialReadRequest("OILPUMPSTATUS","default"));
				break;
			case "0cf603a2"://气泵状态
				processAirPump();
				EventBus.getDefault().post(
						new EventSerialReadRequest("AIRPUMPSTATUS","default"));
				break;
			case "30000000"://Android版本信息反馈语句
				Log.i("Android版本信息反馈", "success");
				break;
			case "30000001"://Android心跳计数器反馈语句
				Log.i("Android心跳计数器", "success");
				break;
			case "30000002"://温度信息反馈语句
				Log.i("温度信息", "success");
				break;
			case "30000003"://工作模式反馈语句
				Log.i("工作模式", "success");
				break;
			case "30000100"://LCD背光控制反馈语句
				Log.i("LCD背光控制", "success");
				break;
			case "30000101"://功放控制反馈语句
				Log.i("功放控制", "success");
				break;
			case "30000302"://空调控制反馈语句
				Log.i("空调控制反馈", "success");
				break;
			case "30000304"://灯光控制反馈语句
				Log.i("灯光控制反馈", "success");
				break;
			case "30000305"://电视开关/翻转折叠反馈语句
				Log.i("电视控制反馈", "success");
				break;
			case "30000306"://风扇控制反馈语句
				Log.i("风扇控制反馈", "success");
				break;
			case "30000307"://电子路牌反馈语句
				Log.i("电子路牌控制反馈", "success");
				break;

			default:
				break;

		}
		return true;

	}

	//MCU版本信息语句处理
	private void proceMcuVersions()
	{
		if(data.length == 17)
			try {
				mMcuVersions.setHardwareMainVers(data[6]);
				mMcuVersions.setHardwareSonVers(data[7]*16*16+data[8]);
				mMcuVersions.setHardwareReviseVers(data[9]);
				mMcuVersions.setSoftwareMainVers(data[10]);
				mMcuVersions.setSoftwareSonVers(data[11]*16*16+data[12]);
				mMcuVersions.setSoftwareReviseVers(data[13]);

			} catch (Exception e) {

			}
	}

	//MCU心跳计数器
	private void proceMcuHeartbeat()
	{
		if(data.length == 10)
			try {
				mMcuHeartbeat.setHeartbeat(data[6]);

			} catch (Exception e) {

			}
	}

	//电源管理状态
	private void procePowerManager()
	{
		if(data.length == 13)
			try {
				mPowerManage.setOutPutVolt12((data[6]&0xC0)>>>6);
				mPowerManage.setOutPutVolt5((data[6]&0x30)>>>4);
				mPowerManage.setOutPutVolt((data[6]&0x0C)>>>2);
				mPowerManage.setOutPutLcd(data[6]&0x03);
				mPowerManage.setBrightnessLcd((data[7]&0xF0)>>>4);
				mPowerManage.setAmplPowerOut((data[8]&0x80)>>>7);
				mPowerManage.setAmplSilenOut((data[8]&0x40)>>>6);
				mPowerManage.setHeatDiss1((data[9]&0xF0)>>>4);
				mPowerManage.setHeatDiss2(data[9]&0x0F);

			} catch (Exception e) {

			}

	}




	//数字IO状态
	private void proceNumIOStatus()
	{
		if(data.length == 14)
			try {

				mNumIOStatus.setSignalSP1((data[6]&0x80)>>>7);
				mNumIOStatus.setSignalSP2((data[6]&0x40)>>>6);
				mNumIOStatus.setSignalSP3((data[6]&0x20)>>>5);
				mNumIOStatus.setSignalSP4((data[6]&0x10)>>>4);
				mNumIOStatus.setSignalSP5((data[6]&0x08)>>>3);

				mNumIOStatus.setSignalSG1((data[7]&0x80)>>>7);
				mNumIOStatus.setSignalSG2((data[7]&0x40)>>>6);
				mNumIOStatus.setSignalSG3((data[7]&0x20)>>>5);
				mNumIOStatus.setSignalSG4((data[7]&0x10)>>>4);
				mNumIOStatus.setSignalSG5((data[7]&0x08)>>>3);
				mNumIOStatus.setSignalSG6((data[7]&0x04)>>>2);
				mNumIOStatus.setSignalSG7((data[7]&0x02)>>>1);
				mNumIOStatus.setSignalSG8((data[7]&0x01));
				mNumIOStatus.setSignalSG9((data[8]&0x80)>>>7);
				mNumIOStatus.setSignalSG10((data[8]&0x40)>>>6);
				mNumIOStatus.setSignalSG11((data[8]&0x20)>>>5);

				mNumIOStatus.setSignalKEY01((data[8]&0x08)>>>3);
				mNumIOStatus.setSignalKEY02((data[8]&0x04)>>>2);
				mNumIOStatus.setSignalKEY03((data[8]&0x02)>>>1);
				mNumIOStatus.setSignalKEY04((data[8]&0x01));
				mNumIOStatus.setSignalKEY05((data[9]&0x80)>>>7);
				mNumIOStatus.setSignalKEY06((data[9]&0x40)>>>6);
				mNumIOStatus.setSignalKEY07((data[9]&0x20)>>>5);
				mNumIOStatus.setSignalKEY08((data[9]&0x10)>>>4);
				mNumIOStatus.setSignalKEY09((data[9]&0x08)>>>3);

				mNumIOStatus.setSignalOUT1((data[10]&0xC0)>>>6);
				mNumIOStatus.setSignalOUT2((data[10]&0x30)>>>4);

			} catch (Exception e) {

			}

	}

	//模拟IO状态A
	private void proceSimulIOStatusA()
	{
		if(data.length == 25)
			try {
				mSimulIOStatusA.setANVolt1(data[6]*16*16+data[7]);
				mSimulIOStatusA.setANVolt2(data[8]*16*16+data[9]);
				mSimulIOStatusA.setANVolt3(data[10]*16*16+data[11]);
				mSimulIOStatusA.setANVolt4(data[12]*16*16+data[13]);
				mSimulIOStatusA.setANVolt5(data[14]*16*16+data[15]);
				mSimulIOStatusA.setANVolt6(data[16]*16*16+data[17]);
				mSimulIOStatusA.setANVolt7(data[18]*16*16+data[19]);
				mSimulIOStatusA.setANVolt8(data[20]*16*16+data[21]);

			} catch (Exception e) {

			}



	}

	//模拟IO状态B
	private void proceSimulIOStatusB()
	{
		if(data.length == 25)
			try {
				mSimulIOStatusB.setANVolt9(data[6]*16*16+data[7]);
				mSimulIOStatusB.setANVolt10(data[8]*16*16+data[9]);
				mSimulIOStatusB.setANVolt11(data[10]*16*16+data[11]);
				mSimulIOStatusB.setANVolt12(data[12]*16*16+data[13]);
				mSimulIOStatusB.setANVolt13(data[14]*16*16+data[15]);
				mSimulIOStatusB.setANVolt14(data[16]*16*16+data[17]);
				mSimulIOStatusB.setANVolt15(data[18]*16*16+data[19]);
				mSimulIOStatusB.setANVolt16(data[20]*16*16+data[21]);

			} catch (Exception e) {

			}



	}

	//车身状态A     协议有问题
	private void proceBusStatusA()
	{
		if(data.length == 17)
			try {
				mBusStatusA.setElecMagRotate(data[6]*16*16+data[7]);
				mBusStatusA.setEngineRotate(data[8]*16*16+data[9]);
				mBusStatusA.setGearInfor((data[10]&0xF0)>>>4);
				mBusStatusA.setDoorStatus((data[10]&0x0C0)>>>2);
//					mBusStatusA.more = data[10]&0x03;
				mBusStatusA.setBrakePlace(data[11]);
				mBusStatusA.setEbdPlace(data[12]);

			} catch (Exception e) {

			}

	}

	//车身状态B
	private void proceBusStatusB()
	{
		if(data.length == 17)
			try {
				mBusStatusB.setBusSpeed(data[6]*16*16+data[7]);
				mBusStatusB.setOrderTorque(data[8]*16*16+data[9]);
				mBusStatusB.setSocStatus(data[10]);
				mBusStatusB.setSystemError(data[11]);
//					mBusStatusB.more = data[12]*16*16+data[13];

			} catch (Exception e) {

			}

	}

	//空调状态语句
	private void proceAirCondition()
	{
		if(data.length == 13)
			try {
				mAirCondition.setWorkMode((data[6]&0xC0)>>>6);
				mAirCondition.setCirculMode((data[6]&0x30)>>>4);
				mAirCondition.setAirOutMode((data[6]&0x0F));
				mAirCondition.setMainAirSpeedAdjust((data[7]&0xF0)>>>4);
				mAirCondition.setMainTemperAdjust((data[7]&0x0F));
				mAirCondition.setViceAirSpeedAdjust((data[8]&0xF0)>>>4);
				mAirCondition.setViceTemperAdjust((data[8]&0x0F));
				mAirCondition.setBehindGlassHeat((data[9]&0x80)>>>7);
				mAirCondition.setSaveEnergyMode((data[9]&0x40)>>>6);
				mAirCondition.setAirPurificationMode((data[9]&0x20)>>>5);

			} catch (Exception e) {

			}

	}

	//处理多功能方向盘语句
	private void proceFlexoWheelCondition()
	{
		if(data.length == 11)
			try {
				mFlexoWheelCondition.setVoiceAdjust((data[6]&0xC0)>>>6);
				mFlexoWheelCondition.setMusicAdjust((data[6]&0x30)>>>4);
				mFlexoWheelCondition.setRadioAdjust((data[6]&0x0C)>>>2);
				mFlexoWheelCondition.setBtCallTelephone((data[6]&0x03));

			} catch (Exception e) {

			}

	}

	//处理灯光状态语句
	private void proceLightCondition()
	{
		if(data.length == 10)
			try {
				mLightCondition.setCourtesyLamp((data[6]&0x80)>>>7);
				mLightCondition.setRoomLamp((data[6]&0x40)>>>6);
				mLightCondition.setDriverLamp((data[6]&0x20)>>>5);
				mLightCondition.setEleCompartmentLamp((data[6]&0x10)>>>4);

			} catch (Exception e) {

			}

	}

	//处理电视开关状态语句
	private void proceTVCondition()
	{
		if(data.length == 10)
			try {
				mTVCondition.setTVSwitch((data[6]&0x80)>>>7);
				mTVCondition.setOverturnSwitch((data[6]&0x40)>>>6);

			} catch (Exception e) {

			}

	}

	//处理风扇状态语句
	private void proceAirFanCondition()
	{
		if(data.length == 11)
			try {
				mAirFanCondition.setFrontAirFan((data[6]&0x80)>>>7);
				mAirFanCondition.setBehindAirFan((data[6]&0x40)>>>6);
				mAirFanCondition.setDriverAirFan((data[6]&0x20)>>>5);

			} catch (Exception e) {

			}

	}


	/******************************************* 新界面协议解析*******************************************************************/
	//处理电机状态1语句
	private void  processMotorStatusOne()
	{
		int value = 0;
		if(data.length == 17)
			try {
				mMotorStatusOne.setRote(data[7]*16*16+data[6]);
				mMotorStatusOne.setTorque(data[9]*16*16+data[8]);
				mMotorStatusOne.setElectricity(data[11]*16*16+data[10]);
				mMotorStatusOne.setWorkMode((data[13]&0xf0)>>>4);
				mMotorStatusOne.setTroubleGrade(data[13]&0x0f);

			} catch (Exception e) {

			}
	}

	//处理电机状态2语句
	private void  processMotorStatusTwo()
	{
		if(data.length == 17)
			try {
				mMotorStatusTwo.setMotorTemper(data[6]);
				mMotorStatusTwo.setControllerTemper(data[7]);
				mMotorStatusTwo.setControllerVoltage(data[9]*16*16+data[8]);
				mMotorStatusTwo.setDriveTorque(data[10]+((data[11]&0x0f)<<8));
				mMotorStatusTwo.setElectTorque(((data[11]&0xf0)>>>4)+(data[12]<<4));

			} catch (Exception e) {

			}
	}

	//处理电机故障语句
	private void  processMotorTrouble()
	{
		if(data.length == 17)
			try {
				mMotorTrouble.setTroubleCode(data[8]*16*16+data[7]);
				mMotorTrouble.setBadlyGrade(data[10]&0x0f);

			} catch (Exception e) {

			}
	}


	//处理电池包信息语句
	private void  processBatteryBagInfor()
	{
		if(data.length == 17)
			try {
				mBatteryBagInfor.setBatteryType(data[6]&0x0f);
				mBatteryBagInfor.setBatteryCapacity(data[8]*16*16+data[7]);
				mBatteryBagInfor.setPlatformVoltage(data[9]);
				mBatteryBagInfor.setMinVoltage(data[10]);
				mBatteryBagInfor.setMaxVoltage(data[11]);

			} catch (Exception e) {

			}
	}

	//处理电池状态信息1语句
	private void  processBatteryStatusOne()
	{
		if(data.length == 17)
			try {
				mBatteryStatusOne.setCurrentStatus((data[7]&0xf0)>>>4);
				mBatteryStatusOne.setRelayStatus(data[8]&0x03);
				mBatteryStatusOne.setBalancedState((data[8]&0x0c)>>>2);
				mBatteryStatusOne.setElectLineStatus((data[8]&0xc0)>>>6);
				mBatteryStatusOne.setElectMode(data[9]&0x03);
				mBatteryStatusOne.setElectStatus((data[9]&0x0c)>>>2);
				mBatteryStatusOne.setTroubleGrade((data[9]&0x30)>>>4);
				mBatteryStatusOne.setCurrentTrouble(data[13]);

			} catch (Exception e) {

			}
	}


	//处理电池状态信息2语句
	private void  processBatteryStatusTwo()
	{
		if(data.length == 17)
			try {
				mBatteryStatusTwo.setSoc(data[6]);
				mBatteryStatusTwo.setSoh(data[7]);
				mBatteryStatusTwo.setAllA(data[8]+data[9]*16*16);
				mBatteryStatusTwo.setMaxChargeA(data[10]+data[11]*16*16);
				mBatteryStatusTwo.setMaxDischargeA(data[12]+data[13]*16*16);

			} catch (Exception e) {

			}
	}

	//处理电池状态信息3语句
	private void  processBatteryStatusThree()
	{
		if(data.length == 17)
			try {
				mBatteryStatusThree.setPosiInsR(data[7]*16*16+data[6]);
				mBatteryStatusThree.setNegaInsR(data[9]*16*16+data[8]);
				mBatteryStatusThree.setBatteryHV(data[11]*16*16+data[10]);
				mBatteryStatusThree.setGenHV(data[13]*16*16+data[12]);

			} catch (Exception e) {

			}
	}

	//处理电池状态信息4语句
	private void  processBatteryStatusFour()
	{
		if(data.length == 17)
			try {
				mBatteryStatusFour.setCellMaxT(data[6]);
				mBatteryStatusFour.setCellMinT(data[7]);
				mBatteryStatusFour.setCellAverT(data[8]);
				mBatteryStatusFour.setMaxTCellCSCNum(data[9]&0x3f);
				mBatteryStatusFour.setMaxTCellCSCLoc(((data[9]&0xc0)>>>6)+((data[10]&0x07)<<2));
				mBatteryStatusFour.setMinTCellCSCNum(((data[10]&0xf8)>>>3)+((data[11]&0x01)<<5));
				mBatteryStatusFour.setMinTCellCSCLoc((data[11]&0x3e)>>>1);

			} catch (Exception e) {

			}
	}

	//处理电池状态信息5语句
	private void  processBatteryStatusFive()
	{
		if(data.length == 17)
			try {
				mBatteryStatusFive.setCellMaxV((data[6])+((data[7]&0x1f)<<8));
				mBatteryStatusFive.setCellMinV(((data[7]&0xe0)>>>5)+(data[8]<<3)+((data[9]&0x03)<<11));
				mBatteryStatusFive.setCellAverV(((data[9]&0xfc)>>>2)+((data[10]&0x7f)<<6));
				mBatteryStatusFive.setMaxVCellCSCNum(((data[10]&0x80)>>>7)+((data[11]&0x1f)<<1));
				mBatteryStatusFive.setMaxVCellCSCLoc(((data[11]&0xe0)>>>5)+((data[12]&0x03)<<3));
				mBatteryStatusFive.setMinVCellCSCNum((data[12]&0xfc)>>>2);
				mBatteryStatusFive.setMinVCellCSCLoc((data[13]&0x1f));

			} catch (Exception e) {

			}
	}


	//处理DCDC状态语句
	private void  processDCDCSatus()
	{
		if(data.length == 17)
			try {
				mDCDCStatus.setWorkStatus(data[6]&0x03);
				mDCDCStatus.setOutA(data[7]);
				mDCDCStatus.setOutV(data[8]);
				mDCDCStatus.setConyrolT(data[9]);
				mDCDCStatus.setAllLineStatue(data[10]&0x01);
				mDCDCStatus.setHardStatus((data[10]&0x02)>>>1);
				mDCDCStatus.setTStatus((data[10]&0x04)>>>2);

				mDCDCStatus.setIntAStatus((data[10]&0x08)>>>3);
				mDCDCStatus.setIntVStatus((data[10]&0x10)>>>4,(data[10]&0x20)>>>5);
				mDCDCStatus.setOutVStatus((data[10]&0x40)>>>6,(data[10]&0x80)>>>7);
				mDCDCStatus.setBusbarV(data[11]+(data[12]<<8));
				mDCDCStatus.setLifecycle(data[13]);


			} catch (Exception e) {

			}
	}



	//处理油泵状态语句
	private void  processOilPump()
	{
		if(data.length == 17)
			try {
				mOilPumpStatus.setOutV(data[6]+(data[7]<<8));
				mOilPumpStatus.setOutA(data[8]+(data[9]<<8));
				mOilPumpStatus.setWorkStatus(data[10]&0x03);
				mOilPumpStatus.setAllLineStatue((data[10]&0x04)>>>2);
				mOilPumpStatus.setTStatus((data[10]&0x08)>>>3);
				mOilPumpStatus.setIntAStatus((data[10]&0x10)>>>4);
				mOilPumpStatus.setIntVStatus((data[10]&0x20)>>>5);
				mOilPumpStatus.setPhaseStatus((data[10]&0x40)>>>6,(data[10]&0x80)>>>7);
				mOilPumpStatus.setLifecycle(data[13]);


			} catch (Exception e) {

			}
	}


	//处理气泵状态语句
	private void  processAirPump()
	{
		if(data.length == 17)
			try {
				mAirPumpStatus.setOutV(data[6]+(data[7]<<8));
				mAirPumpStatus.setOutA(data[8]+(data[9]<<8));
				mAirPumpStatus.setWorkStatus(data[10]&0x03);
				mAirPumpStatus.setAllLineStatue((data[10]&0x04)>>>2);
				mAirPumpStatus.setTStatus((data[10]&0x08)>>>3);
				mAirPumpStatus.setIntAStatus((data[10]&0x10)>>>4);
				mAirPumpStatus.setIntVStatus((data[10]&0x20)>>>5);
				mAirPumpStatus.setPhaseStatus((data[10]&0x40)>>>6,(data[10]&0x80)>>>7);
				mAirPumpStatus.setAirPumpT(data[11]);
				mAirPumpStatus.setLifecycle(data[13]);

			} catch (Exception e) {

			}
	}

	//判断接受的语句校验是否正确
	private boolean judgeCheckSum(String str)
	{
		String checkStr;
		byte[] checkByte;
		long checkSum;
		long checkdata;

		if(str.length()<=6)
		{
			return false;
		}
		checkStr = str.substring(2, str.length()-6);
		checkByte = mDataConvert.getByteArray(checkStr);
		checkSum = CRC16.calcCrc16(checkByte);
		int dataLength = databyte.length;
		checkdata = mDataConvert.getCheckData(databyte[dataLength-3],databyte[dataLength-2]);
		long unsignedValue=0;
		if(checkdata<0)
		{
			unsignedValue = checkdata &0xFFFF;

		}
		if((checkSum==checkdata)||(checkSum==unsignedValue))
		{
			return true;
		}
		else
		{
			return false;
		}
	}







}
