package com.hejia.tp_launcher_v3.android.tp_launcher_v3;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hejia.eventbus.EventAudioRequest;
import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventCommand;
import com.hejia.eventbus.EventIATRequest;
import com.hejia.eventbus.EventIATResponse;
import com.hejia.eventbus.EventMediaRequest;
import com.hejia.eventbus.EventRadioRequest;
import com.hejia.eventbus.EventSetRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.fragment.FragmentCarlife;
import com.hejia.fragment.FragmentVInfo;
import com.hejia.myinterface.MyOnTouchEvent;
import com.hejia.serialport.DataConvert;
import com.hejia.service.AudioService;
import com.hejia.service.BluetoothService;
import com.hejia.service.SerialPortReadService;
import com.hejia.service.SerialPortWriteService;
import com.hejia.service.SerialService;
import com.hejia.service.SysSetService;
import com.hejia.service.UpdateTimeService;
import com.hejia.bean.User;
import com.hejia.bean.UserSet;
import com.hejia.db.ToDoDB;
import com.hejia.fragment.FragmentBt;
import com.hejia.fragment.FragmentMedia;
import com.hejia.fragment.FragmentMoni;
import com.hejia.fragment.FragmentRadio;
import com.hejia.fragment.FragmentSet;
import com.hejia.fragment.FragmentSetWifi;
import com.hejia.media.MusicService;
import com.hejia.receiver.SpeechReceiver;
import com.hejia.receiver.UITimeReceiver;
import com.hejia.serialport.SendDataAnalysis;
import com.hejia.time.TimerEntity;
import com.hejia.util.IsFastChangeFragmentUtil;
import com.hejia.util.IsFastDoubleClickUtil;
import com.iflytek.cloud.SpeechUtility;
import com.hejia.tp_launcher_v3.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends FragmentActivity {
	// 碎片管理器
	private FragmentManager manager;
	private Fragment mContentFragment;

	private boolean isFirst = false;
	// 设置字体
	private Typeface typeface;

	// 当前界面
	public String nowFragment = "Cloud";// Radio Media BT Moni Navi Set Cloud
	private Bundle hideBundle;
	private Intent hideIntent;
	private Bundle showBundle;
	private Intent showIntent;
	private Intent speechIntent;
	private Bundle speechbundle;

	// 热点状态相关
	private boolean shareIsOpen;
	public boolean shareHavePassword;
	private String shareName;
	private String sharePassword;
	public WifiConfiguration apConfig;
	private WifiManager wifiManager;

	// 音源相关
	private Button btnVoiceAdd;
	private Button btnVoiceSub;
	private ImageView ivVoiceBg;
	private AudioService audioService;
	private int audioVolumeLevel;
	private String isMute;// 静音相关
	private Button btnMute;
	private Button btnCancelMute;
	private String audioStatus;
	private String audioStatusTemp;
	private String audioBt;

	private Intent closeMediaMusicIntent;
	private Intent closeMediaVideoIntent;

	private String tagAudioStatus;// 通过广播传入的音源状态

	private String bt_Music_IsPlaying;

	// log编码相关
	public static int logNumber;

	// CID popwindow
	private PopupWindow cid_window;
	// SSPPIN popwindow
	private PopupWindow ssppin_window;
	// 蓝牙开启可被搜索pop
	private PopupWindow discovered_window;
	public static boolean isshowing_discovered_pop = false;
	private SpeechReceiver mspeechReceive;

	// 控件
	private RadioGroup rGroup;
	// 来电显示界面按钮
	private Button btn_Callin_Answer;
	private Button btn_Callin_Cut;
	private TextView tv_Callin_Number;
	// 蓝牙配对码
	private String ssppin;
	private Button btn_ssppin_cancel;
	private Button btn_ssppin_yes;
	private Button btn_ssppin_no;
	private TextView tv_ssppin_pairing;
	private TextView tv_ssppin_ssppin;
	private TimeCount ssppinTimeCount;

	// 导航栏按键
	private RadioButton m_rb_monitor;
	private RadioButton m_rb_media;
	private RadioButton m_rb_radio;
	private RadioButton m_rb_bluetooth;
	private RadioButton m_rb_setting;
	private RadioButton m_rb_cloud;
	private RadioButton m_rb_carlife;

	private Button m_button_ac_ac;

	// pop出风方式
	private Button m_button_ac_zc;
	private Button m_button_ac_xc;
	private Button m_button_ac_sc;
	private Button bt_wind_grade;

	// 语音识别按钮
	private Button ibtn_main_speak;

	private TextView tvPopAcTemp;
	//	private Button btnPopAC;
	private Button btnPopACQfd;
	private Button btnPopACHfd;
	private Button btnPopACXh;

	public SerialPortReadService serialPortReadBindService;
	public SerialPortWriteService serialPortWriteBindService;
	public MusicService musicBindService;

	private SendDataAnalysis mSendDataAnalysis = null;
	private SerialService mSerialService = null;

	// 声音显示textview
	private TextView tv_voice;
	// private static TextView m_tv_time;// 系统时间
	private TextView tvTime;//系统时间

	// Wifi相关
	private WifiInfo wifiInfo = null; // 获得的Wifi信息
	private int level; // 信号强度值

	// 蓝牙倒计时
	private TimeCount bluetooth_CountDownTime;
	private TextView tv_bluetooth_CountDownTime;
	// 蓝牙电话簿
	private String pbc_Name;
	private String pbc_Number;
	private UserSet userSet;
	public static TreeSet<User> user_TreeSet;
	// 蓝牙通话记录
	public static TreeSet<User> history_TreeSet;
	private String history_name;
	private String history_number;

	public static final String PATH = "/dev/mcudevice";// 1

	public static int fdData = 0;

	// 串口连接内部类
	private SerialPortReceiveServiceConnected serialPortReceiveServiceConn;
	private SerialPortSendServiceConnected serialPortSendServiceConn;

	// 音乐服务连接
	private MusicServiceConnected musicServiceConn;

	// 数据库
	public static ToDoDB toDoDB;

	// 碎片
	private FragmentMoni fm_Moni;
	public FragmentMedia fm_Media;
	private FragmentRadio fm_Radio;
	private FragmentBt fm_Bt;
	private FragmentSet fm_Set;
	private FragmentSetWifi fm_SetWifi;
	private FragmentCarlife fm_Carlife;
	private FragmentVInfo fm_VehicleInfo;


	private DataConvert mDataConvert;

	private boolean isIatReady = true;

	// TODO 各模块的标记
	private int tag = R.id.rb_main_cloud;// 默认启动界面

	private int windGrade = 0;// 风速等级

	// 数据库中空调相关
	private int infor_mode_work = 0;
	private int infor_mode_circul = 0;
	private int infor_mode_air_out = 0;
	private int infor_main_air_speed = 0;
	private int infor_main_temp = 0;
	private int infor_vice_air_speed = 0;
	private int infor_vice_temp = 0;
	private int infor_behind_glass_heat = 0;
	private int infor_save_energy = 0;
	private int infor_air_purification = 0;
	// 系统设置服务
	public static SysSetService sysSetService;
	private SysSetServiceConnected sysSetServiceConn;

	// 蓝牙模块所需内容+蓝牙服务
	// 蓝牙连接内部类
	private BluetoothServiceConnected bluetoothServiceConn;
	private static final String DEV = "/dev/ttymxc2";
	private String device_address;// 远程设备地址
	public static BluetoothService bluetoothBindService;
	// 设备与蓝牙断开连接
	private Button btn_bt_disconn_close;
	private PopupWindow pop_disconnect;
	// 断开连接倒计时
	private TimeCount disconnectTimeCount;

	// 更新时间
	private Intent timeService = null;
	// 更新蓝牙广播频段
	public static String BLUETOOTH_UPDATE_ACTION = "com.hejia.project_two.BLUETOOTH_UPDATE_ACTION";

	// 储存是否为首次安装数据库
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private GoogleApiClient client;

	//语音识别各个界面标志
	private String[] cloudMark;
	private String[] naviMark;
	private String[] moniMark;
	private String[] mediaMark;
	private String[] radioMark;
	private String[] btMark;
	private String[] setMark;
	private String[] carMark;


	// 防止launcher重复启动
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
			Log.i("MainActivity", "onConfigurationChanged---->横屏");
		} else {// 竖屏
			Log.i("MainActivity", "onConfigurationChanged---->竖屏");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 防止launcher重复启动
		if (!isTaskRoot()) {
			Log.i("MainActivity", "onCreate isTaskRoot---->重复启动");
			finish();
			return;
		}
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			Log.i("MainActivity", "onCreate FLAG_ACTIVITY_BROUGHT_TO_FRONT---->重复启动");
			finish();
			return;
		}
		// 防止休眠
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		// 定义全屏参数
		int flag_screen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// 获得当前窗体对象
		Window window = MainActivity.this.getWindow();
		// 设置当前窗体为全屏显示
		window.setFlags(flag_screen, flag_screen);
		setContentView(R.layout.activity_main);
		// 注册EventBus
		EventBus.getDefault().register(this);
		// 系统设置服务
		sysSetServiceConn = new SysSetServiceConnected();
		Intent sysSetConnIntent = new Intent(this, SysSetService.class);
		bindService(sysSetConnIntent, sysSetServiceConn, Context.BIND_AUTO_CREATE);
	}

	private void initMain() {
		// 初始化数据库
		toDoDB = new ToDoDB(this);
		// 初始化即创建语音配置对象，只有初始化后才可以使用MSC的各项服务 注意： appid 必须和下载的SDK保持一致
		SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
		mSendDataAnalysis = new SendDataAnalysis();
		mSerialService = new SerialService();
		mDataConvert = new DataConvert();
		// 注册广播
		register_Broadcast();
		// 初始化串口连接接收内部类
		serialPortReceiveServiceConn = new SerialPortReceiveServiceConnected();
		Intent serialPortReceiveConnIntent = new Intent(this, SerialPortReadService.class);
		bindService(serialPortReceiveConnIntent, serialPortReceiveServiceConn, Context.BIND_AUTO_CREATE);
		// 初始化串口连接发送内部类
		serialPortSendServiceConn = new SerialPortSendServiceConnected();
		Intent serialPortWriteIntent = new Intent(this, SerialPortWriteService.class);
		bindService(serialPortWriteIntent, serialPortSendServiceConn, Context.BIND_AUTO_CREATE);
		// 初始化音乐连接内部类
		musicServiceConn = new MusicServiceConnected();
		Intent musicServiceConnIntent = new Intent(this, MusicService.class);
		bindService(musicServiceConnIntent, musicServiceConn, Context.BIND_AUTO_CREATE);
		// 初始化蓝牙连接内部类
		bluetoothServiceConn = new BluetoothServiceConnected();
		Intent bluetoothConnIntent = new Intent(this, BluetoothService.class);
		bindService(bluetoothConnIntent, bluetoothServiceConn, Context.BIND_AUTO_CREATE);
		// 初始化蓝牙倒计时
		bluetooth_CountDownTime = new TimeCount(180000, 1000, 0);
		disconnectTimeCount = new TimeCount(5000, 1000, 1);
		ssppinTimeCount = new TimeCount(10000, 1000, 2);
		// 初始化蓝牙电话簿集合
		userSet = new UserSet();
		user_TreeSet = new TreeSet<User>(userSet);
		// 初始化蓝牙通话记录集合
		history_TreeSet = new TreeSet<User>(userSet);
		// 开始更新时间服务
		timeService = new Intent(this, UpdateTimeService.class);
		startService(timeService);
		// 判断是否为首次启动该程序
		preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
		if (preferences.getBoolean("firststart", true)) {
			toDoDB.initDB();
			editor = preferences.edit();
			// 将登录标志位设置为false，下次登录时不在显示首次登录界面
			editor.putBoolean("firststart", false);
			editor.commit();
			isFirst = true;
		}
		// 热点相关
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		shareIsOpen = toDoDB.getShareIsOpen();
		shareHavePassword = toDoDB.getShareHavePassword();
		shareName = toDoDB.getShareName();
		sharePassword = toDoDB.getSharePassword();
		if (shareIsOpen) {
			// 开启热点
			// 开启热点密码
			setWifiApEnabled(false, null, null);
			setWifiApEnabled(shareIsOpen, shareName, sharePassword);
		}
		// 初始化语音相关
		speechIntent = new Intent();
		speechbundle = new Bundle();
		// 音源相关初始化
		audioService = new AudioService();
		closeMediaMusicIntent = new Intent();
		closeMediaVideoIntent = new Intent();
		// 界面控件隐藏初始化
		hideBundle = new Bundle();
		hideIntent = new Intent();
		showBundle = new Bundle();
		showIntent = new Intent();
		// 获得静音状态
		isMute = toDoDB.getSetDataProperty("set_mute_switch");
		// 获得当前音量
		audioVolumeLevel = Integer.parseInt(toDoDB.getSetDataProperty("set_vol"));
		// 获得当前log编码
		logNumber = Integer.parseInt(toDoDB.getSetDataProperty("set_log"));
		// 获得当前音源状态
		audioStatus = toDoDB.getSetDataProperty("set_audio");
		// 获得当前缓存音源
		audioStatusTemp = toDoDB.getSetDataProperty("set_audio_temp");
		// 获得蓝牙电话音源状态
		audioBt = toDoDB.getSetDataProperty("set_audiobt");
		if ("no".equals(audioBt)) {
			switch (audioStatus) {
				case "media":
					audioService.setSource(2);
					// 更新数据库
					toDoDB.update_setting("set_audio", "media", "table_set");
					toDoDB.update_setting("set_audio_temp", "media", "table_set");
					break;
				case "radio":
					audioService.setSource(0);
					// 更新数据库
					toDoDB.update_setting("set_audio", "radio", "table_set");
					toDoDB.update_setting("set_audio_temp", "radio", "table_set");
					break;
				case "btmusic":
					audioService.setSource(1);
					// 更新数据库
					toDoDB.update_setting("set_audio", "btmusic", "table_set");
					break;
				default:
					audioService.setSource(2);
					// 更新数据库
					toDoDB.update_setting("set_audio", "media", "table_set");
					break;
			}
		} else if ("yes".equals(audioBt)) {
			audioService.setSource(1);
			// 更新数据库
			toDoDB.update_setting("set_audiobt", "yes", "table_set");
		}
		manager = getSupportFragmentManager();
		initView();
		setMarkValue();//为语音识别各个界面标志赋值
		initNowFragment();
		rbtnUnable();
		tv_voice.setText(Integer.toString(audioVolumeLevel));
		// 判断是否处于静音状态
		if ("off".equals(isMute)) {
			// 音量设为数据库中
			audioService.setVolume(audioVolumeLevel);
			voiceBtnAble();
			// TODO 加减音量设为可用
		} else if ("on".equals(isMute)) {
			audioService.setVolume(0);
			// TODO 加减音量设为不可用
			voiceBtnUnable();
		}
		initListener();
		// 对单选按钮进行监听
		rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (IsFastChangeFragmentUtil.isFastChangeFragment()) {

				} else {

					switch (checkedId) {
						// 视频监控
						case R.id.rb_main_moni:
							OpenMoniFragment();
							break;
						// 手机互联
						case R.id.rb_main_carlife:
							OpenCarlifeFragment();
							break;
						// 影音播放
						case R.id.rb_main_media:
							OpenMediaFragment();
							break;
						// 收音机
						case R.id.rb_main_radio:
							OpenRadioFragment();
							break;
						// 蓝牙
						case R.id.rb_main_bt:
							OpenBTFragment();
							break;
						// 系统设置
						case R.id.rb_main_set:
							OpenSetFragment();
							break;
						// 云诊断
						case R.id.rb_main_cloud:
							OpenCloudFragment();
							break;
						default:
							break;
					}
				}
			}
		});
		setSystemTime();
		//收音机开机初始化设置频道
		EventBus.getDefault().post(new EventRadioRequest("RESETFREQ", "default", "default"));
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
		EventBus.getDefault().postSticky(new EventIATRequest("IATINIT", "default", MainActivity.this));
		client.connect();
		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	//切换云诊断界面
	private void OpenCloudFragment() {
		fm_Media.isNowPaper = false;
		fm_Radio.invisiblePop();
		sendCloseMediaVideo();
		changeMcontentFragmentMain(fm_VehicleInfo);
		m_rb_cloud.setChecked(true);
		nowFragment = "Cloud";
		hideOrShow();
		sendNowFragment();
		toDoDB.setNowFragment("cloud");
		tag = R.id.rb_main_cloud;
	}

	//切换监控界面
	private void OpenMoniFragment() {
		fm_Media.isNowPaper = false;
		// TODO 添加关闭多媒体视频方法
		fm_Radio.invisiblePop();
		sendCloseMediaVideo();
		changeMcontentFragmentMain(fm_Moni);
		nowFragment = "Moni";
		hideOrShow();
		sendNowFragment();
		toDoDB.setNowFragment("moni");
		m_rb_monitor.setChecked(true);
		tag = R.id.rb_main_moni;
	}

	// 切换手机互联界面
	private void OpenCarlifeFragment() {
		fm_Media.isNowPaper = false;
		fm_Radio.invisiblePop();
		sendCloseMediaVideo();
		changeMcontentFragmentMain(fm_Carlife);
		m_rb_carlife.setChecked(true);
		nowFragment = "Carlife";
		hideOrShow();
		sendNowFragment();
		toDoDB.setNowFragment("carlife");
		tag = R.id.rb_main_carlife;
	}

	//切换影音界面
	private void OpenMediaFragment() {
		fm_Media.isStarted = true;
		fm_Media.isNowPaper = true;
		fm_Radio.invisiblePop();
		changeMcontentFragmentMain(fm_Media);
		m_rb_media.setChecked(true);
		nowFragment = "Media";
		hideOrShow();
		sendNowFragment();
		toDoDB.setNowFragment("media");
		tag = R.id.rb_main_media;
	}

	//切换收音机界面
	private void OpenRadioFragment() {
		fm_Media.isNowPaper = false;
		fm_Radio.isFirstStart = true;
		// TODO 添加关闭多媒体视频方法
		sendCloseMediaVideo();
		changeMcontentFragmentMain(fm_Radio);
		m_rb_radio.setChecked(true);
		nowFragment = "Radio";
		hideOrShow();
		sendNowFragment();
		tag = R.id.rb_main_radio;
		toDoDB.setNowFragment("radio");
		// 未启动过该界面，将在启动时初始化；之后切换各个界面将执行该初始化
		if (FragmentRadio.isStart) {
			initFragmentRadio();
		}
	}

	//切换蓝牙界面
	private void OpenBTFragment() {
		fm_Media.isNowPaper = false;
		fm_Radio.invisiblePop();
		sendCloseMediaVideo();
		changeMcontentFragmentMain(fm_Bt);
		m_rb_bluetooth.setChecked(true);
		nowFragment = "BT";
		hideOrShow();
		sendNowFragment();
		toDoDB.setNowFragment("bt");
		tag = R.id.rb_main_bt;
	}

	//切换设置界面
	private void OpenSetFragment() {
		fm_Media.isNowPaper = false;
		fm_Radio.invisiblePop();
		sendCloseMediaVideo();
		changeMcontentFragmentMain(fm_Set);
		m_rb_setting.setChecked(true);
		nowFragment = "Set";
		hideOrShow();
		sendNowFragment();
		toDoDB.setNowFragment("set");
		tag = R.id.rb_main_set;
	}


	// 动态注册广播
	private void register_Broadcast() {
		// 注册语音拼写与语音合成广播
		mspeechReceive = new SpeechReceiver(MainActivity.this);
		IntentFilter speechFilter = new IntentFilter("SPEECH_TTS");
		registerReceiver(mspeechReceive, speechFilter);
	}

	// 初始化监听事件
	private void initListener() {
	}

	private String dbNowFragment;// 数据库中当前界面

	// 初始化当前界面
	private void initNowFragment() {
		initFragmentMedia();
		dbNowFragment = toDoDB.getNowFragment();
		switch (dbNowFragment) {
			case "cloud":
				mContentFragment = fm_VehicleInfo;
				manager.beginTransaction().replace(R.id.fragment_main, fm_VehicleInfo).commit();
				m_rb_cloud.setChecked(true);
				nowFragment = "Cloud";
				break;
			case "moni":
				mContentFragment = fm_Moni;
				manager.beginTransaction().replace(R.id.fragment_main, fm_Moni).commit();
				m_rb_monitor.setChecked(true);
				nowFragment = "Moni";
				break;
			case "carlife":
				mContentFragment = fm_Radio;
				manager.beginTransaction().replace(R.id.fragment_main, fm_Radio).commit();
				m_rb_radio.setChecked(true);
				nowFragment = "Radio";
				break;
			case "media":
				fm_Media.isStarted = true;
				fm_Media.isNowPaper = true;
				mContentFragment = fm_Media;
				manager.beginTransaction().replace(R.id.fragment_main, fm_Media).commit();
				m_rb_media.setChecked(true);
				nowFragment = "Media";
				break;
			case "radio":
				mContentFragment = fm_Radio;
				manager.beginTransaction().replace(R.id.fragment_main, fm_Radio).commit();
				m_rb_radio.setChecked(true);
				nowFragment = "Radio";
				break;
			case "bt":
				mContentFragment = fm_Radio;
				manager.beginTransaction().replace(R.id.fragment_main, fm_Radio).commit();
				m_rb_radio.setChecked(true);
				nowFragment = "Radio";
				break;
			case "set":
				mContentFragment = fm_Radio;
				manager.beginTransaction().replace(R.id.fragment_main, fm_Radio).commit();
				m_rb_radio.setChecked(true);
				nowFragment = "Radio";
				break;
		}
	}

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("Main Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.connect();
//		AppIndex.AppIndexApi.start(client, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client, getIndexApiAction());
		client.disconnect();
	}


	public class ChangeTimeConn implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("chanyeol", "时间服务已连通");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	}

	// TODO 设置系统时间
	public void setSystemTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2017);
		c.set(Calendar.MONTH, 5);
		c.set(Calendar.DAY_OF_MONTH, 20);
		c.set(Calendar.HOUR_OF_DAY, 10);
		c.set(Calendar.MINUTE, 30);
		// c.set(Calendar.SECOND, entity.second);
		long time = c.getTimeInMillis();
		SystemClock.setCurrentTimeMillis(time);
	}

	// 设置网络时间
	private void setText(TimerEntity entity) {
		// String netTime = formatText(entity.year) + "-" +
		// formatText(entity.month) + "-" + formatText(entity.day) + " "
		// + formatText(entity.hour) + ":" + formatText(entity.minute) + ":" +
		// formatText(entity.second);
	}

	// 排版
	private String formatText(int num) {
		String txt;
		if (num < 10) {
			txt = "0" + num;
		} else {
			txt = String.valueOf(num);
		}
		return txt;
	}

	// private class NetTimeRunnable implements Runnable {
	//
	// @Override
	// public void run() {
	// URL url = null;
	// // if (isNetAvailable(MainActivity.this)) {
	// try {
	// url = new URL("http://www.baidu.com");
	// URLConnection uc = url.openConnection();
	// uc.connect();
	// long ld = uc.getDate();
	// Date date = new Date(ld);
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// entity = TimerEntity.instance(calendar);
	// mIntent.putExtra("time", entity);
	// Thread.sleep(3000);
	// changeTimeService.startGetTime(mIntent);
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// // }
	// }

	// 判断网络连接是否可用
	public static boolean isNetAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isAvailable());
	}

	// 更新蓝牙界面视图
	public void updateView(String data) {
		// myTextView = (TextView) findViewById(R.id.tv);
		Log.d("---BLUETOOTH---", "updateView()");
		Toast.makeText(this, data, Toast.LENGTH_LONG).show();
		// mText_Editor.setText(data);
	}

	// 碎片替换帮助函数
	private void changeMcontentFragmentMain(Fragment to) {
		rbtnUnable();
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = manager.beginTransaction();
			if (!to.isAdded()) {
				// 初始界面
				fm_SetWifi.hideKeyboard();
				transaction.hide(mContentFragment).add(R.id.fragment_main, to).commitAllowingStateLoss();
			} else {
				fm_SetWifi.hideKeyboard();
				transaction.hide(mContentFragment).show(to).commitAllowingStateLoss();
			}
			mContentFragment = to;
		}
	}

	// RadioButton失效200ms
	private void rbtnUnable() {
		m_rb_cloud.setEnabled(false);
		m_rb_monitor.setEnabled(false);
		m_rb_media.setEnabled(false);
		m_rb_radio.setEnabled(false);
		m_rb_bluetooth.setEnabled(false);
		m_rb_setting.setEnabled(false);
		m_rb_carlife.setEnabled(false);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m_rb_cloud.setEnabled(true);
		m_rb_monitor.setEnabled(true);
		m_rb_media.setEnabled(true);
		m_rb_radio.setEnabled(true);
		m_rb_bluetooth.setEnabled(true);
		m_rb_setting.setEnabled(true);
		m_rb_carlife.setEnabled(true);
	}

	private void initView() {
		// 设置字体
		typeface = Typeface.createFromAsset(getAssets(), "fonts/Let's go Digital Regular.ttf");
		rGroup = (RadioGroup) findViewById(R.id.rg_main);
		// 碎片的实例化
		fm_Moni = new FragmentMoni();
		fm_Carlife = new FragmentCarlife();
		fm_Media = FragmentMedia.getInstance();
		fm_Radio = new FragmentRadio();
		fm_Bt = new FragmentBt();
		fm_Set = new FragmentSet();
		fm_VehicleInfo = new FragmentVInfo();
		fm_SetWifi = new FragmentSetWifi();

		// 单选按钮的实例化
		m_rb_monitor = (RadioButton) findViewById(R.id.rb_main_moni);
		m_rb_media = (RadioButton) findViewById(R.id.rb_main_media);
		m_rb_radio = (RadioButton) findViewById(R.id.rb_main_radio);
		m_rb_bluetooth = (RadioButton) findViewById(R.id.rb_main_bt);
		m_rb_setting = (RadioButton) findViewById(R.id.rb_main_set);
		m_rb_cloud = (RadioButton) findViewById(R.id.rb_main_cloud);
		m_rb_carlife = (RadioButton) findViewById(R.id.rb_main_carlife);
		tv_voice = (TextView) findViewById(R.id.tv_main_bottom_voice);
		tv_voice.setTypeface(typeface);
		tvTime = (TextView) findViewById(R.id.tv_main_bottom_time);
		tvTime.setTypeface(typeface);
		// 语音识别按钮
		//ibtn_main_speak = (Button) findViewById(R.id.btn_main_speak);

		// 音量控制
		btnVoiceAdd = (Button) findViewById(R.id.btn_main_bottom_voiceadd);
		btnVoiceSub = (Button) findViewById(R.id.btn_main_bottom_voicesub);
		ivVoiceBg = (ImageView) findViewById(R.id.iv_main_bottom_voice);
		// 静音相关
		btnMute = (Button) findViewById(R.id.btn_main_bottom_mute);
		btnCancelMute = (Button) findViewById(R.id.btn_main_bottom_cancelmute);
		// EventBus初始化各个界面
		EventBus.getDefault().postSticky(new EventSystem("INIT"));
	}

	// 判断当前的碎片标记，完成不同的响应
	private void judge_Fragment_Tag() {
		switch (tag) {
			case R.id.rb_main_moni:
				m_rb_monitor.setChecked(true);
				break;
			case R.id.rb_main_carlife:
				m_rb_carlife.setChecked(true);
				break;
			case R.id.rb_main_media:
				m_rb_media.setChecked(true);
				break;
			case R.id.rb_main_radio:
				m_rb_radio.setChecked(true);
				break;
			case R.id.rb_main_bt:
				m_rb_bluetooth.setChecked(true);
				break;
			case R.id.rb_main_set:
				m_rb_setting.setChecked(true);
				break;
			case R.id.rb_main_cloud:
				m_rb_cloud.setChecked(true);
				break;
		}
	}

	// 熄屏事件
	public void extinguish(View view) {
		sendTtsBroadcast("hello, good morning!", 1);
	}


	// CID接听电话
	public void CID_Answer(View view) {
		bluetoothBindService.send_Command("AT+B HFANSW\r\n");
		cid_window.dismiss();

	}

	// CID挂断电话
	public void CID_Hangup(View view) {
		bluetoothBindService.send_Command("AT+B HFCHUP\r\n");
		cid_window.dismiss();
	}

	// 定义一个实现自ServiceConnection的类
	public class BluetoothServiceConnected implements ServiceConnection {
		// 服务连通时回调方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("chanyeol", "onServiceConnected---->客户端连通了服务");
			// 获取服务端传来的Ibinder对象
			BluetoothService.MyBluetoothBinder binder = (BluetoothService.MyBluetoothBinder) service;
			// 调用MyBinder中公共的方法
			bluetoothBindService = binder.getBluetoothService();
			// 开启蓝牙
			bluetoothBindService.openBluetooth();
			if (isFirst) {
				bluetoothBindService.deleteAllPairDevice();
				bluetoothBindService.updateBTName("FAW-J7");
			}

		}

		// 服务没有连通时回调方法，但是通常表示意外中断连通的时候才会调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("chanyeol", "onServiceConnected---->客户端没有连通服务");
		}

	}

	// 定义一个实现自ServiceConnection的类
	public class SysSetServiceConnected implements ServiceConnection {
		// 服务连通时回调方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("chanyeol", "onServiceConnected---->客户端连通了系统设置服务");
			// 获取服务端传来的Ibinder对象
			SysSetService.SysSetBinder binder = (SysSetService.SysSetBinder) service;
			// 调用MyBinder中公共的方法
			sysSetService = binder.getSysSetService();
			// 在服务中开启初始化主界面方法
			sysSetService.initMain();
		}

		// 服务没有连通时回调方法，但是通常表示意外中断连通的时候才会调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("chanyeol", "onServiceConnected---->客户端没有连通系统设置服务");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
		// System.exit(0);// 直接结束程序
		// // 关闭蓝牙
		// bluetoothBindService.closeBluetooth();
		// // 解绑蓝牙服务
		// unbindService(bluetoothServiceConn);
		// 解绑定位服务
