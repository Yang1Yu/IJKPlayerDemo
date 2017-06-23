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

public class FragmentCloudInfoBatteryManageBasic extends Fragment{
	private View view;

	private TextView one_tv_right_01;
	private TextView one_tv_right_02;
	private TextView one_tv_right_03;
	private TextView one_tv_right_04;
	private TextView one_tv_right_05;
	private TextView one_tv_right_06;
	private TextView one_tv_right_07;
	private TextView one_tv_right_08;

	private TextView one_tv_left_01;
	private TextView one_tv_left_02;
	private TextView one_tv_left_03;
	private TextView one_tv_left_04;
	private TextView one_tv_left_05;
	private TextView one_tv_left_06;
	private TextView one_tv_left_07;
	private TextView one_tv_left_08;


	private ReceiveDataAnalysis mReceiveDataAnalysis;
	private DataConvert mDataConvert;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_info_battery_basic, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}

	// EventBus
	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void initSystem(EventSystem event) {
		if ("INIT".equals(event.getKey())) {
			// 界面初始化
			initView();
		}
	}

	private void initView() {
		one_tv_right_01 = (TextView) view.findViewById(R.id.one_tv_right_01);
		one_tv_right_02 = (TextView) view.findViewById(R.id.one_tv_right_02);
		one_tv_right_03 = (TextView) view.findViewById(R.id.one_tv_right_03);
		one_tv_right_04 = (TextView) view.findViewById(R.id.one_tv_right_04);
		one_tv_right_05 = (TextView) view.findViewById(R.id.one_tv_right_05);
		one_tv_right_06 = (TextView) view.findViewById(R.id.one_tv_right_06);
		one_tv_right_07 = (TextView) view.findViewById(R.id.one_tv_right_07);
		one_tv_right_08 = (TextView) view.findViewById(R.id.one_tv_right_08);
		one_tv_left_01 = (TextView) view.findViewById(R.id.one_tv_left_01);
		one_tv_left_02 = (TextView) view.findViewById(R.id.one_tv_left_02);
		one_tv_left_03 = (TextView) view.findViewById(R.id.one_tv_left_03);
		one_tv_left_04 = (TextView) view.findViewById(R.id.one_tv_left_04);
		one_tv_left_05 = (TextView) view.findViewById(R.id.one_tv_left_05);
		one_tv_left_06 = (TextView) view.findViewById(R.id.one_tv_left_06);
		one_tv_left_07 = (TextView) view.findViewById(R.id.one_tv_left_07);
		one_tv_left_08 = (TextView) view.findViewById(R.id.one_tv_left_08);

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
				one_tv_left_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryBagInfor.getBatteryType()));
				one_tv_left_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryBagInfor.getBatteryCapacity() + ""));

				break;
			case "BATTERYSTATUSONE":
				one_tv_left_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getElectLineStatus() + ""));
				one_tv_left_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getElectMode() + ""));
				one_tv_left_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getElectStatus() + ""));
				one_tv_left_06.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getCurrentStatus() + ""));
				break;
			case "BATTERYSTATUSTWO":
				one_tv_left_07.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusTwo.getAllA() + ""));
				one_tv_left_08.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusTwo.getSoh() + ""));
				one_tv_right_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusTwo.getMaxChargeA() + ""));
				one_tv_right_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusTwo.getMaxDischargeA() + ""));
				one_tv_right_07.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusTwo.getSoc() + ""));
				break;
			case "BATTERYSTATUSTHREE":
				one_tv_right_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusThree.getPosiInsR() + ""));
				one_tv_right_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusThree.getNegaInsR() + ""));
				one_tv_right_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusThree.getBatteryHV() + ""));
				one_tv_right_06.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusThree.getGenHV() + ""));
				break;
		}

	}


}
