package com.hejia.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentBtNo extends Fragment {
    private View view;
    private MainActivity activity;
    private TextView tvBtCountDown;
    private TextView tvBtName;
    private Button btnBtOpen;
    private Button btnBtCancel;
    private TimeCount btTimeCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_bt_no, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            // 初始化蓝牙倒计时
            btTimeCount = new TimeCount(180000, 1000);
            tvBtCountDown = (TextView) view.findViewById(R.id.tv_bt_no_countdowntime);
            tvBtName = (TextView) view.findViewById(R.id.tv_bt_no);
            btnBtOpen = (Button) view.findViewById(R.id.btn_bt_no_open);
            btnBtCancel = (Button) view.findViewById(R.id.btn_bt_no_cancel);
            btnBtOpen.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ("BtNo".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                        // TODO 开启蓝牙可被搜寻 进行倒计时
                        btnBtCancel.setVisibility(View.VISIBLE);
                        btnBtOpen.setVisibility(View.GONE);
                        tvBtCountDown.setVisibility(View.VISIBLE);
                        tvBtName.setText("\"FAW-J7\"可被搜索模式");
                        // 设置蓝牙为可见可连接
                        activity.bluetoothBindService.send_Command("AT+B SCAN 3\r\n");
                        btTimeCount.start();
                    }
                }
            });
            btnBtCancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ("BtNo".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                        // 取消开启蓝牙可被搜索
                        btnBtCancel.setVisibility(View.GONE);
                        btnBtOpen.setVisibility(View.VISIBLE);
                        tvBtCountDown.setVisibility(View.GONE);
                        tvBtName.setText("设备未连接");
                        btTimeCount.cancel();// TODO 取消?
                        // 蓝牙关闭事件
                        // 设置蓝牙为可连接但不可见
                        activity.bluetoothBindService.send_Command("AT+B SCAN 2\r\n");
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
    public void btEvent(EventBTRequest event){
        switch (event.getKey()){
            case "BTCONN":
                //蓝牙已连接
                //  如有设备连接 则将界面恢复初始状态 以便再次调用
                btTimeCount.cancel();
                tvBtCountDown.setVisibility(View.GONE);
                tvBtName.setText("设备未连接");
                btnBtCancel.setVisibility(View.GONE);
                btnBtOpen.setVisibility(View.VISIBLE);
                // 设置蓝牙为不可连接但可见
                activity.bluetoothBindService.send_Command("AT+B SCAN 1\r\n");
                break;
        }
    }

    // 开启蓝牙计时的内部类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 总时长 计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            // 将未连接设备界面复原
            tvBtName.setText("设备未连接");
            tvBtCountDown.setVisibility(View.GONE);
            btnBtCancel.setVisibility(View.GONE);
            // 设置蓝牙为可连接但不可见
            activity.bluetoothBindService.send_Command("AT+B SCAN 2\r\n");
            btnBtOpen.setVisibility(View.VISIBLE);
            // 设置蓝牙为可连接但不可见
            // activity.bluetoothBindService.send_Command("AT+B SCAN 2\r\n");
        }

        @Override
        public void onTick(long millisUntilFinished) {//  计时过程显示
            tvBtCountDown.setText(millisUntilFinished / 1000 + "秒后关闭");
        }
    }
}
