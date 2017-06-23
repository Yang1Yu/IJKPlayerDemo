package com.hejia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hejia.eventbus.EventCommand;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.util.IsFastDoubleClickUtil;
import com.hejia.webcam.WebcamPreview;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class FragmentMoni extends Fragment {
	private WebcamPreview webcamPreview0;
	private WebcamPreview webcamPreview1;
	private WebcamPreview webcamPreview2;
	private WebcamPreview webcamPreview3;
	private LinearLayout ll_top;
	private LinearLayout ll_bottom;
	private int tag_Webcam = 0;
	private MainActivity activity;
	private String showOrHide;

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_moni, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}

	// EventBus
	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void initSystem(EventSystem event) {
		if ("INIT".equals(event.getKey())) {
			// 界面初始化
			//  实例化摄像头
			webcamPreview0 = (WebcamPreview) view.findViewById(R.id.webcam_monitor_0);
			webcamPreview1 = (WebcamPreview) view.findViewById(R.id.webcam_monitor_1);
			webcamPreview2 = (WebcamPreview) view.findViewById(R.id.webcam_monitor_2);
			webcamPreview3 = (WebcamPreview) view.findViewById(R.id.webcam_monitor_3);
			ll_top = (LinearLayout) view.findViewById(R.id.ll_monitor_top);
			ll_bottom = (LinearLayout) view.findViewById(R.id.ll_monitor_bottom);
			if ("Moni".equals(activity.nowFragment)) {
				webcamPreview0.setVisibility(View.VISIBLE);
				webcamPreview1.setVisibility(View.VISIBLE);
				webcamPreview2.setVisibility(View.VISIBLE);
				webcamPreview3.setVisibility(View.VISIBLE);
			}
			// 摄像头点击事件
			webcamPreview0.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("Moni".equals(activity.nowFragment)) {
						if (IsFastDoubleClickUtil.isFastDoubleClick()) {

						} else {

							if (tag_Webcam != R.id.webcam_monitor_0) {
								webcamPreview0.setVisibility(View.GONE);
								webcamPreview1.setVisibility(View.GONE);
								webcamPreview2.setVisibility(View.GONE);
								webcamPreview3.setVisibility(View.GONE);
								webcamPreview0.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.VISIBLE);
								ll_bottom.setVisibility(View.GONE);
								tag_Webcam = R.id.webcam_monitor_0;
								// 防止画面重复 设置起始位
								WebcamPreview.cameraId = 1;
							} else {
								webcamPreview0.setVisibility(View.GONE);
								webcamPreview0.setVisibility(View.VISIBLE);
								webcamPreview1.setVisibility(View.VISIBLE);
								webcamPreview2.setVisibility(View.VISIBLE);
								webcamPreview3.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.VISIBLE);
								ll_bottom.setVisibility(View.VISIBLE);
								// 标记复位
								tag_Webcam = 0;
							}

						}
					}
				}
			});
			webcamPreview1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("Moni".equals(activity.nowFragment)) {
						if (IsFastDoubleClickUtil.isFastDoubleClick()) {

						} else {

							if (tag_Webcam != R.id.webcam_monitor_1) {
								webcamPreview0.setVisibility(View.GONE);
								webcamPreview1.setVisibility(View.GONE);
								webcamPreview2.setVisibility(View.GONE);
								webcamPreview3.setVisibility(View.GONE);
								webcamPreview1.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.VISIBLE);
								ll_bottom.setVisibility(View.GONE);
								tag_Webcam = R.id.webcam_monitor_1;
							} else {
								// 让其先消失 ∵从0加载后再路过此界面会重复 即重新初始化界面
								webcamPreview1.setVisibility(View.GONE);
								webcamPreview0.setVisibility(View.VISIBLE);
								webcamPreview1.setVisibility(View.VISIBLE);
								webcamPreview2.setVisibility(View.VISIBLE);
								webcamPreview3.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.VISIBLE);
								ll_bottom.setVisibility(View.VISIBLE);
								// 标记复位
								tag_Webcam = 0;
							}
						}
					}
				}
			});
			webcamPreview2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("Moni".equals(activity.nowFragment)) {
						if (IsFastDoubleClickUtil.isFastDoubleClick()) {

						} else {

							if (tag_Webcam != R.id.webcam_monitor_2) {
								webcamPreview0.setVisibility(View.GONE);
								webcamPreview1.setVisibility(View.GONE);
								webcamPreview2.setVisibility(View.GONE);
								webcamPreview3.setVisibility(View.GONE);
								webcamPreview2.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.GONE);
								ll_bottom.setVisibility(View.VISIBLE);
								tag_Webcam = R.id.webcam_monitor_2;
							} else {
								// 让其先消失 ∵从0加载后再路过此界面会重复 即重新初始化界面
								webcamPreview2.setVisibility(View.GONE);
								webcamPreview0.setVisibility(View.VISIBLE);
								webcamPreview1.setVisibility(View.VISIBLE);
								webcamPreview2.setVisibility(View.VISIBLE);
								webcamPreview3.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.VISIBLE);
								ll_bottom.setVisibility(View.VISIBLE);
								// 标记复位
								tag_Webcam = 0;
							}
						}
					}
				}
			});
			webcamPreview3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("Moni".equals(activity.nowFragment)) {
						if (IsFastDoubleClickUtil.isFastDoubleClick()) {

						} else {

							if (tag_Webcam != R.id.webcam_monitor_3) {
								webcamPreview0.setVisibility(View.GONE);
								webcamPreview1.setVisibility(View.GONE);
								webcamPreview2.setVisibility(View.GONE);
								webcamPreview3.setVisibility(View.GONE);
								webcamPreview3.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.GONE);
								ll_bottom.setVisibility(View.VISIBLE);
								tag_Webcam = R.id.webcam_monitor_3;
								// 防止画面重复 设置起始位
								// WebcamPreview.cameraId = 0;
							} else {
								webcamPreview3.setVisibility(View.GONE);
								webcamPreview0.setVisibility(View.VISIBLE);
								webcamPreview1.setVisibility(View.VISIBLE);
								webcamPreview2.setVisibility(View.VISIBLE);
								webcamPreview3.setVisibility(View.VISIBLE);
								ll_top.setVisibility(View.VISIBLE);
								ll_bottom.setVisibility(View.VISIBLE);
								// 标记复位
								tag_Webcam = 0;
							}
						}
					}
				}
			});
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}


	@Subscribe(threadMode = ThreadMode.MainThread)
	public void hideOrShow(EventCommand event) {
		if ("SURFACEVIEW".equals(event.getKey())) {
			if ("Moni".equals(event.getArg1())) {
				switch ((String) event.getArg2()) {
					case "HIDE":
						switch (tag_Webcam) {
							case R.id.webcam_monitor_0:
								webcamPreview0.setVisibility(View.GONE);
								break;
							case R.id.webcam_monitor_1:
								webcamPreview1.setVisibility(View.GONE);
								break;
							case R.id.webcam_monitor_2:
								webcamPreview2.setVisibility(View.GONE);
								break;
							case R.id.webcam_monitor_3:
								webcamPreview3.setVisibility(View.GONE);
								break;
							case 0:
								webcamPreview0.setVisibility(View.GONE);
								webcamPreview1.setVisibility(View.GONE);
								webcamPreview2.setVisibility(View.GONE);
								webcamPreview3.setVisibility(View.GONE);
								break;

							default:
								break;
						}
						ll_top.setVisibility(View.VISIBLE);
						ll_bottom.setVisibility(View.VISIBLE);
						tag_Webcam = 0;
						break;
					case "SHOW":
						webcamPreview0.setVisibility(View.VISIBLE);
						webcamPreview1.setVisibility(View.VISIBLE);
						webcamPreview2.setVisibility(View.VISIBLE);
						webcamPreview3.setVisibility(View.VISIBLE);
						break;
				}
			}
		}
	}
}
