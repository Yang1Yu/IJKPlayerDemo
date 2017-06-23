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

public class FragmentCloudInfoBatteryManageTrouble extends Fragment{
	
	private View view;
	
	private TextView two_tv_right_01;
	private TextView two_tv_left_01;
	private TextView two_tv_left_02;
	
	private ReceiveDataAnalysis mReceiveDataAnalysis;
	private DataConvert mDataConvert;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_info_battery_trouble, container, false);
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
		two_tv_right_01 =  (TextView) view.findViewById(R.id.two_tv_right_01);
		two_tv_left_01 =  (TextView) view.findViewById(R.id.two_tv_left_01);
		two_tv_left_02 =  (TextView) view.findViewById(R.id.two_tv_left_02);

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
		if ((mReceiveDataAnalysis == null) || (mDataConvert == null)) {
			return;
		}
		switch (event.getKey()) {
			case "BATTERYSTATUSONE":
				two_tv_left_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getRelayStatus()));
				two_tv_left_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getTroubleGrade()+""));
				two_tv_right_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mBatteryStatusOne.getCurrentTrouble()+""));
				break;
		}
	}


}
