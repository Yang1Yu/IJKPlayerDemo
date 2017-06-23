package com.hejia.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hejia.eventbus.EventSerialReadRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.R;
import com.hejia.serialport.DataConvert;
import com.hejia.serialport.ReceiveDataAnalysis;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCloudInfoBatteryManageModule extends Fragment{

	private View view;
	
	private TextView three_tv_right_01;
	private TextView three_tv_right_02;
	private TextView three_tv_right_03;
	private TextView three_tv_right_04;
	private TextView three_tv_right_05;
	private TextView three_tv_right_06;
	private TextView three_tv_right_07;
	private TextView three_tv_right_08;
	private TextView three_tv_right_09;
	
	private TextView three_tv_left_01;
	private TextView three_tv_left_02;
	private TextView three_tv_left_03;
	private TextView three_tv_left_04;
	private TextView three_tv_left_05;
	private TextView three_tv_left_06;
	private TextView three_tv_left_07;
	private TextView three_tv_left_08;
	private TextView three_tv_left_09;
	
	
	
	private ReceiveDataAnalysis mReceiveDataAnalysis;
	private DataConvert mDataConvert;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_info_battery_module, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}
	// EventBus
	@Subscribe(threadMode= ThreadMode.MainThread,sticky = true)
	public void initSystem(EventSystem event){
		if ("INIT".equals(event.getKey())){
			// 界面初始化
			initView();
		}
	}
	private void initView() {
		three_tv_right_01 =  (TextView) view.findViewById(R.id.three_tv_right_01);
		three_tv_right_02 =  (TextView) view.findViewById(R.id.three_tv_right_02);
		three_tv_right_03 =  (TextView) view.findViewById(R.id.three_tv_right_03);
		three_tv_right_04 =  (TextView) view.findViewById(R.id.three_tv_right_04);
		three_tv_right_05 =  (TextView) view.findViewById(R.id.three_tv_right_05);
		three_tv_right_06 =  (TextView) view.findViewById(R.id.three_tv_right_06);
		three_tv_right_07 =  (TextView) view.findViewById(R.id.three_tv_right_07);
		three_tv_right_08 =  (TextView) view.findViewById(R.id.three_tv_right_08);
		three_tv_right_09 =  (TextView) view.findViewById(R.id.three_tv_right_09);
		three_tv_left_01 =  (TextView) view.findViewById(R.id.three_tv_left_01);
		three_tv_left_02 =  (TextView) view.findViewById(R.id.three_tv_left_02);
		three_tv_left_03 =  (TextView) view.findViewById(R.id.three_tv_left_03);
		three_tv_left_04 =  (TextView) view.findViewById(R.id.three_tv_left_04);
		three_tv_left_05 =  (TextView) view.findViewById(R.id.three_tv_left_05);
		three_tv_left_06 =  (TextView) view.findViewById(R.id.three_tv_left_06);
		three_tv_left_07 =  (TextView) view.findViewById(R.id.three_tv_left_07);
		three_tv_left_08 =  (TextView) view.findViewById(R.id.three_tv_left_08);
		three_tv_left_09 =  (TextView) view.findViewById(R.id.three_tv_left_09);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mReceiveDataAnalysis = ReceiveDataAnalysis.getInstance();
		mDataConvert = new DataConvert();
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onEvent(EventSerialReadRequest event) {
		if ((mReceiveDataAnalysis == null)||(mDataConvert==null)) {
			return;
		}
		switch (event.getKey())
		{
			case "BATTERYBAGINFOR":
				three_tv_left_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryBagInfor.getMinVoltage()+""));
				three_tv_right_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryBagInfor.getPlatformVoltage()+""));
				three_tv_right_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryBagInfor.getMaxVoltage()+""));
				break;
			case "BATTERYSTATUSONE":
				three_tv_right_09.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getBalancedState()+""));
				break;
			case "BATTERYSTATUSFOUR":
				three_tv_left_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getCellAverT()+""));
				three_tv_left_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getCellMinT()+""));
				three_tv_left_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getMinTCellCSCNum()+""));
				three_tv_left_06.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getMinTCellCSCLoc()+""));
				three_tv_right_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getCellMaxT()+""));
				three_tv_right_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getMaxTCellCSCNum()+""));
				three_tv_right_06.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFour.getMaxTCellCSCLoc()+""));
				break;
			case "BATTERYSTATUSFIVE":
				three_tv_left_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getCellMinV()+""));
				three_tv_right_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getCellMaxV()+""));
				three_tv_left_07.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getMinVCellCSCNum()+""));
				three_tv_left_08.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getMinVCellCSCLoc()+""));
				three_tv_right_07.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getMaxVCellCSCNum()+""));
				three_tv_right_08.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getMaxVCellCSCLoc()+""));
				three_tv_left_09.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusFive.getCellAverV()+""));
				break;
		}
	}

}
