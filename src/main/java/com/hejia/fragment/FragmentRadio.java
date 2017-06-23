package com.hejia.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.hejia.eventbus.EventAudioRequest;
import com.hejia.eventbus.EventCommand;
import com.hejia.eventbus.EventMediaRequest;
import com.hejia.eventbus.EventRadioRequest;
import com.hejia.serialport.DataConvert;
import com.hejia.speech.IatDataFormat;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

@SuppressLint("InflateParams")
public class FragmentRadio extends Fragment {
	private PopupWindow window;
	private PopupWindow pop_radio;
	public Activity radioActivity;
	private View view;
	private Button bt_radiopop_know;
	private Button bt_radio_button1;
	private Button bt_radio_button2;
	private Button bt_radio_button3;
	private Button bt_radio_button4;
	private Button bt_radio_button5;
	private Button bt_radio_button6;
	private Button bt_radio_pre;
	private Button bt_radio_left;
	private Button bt_radio_right;
	private Button bt_radio_next;
	public static Button bt_radio_switch;
	private Button bt_radio_preview;
	private Button bt_radio_save;
	private TextView tv_radio_text;
	private Button btnRadioShield;
	// private TextView tv_radio_unit;
	// private TextView tv_fm;
	private Button bt_radio_pop_ok;
	private Button bt_radio_pop_no;
	private boolean tagIsSave = false;
	// 滑动刻度条相关
	private HorizontalScrollView rulerMain;
	private LinearLayout layoutRuler;
	private int beginChannel = 870;  //初始频道
	private int intNowChannel = 0;// 缩小十倍显示 968表示调频96.8即jni中的9680
	private String strNowChannel;
	private int screenWidth = 770;

	public static boolean isradio = false;// 收音机为关闭状态
	// 频率
	private float freq;
	private int iRetAuto;
	private int iRetscanup;
	private int iRetscandown;
	private int iRetstepup;
	private int iRetstepdown;

	private int iretSet;

	private int iRet1;

	private int iVolumeLev = 8;

	// jni functions
	public native int FmSetFreq(int Freq);

	public native int FmStepUp();

	public native int FmStepDown();

	public native int FmScanUp();

	public native int FmScanDown();

	public native int[] FmAutoScan();

	public native int RadioAudioInit();//

	public static Handler mhandler = null;
	private int[] FreqScanArray;

	private boolean FmAutoScanStart = false;
	private boolean FmScanUpStart = false;
	private boolean FmScanDownStart = false;
	private boolean FmBusy = false;
	private boolean radioPopBusy = false;

	public static boolean isFirstStart = true;
	public static boolean isStart = false;
	private boolean isEnable = true;
	private ArrayList<String> freqlist = null;///用来存储当前更新出的频道或者数据库存储的

	//	private SeekBar sb_radio_seekbar;
	private FragmentMedia mFragmentMedia;
	private MainActivity activity;
	private IatDataFormat mIatDataConvert;
	private DataConvert mDataConvert;

	private String[] openMark;
	private String[] closeMark;
	private String[] setFreMark;
	private String[] updataMark;

	// 设置字体
	private Typeface typeface;

	public String mfreq = "";

	private boolean radioViewIsOpen = false;

	// public native int RadioAudioInit();

	/* AudioSource: 0=RADIO,1=BT,2=CPU */
	// public native int AudioSetSource(int AudioSource);


	static {
		// Log.d(TAG, "loadLibrary");
		System.loadLibrary("tef663x_radio_jni");
	}

