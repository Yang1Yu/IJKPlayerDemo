package com.hejia.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hejia.eventbus.EventCommand;
import com.hejia.eventbus.EventSystem;
import com.hejia.myinterface.MyOnTouchEvent;
import com.hejia.myview.CarlifeSurfaceView;
import com.hejia.tp_launcher_v3.R;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCarlife extends Fragment {
	private View view;
	private MainActivity activity;

	private MyOnTouchEvent myOnTouchEvent;

	// 标志位
	private static final int CARLIFECONN = 0;// 成功连接CarLife
	private static final int CARLIFEDISCONN = 1;// 由手机端断开CarLife
	private static final int SHOWSURFACEVIEW = 2;// 显示SurfaceView
	private static final int CONNCARLIFEFAIL = 3;// 连接CarLife失败
	private static boolean carlifeIsConn = false;// CarLife是否连接
	private static boolean connCarLifeIsFinish = true;// 是否完成连接事件 防止多次点击连接按键
	private static String nowFragment = "Carlife";

	private static CarlifeSurfaceView sviewCarlife;
	private static FrameLayout flCarlife;
	private static ImageView ivCarlifeQR;
	private static Button btnCarlifeConn;
	private static Button btnCarlifeExit;
	private static Button btnCarlifeReconn;
	private static MyCarlifeFailDialog dialogCarlifeFail;
	private static MyCarlifeConnAnimDialog dialogCarlifeConnAnim;

	private int i;
	private int intRelativeX;// 相对位置 手指抬起时位置 用来判断是否为点击事件
	private int intRelativeY;// 相对位置 手指抬起时位置 用来判断是否为点击事件
	private static final String TAG = "CarLifeJni";

	private static int jniWidth = -1;//传入jni的位置 即在surfaceview中位置
	private static int jniHeight = -1;//传入jni的位置 即在surfaceview中位置

	private float startX;
	private float startY;
	private float endX;
	private float endY;

	// JNI functions
	public static native int connectToMD();

	public static native int carlifeInit();

	public static native int ctrlTouchAction(int action, int x, int y);

	static {
		//加载动态库
		Log.d(TAG, "load carlife_jni Library");
		System.loadLibrary("carlife_jni");
	}

	private static Handler handlerCarlife = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CARLIFECONN:
					// 连接成功
					dialogCarlifeConnAnim.dismiss();
					carlifeIsConn = true;
					btnCarlifeConn.setBackgroundResource(R.drawable.img_carlife_conn1);
					if (jniHeight != -1 && jniWidth != -1) {
						setLayoutParams(jniWidth, jniHeight);
					}
					flCarlife.setBackgroundResource(R.drawable.bg_carlife_main_conn);
					if ("Carlife".equals(nowFragment)) {
						sviewCarlife.setVisibility(View.VISIBLE);
					}
					ivCarlifeQR.setVisibility(View.GONE);
					btnCarlifeConn.setVisibility(View.GONE);
					connCarLifeIsFinish = true;
					break;
				case CARLIFEDISCONN:
					carlifeIsConn = false;
					connCarLifeIsFinish = true;
					btnCarlifeConn.setBackgroundResource(R.drawable.img_carlife_conn1);
					// 中途断开
					flCarlife.setBackgroundResource(R.drawable.bg_carlife_main_disconn);
					sviewCarlife.setVisibility(View.GONE);
					ivCarlifeQR.setVisibility(View.VISIBLE);
					btnCarlifeConn.setVisibility(View.VISIBLE);
					break;
				case SHOWSURFACEVIEW:
					if ("Carlife".equals(nowFragment)) {
						sviewCarlife.setVisibility(View.VISIBLE);
					}
					break;
				case CONNCARLIFEFAIL:
					carlifeIsConn = false;
					btnCarlifeConn.setBackgroundResource(R.drawable.img_carlife_conn1);
					dialogCarlifeFail.show();
					dialogCarlifeConnAnim.dismiss();
					connCarLifeIsFinish = true;
					break;
			}
		}
	};

	private static void setLayoutParams(int width, int height) {
		ViewGroup.LayoutParams lp = sviewCarlife.getLayoutParams();
		lp.width = width;
		lp.height = height;
		sviewCarlife.setLayoutParams(lp);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_carlife, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}

	//由JNI中的cmd接收线程回调
	public static void JniCallBackEncInitDone(int width, int height) {
		Log.i(TAG, "callback VideoEncoderInitDone");
		//setLayoutParams(width,height);
		jniHeight = height;
		jniWidth = width;
		handlerCarlife.sendEmptyMessage(CARLIFECONN);

	}

	//JNI回调 中途断开
	public static void JniCallBackConnectionDown() {
		Log.i(TAG, "Java JniCallBackConnectionDown");
		handlerCarlife.sendEmptyMessage(CARLIFEDISCONN);
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

	// 初始化界面
	private void initView() {
		flCarlife = (FrameLayout) view.findViewById(R.id.fl_carlife);
		sviewCarlife = (CarlifeSurfaceView) view.findViewById(R.id.sview_carlife);
		ivCarlifeQR = (ImageView) view.findViewById(R.id.iv_carlife_qr);
		btnCarlifeConn = (Button) view.findViewById(R.id.btn_carlife_conn);
		btnCarlifeExit = (Button) view.findViewById(R.id.btn_carlife_exit);
		btnCarlifeReconn = (Button) view.findViewById(R.id.btn_carlife_reconn);
		dialogCarlifeFail = new MyCarlifeFailDialog(activity);
		dialogCarlifeFail.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogCarlifeConnAnim = new MyCarlifeConnAnimDialog(activity);
		dialogCarlifeFail.requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	private void initListener() {
		myOnTouchEvent = new MyOnTouchEvent() {
			@Override
			public void onTouch(MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						startX = event.getX();
						startY = event.getY();
						break;
					case MotionEvent.ACTION_UP:
						endX = event.getX();
						endY = event.getY();
						if (isPress(startX, startY, endX, endY)) {
							intRelativeX = (int) startX;
							intRelativeY = getRelativeY((int) startY);
							if ("Carlife".equals(nowFragment)) {
								Log.i(TAG, "onTouchEvent intRelativeX" + intRelativeX + "intRelativeY" + intRelativeY);
								ctrlTouchAction(3, intRelativeX, intRelativeY);
							}
						}
						startX = 0;
						startY = 0;
						endX = 0;
						endY = 0;
						break;
				}
			}
		};
		activity.registerMyOnTouchEvent(myOnTouchEvent);
		btnCarlifeConn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (connCarLifeIsFinish && "Carlife".equals(nowFragment)) {
//					dialogCarlifeConnAnim.show();
					btnCarlifeConn.setBackgroundResource(R.drawable.img_carlife_conn2);
					connCarLifeIsFinish = false;
					new Thread() {
						@Override
						public void run() {
							super.run();
							if (0 == connectToMD()) {
								carlifeInit();
							} else {
								handlerCarlife.sendEmptyMessage(CONNCARLIFEFAIL);
							}
						}
					}.start();
				}
			}
		});
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

	private boolean isPress(float startX, float startY, float endX, float endY) {
		// 判断是否要进行按压事件
		if (Math.abs(startX - endX) < 10 && Math.abs(startY - endY) < 10) {
			return true;
		}
		return false;
	}

	private int getRelativeY(int absoluteY) {
		return absoluteY - 263;
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void hideOrShow(EventCommand event) {
		if ("SURFACEVIEW".equals(event.getKey())) {
			nowFragment = activity.nowFragment;
			if ("Carlife".equals(event.getArg1())) {
				switch ((String) event.getArg2()) {
					case "HIDE":
						sviewCarlife.setVisibility(View.GONE);
						break;
					case "SHOW":
						if (carlifeIsConn) {
							sviewCarlife.setVisibility(View.VISIBLE);
						}
						break;
				}
			}
		}
	}
}
