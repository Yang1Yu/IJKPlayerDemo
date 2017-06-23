package com.hejia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hejia.eventbus.EventMediaRequest;
import com.hejia.eventbus.EventSerialReadRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.myview.RotateCircleProgressBar;
import com.hejia.myview.SpeedCircleProgressBar;
import com.hejia.tp_launcher_v3.R;
import com.hejia.exception.MyRuntimeException;
import com.hejia.serialport.ReceiveDataAnalysis;
import com.hejia.util.IsFastChangeFragmentUtil;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCloud extends Fragment {
	private View view;
	// 设置界面的子碎片管理
	private FragmentManager managerCloud;
	private Fragment mContentFragment;

	private RadioGroup rgCloud;
	private RadioButton rbCloudInfo;
	private RadioButton rbCloudControl;
	private RadioButton rbCloudDiagnose;

//	private ImageView ivCloudInfoTitleBatteryQuantity;

	private FrameLayout flCloudTitle;
	//	private LinearLayout llCloudTitleLeft01;
//	private LinearLayout llCloudTitleLeft02;
//	private LinearLayout llCloudTitleRight;
	private RotateCircleProgressBar pbarRotate;
	private SpeedCircleProgressBar pbarSpeed;
	private ImageView ivCloudInfoCover;

	private FragmentCloudInfo fmCloudInfo;
	private FragmentCloudControl fmCloudControl;
	private FragmentCloudDiagnose fmCloudDiagnose;

	private TextView tvCloudInfoTitleTemper;
	private TextView tvCloudInfoTitleSpeed;
	private TextView tvCloudInfoTitleRotate;
//	private TextView tvCloudInfoTitleBatteryQuantity;
//	private TextView tvCloudInfoTitleMileage;

	private ReceiveDataAnalysis mReceiveDataAnalysis;
//    public static Handler mHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}

	private void initView() {
		rgCloud = (RadioGroup) view.findViewById(R.id.rg_cloud);
		rbCloudInfo = (RadioButton) view.findViewById(R.id.rb_cloud_info);
		rbCloudControl = (RadioButton) view.findViewById(R.id.rb_cloud_control);
		rbCloudDiagnose = (RadioButton) view.findViewById(R.id.rb_cloud_diagnose);

		flCloudTitle = (FrameLayout) view.findViewById(R.id.fl_cloud_title);
//		llCloudTitleLeft01 = (LinearLayout) view.findViewById(R.id.ll_cloud_title_left_01);
//		llCloudTitleLeft02 = (LinearLayout) view.findViewById(R.id.ll_cloud_title_left_02);
//		llCloudTitleRight = (LinearLayout) view.findViewById(R.id.ll_cloud_title_right);
		pbarRotate = (RotateCircleProgressBar) view.findViewById(R.id.pbar_cloud_info_title_rotate);
		pbarRotate.setPercent(67);
		pbarSpeed = (SpeedCircleProgressBar) view.findViewById(R.id.pbar_cloud_info_title_speed);
		pbarSpeed.setSpeed(50);
		ivCloudInfoCover = (ImageView) view.findViewById(R.id.iv_cloud_title_info_cover);


		tvCloudInfoTitleSpeed = (TextView) view.findViewById(R.id.tv_cloud_info_title_speed);
		tvCloudInfoTitleRotate = (TextView) view.findViewById(R.id.tv_cloud_info_title_rotate);
//		tvCloudInfoTitleBatteryQuantity = (TextView) view.findViewById(R.id.tv_cloud_info_title_battery_quantity);
//		tvCloudInfoTitleMileage = (TextView) view.findViewById(R.id.tv_cloud_info_title_mileage);

//		ivCloudInfoTitleBatteryQuantity = (ImageView) view.findViewById(R.id.iv_cloud_info_title_battery_quantity);


	}

	// EventBus
	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void initSystem(EventSystem event) {
		if ("INIT".equals(event.getKey())) {
			// 界面初始化
			managerCloud = getChildFragmentManager();
			initFragment();
			initView();
			initListener();
			mContentFragment = fmCloudControl;
			managerCloud.beginTransaction().replace(R.id.fragment_cloud, fmCloudControl).commit();
			rbCloudControl.setChecked(true);
		}
	}

	private void initFragment() {
		fmCloudInfo = new FragmentCloudInfo();
		fmCloudControl = new FragmentCloudControl();
		fmCloudDiagnose = new FragmentCloudDiagnose();
	}

	private void initListener() {
		rgCloud.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (IsFastChangeFragmentUtil.isFastChangeFragment()) {

				} else {

					switch (checkedId) {
						case R.id.rb_cloud_info:
							rbCloudInfo.setChecked(true);
							updataCloudTitle(0);
							changeMcontentFragmentCloud(fmCloudInfo);
							rbCloudInfo.setChecked(true);
							break;
						case R.id.rb_cloud_control:
							rbCloudControl.setChecked(true);
							updataCloudTitle(1);
							changeMcontentFragmentCloud(fmCloudControl);
							rbCloudControl.setChecked(true);
							break;
						case R.id.rb_cloud_diagnose:
							rbCloudDiagnose.setChecked(true);
							updataCloudTitle(2);
							changeMcontentFragmentCloud(fmCloudDiagnose);
							rbCloudDiagnose.setChecked(true);
							break;
						default:
							break;
					}
				}
			}
		});
	}

	// 碎片替换帮助函数
	private synchronized void changeMcontentFragmentCloud(Fragment to) {
		rbtnUnable();
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerCloud.beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(mContentFragment).add(R.id.fragment_cloud, to).commit();
			} else {
				transaction.hide(mContentFragment).show(to).commit();
			}
			mContentFragment = to;
		}


	}

	// RadioButton失效200ms
	private void rbtnUnable() {
		rbCloudInfo.setEnabled(false);
		rbCloudControl.setEnabled(false);
		rbCloudDiagnose.setEnabled(false);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rbCloudInfo.setEnabled(true);
		rbCloudControl.setEnabled(true);
		rbCloudDiagnose.setEnabled(true);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			// throw new RuntimeException(e);
			throw new MyRuntimeException("No Activity");
		} catch (IllegalAccessException e) {
			// throw new RuntimeException(e);
			throw new MyRuntimeException("No Activity");
		}
	}

	// 根据切换的标题更新标题界面 0：车辆信息 1：车辆控制 2：整车诊断
	private void updataCloudTitle(int index) {
		switch (index) {
			case 0:
				flCloudTitle.setBackgroundResource(R.drawable.img_cloud_info);
//				llCloudTitleLeft01.setVisibility(View.INVISIBLE);
//				llCloudTitleLeft02.setVisibility(View.VISIBLE);
//				llCloudTitleRight.setVisibility(View.VISIBLE);
//				tvCloudInfoTitleMileage.setVisibility(View.VISIBLE);
				tvCloudInfoTitleSpeed.setVisibility(View.VISIBLE);
				tvCloudInfoTitleRotate.setVisibility(View.VISIBLE);
				pbarRotate.setVisibility(View.VISIBLE);
				pbarSpeed.setVisibility(View.VISIBLE);
				ivCloudInfoCover.setVisibility(View.VISIBLE);
				break;
			case 1:
				flCloudTitle.setBackgroundResource(R.drawable.img_cloud_control);
//				llCloudTitleLeft01.setVisibility(View.INVISIBLE);
//				llCloudTitleLeft02.setVisibility(View.INVISIBLE);
//				llCloudTitleRight.setVisibility(View.INVISIBLE);
//				tvCloudInfoTitleMileage.setVisibility(View.GONE);
				tvCloudInfoTitleSpeed.setVisibility(View.GONE);
				tvCloudInfoTitleRotate.setVisibility(View.GONE);
				pbarRotate.setVisibility(View.GONE);
				pbarSpeed.setVisibility(View.GONE);
				ivCloudInfoCover.setVisibility(View.GONE);
				break;
			case 2:
				flCloudTitle.setBackgroundResource(R.drawable.img_cloud_diagnose);
//				llCloudTitleLeft01.setVisibility(View.VISIBLE);
//				llCloudTitleLeft02.setVisibility(View.INVISIBLE);
//				llCloudTitleRight.setVisibility(View.INVISIBLE);
//				tvCloudInfoTitleMileage.setVisibility(View.GONE);
				tvCloudInfoTitleSpeed.setVisibility(View.GONE);
				tvCloudInfoTitleRotate.setVisibility(View.GONE);
				pbarRotate.setVisibility(View.GONE);
				pbarSpeed.setVisibility(View.GONE);
				ivCloudInfoCover.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mReceiveDataAnalysis = ReceiveDataAnalysis.getInstance();
	}

	@Override
	public void onStart() {
		super.onStart();
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                //更新速度
//                Bundle b = msg.getData();
//                float speed = b.getFloat("speed");
//                tvCloudInfoTitleSpeed.setText(speed + "");
//            }
//
//		;
//	}

		;
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
		if (mReceiveDataAnalysis == null) {
			return;
		}
		switch (event.getKey()) {
			case "MOTORSTATUSONE":
				float rote = (float) (mReceiveDataAnalysis.mMotorStatusOne.getRote() * 0.001);
				tvCloudInfoTitleRotate.setText(rote + "");
				break;
			case "BATTERYSTATUSTWO":
//				tvCloudInfoTitleBatteryQuantity.setText((mReceiveDataAnalysis.mBatteryStatusTwo.getSoc()) + "%");
				float quantity = (int) mReceiveDataAnalysis.mBatteryStatusTwo.getSoc();
				if (quantity >= 10) {
//					ivCloudInfoTitleBatteryQuantity.setImageResource(R.drawable.img_cloud_battery_quantity1);
//					LayoutParams lp = ivCloudInfoTitleBatteryQuantity.getLayoutParams();
//					lp.width = (int) (mReceiveDataAnalysis.mBatteryStatusTwo.getSoc() * 0.66);
//					ivCloudInfoTitleBatteryQuantity.setLayoutParams(lp);
				} else if ((quantity >= 0) && (quantity < 10)) {
//					LayoutParams lp = ivCloudInfoTitleBatteryQuantity.getLayoutParams();
					if (quantity != 0) {
//						lp.width = (int) (100 * 0.5);
					} else {
//						lp.width = (int) (0 * 0.5);
					}
//					ivCloudInfoTitleBatteryQuantity.setLayoutParams(lp);
//					ivCloudInfoTitleBatteryQuantity.setImageResource(R.drawable.img_cloud_battery_quantity_low);
				}


			default:
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void changeSpeed(EventMediaRequest event) {
		if ("SPEED".equals(event.getKey())) {
			tvCloudInfoTitleSpeed.setText(event.getObj() + "");
		}
	}

}