	public FragmentRadio() {
		// 注册EventBus
		EventBus.getDefault().register(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		radioActivity = getActivity();
		mFragmentMedia = com.hejia.fragment.FragmentMedia.getInstance();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_radio, container, false);
		freqlist = new ArrayList<String>();
		mIatDataConvert = new IatDataFormat();
		mDataConvert = new DataConvert();
		isStart = true;

		// 设置字体
		typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Let's go Digital Regular.ttf");


		bt_radio_button1 = (Button) view.findViewById(R.id.btn_radio_channel1);
		bt_radio_button2 = (Button) view.findViewById(R.id.btn_radio_channel2);
		bt_radio_button3 = (Button) view.findViewById(R.id.btn_radio_channel3);
		bt_radio_button4 = (Button) view.findViewById(R.id.btn_radio_channel4);
		bt_radio_button5 = (Button) view.findViewById(R.id.btn_radio_channel5);
		bt_radio_button6 = (Button) view.findViewById(R.id.btn_radio_channel6);
		bt_radio_button1.setTypeface(typeface);
		bt_radio_button2.setTypeface(typeface);
		bt_radio_button3.setTypeface(typeface);
		bt_radio_button4.setTypeface(typeface);
		bt_radio_button5.setTypeface(typeface);
		bt_radio_button6.setTypeface(typeface);
		bt_radio_pre = (Button) view.findViewById(R.id.btn_radio_last);
		bt_radio_left = (Button) view.findViewById(R.id.btn_radio_sub);
		bt_radio_right = (Button) view.findViewById(R.id.btn_radio_add);
		bt_radio_next = (Button) view.findViewById(R.id.btn_radio_next);
		bt_radio_switch = (Button) view.findViewById(R.id.btn_radio_switch);
		bt_radio_preview = (Button) view.findViewById(R.id.btn_radio_preview);
		bt_radio_save = (Button) view.findViewById(R.id.btn_radio_save);
		tv_radio_text = (TextView) view.findViewById(R.id.tv_radio_channel);
		tv_radio_text.setTypeface(typeface);
		btnRadioShield = (Button) view.findViewById(R.id.btn_radio_shield);
		// tv_radio_unit = (TextView) view.findViewById(R.id.tv_unit);
		mfreq = activity.toDoDB.select_channel_one();
		freqlist.add(mfreq);
		bt_radio_button1.setText(mfreq);
		mfreq = activity.toDoDB.select_channel_two();
		freqlist.add(mfreq);
		bt_radio_button2.setText(mfreq);
		mfreq = activity.toDoDB.select_channel_three();
		freqlist.add(mfreq);
		bt_radio_button3.setText(mfreq);
		mfreq = activity.toDoDB.select_channel_four();
		freqlist.add(mfreq);
		bt_radio_button4.setText(mfreq);
		mfreq = activity.toDoDB.select_channel_five();
		freqlist.add(mfreq);
		bt_radio_button5.setText(mfreq);
		mfreq = activity.toDoDB.select_channel_six();
		freqlist.add(mfreq);

		bt_radio_button6.setText(mfreq);


//		SetProgress(Float.parseFloat(tv_radio_text.getText().toString()));
		init();//  调频事件
		setMarkValue();
		initRadio();
		initRuler();
		updataOffStatus();
		scanThread.start();
		radioViewIsOpen = true;

		mhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 0://  更新
						updataOffStatus();
						String freq_str_scan = null;
						window.dismiss();
						if ((FreqScanArray != null) && (FreqScanArray[0] > 0)) {
							for (int i = 0; i < FreqScanArray[0]; i++) {

								freq = (float) FreqScanArray[1 + i] / 100;
								freq_str_scan = String.valueOf(freq);
								setStringToFreq(freq_str_scan);
								String[] freqArray = (String[]) freqlist.toArray(new String[0]);
								if (!mDataConvert.isContains(freqArray, freq_str_scan))
									freqlist.add(freq_str_scan);

								switch (i) {
									case 0:
										bt_radio_button1.setText(freq_str_scan);

										break;
									case 1:
										bt_radio_button2.setText(freq_str_scan);

										break;
									case 2:
										bt_radio_button3.setText(freq_str_scan);
										break;
									case 3:
										bt_radio_button4.setText(freq_str_scan);

										break;
									case 4:
										bt_radio_button5.setText(freq_str_scan);

										break;
									case 5:
										bt_radio_button6.setText(freq_str_scan);

										break;

									default:
										break;
								}

							}
						} else {
							radioPopBusy = true;

//						RadioPopwindow("没有搜索到可用频道！");
							updataOffStatus();
						}
						updataOffStatus();
						saveCurrentFres();

						break;
					case 1:
						String freq_str_scan_up = null;
						updataOffStatus();
						window.dismiss();
						if (iRetscanup > 0) {
							freq = (float) iRetscanup / 100;
							freq_str_scan_up = String.valueOf(freq);
							updateView(freq_str_scan_up);
						} else {
							// RadioPopwindow("没有搜索到相应频道！");
						}
						updataOffStatus();
						saveCurrentFres();
						break;
					case 2:
						String fre_str_scan_down = null;
						updataOffStatus();
						window.dismiss();
						if (iRetscandown > 0) {
							freq = (float) iRetscandown / 100;
							fre_str_scan_down = String.valueOf(freq);
							updateView(fre_str_scan_down);
						} else {
							// RadioPopwindow("没有搜索到相应频道！");
						}
						updataOffStatus();
						saveCurrentFres();
						break;
					case 3:
						String fre_str_step_up = null;
						updataOffStatus();
						if (iRetstepup > 0) {
							freq = (float) iRetstepup / 100;
							fre_str_step_up = String.valueOf(freq);
							updateView(fre_str_step_up);
						} else {
							// RadioPopwindow("没有搜索到相应频道！");
						}
						updataOffStatus();
//					saveCurrentFres();
						break;
					case 4:
						String fre_str_step_down = null;
						updataOffStatus();
						if (iRetstepdown > 0) {
							freq = (float) iRetstepdown / 100;
							fre_str_step_down = String.valueOf(freq);
							updateView(fre_str_step_down);
						} else {
							// RadioPopwindow("没有搜索到相应频道！");
						}
						updataOffStatus();
//					saveCurrentFres();
						break;
					case 5:
						// updataOffStatus();
						// activity.sendAudioBradcast("audioRadioOff");
						//// activity.toDoDB.update_setting("rb_radio_fm",
						// "off","table_radio");//保存收音机关闭
						// saveRadioSwitch(activity);
						initRadio();
						updataOffStatus();
						saveCurrentFres();
						break;

					default:
						break;
				}
			}

			;
		};

		initRadio();
		updataButtonBackground();
		return view;
	}

	private int intMainTVChannel = 0;
	private int remainderMainTVChannel = 0;

	// 初始化收音机滑动刻度尺
	private void initRuler() {
		// TODO 初始化收音机频道显示控件TextView
		strNowChannel = activity.toDoDB.select_channel_current();
		intNowChannel = (int) (Float.parseFloat(strNowChannel) * 10);
		tv_radio_text.setText(strNowChannel);
		rulerMain = (HorizontalScrollView) view.findViewById(R.id.hsv_radio_ruler_main);
		layoutRuler = (LinearLayout) view.findViewById(R.id.layout_radio_ruler);

		rulerMain.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				tagIsSave = false;
				int action = event.getAction();

				switch (action) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						if ((isradio) && (!FmBusy)) {
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									// TODO 更新收音机频道显示控件TextView
									// TODO 对收音机进行相应操作
									intNowChannel = (int) Math.ceil(rulerMain.getScrollX() / 20) + beginChannel;
									if (intNowChannel < 875) {
										intNowChannel = 875;
										scroll(intNowChannel);
									}
									FmSetFreq(intNowChannel * 10);
									activity.toDoDB.update_setting("channel_current", channelChangeFM(intNowChannel) + "", "table_radio");
									intMainTVChannel = intNowChannel / 10;
									remainderMainTVChannel = intNowChannel % 10;
									tv_radio_text.setText(intMainTVChannel + "." + remainderMainTVChannel);
									updataButtonBackground();
								}
							}, 2000);
						}

						break;
				}
				return false;
			}

		});
		// 初始化收音机进度条
		constructRuler();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				scroll(intNowChannel);
			}
		}, 150);
	}

	private float channelChangeFM(int nowChannle) {
		// 调频96.8 对应输入968
		float fm = (float) nowChannle / 10;
		return fm;
	}

	// 设置显示滑动刻度
	private void scroll(int channel) {
		// TODO 收音机进度条初始化
		// rulerMain.smoothScrollTo(( 当前值 - 开始值（即调频88.0）) * 2, 0);
		// 乘以20:每个小单元宽度200dp，每个小单元中含有10个小刻度，每个小刻度宽度为20
		if (channel >= beginChannel) {
			rulerMain.smoothScrollTo((channel - beginChannel) * 20, 0);
		}
	}

	// 构造滑动刻度尺
	private void constructRuler() {
		View leftview = (View) LayoutInflater.from(activity).inflate(
				R.layout.radio_ruler_fill, null);
		leftview.setLayoutParams(new LayoutParams(screenWidth / 2,
				LayoutParams.MATCH_PARENT));
		layoutRuler.addView(leftview);
		int intTVChannel = 0;
		int remainderTVChannel = 0;
		for (int i = 0; i < 21; i++) {
			View view = (View) LayoutInflater.from(activity).inflate(
					R.layout.radio_ruler_cell, null);
			view.setLayoutParams(new LayoutParams(200,
					LayoutParams.MATCH_PARENT));
			TextView tv = (TextView) view.findViewById(R.id.tv_radio_ruler_cell);
			tv.setTypeface(typeface);
			intTVChannel = (beginChannel + i * 10) / 10;
			remainderTVChannel = (beginChannel + i * 10) % 10;
			tv.setText(intTVChannel + "." + remainderTVChannel);
			layoutRuler.addView(view);
		}
		View rightview = (View) LayoutInflater.from(activity).inflate(
				R.layout.radio_ruler_fill, null);
		rightview.setLayoutParams(new LayoutParams(screenWidth / 2,
				LayoutParams.MATCH_PARENT));
		layoutRuler.addView(rightview);
	}

	Thread scanThread = new Thread(new Runnable() {

		@Override
		public void run() {

			while (true) {
				if (FmScanUpStart) {

					iRetscanup = FmScanUp();
					Message msg = mhandler.obtainMessage();
					msg.what = 1;
					mhandler.sendMessage(msg);
					intNowChannel = iRetscanup / 10;
					FmScanUpStart = false;
				}
				if (FmScanDownStart) {

					iRetscandown = FmScanDown();
					Message msg = mhandler.obtainMessage();
					msg.what = 2;
					mhandler.sendMessage(msg);
					intNowChannel = iRetscandown / 10;
					FmScanDownStart = false;
				}
				if (FmAutoScanStart) {
					FreqScanArray = null;
					FreqScanArray = FmAutoScan();

					Message msg = mhandler.obtainMessage();
					msg.what = 0;
					mhandler.sendMessage(msg);

					FmAutoScanStart = false;
				}

				FmBusy = false;

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}

		}
	});

	private void init() {
		bt_radio_switch.setOnClickListener(new OnClickListener() {
			String freq_str;

			@Override
			public void onClick(View v) {
				OnOffSwitch();
			}
		});
		bt_radio_preview.setOnClickListener(new OnClickListener() {
			// float freq1;

			@Override
			public void onClick(View v) {
				if (isradio && (!FmBusy)) {
					popWarning("搜索进行中......");
					resetButtonText();
					FmBusy = true;

					FmAutoScanStart = true;

					updataOffStatus();

					// scanThread.start();
				}

			}
		});
		bt_radio_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy) && (isEnable)) {
					isEnable = false;
					tagIsSave = true;
					// radioPopBusy = true ;
					RadioPopwindow("请选择保存的位置");
					updataOffStatus();
				}
			}
		});
		bt_radio_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					// 向左调频
					// scanDownThread.start();
					popWarning("搜索进行中......");
					FmBusy = true;
					FmScanDownStart = true;
					updataOffStatus();

				}

			}
		});
		bt_radio_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					// 向左调频0.1
					iRetstepdown = FmStepDown();
					intNowChannel = iRetstepdown / 10;
					Message msg = mhandler.obtainMessage();
					msg.what = 4;
					mhandler.sendMessage(msg);