//		unbindService(mapServiceConn);
		// // 取消广播注册

		// unregisterReceiver(timereceiver);
		// unregisterReceiver(m_SSPPINReceiver);

		// unregisterReceiver(audioBroadcastReceiver);


		// 解绑串口服务
		// unbindService(serialPortReceiveServiceConn);
		// unbindService(serialPortSendServiceConn);

	}

	public void voiceAdd(View view) {
		if (audioVolumeLevel < 9) {
			audioVolumeLevel++;
			audioService.setVolume(audioVolumeLevel);
			// 存入数据库
			toDoDB.update_setting("set_vol", Integer.toString(audioVolumeLevel), "table_set");
			tv_voice.setText(Integer.toString(audioVolumeLevel));
		}
		sendNowFragment();
	}

	public void voiceMute(View view) {
		// audioVolumeLevel = 0;
		audioService.setVolume(0);
		// 存入数据库
		toDoDB.update_setting("set_mute_switch", "on", "table_set");
		voiceBtnUnable();
		sendNowFragment();
	}

	public void voiceCancelMute(View view) {
		audioService.setVolume(audioVolumeLevel);
		// 存入数据库
		toDoDB.update_setting("set_mute_switch", "off", "table_set");
		voiceBtnAble();
		sendNowFragment();
	}

	private void voiceBtnAble() {
		// 点击取消静音按键后 使音量按键有效 隐藏取消静音 恢复静音
		btnCancelMute.setVisibility(View.GONE);
		btnMute.setVisibility(View.VISIBLE);
		ivVoiceBg.setImageResource(R.drawable.bg_main_voice);
		btnVoiceAdd.setClickable(true);
		btnVoiceSub.setClickable(true);
		// 音量相关
		btnVoiceAdd.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ivVoiceBg.setImageResource(R.drawable.bg_main_voiceadd);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					ivVoiceBg.setImageResource(R.drawable.bg_main_voice);
				}
				return false;
			}
		});
		btnVoiceSub.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					ivVoiceBg.setImageResource(R.drawable.bg_main_voicesub);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					ivVoiceBg.setImageResource(R.drawable.bg_main_voice);
				}
				return false;
			}
		});
		tv_voice.setTextColor(Color.WHITE);
	}

	@SuppressWarnings("deprecation")
	private void voiceBtnUnable() {
		// 点击取消静音按键后 使音量按键失效 隐藏静音 显示取消静音
		btnCancelMute.setVisibility(View.VISIBLE);
		btnMute.setVisibility(View.GONE);
		ivVoiceBg.setImageResource(R.drawable.bg_main_voicemute);
		btnVoiceAdd.setClickable(false);
		btnVoiceSub.setClickable(false);
		// 音量相关
		btnVoiceAdd.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//ivVoiceBg.setImageResource(R.drawable.bg_main_voicemute);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					//ivVoiceBg.setImageResource(R.drawable.bg_main_voice);
				}
				return false;
			}
		});
		btnVoiceSub.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//ivVoiceBg.setImageResource(R.drawable.bg_main_voicemute);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					//ivVoiceBg.setImageResource(R.drawable.bg_main_voice);
				}
				return false;
			}
		});
		tv_voice.setTextColor(Color.GRAY);

	}

	public void voiceSub(View view) {
		if (audioVolumeLevel > 0) {
			audioVolumeLevel--;
			audioService.setVolume(audioVolumeLevel);
			// 存入数据库
			toDoDB.update_setting("set_vol", Integer.toString(audioVolumeLevel), "table_set");
			tv_voice.setText(Integer.toString(audioVolumeLevel));
		}
		sendNowFragment();
	}

	// 来电显示popwindow
	public void popCID(String number) {
		// 利用layoutInflater获得View
		LayoutInflater cid_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View m_view_cid = cid_inflater.inflate(R.layout.pop_bt_callin, null);

		// 内部按钮实例化
		tv_Callin_Number = (TextView) m_view_cid.findViewById(R.id.tv_bt_callin_number);
		btn_Callin_Answer = (Button) m_view_cid.findViewById(R.id.btn_bt_callin_answer);
		btn_Callin_Cut = (Button) m_view_cid.findViewById(R.id.btn_bt_callin_cut);
		// 设置显示电话号码
		tv_Callin_Number.setText(number);
		btn_Callin_Answer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 电话接听事件
				bluetoothBindService.send_Command("AT+B HFANSW\r\n");
			}
		});
		btn_Callin_Cut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bluetoothBindService.send_Command("AT+B HFCHUP\r\n");
			}
		});
		// 设置窗口的宽高
		cid_window = new PopupWindow(m_view_cid, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		cid_window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明,确保点击除pop外的部分pop可消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		cid_window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		cid_window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		cid_window.showAtLocation(MainActivity.this.findViewById(R.id.btn_main_bottom_switchbutton), Gravity.CENTER_HORIZONTAL, 0, 0);
		// popWindow消失监听方法
		cid_window.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				// 延时复原tag 以防退出时再次启动popwindow
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// bluetoothBindService.tag_CID = 1;
					}
				}, 2000);
			}
		});

	}

	//	 有设备连接pop SSPPIN
	public void popSSPPIN(final String ssppin) {
		// 利用layoutInflater获得View
		LayoutInflater ssppin_inflater = (LayoutInflater)
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View m_view_ssppin = ssppin_inflater.inflate(R.layout.pop_bt_pair,
				null);
		ssppinTimeCount.start();
		// 内部按钮实例化
		btn_ssppin_cancel = (Button)
				m_view_ssppin.findViewById(R.id.btn_bt_ssppin_cancel);
		btn_ssppin_no = (Button)
				m_view_ssppin.findViewById(R.id.btn_bt_ssppin_no);
		btn_ssppin_yes = (Button)
				m_view_ssppin.findViewById(R.id.btn_bt_ssppin_yes);
		tv_ssppin_pairing = (TextView)
				m_view_ssppin.findViewById(R.id.tv_bt_ssppin_pairing);
		tv_ssppin_ssppin = (TextView)
				m_view_ssppin.findViewById(R.id.tv_bt_ssppin);

		// 设置显配对号码
		tv_ssppin_ssppin.setText(ssppin);
		btn_ssppin_yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				bluetoothBindService.acceptOrReject(1);
				ssppin_window.dismiss();
			}

		});
		btn_ssppin_no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				bluetoothBindService.acceptOrReject(0);
				ssppin_window.dismiss();
			}
		});
		// 设置窗口的宽高
		ssppin_window = new PopupWindow(m_view_ssppin,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		ssppin_window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明,确保点击除pop外的部分pop可消失
		ColorDrawable dw = new ColorDrawable(0x00000000);
		ssppin_window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		ssppin_window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		ssppin_window.showAtLocation(MainActivity.this.findViewById(R.id.btn_main_bottom_switchbutton),
				Gravity.CENTER_HORIZONTAL, 0, 0);
		// popWindow消失监听方法
		ssppin_window.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

	}

	// 蓝牙开启可被发现时间popwindow
	public void popDiscovered() {
		// 利用layoutInflater获得View
		LayoutInflater discovered_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View m_view_discovered = discovered_inflater.inflate(R.layout.pop_bt_open, null);

		// 内部按钮实例化
		tv_bluetooth_CountDownTime = (TextView) m_view_discovered.findViewById(R.id.tv_popbtopen_time);
		// 开始倒计时
		bluetooth_CountDownTime.start();

		// 设置窗口的宽高
		discovered_window = new PopupWindow(m_view_discovered, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		discovered_window.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明,确保点击除pop外的部分pop可消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		discovered_window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		discovered_window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		discovered_window.showAtLocation(MainActivity.this.findViewById(R.id.btn_main_bottom_switchbutton), Gravity.CENTER_HORIZONTAL, 0,
				0);
		// popWindow消失监听方法
		discovered_window.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				// 设置蓝牙为可连接但不可见
				bluetoothBindService.send_Command("AT+B SCAN 2\r\n");
			}
		});

	}

	// 设备与蓝牙断开连接popwindow
	public void popDisconnect() {
		// 利用layoutInflater获得View
		LayoutInflater disconnect_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View m_view_disconnect = disconnect_inflater.inflate(R.layout.pop_bt_disconn, null);

		// 内部按钮实例化
		btn_bt_disconn_close = (Button) m_view_disconnect.findViewById(R.id.btn_popbtdisconn_close);
		// 开始倒计时
		disconnectTimeCount.start();
		// 设置窗口的宽高
		pop_disconnect = new PopupWindow(m_view_disconnect, 450, 300);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		pop_disconnect.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明,确保点击除pop外的部分pop可消失
		ColorDrawable dw = new ColorDrawable(0x00000000);
		pop_disconnect.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		pop_disconnect.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		pop_disconnect.showAtLocation(MainActivity.this.findViewById(R.id.btn_main_bottom_switchbutton), Gravity.CENTER_HORIZONTAL, 0, 0);
		// popWindow消失监听方法
		pop_disconnect.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				disconnectTimeCount.cancel();
			}
		});

	}

	// 蓝牙可被搜索界面取消事件
	public void newpair_Cancel(View view) {
		discovered_window.dismiss();
	}

	// 设备与蓝牙断开连接pop关闭按键点击事件
	public void close_DisconnectPop(View view) {
		pop_disconnect.dismiss();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void btEvent(EventBTRequest event) {
		switch (event.getKey()) {
			case "BTCONN":
				//蓝牙已连接
				// 如蓝牙可被搜索pop开启时有设备连接 则关闭蓝牙可被搜索pop
				if (isshowing_discovered_pop) {
					discovered_window.dismiss();
					isshowing_discovered_pop = false;
				}
				break;
			case "BTDISCONN":
				//蓝牙断开连接
				// 切回音源
				audioStatusTemp = toDoDB.getSetDataProperty("set_audio_temp");
				switch (audioStatusTemp) {
					case "radio":
						// 切换音源
						audioService.setSource(0);
						// 添加开启收音机方法
						EventBus.getDefault().post(new EventRadioRequest("RADIOON", "default", "default"));

						// 更新数据库
						toDoDB.update_setting("set_audio", "radio", "table_set");
						toDoDB.update_setting("set_audio_temp", "radio", "table_set");
						break;
					case "media":
						// 切换音源
						audioService.setSource(2);
						// 更新数据库
						toDoDB.update_setting("set_audio", "media", "table_set");
						toDoDB.update_setting("set_audio_temp", "media", "table_set");
						break;
					default:
						break;
				}
				user_TreeSet.clear();
				history_TreeSet.clear();
				// 设置蓝牙为可连接但不可见
				bluetoothBindService.send_Command("AT+B SCAN 2\r\n");
				if ("pop".equals(event.getArg1())) {
					popDisconnect();
				}
				break;
			case "SSPPIN":
				ssppin = event.getArg1();
				if (ssppin != null) {
					popSSPPIN(ssppin);
				}
				break;
			case "PB":
				//接收电话簿信息
				pbc_Name = event.getArg1();
				pbc_Number = (String) event.getArg2();
				user_TreeSet.add(new User(pbc_Name, pbc_Number));
				break;
			case "HISTORY":
				//通话记录
				history_name = event.getArg1();
				history_number = (String) event.getArg2();
				history_TreeSet.add(new User(history_name, history_number));
				break;
			case "BTMUSICSTATUS":
				//蓝牙音乐状态
			{
				bt_Music_IsPlaying = event.getArg1();
				if ("1".equals(bt_Music_IsPlaying)) {
					changeAudio("audioBtMusic");
				} else if ("0".equals(bt_Music_IsPlaying) || "2".equals(bt_Music_IsPlaying)) {
					// 切回音源
					audioStatusTemp = toDoDB.getSetDataProperty("set_audio_temp");
					switch (audioStatusTemp) {
						case "radio":
							// 切换音源
							changeAudio("audioRadioOn");
							break;
						case "media":
							// 切换音源
							changeAudio("audioMediaOn");
							break;
						default:
							break;
					}
				}

			}
			break;
			case "HFCCIN":
				//获得主动拨号
				changeAudio("audioBtDialCall");
				changeNaviBTBg(true);
				break;
			case "CID":
				//获得呼入号码
				popCID(event.getArg1());
				changeNaviBTBg(true);
				break;
			case "CIDCUT":
				//关闭呼入电话pop
				if (bluetoothBindService.tag_CID == 0) {
					cid_window.dismiss();
					// 复原tag
					bluetoothBindService.tag_CID = 1;
				}
				break;
			case "HFCCINCUT":
				//电话挂断 切回音源
				changeAudio("audioBtDialCut");
				changeNaviBTBg(false);
				break;

		}
	}

	private void changeNaviBTBg(boolean isCall) {
		// true为通话中 false为非通话中
		if (isCall) {
			m_rb_bluetooth.setBackgroundResource(R.drawable.img_main_bt3);
		} else {
			m_rb_bluetooth.setBackgroundResource(R.drawable.img_main_bt);
		}

	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void eventAudio(EventAudioRequest event) {
		if ("AUDIO".equals(event.getKey())) {
			// 音源转换EventBus
			changeAudio(event.getValues());
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void eventSet(EventSetRequest event) {
		switch (event.getKey()) {
			case "UPDATE":
				//更新时间相关
				if (event.getArg1() != null) {
					tvTime.setText(event.getArg1());
				}
				break;
			default:
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void eventInitMain(EventSystem event) {
		switch (event.getKey()) {
			case "INITMAIN":
				initMain();
				break;
		}
	}

	// 转换音源公共方法
	private void changeAudio(String tagAudioStatus) {
		// 获得当前音源状态
		audioStatus = toDoDB.getSetDataProperty("set_audio");
		// 获得蓝牙电话音源状态
		audioBt = toDoDB.getSetDataProperty("set_audiobt");

		if ("no".equals(audioBt)) {
			switch (tagAudioStatus) {
				case "audioBtDialCall":
					// 切换音源
					audioService.setSource(1);
					// 更新数据库
					toDoDB.update_setting("set_audiobt", "yes", "table_set");
					break;
				case "audioMediaOn":
					if ("1".equals(bluetoothBindService.bt_service_music_isplay)) {
						bluetoothBindService.send_Command("AT+B AVRCPPAUSE\r\n");
					}
					// 切换音源
					audioService.setSource(2);
					// 获得当前音源状态
					audioStatus = toDoDB.getSetDataProperty("set_audio");
					// 更新数据库
					toDoDB.update_setting("set_audio", "media", "table_set");
					toDoDB.update_setting("set_audio_temp", "media", "table_set");
					break;
				case "audioRadioOff":
					switch (audioStatus) {
						case "media":
							// 切换音源
							audioService.setSource(2);
							break;
						case "btmusic":
							// 切换音源
							audioService.setSource(1);
							break;
						default:
							// 切换音源
							audioService.setSource(2);
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
					}
					break;
				case "audioRadioOn":
					// if ("1".equals(bt_Music_IsPlaying)) {
					if ("1".equals(bluetoothBindService.bt_service_music_isplay)) {
						bluetoothBindService.send_Command("AT+B AVRCPPAUSE\r\n");
					}
					// 更新数据库
					toDoDB.update_setting("set_audio", "radio", "table_set");
					toDoDB.update_setting("set_audio_temp", "radio", "table_set");
					// 切换音源
					audioService.setSource(0);
					break;
				case "audioBtMusic":
					// TODO 添加关闭收音机方法 关闭多媒体方法
					EventBus.getDefault().post(new EventRadioRequest("RADIOOFF", "default", "default"));
					sendCloseMediaMusic();
					// 切换音源
					audioService.setSource(1);
					// 更新数据库
					toDoDB.update_setting("set_audio", "btmusic", "table_set");
					break;
				case "audioBtDialCut":

					// 重新获得音源状态
					audioStatus = toDoDB.getSetDataProperty("set_audio");
					switch (audioStatus) {
						case "media":
							// 切换音源
							audioService.setSource(2);
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
						case "radio":
							// 切换音源
							audioService.setSource(0);
							// 更新数据库
							toDoDB.update_setting("set_audio", "radio", "table_set");
							toDoDB.update_setting("set_audio_temp", "radio", "table_set");
							break;
						case "btmusic":
							// 切换音源
							audioService.setSource(1);
							// 更新数据库
							toDoDB.update_setting("set_audio", "btmusic", "table_set");
							break;
						default:
							// 切换音源
							audioService.setSource(2);
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
					}
					// 更新数据库
					toDoDB.update_setting("set_audiobt", "no", "table_set");
					break;
				default:
					audioService.setSource(2);
					toDoDB.update_setting("set_audio", "media", "table_set");
					toDoDB.update_setting("set_audio_temp", "media", "table_set");
					break;
			}
		} else if ("yes".equals(audioBt)) {
			switch (tagAudioStatus) {
				case "audioBtDialCall":
					// 切换音源
					audioService.setSource(1);
					// 更新数据库
					toDoDB.update_setting("set_audiobt", "yes", "table_set");
					break;
				case "audioMediaOn":
					if ("1".equals(bluetoothBindService.bt_service_music_isplay)) {
						bluetoothBindService.send_Command("AT+B AVRCPPAUSE\r\n");
					}
					// 更新数据库
					toDoDB.update_setting("set_audio", "media", "table_set");
					toDoDB.update_setting("set_audio_temp", "media", "table_set");
					break;
				case "audioRadioOff":
					switch (audioStatus) {
						case "media":
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
						case "btmusic":
							// 更新数据库
							toDoDB.update_setting("set_audio", "btmusic", "table_set");
							break;
						default:
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
					}
					break;
				case "audioRadioOn":
					if ("1".equals(bluetoothBindService.bt_service_music_isplay)) {
						bluetoothBindService.send_Command("AT+B AVRCPPAUSE\r\n");
					}
					// 更新数据库
					toDoDB.update_setting("set_audio", "radio", "table_set");
					toDoDB.update_setting("set_audio_temp", "radio", "table_set");
					break;
				case "audioBtMusic":
					// 更新数据库
					toDoDB.update_setting("set_audio", "btmusic", "table_set");
					break;
				case "audioBtDialCut":
					// 重新获得音源状态
					audioStatus = toDoDB.getSetDataProperty("set_audio");
					switch (audioStatus) {
						case "media":
							// 切换音源
							audioService.setSource(2);
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
						case "radio":
							// 切换音源
							audioService.setSource(0);
							// 更新数据库
							toDoDB.update_setting("set_audio", "radio", "table_set");
							toDoDB.update_setting("set_audio_temp", "radio", "table_set");
							break;
						case "btmusic":
							// 切换音源
							audioService.setSource(1);
							// 更新数据库
							toDoDB.update_setting("set_audio", "btmusic", "table_set");
							break;

						default:
							// 切换音源
							audioService.setSource(2);
							// 更新数据库
							toDoDB.update_setting("set_audio", "media", "table_set");
							toDoDB.update_setting("set_audio_temp", "media", "table_set");
							break;
					}
					// 更新数据库
					toDoDB.update_setting("set_audiobt", "no", "table_set");
					break;
				default:
					// audioService.setSource(2);
					toDoDB.update_setting("set_audio", "media", "table_set");
					toDoDB.update_setting("set_audio_temp", "media", "table_set");
					break;
			}
		}
	}

	// 开启蓝牙计时的内部类
	class TimeCount extends CountDownTimer {
		private int key;

		public TimeCount(long millisInFuture, long countDownInterval, int key) {// 0表示开放蓝牙倒计时 1表示蓝牙断开倒计时 2表示配对码倒计时
			super(millisInFuture, countDownInterval);// 总时长 计时的时间间隔
			this.key = key;
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			switch (key) {
				case 0:
					discovered_window.dismiss();
					break;
				case 1:
					pop_disconnect.dismiss();
					break;
				case 2:
					bluetoothBindService.acceptOrReject(0);
					ssppin_window.dismiss();
					break;
				case 3:
					mContentFragment = fm_VehicleInfo;
					manager.beginTransaction().replace(R.id.fragment_main, fm_VehicleInfo).commit();
					m_rb_cloud.setChecked(true);
					nowFragment = "Cloud";
					break;
			}

		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			switch (key) {
				case 0:
					tv_bluetooth_CountDownTime.setText(millisUntilFinished / 1000 + "秒后关闭");
					break;
				case 1:
					btn_bt_disconn_close.setText("关闭" + "（" + millisUntilFinished / 1000 + "s）");
					break;
				case 2:
					btn_ssppin_no.setText("（" + millisUntilFinished / 1000 + "s）");
					break;
			}
		}
	}

	// 发送关闭多媒体音乐广播
	private void sendCloseMediaMusic() {
		EventBus.getDefault().post(
				new EventMediaRequest("CLOSEMEDIAMUSIC", "default", "default"));
	}

	// 发送关闭多媒体视频广播
	private void sendCloseMediaVideo() {
		EventBus.getDefault().post(
				new EventMediaRequest("CLOSEMEDIAVIDEO", "default", "default"));
	}

	// 发送语音合成广播
	public void sendTtsBroadcast(String content, int priority) {
		speechbundle.putInt("Priority", priority);
		speechbundle.putString("SpeechString", content);
		speechIntent.putExtras(speechbundle);
		speechIntent.setAction("SPEECH_TTS");
		sendBroadcast(speechIntent);
	}

	// 发送隐藏Fragment
	private void hideFragment(String oldFragment) {
		EventBus.getDefault().post(new EventCommand("SURFACEVIEW", oldFragment, "HIDE"));
	}

	// 发送显示Fragment
	private void showFragment(String toFragment) {
		EventBus.getDefault().post(new EventCommand("SURFACEVIEW", toFragment, "SHOW"));
	}

	private void hideOrShow() {
		switch (nowFragment) {
			case "Moni":
				hideFragment("NaviMap");
				hideFragment("Carlife");
				showFragment("Moni");
				break;
			case "NaviMap":
				hideFragment("Moni");
				hideFragment("Carlife");
				showFragment("NaviMap");
				break;
			case "Carlife":
				hideFragment("NaviMap");
				hideFragment("Moni");
				showFragment("Carlife");
				break;
			default:
				hideFragment("NaviMap");
				hideFragment("Moni");
				hideFragment("Carlife");
				break;
		}

	}

	// 通知界面当前所处界面
	private void sendNowFragment() {
		EventBus.getDefault().post(new EventCommand("NOWFRAGMENT", nowFragment, "default"));
	}

	// 定义一个实现自ServiceConnection的类
	public class SerialPortReceiveServiceConnected implements ServiceConnection {
		// 服务连通时回调方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("MainActivity", "onServiceConnected---->客户端连通了服务");
			// 获取服务端传来的Ibinder对象
			SerialPortReadService.SerialPortReadBinder binder = (SerialPortReadService.SerialPortReadBinder) service;
			// 调用MyBinder中公共的方法
			serialPortReadBindService = binder.getSerialPortService();
			// 打开串口，开启串口接收线程
			serialPortReadBindService.openSeralPort();
		}

		// 服务没有连通时回调方法，但是通常表示意外中断连通的时候才会调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("MainActivity", "onServiceConnected---->串口没有连通服务");
		}

	}

	// 定义一个实现自ServiceConnection的类
	public class SerialPortSendServiceConnected implements ServiceConnection {
		// 服务连通时回调方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("MainActivity", "onServiceConnected---->客户端连通了服务");
			// 获取服务端传来的Ibinder对象
			SerialPortWriteService.SerialPortWriteBinder binder = (SerialPortWriteService.SerialPortWriteBinder) service;
			// 调用MyBinder中公共的方法
			serialPortWriteBindService = binder.getSerialPortService();
			// 打开串口，开启串口发送线程
			serialPortWriteBindService.openSeralPort();
		}

		// 服务没有连通时回调方法，但是通常表示意外中断连通的时候才会调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("MainActivity", "onServiceConnected---->串口没有连通服务");
		}

	}

	// 定义一个实现自ServiceConnection的类
	public class MusicServiceConnected implements ServiceConnection {
		// 服务连通时回调方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("MainActivity", "onServiceConnected---->客户端连通了服务");
			// 获取服务端传来的Ibinder对象
			MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
			// 调用MyBinder中公共的方法
			musicBindService = binder.getMusicService();
		}

		// 服务没有连通时回调方法，但是通常表示意外中断连通的时候才会调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("MainActivity", "onServiceConnected---->music没有连通服务");
		}
	}

	// 初始化收音机
	private void initFragmentRadio() {
		Message msg = FragmentRadio.mhandler.obtainMessage();
		msg.what = 5;
		FragmentRadio.mhandler.sendMessage(msg);
	}

	// 初始化多媒体
	private void initFragmentMedia() {
		fm_Media.initMediaPlayer(MainActivity.this);
	}

	// 语音识别
	SpeechIatActivity mSpeechIatActivity;
	Intent intent;

	// 语音识别
	public void onSpeech(View view) {
		if (IsFastDoubleClickUtil.isFastDoubleClick()) {

		} else {
			if (mDataConvert == null) {
				return;
			}
			if (mDataConvert.isNetworkConnected(MainActivity.this)) {
				if (isIatReady) {
					isIatReady = false;
					if (audioVolumeLevel > 1) {
						audioService.setVolume(0);
					}
					sendCloseMediaMusic();
					Intent intent = new Intent(this, SpeechIatActivity.class);
					startActivity(intent);
				}
			} else {
				Toast toast = Toast.makeText(MainActivity.this, "请先连接网络！", Toast.LENGTH_SHORT);
				toast.show();
			}
		}

	}

	// wifi热点开关
	public boolean setWifiApEnabled(boolean enabled, String name, String password) {
		if (enabled) {
			// wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
			wifiManager.setWifiEnabled(false);
		}
		try {
			// 热点的配置类
			apConfig = new WifiConfiguration();
			apConfig.SSID = name;
			if (!shareHavePassword) {
				// 无密码
				apConfig.wepKeys[0] = "";
				apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				apConfig.wepTxKeyIndex = 0;
			} else {
				apConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
				apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
				apConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				apConfig.preSharedKey = password;
				apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			}

			// 通过反射调用设置热点
			Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			// 返回热点打开状态
			return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
			return false;
		}
	}

	//为各个界面标志赋值
	private void setMarkValue() {
		Resources res = getResources();
		cloudMark = res.getStringArray(R.array.cloud_mark);
		naviMark = res.getStringArray(R.array.navi_mark);
		moniMark = res.getStringArray(R.array.moni_mark);
		mediaMark = res.getStringArray(R.array.media_mark);
		radioMark = res.getStringArray(R.array.radio_mark);
		btMark = res.getStringArray(R.array.bt_mark);
		setMark = res.getStringArray(R.array.set_mark);
		carMark = res.getStringArray(R.array.car_mark);

	}

	//获取语音识别语句，进行判断后进入命令对应界面
	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onsPeechIatEvent(EventIATResponse event) {
		if (mDataConvert == null) {
			return;
		}

		switch (event.getKey()) {
			case "IATSTRING":

				String str = event.getValues();
				String key = "SUBCOMMAND";//初始值为当前界面的命令，不切换界面
				if (mDataConvert.isContains(cloudMark, str)) {
					OpenCloudFragment();
					key = "CLOUDCOMMAND";
				} else if (mDataConvert.isContains(naviMark, str)) {
					// OpenNaviFragment();
					// key = "NAVICOMMAND";
				} else if (mDataConvert.isContains(moniMark, str)) {
					OpenMoniFragment();
					key = "MONICOMMAND";
				} else if (mDataConvert.isContains(mediaMark, str)) {
					OpenMediaFragment();
					key = "MEDIACOMMAND";
				} else if (mDataConvert.isContains(radioMark, str)) {
					OpenRadioFragment();
					key = "RADIOCOMMAND";
				} else if (mDataConvert.isContains(btMark, str)) {
					OpenBTFragment();
					key = "BTCOMMAND";
				} else if (mDataConvert.isContains(setMark, str)) {
					OpenSetFragment();
					key = "SETCOMMAND";
				} else if (mDataConvert.isContains(carMark, str)) {
					OpenCarlifeFragment();
					key = "CARCOMMAND";
				}
				EventBus.getDefault().postSticky(new EventCommand(key, str, "default"));
				break;
			case "IATOVER":
				isIatReady = true;
				audioService.setVolume(audioVolumeLevel);
				break;
		}
	}

	private ArrayList<MyOnTouchEvent> listOnTouchEvent = new ArrayList<MyOnTouchEvent>(
			10);

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyOnTouchEvent event : listOnTouchEvent) {
			if (event != null) {
				event.onTouch(ev);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	public void registerMyOnTouchEvent(MyOnTouchEvent myOnTouchEvent) {
		listOnTouchEvent.add(myOnTouchEvent);
	}

	public void unregisterMyOnTouchEvent(MyOnTouchEvent myOnTouchEvent) {
		listOnTouchEvent.remove(myOnTouchEvent);
	}
}
