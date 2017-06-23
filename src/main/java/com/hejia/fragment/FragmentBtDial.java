package com.hejia.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

import java.util.Timer;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentBtDial extends Fragment {
    private View view;
    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_star;
    private Button btn_well;
    private Button btn_call;
    private Button btn_cut;
    private Button btn_back;
    private EditText et_print;
    private String phone_Number = "";
    private MainActivity activity;
    private TextView tv_hfccin_status;
    private TextView tv_hfccin_number;
    private TextView tv_ctd_name;// 当前连接设备名


    private String device_name;

    private String str_hfccin_number = "";
    private String str_hfstat;

    private TextView chronometer;

    private Timer timer;
    private int dial_time;

    private Message message;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    chronometer.setText(toTime(dial_time));
                    dial_time += 1000;
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_bt_dial, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = new Message();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除EventBus
        EventBus.getDefault().unregister(this);
    }

    // 获得Activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    // 对控件进行实例化
    private void initBtn() {
        btn_0 = (Button) view.findViewById(R.id.btn_bt_dial_0);
        btn_1 = (Button) view.findViewById(R.id.btn_bt_dial_1);
        btn_2 = (Button) view.findViewById(R.id.btn_bt_dial_2);
        btn_3 = (Button) view.findViewById(R.id.btn_bt_dial_3);
        btn_4 = (Button) view.findViewById(R.id.btn_bt_dial_4);
        btn_5 = (Button) view.findViewById(R.id.btn_bt_dial_5);
        btn_6 = (Button) view.findViewById(R.id.btn_bt_dial_6);
        btn_7 = (Button) view.findViewById(R.id.btn_bt_dial_7);
        btn_8 = (Button) view.findViewById(R.id.btn_bt_dial_8);
        btn_9 = (Button) view.findViewById(R.id.btn_bt_dial_9);
        btn_star = (Button) view.findViewById(R.id.btn_bt_dial_star);
        btn_well = (Button) view.findViewById(R.id.btn_bt_dial_well);
        btn_call = (Button) view.findViewById(R.id.btn_bt_dial_call);
        btn_cut = (Button) view.findViewById(R.id.btn_bt_dial_cut);
        btn_back = (Button) view.findViewById(R.id.btn_bt_dial_delete);
        tv_hfccin_status = (TextView) view.findViewById(R.id.tv_bt_dial_status);
        tv_hfccin_number = (TextView) view.findViewById(R.id.tv_bt_dial_number);
        tv_ctd_name = (TextView) view.findViewById(R.id.tv_bt_dial_conn);
        chronometer = (TextView) view.findViewById(R.id.chronometer);
        et_print = (EditText) view.findViewById(R.id.et_bt_dial);
    }

    private void initListener() {
        btn_0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    phone_Number = phone_Number + "0";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 0\r\n");
                }
            }
        });

        btn_1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    phone_Number = phone_Number + "1";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 1\r\n");
                }
            }
        });

        btn_2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    phone_Number = phone_Number + "2";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 2\r\n");
                }
            }
        });
        btn_3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "3";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 3\r\n");
                }
            }
        });
        btn_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    phone_Number = phone_Number + "4";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 4\r\n");
                }
            }
        });
        btn_5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "5";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 5\r\n");
                }
            }
        });
        btn_6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "6";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 6\r\n");
                }
            }
        });
        btn_7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "7";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 7\r\n");
                }
            }
        });
        btn_8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "8";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 8\r\n");
                }
            }
        });
        btn_9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "9";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF 9\r\n");
                }
            }
        });
        btn_star.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "*";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF *\r\n");
                }
            }
        });
        btn_well.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    phone_Number = phone_Number + "#";
                    et_print.setText(phone_Number);
                    et_print.setSelection(phone_Number.length());
                    activity.bluetoothBindService.send_Command("AT+B HFDTMF #\r\n");
                }
            }
        });
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    if (phone_Number.length() > 0) {
                        phone_Number = phone_Number.substring(0, phone_Number.length() - 1);
                        et_print.setText(phone_Number);
                        et_print.setSelection(phone_Number.length());
                    }
                }
            }
        });
        btn_call.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    if (!phone_Number.isEmpty()) {
                        activity.bluetoothBindService.send_Command("AT+B HFDIAL 0," + phone_Number + "\r\n");
                        btn_call.setVisibility(View.GONE);
                        btn_cut.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        btn_cut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("BtDial".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                    activity.bluetoothBindService.send_Command("AT+B HFCHUP\r\n");
                    btn_call.setVisibility(View.VISIBLE);
                    btn_cut.setVisibility(View.GONE);
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void btEvent(EventBTRequest event) {
        switch (event.getKey()) {
            case "BTCONN":
                //蓝牙已连接
                device_name = event.getArg1();
                tv_ctd_name.setText(device_name);
                break;
            case "BTDISCONN":
                //蓝牙断开连接
                tv_ctd_name.setText("");
                phone_Number = "";
                et_print.setText("");
                break;
            case "HFCCIN":
                //获得主动拨号
                Log.i("chanyeol", "hfccin");
                str_hfccin_number = (String) event.getArg2();
                str_hfstat = event.getArg1();
                btn_call.setVisibility(View.GONE);
                btn_cut.setVisibility(View.VISIBLE);
                tv_hfccin_number.setVisibility(View.VISIBLE);
                tv_hfccin_number.setText(str_hfccin_number);
                if ("5".equals(str_hfstat) || "3".equals(str_hfstat)) {
                    tv_hfccin_status.setText("正在拨号...");

                } else if ("6".equals(str_hfstat)) {
                    handler.removeMessages(1);
                    tv_hfccin_status.setVisibility(view.GONE);
                    chronometer.setVisibility(view.VISIBLE);
                    dial_time = 1000;
                    // 开始计时
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
                break;
            case "HFCCINCUT":
                //挂断电话
                // activity.sendAudioBradcast("audioBtDialCut");
                handler.removeMessages(1);
                tv_hfccin_status.setText("");
                chronometer.setText("00:00");
                tv_hfccin_number.setText("");
                dial_time = 1000;
                chronometer.setVisibility(view.GONE);
                tv_hfccin_status.setVisibility(view.VISIBLE);
                tv_hfccin_number.setVisibility(View.GONE);

                et_print.setText("");
                phone_Number = "";
                activity.bluetoothBindService.HFCLIP_NUMBER = "";
                activity.bluetoothBindService.str_hfccin_number = "";
                btn_call.setVisibility(View.VISIBLE);
                btn_cut.setVisibility(View.GONE);
                break;
        }
    }

    private String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            initBtn();
            phone_Number = et_print.getText().toString();
            initListener();
            tv_ctd_name.setText(activity.bluetoothBindService.device_name);
            // TODO 添加接听挂断按键切换初始化
        }
    }
}