//					if (intNowChannel > 880 && intNowChannel < 1091) {
//						intNowChannel--;
//					}
					updataButtonBackground();

				}
			}
		});
		bt_radio_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					// 向右调频0.1
					iRetstepup = FmStepUp();
					intNowChannel = iRetstepup / 10;
					Message msg = mhandler.obtainMessage();
					msg.what = 3;
					mhandler.sendMessage(msg);
//					if (intNowChannel > 879 && intNowChannel < 1090) {
//						intNowChannel++;
//					}
					updataButtonBackground();
				}
			}
		});
		bt_radio_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					// scanUpThread.start();
					popWarning("搜索进行中......");
					FmBusy = true;
					FmScanUpStart = true;
					updataOffStatus();

				}

			}
		});
		bt_radio_button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String freq_str;
				if ((isradio) && (!FmBusy)) {
					if (tagIsSave) {
						String fm = tv_radio_text.getText().toString();

						savetvtobutton(fm, 1);
						// updataButtonBackground();
						tagIsSave = false;
					} else {
						if (bt_radio_button1.getText().equals("000") || bt_radio_button1.getText().equals("")) {

						} else {
							String radio = bt_radio_button1.getText().toString();
							setStringToFreq(radio);
						}

					}
				} else {

				}
				updataOffStatus();
			}
		});
		bt_radio_button2.setOnClickListener(new OnClickListener() {
			String freq_str;

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					if (tagIsSave) {
						String fm = tv_radio_text.getText().toString();
//						bt_radio_button2.setText(fm);
//						activity.toDoDB.update_setting("channel_two", bt_radio_button2.getText().toString(),
//								"table_radio");
						savetvtobutton(fm, 2);
						// updataButtonBackground();
						tagIsSave = false;
					} else {
						if (bt_radio_button2.getText().equals("000") || bt_radio_button2.getText().equals("")) {

						} else {
							String radio = bt_radio_button2.getText().toString();
							setStringToFreq(radio);
						}

					}
				} else {

				}
				updataOffStatus();

			}
		});
		bt_radio_button3.setOnClickListener(new OnClickListener() {
			String freq_str;

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					if (tagIsSave) {
						String fm = tv_radio_text.getText().toString();
						// bt_radio_button3.setText(fm);
						// activity.toDoDB.update_setting("channel_three",
						// bt_radio_button3.getText().toString(),
						// "table_radio");
						savetvtobutton(fm, 3);
						// updataButtonBackground();
						tagIsSave = false;
					} else {

						if (bt_radio_button3.getText().equals("000") || bt_radio_button3.getText().equals("")) {

						} else {
							String radio = bt_radio_button3.getText().toString();
							setStringToFreq(radio);
						}

					}
				} else {

				}
				updataOffStatus();
			}
		});
		bt_radio_button4.setOnClickListener(new OnClickListener() {
			String freq_str;

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					if (tagIsSave) {
						String fm = tv_radio_text.getText().toString();
						savetvtobutton(fm, 4);
						// updataButtonBackground();
						// bt_radio_button4.setText(fm);
						// activity.toDoDB.update_setting("channel_four",
						// bt_radio_button4.getText().toString(),
						// "table_radio");
						tagIsSave = false;
					} else {
						if (bt_radio_button4.getText().equals("000") || bt_radio_button4.getText().equals("")) {

						} else {
							String radio = bt_radio_button4.getText().toString();
							setStringToFreq(radio);
						}

					}
				} else {

				}
				updataOffStatus();

			}
		});
		bt_radio_button5.setOnClickListener(new OnClickListener() {
			String freq_str;

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					if (tagIsSave) {
						String fm = tv_radio_text.getText().toString();
						// bt_radio_button5.setText(fm);
						// activity.toDoDB.update_setting("channel_five",
						// bt_radio_button5.getText().toString(),
						// "table_radio");
						savetvtobutton(fm, 5);
						tagIsSave = false;
					} else {

						if (bt_radio_button5.getText().equals("000") || bt_radio_button5.getText().equals("")) {

						} else {
							String radio = bt_radio_button5.getText().toString();
							setStringToFreq(radio);
						}

					}
					updataOffStatus();
				} else {

				}

			}
		});
		bt_radio_button6.setOnClickListener(new OnClickListener() {
			String freq_str;

			@Override
			public void onClick(View v) {
				if ((isradio) && (!FmBusy)) {
					if (tagIsSave) {
						String fm = tv_radio_text.getText().toString();
						// bt_radio_button6.setText(fm);
						// activity.toDoDB.update_setting("channel_six",
						// bt_radio_button6.getText().toString(),
						// "table_radio");
						savetvtobutton(fm, 6);
						tagIsSave = false;
					} else {
						if (bt_radio_button6.getText().equals("000") || bt_radio_button6.getText().equals("")) {

						} else {
							String radio = bt_radio_button6.getText().toString();
							setStringToFreq(radio);
						}

					}
					updataOffStatus();
				} else {

				}

			}
		});
	}

	//为收音机语音标志赋值
	private void setMarkValue() {
		Resources res = getResources();
		openMark = res.getStringArray(R.array.radio_open_mark);
		closeMark = res.getStringArray(R.array.radio_close_mark);
		setFreMark = res.getStringArray(R.array.radio_setfreq_mark);
		updataMark = res.getStringArray(R.array.radio_updata_mark);
	}

	public void updateView(String data) {
		tv_radio_text.setText(data);
//		SetProgress(Float.parseFloat(data));
		scroll(intNowChannel);
		activity.toDoDB.update_setting("channel_current", data, "table_radio");
		tagIsSave = false;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	// 保存提醒pop
	public void RadioPopwindow(String str) {
		// if(isNowPaper){
		LayoutInflater radio_inflater = (LayoutInflater) radioActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View radio_pop = radio_inflater.inflate(R.layout.pop_radio_save, null);
		bt_radiopop_know = (Button) radio_pop.findViewById(R.id.bt_radiopop_know);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		pop_radio = new PopupWindow(radio_pop, metrics.widthPixels, metrics.heightPixels);
		pop_radio.setFocusable(false);

		ColorDrawable dw = new ColorDrawable(0x66111111);
		pop_radio.setBackgroundDrawable(dw);

		// pop_radio.setAnimationStyle(R.style.mypopwindow_anim_style);

		TextView tv = (TextView) radio_pop.findViewById(R.id.textView1);
		tv.setText(str);
		pop_radio.showAtLocation(activity.findViewById(R.id.btn_main_bottom_switchbutton), Gravity.CENTER_HORIZONTAL, 0, 0);
		bt_radiopop_know.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop_radio.dismiss();
				radioPopBusy = false;
				updataOffStatus();
				isEnable = true;
			}
		});
		// }
		// else
		// {
		// radioPopBusy = false ;
		// updataOffStatus();
		// }
	}

	public void popWarning(String str) {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View m_view_pop = inflater.inflate(R.layout.pop_radio_warning, null);

		window = new PopupWindow(m_view_pop, 400, 230);

		// window.setTouchable(true);
		window.setFocusable(false);
		// window.setOutsideTouchable(false);

		ColorDrawable dw = new ColorDrawable(0x00000000);
		window.setBackgroundDrawable(dw);
		// window.setAnimationStyle(R.style.mypopwindow_anim_style);
		window.setBackgroundDrawable(new BitmapDrawable());
		window.showAtLocation(m_view_pop, Gravity.CENTER, 0, 0);

		// judge_AC_Tag(m_view_pop);
		TextView tv = (TextView) m_view_pop.findViewById(R.id.tv_content);
		tv.setText(str);

	}

	// 根据收音机开关与忙碌情况更新收音机显示状态
	private void updataOffStatus() {
//        Log.e("updataOffStatus","---");
		if ((!isradio) || (FmBusy)) {
			if (isAdded()) {
//				activity.toDoDB.update_setting("channel_current", tv_radio_text.getText().toString(), "table_radio");

				bt_radio_button1.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_button2.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_button3.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_button4.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_button5.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_button6.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_save.setTextColor(this.getResources().getColor(R.color.gray));
				tv_radio_text.setTextColor(this.getResources().getColor(R.color.current_radio));
				// tv_radio_unit.setTextColor(this.getResources().getColor(R.color.gray));
				// tv_fm.setTextColor(this.getResources().getColor(R.color.gray));
				bt_radio_preview.setBackgroundResource(R.drawable.bg_radio_preview);

				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel13);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel23);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel33);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel43);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel53);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel63);

				bt_radio_button1.setEnabled(false);
				bt_radio_button2.setEnabled(false);
				bt_radio_button3.setEnabled(false);
				bt_radio_button4.setEnabled(false);
				bt_radio_button5.setEnabled(false);
				bt_radio_button6.setEnabled(false);
				bt_radio_pre.setEnabled(false);
				bt_radio_left.setEnabled(false);
				bt_radio_right.setEnabled(false);
				bt_radio_next.setEnabled(false);
				// bt_radio_on.setEnabled(false);
				bt_radio_preview.setEnabled(false);
				bt_radio_save.setEnabled(false);
				btnRadioShield.setVisibility(View.VISIBLE);
				if (radioViewIsOpen) {
					rulerMain.setSmoothScrollingEnabled(false);
				}
