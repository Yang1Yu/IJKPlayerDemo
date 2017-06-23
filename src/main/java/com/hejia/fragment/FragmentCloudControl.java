package com.hejia.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCloudControl extends Fragment {
	private View view;

	private boolean tagFan = false;
	private boolean tagBVen = false;
	private boolean tagAVen = false;
	private boolean tagFootLamp = false;
	private boolean tagRoomLamp = false;
	private boolean tagDriverLamp = false;
	private boolean tagGuideBoard = false;
	private boolean tagStopLamp = false;
	private boolean tagExtendedLamp = false;
	private boolean tagRearviewDefrost = false;
	private boolean tagBDoor = false;
	private boolean tagADoor = false;

	private Button btnFan;
	private Button btnBVen;
	private Button btnAVen;
	private Button btnFootLamp;
	private Button btnRoomLamp;
	private Button btnDriverLamp;
	private Button btnGuideBoard;
	private Button btnStopLamp;
	private Button btnExtendedLamp;
	private Button btnRearviewDefrost;
	private Button btnBDoor;
	private Button btnADoor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_control, container, false);
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
			initListener();

		}
	}

	private void initView() {
		btnFan = (Button) view.findViewById(R.id.btn_cloud_control_fan);
		btnBVen = (Button) view.findViewById(R.id.btn_cloud_control_bven);
		btnAVen = (Button) view.findViewById(R.id.btn_cloud_control_aven);
		btnFootLamp = (Button) view.findViewById(R.id.btn_cloud_control_footlamp);
		btnRoomLamp = (Button) view.findViewById(R.id.btn_cloud_control_roomlamp);
		btnDriverLamp = (Button) view.findViewById(R.id.btn_cloud_control_driverlamp);
		btnGuideBoard = (Button) view.findViewById(R.id.btn_cloud_control_guideboard);
		btnStopLamp = (Button) view.findViewById(R.id.btn_cloud_control_stoplamp);
		btnExtendedLamp = (Button) view.findViewById(R.id.btn_cloud_control_extendedlamp);
		btnRearviewDefrost = (Button) view.findViewById(R.id.btn_cloud_control_rearviewdefrost);
		btnBDoor = (Button) view.findViewById(R.id.btn_cloud_control_bdoor);
		btnADoor = (Button) view.findViewById(R.id.btn_cloud_control_adoor);
	}

	private void initListener() {
		btnFan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagFan) {
					btnFan.setBackgroundResource(R.drawable.img_cloud_control_fan1);
				} else {
					btnFan.setBackgroundResource(R.drawable.img_cloud_control_fan2);
				}
				tagFan = !tagFan;
			}
		});
		btnBVen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagBVen) {
					btnBVen.setBackgroundResource(R.drawable.img_cloud_control_bven1);
				} else {
					btnBVen.setBackgroundResource(R.drawable.img_cloud_control_bven2);
				}
				tagBVen = !tagBVen;
			}
		});
		btnAVen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagAVen) {
					btnAVen.setBackgroundResource(R.drawable.img_cloud_control_aven1);
				} else {
					btnAVen.setBackgroundResource(R.drawable.img_cloud_control_aven2);
				}
				tagAVen = !tagAVen;
			}
		});
		btnFootLamp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagFootLamp) {
					btnFootLamp.setBackgroundResource(R.drawable.img_cloud_control_footlamp1);
				} else {
					btnFootLamp.setBackgroundResource(R.drawable.img_cloud_control_footlamp2);
				}
				tagFootLamp = !tagFootLamp;
			}
		});
		btnRoomLamp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagRoomLamp) {
					btnRoomLamp.setBackgroundResource(R.drawable.img_cloud_control_roomlamp1);
				} else {
					btnRoomLamp.setBackgroundResource(R.drawable.img_cloud_control_roomlamp2);
				}
				tagRoomLamp = !tagRoomLamp;
			}
		});
		btnDriverLamp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagDriverLamp) {
					btnDriverLamp.setBackgroundResource(R.drawable.img_cloud_control_driverlamp1);
				} else {
					btnDriverLamp.setBackgroundResource(R.drawable.img_cloud_control_driverlamp2);
				}
				tagDriverLamp = !tagDriverLamp;
			}
		});
		btnGuideBoard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagGuideBoard) {
					btnGuideBoard.setBackgroundResource(R.drawable.img_cloud_control_guideboard1);
				} else {
					btnGuideBoard.setBackgroundResource(R.drawable.img_cloud_control_guideboard2);
				}
				tagGuideBoard = !tagGuideBoard;
			}
		});
		btnStopLamp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagStopLamp) {
					btnStopLamp.setBackgroundResource(R.drawable.img_cloud_control_stoplamp1);
				} else {
					btnStopLamp.setBackgroundResource(R.drawable.img_cloud_control_stoplamp2);
				}
				tagStopLamp = !tagStopLamp;
			}
		});
		btnExtendedLamp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagExtendedLamp) {
					btnExtendedLamp.setBackgroundResource(R.drawable.img_cloud_control_extendedlamp1);
				} else {
					btnExtendedLamp.setBackgroundResource(R.drawable.img_cloud_control_extendedlamp2);
				}
				tagExtendedLamp = !tagExtendedLamp;
			}
		});
		btnRearviewDefrost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagRearviewDefrost) {
					btnRearviewDefrost.setBackgroundResource(R.drawable.img_cloud_control_rearviewdefrost1);
				} else {
					btnRearviewDefrost.setBackgroundResource(R.drawable.img_cloud_control_rearviewdefrost2);
				}
				tagRearviewDefrost = !tagRearviewDefrost;
			}
		});
		btnBDoor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagBDoor) {
					btnBDoor.setBackgroundResource(R.drawable.img_cloud_control_bdoor1);
				} else {
					btnBDoor.setBackgroundResource(R.drawable.img_cloud_control_bdoor2);
				}
				tagBDoor = !tagBDoor;
			}
		});
		btnADoor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tagADoor) {
					btnADoor.setBackgroundResource(R.drawable.img_cloud_control_adoor1);
				} else {
					btnADoor.setBackgroundResource(R.drawable.img_cloud_control_adoor2);
				}
				tagADoor = !tagADoor;
			}
		});


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
	}
}
