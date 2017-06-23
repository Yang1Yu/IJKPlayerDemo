package com.hejia.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Spanned;
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

public class FragmentCloudInfoDCACDCDC extends Fragment{
	
	private View view;
	
	private TextView dcdc_tv_right_01;
	private TextView dcdc_tv_right_02;
	private TextView dcdc_tv_right_03;
	private TextView dcdc_tv_right_04;
	private TextView dcdc_tv_right_05;
	private TextView dcdc_tv_right_06;
	
	private TextView dcdc_tv_left_01;
	private TextView dcdc_tv_left_02;
	private TextView dcdc_tv_left_03;
	private TextView dcdc_tv_left_04;
	private TextView dcdc_tv_left_05;
	private TextView dcdc_tv_left_06;
	
	private ReceiveDataAnalysis mReceiveDataAnalysis;
	private DataConvert mDataConvert;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_info_dcac_dcdc, container, false);
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
		dcdc_tv_right_01 =  (TextView) view.findViewById(R.id.dcdc_tv_right_01);
		dcdc_tv_right_02 =  (TextView) view.findViewById(R.id.dcdc_tv_right_02);
		dcdc_tv_right_03 =  (TextView) view.findViewById(R.id.dcdc_tv_right_03);
		dcdc_tv_right_04 =  (TextView) view.findViewById(R.id.dcdc_tv_right_04);
		dcdc_tv_right_05 =  (TextView) view.findViewById(R.id.dcdc_tv_right_05);
		dcdc_tv_right_06 =  (TextView) view.findViewById(R.id.dcdc_tv_right_06);
		dcdc_tv_left_01 =  (TextView) view.findViewById(R.id.dcdc_tv_left_01);
		dcdc_tv_left_02 =  (TextView) view.findViewById(R.id.dcdc_tv_left_02);
		dcdc_tv_left_03 =  (TextView) view.findViewById(R.id.dcdc_tv_left_03);
		dcdc_tv_left_04 =  (TextView) view.findViewById(R.id.dcdc_tv_left_04);
		dcdc_tv_left_05 =  (TextView) view.findViewById(R.id.dcdc_tv_left_05);
		dcdc_tv_left_06 =  (TextView) view.findViewById(R.id.dcdc_tv_left_06);

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
		case "DCDCSTATUS":
			Spanned a = mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getOutA()+"");
			dcdc_tv_right_01.setText(a);
			dcdc_tv_right_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getConyrolT()+""));
			dcdc_tv_right_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getHardStatus()+""));
			dcdc_tv_right_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getIntAStatus()+""));
			dcdc_tv_right_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getOutVStatus()+""));
			dcdc_tv_right_06.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getBusbarV()+""));
			dcdc_tv_left_01.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getWorkStatus()+""));
			dcdc_tv_left_02.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getOutV()+""));
			dcdc_tv_left_03.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getAllLineStatue()+""));
			dcdc_tv_left_04.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getTStatus()+""));
			dcdc_tv_left_05.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getIntVStatus()+""));
			dcdc_tv_left_06.setText(mDataConvert.addUnlineToString(mReceiveDataAnalysis.mDCDCStatus.getLifecycle()+""));
			break;
		default:
			break;
		
		}
	}

}