//				sb_radio_seekbar.setEnabled(false);
			}
		} else {
			if (isAdded()) {
				tv_radio_text.setText(activity.toDoDB.select_channel_current());
//				SetProgress(Float.parseFloat(activity.toDoDB.select_channel_current()));
				bt_radio_button1.setTextColor(this.getResources().getColor(R.color.text_radio));
				bt_radio_button2.setTextColor(this.getResources().getColor(R.color.text_radio));
				bt_radio_button3.setTextColor(this.getResources().getColor(R.color.text_radio));
				bt_radio_button4.setTextColor(this.getResources().getColor(R.color.text_radio));
				bt_radio_button5.setTextColor(this.getResources().getColor(R.color.text_radio));
				bt_radio_button6.setTextColor(this.getResources().getColor(R.color.text_radio));
				bt_radio_preview.setBackgroundResource(R.drawable.bg_radio_preview);
				bt_radio_save.setTextColor(this.getResources().getColor(R.color.text_radio));
				tv_radio_text.setTextColor(this.getResources().getColor(R.color.gray));
				// tv_radio_unit.setTextColor(this.getResources().getColor(R.color.current_radio));
				// tv_fm.setTextColor(this.getResources().getColor(R.color.current_radio));
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);

				bt_radio_button1.setEnabled(true);
				bt_radio_button2.setEnabled(true);
				bt_radio_button3.setEnabled(true);
				bt_radio_button4.setEnabled(true);
				bt_radio_button5.setEnabled(true);
				bt_radio_button6.setEnabled(true);
				bt_radio_pre.setEnabled(true);
				bt_radio_left.setEnabled(true);
				bt_radio_right.setEnabled(true);
				bt_radio_next.setEnabled(true);
				bt_radio_preview.setEnabled(true);
				bt_radio_save.setEnabled(true);
				btnRadioShield.setVisibility(View.GONE);
				if (radioViewIsOpen) {
					rulerMain.setSmoothScrollingEnabled(true);
				}
//				sb_radio_seekbar.setEnabled(true);
			}
		}
		updataButtonBackground();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	//  隐藏popwindow
	public void invisiblePop() {
		isFirstStart = false;
		if (FmBusy) {
			window.dismiss();
		}
		if (radioPopBusy) {
//			pop_radio.dismiss();
			radioPopBusy = false;
			updataOffStatus();
		}
	}

	// 初始化收音机，判断当前收音机是开机还是关闭，状态与数据库保持一致
	private void initRadio() {
		String index = activity.toDoDB.select_radio_on_off();
		if (index.equals("off")) {
			isradio = false;
			bt_radio_switch.setBackgroundResource(R.drawable.bg_radio_switch1);
		} else {
			isradio = true;
			bt_radio_switch.setBackgroundResource(R.drawable.bg_radio_switch2);
		}
		if (isradio)
			EventBus.getDefault().post(new EventRadioRequest("RADIOON", "default", "default"));
		else
			EventBus.getDefault().post(new EventRadioRequest("RADIOOFF", "default", "default"));

	}

	//切换开关
	private void OnOffSwitch() {
		String freq_str;
		if (isradio) {
			EventBus.getDefault().post(new EventRadioRequest("RADIOOFF", "default", "default"));
		} else {
			if (mFragmentMedia.isplayingmp3 == true) {
				EventBus.getDefault().post(
						new EventMediaRequest("CLOSEMEDIAMUSIC", "default", "default"));
			}


			EventBus.getDefault().post(new EventRadioRequest("RADIOON", "default", "default"));
			String frenowstr = activity.toDoDB.select_channel_current();
			double frenowfloat = Double.parseDouble(frenowstr);
			iretSet = FmSetFreq((int) (frenowfloat * 100));// FM设置频段
			if (iretSet > 0) {
				freq = (float) iretSet / 100;
				freq_str = String.valueOf(freq);
				updateView(freq_str);
			}
			updataButtonBackground();
			// isradio = true;
//                    bt_radio_on.setBackgroundResource(R.drawable.img_radio_on);
		}
	}

	//将显示的字符串设置为当前频道
	private void setStringToFreq(String str) {
		String freq_str;
		if (!"".equals(str) && !"000".equals(str) && !str.isEmpty()) {
			float floatradio = Float.parseFloat(str);
			floatradio = floatradio * 10;
			intNowChannel = (int) floatradio;
			iretSet = FmSetFreq(intNowChannel * 10);
			if (iretSet > 0) {
				freq = (float) iretSet / 100;
				freq_str = String.valueOf(freq);
				updateView(freq_str);
			}
		}
	}

	// 保存当前频道时进行判断，如果已经保存了当前频道，将弹出窗口提示，是否重复保存
	private void savetvtobutton(String str, int NO) {
		String bt1str = bt_radio_button1.getText().toString();
		String bt2str = bt_radio_button2.getText().toString();
		String bt3str = bt_radio_button3.getText().toString();
		String bt4str = bt_radio_button4.getText().toString();
		String bt5str = bt_radio_button5.getText().toString();
		String bt6str = bt_radio_button6.getText().toString();
		if ((str.equals(bt1str)) || (str.equals(bt2str)) || (str.equals(bt3str)) || (str.equals(bt4str))
				|| (str.equals(bt5str)) || (str.equals(bt6str))) {
			if (isEnable) {
				isEnable = false;
				RadioSavePopwindow("该频道已存储");
			}

		} else {
			switch (NO) {
				case 1:
					bt_radio_button1.setText(str);
					activity.toDoDB.update_setting("channel_one", str, "table_radio");
					break;
				case 2:
					bt_radio_button2.setText(str);
					activity.toDoDB.update_setting("channel_two", str, "table_radio");
					break;
				case 3:
					bt_radio_button3.setText(str);
					activity.toDoDB.update_setting("channel_three", str, "table_radio");
					break;
				case 4:
					bt_radio_button4.setText(str);
					activity.toDoDB.update_setting("channel_four", str, "table_radio");
					break;
				case 5:
					bt_radio_button5.setText(str);
					activity.toDoDB.update_setting("channel_five", str, "table_radio");
					break;
				case 6:
					bt_radio_button6.setText(str);
					activity.toDoDB.update_setting("channel_six", str, "table_radio");
					break;
				default:
					break;
			}
		}

	}

	//更新收音机控件背景
	private void updataButtonBackground() {
		String str = tv_radio_text.getText().toString();
		String bt1str = bt_radio_button1.getText().toString();
		String bt2str = bt_radio_button2.getText().toString();
		String bt3str = bt_radio_button3.getText().toString();
		String bt4str = bt_radio_button4.getText().toString();
		String bt5str = bt_radio_button5.getText().toString();
		String bt6str = bt_radio_button6.getText().toString();
		if (isradio) {
			if (str.equals(bt1str)) {
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel12);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);
			} else if (str.equals(bt2str)) {
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel22);
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);
			} else if (str.equals(bt3str)) {
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel32);
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);
			} else if (str.equals(bt4str)) {
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel42);
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);
			} else if (str.equals(bt5str)) {
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel52);
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);
			} else if (str.equals(bt6str)) {
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel62);
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
			} else {
				bt_radio_button1.setBackgroundResource(R.drawable.bg_radio_channel11);
				bt_radio_button2.setBackgroundResource(R.drawable.bg_radio_channel21);
				bt_radio_button3.setBackgroundResource(R.drawable.bg_radio_channel31);
				bt_radio_button4.setBackgroundResource(R.drawable.bg_radio_channel41);
				bt_radio_button5.setBackgroundResource(R.drawable.bg_radio_channel51);
				bt_radio_button6.setBackgroundResource(R.drawable.bg_radio_channel61);
			}
		}

	}

	//  保存当前频道提示
	public void RadioSavePopwindow(String Content) {
		LayoutInflater media_inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View radio_pop = media_inflater.inflate(R.layout.pop_radio_save, null);
		bt_radio_pop_ok = (Button) radio_pop.findViewById(R.id.bt_radiopop_know);
		TextView tv_pop_content = (TextView) radio_pop.findViewById(R.id.textView1);
		tv_pop_content.setText(Content);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		pop_radio = new PopupWindow(radio_pop, metrics.widthPixels, metrics.heightPixels);
		pop_radio.setFocusable(false);

		ColorDrawable dw = new ColorDrawable(0x66111111);
		pop_radio.setBackgroundDrawable(dw);

		// pop_media.setAnimationStyle(R.style.mypopwindow_anim_style);
		pop_radio.showAtLocation(activity.findViewById(R.id.btn_main_bottom_switchbutton), Gravity.CENTER_HORIZONTAL, 0, 140);
		bt_radio_pop_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pop_radio.dismiss();
				isEnable = true;


			}
		});

	}

	//将显示频道的按钮回归原始状态，避免每次更新出现重复值
	private void resetButtonText() {
		String[] arrayString = {"000", "000", "000", "000", "000", "000"};
		bt_radio_button1.setText(arrayString[0]);
		bt_radio_button2.setText(arrayString[1]);
		bt_radio_button3.setText(arrayString[2]);
		bt_radio_button4.setText(arrayString[3]);
		bt_radio_button5.setText(arrayString[4]);
		bt_radio_button6.setText(arrayString[5]);

	}

	//保存收音机开关状态
	private static void saveRadioSwitch() {
		if (FragmentRadio.isradio) {
			MainActivity.toDoDB.update_setting("rb_radio_fm", "on", "table_radio");

		} else {
			MainActivity.toDoDB.update_setting("rb_radio_fm", "off", "table_radio");
		}
	}

	// 数据频段，设置进度条进度
