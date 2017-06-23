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

public class FragmentCloudInfoDCACAirPump extends Fragment{
	
	private View view;
	
	private TextView airpump_tv_right_01;
	private TextView airpump_tv_right_02;
	private TextView airpump_tv_right_03;
	private TextView airpump_tv_right_04;
	private TextView airpump_tv_right_05;
	
	private TextView airpump_tv_left_01;
	private TextView airpump_tv_left_02;
	private TextView airpump_tv_left_03;
	private TextView airpump_tv_left_04;
	private TextView airpump_tv_left_05;
	
	
	
	private ReceiveDataAnalysis mReceiveDataAnalysis;
	private DataConvert mDataConvert;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_info_dcac_airpump, container, false);
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
		airpump_tv_right_01 =  (TextView) view.findViewById(R.id.airpump_tv_right_01);
		airpump_tv_right_02 =  (TextView) view.findViewById(R.id.airpump_tv_right_02);
		airpump_tv_right_03 =  (TextView) view.findViewById(R.id.airpump_tv_right_03);
		airpump_tv_right_04 =  (TextView) view.findViewById(R.id.airpump_tv_right_04);
		airpump_tv_right_05 =  (TextView) view.findViewById(R.id.airpump_tv_right_05);
		airpump_tv_left_01 =  (TextView) view.findViewById(R.id.airpump_tv_left_01);
		airpump_tv_left_02 =  (TextView) view.findViewById(R.id.airpump_tv_left_02);
		airpump_tv_left_03 =  (TextView) view.findViewById(R.id.airpump_tv_left_03);
		airpump_tv_left_04 =  (TextView) view.findViewById(R.id.airpump_tv_left_04);
		airpump_tv_left_05 =  (TextView) view.findViewById(R.id.airpump_tv_left_05);
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
			case "AIRPUMPSTATUS":
				airpump_tv_right_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getOutA()+""));
				airpump_tv_right_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getTStatus()+""));
				airpump_tv_right_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getIntAStatus()+""));
				airpump_tv_right_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getIntVStatus()+""));
				airpump_tv_right_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getAirPumpT()+""));
				airpump_tv_left_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getOutV()+""));
				airpump_tv_left_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getWorkStatus()+""));
				airpump_tv_left_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getAllLineStatue()+""));
				airpump_tv_left_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getPhaseStatus()+""));
				airpump_tv_left_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mAirPumpStatus.getLifecycle()+""));
				break;
		}
		
	}

}