//	private void SetProgress(float fm) {
//		int progress = (int) ((fm - 88) * 100 / (107 - 88));
////		sb_radio_seekbar.setProgress(progress);
//	}

	//根据进度获取收音机频段
	private float SetFM(int progress) {
		float fm = progress * (107 - 88) / 100 + 88;
		return fm;
	}

	//保存当前按键的各个频段
	private void saveCurrentFres() {
		activity.toDoDB.update_setting("channel_one", bt_radio_button1.getText().toString(), "table_radio");
		activity.toDoDB.update_setting("channel_two", bt_radio_button2.getText().toString(), "table_radio");
		activity.toDoDB.update_setting("channel_three", bt_radio_button3.getText().toString(), "table_radio");
		activity.toDoDB.update_setting("channel_four", bt_radio_button4.getText().toString(), "table_radio");
		activity.toDoDB.update_setting("channel_five", bt_radio_button5.getText().toString(), "table_radio");
		activity.toDoDB.update_setting("channel_six", bt_radio_button6.getText().toString(), "table_radio");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//解除EventBus
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onEvent(EventRadioRequest event) {
		switch (event.getKey()) {
			case "RADIOON"://打开收音机
				isradio = true;
				if ((activity != null) && (activity.nowFragment.equals("Radio"))) {
					bt_radio_switch.setBackgroundResource(R.drawable.bg_radio_switch2);
					updataOffStatus();
				}
				EventBus.getDefault().post(new EventAudioRequest("AUDIO", "audioRadioOn"));
				saveRadioSwitch();
				break;
			case "RADIOOFF"://关闭收音机
				isradio = false;
				if ((activity != null) && (activity.nowFragment.equals("Radio"))) {
					bt_radio_switch.setBackgroundResource(R.drawable.bg_radio_switch1);
					updataOffStatus();
				}
				EventBus.getDefault().post(new EventAudioRequest("AUDIO", "audioRadioOff"));
				saveRadioSwitch();
				break;
			case "RESETFREQ":
				if (!FragmentRadio.isStart) {
					String frenowstr = MainActivity.toDoDB.select_channel_current();
					double frenowfloat = Double.parseDouble(frenowstr);
					FmSetFreq((int) (frenowfloat * 100));// FM设置频段
				}
				break;
			default:
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void onEvent(EventCommand event) {
		String key = "";
		String iatStr = "";
		String freq = "";

		key = event.getKey();
		if (key.equals("RADIOCOMMAND") || key.equals("SUBCOMMAND")) {
			if (mDataConvert == null)
				return;
			//如何识别语句中没有指定收音机页面，判断是否在收音机页面，如果在则处理识别语句
			if (key.equals("SUBCOMMAND")) {
				if ((activity != null) && (!activity.nowFragment.equals("Radio"))) {
					return;
				}
			}
			iatStr = event.getArg1();

			//--------识别的打开收音机的数据
			if (mDataConvert.isContains(openMark, iatStr)) {
				if (!isradio)
					OnOffSwitch();

			}
			//--------识别的关闭收音机的数据
			if (mDataConvert.isContains(closeMark, iatStr)) {
				if (isradio)
					OnOffSwitch();
			}

			//--------识别的切换收音机的频道
			if (mDataConvert.isContains(setFreMark, iatStr)) {
				Float maxValux = (float) 0;
				int index = -1;
				freq = mIatDataConvert.ToFreqFormat(iatStr);
				//识别的数据与保存的频道匹配，播放匹配度最高的
				for (int i = 0; i < freqlist.size(); i++) {
					Float value = mDataConvert.getSimilarityRadio(freq, freqlist.get(i));
					if (value > maxValux) {
						maxValux = value;
						index = i;
					}
				}
				//找出相似度最大频道后进行设置
				if ((maxValux > 0) && (index != -1)) {
					String radio = freqlist.get(index);
					setStringToFreq(radio);
					updataOffStatus();
				}

			}
			//--------识别的更新收音机的数据
			if (mDataConvert.isContains(updataMark, iatStr)) {
				if (isradio && (!FmBusy)) {
					popWarning("搜索进行中......");
					resetButtonText();
					FmBusy = true;
					FmAutoScanStart = true;
					updataOffStatus();
				}
			}
		}
		if ("NOWFRAGMENT".equals(key)) {
			tagIsSave = false;
		}
	}


}
