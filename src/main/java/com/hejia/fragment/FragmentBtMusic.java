package com.hejia.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hejia.eventbus.EventAudioRequest;
import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

@SuppressLint("NewApi")
public class FragmentBtMusic extends Fragment {
    private View view;
    private MainActivity activity;

    private Button btn_Bt_Music_Forward;// 上一曲
    private Button btn_Bt_Music_Next;// 下一曲
    private Button btn_Bt_Music_Play;// 播放

    private TextView tv_Bt_Music_Title;// 歌名+歌手信息
    private ProgressBar progress_Bt_Music;// 进度条

    private String bt_Music_Title;
    private String bt_Music_Artist;
    private String bt_Music_Info = "";
    private int bt_Music_Time = 100000000;
    private int bt_Music_Pos = 0;
    private String bt_Music_IsPlaying;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_bt_music, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            // 初始化蓝牙音乐界面按键
            initBtMusicView();
            // 蓝牙音乐界面控制事件
            initListener();
            if (activity.bluetoothBindService.bt_service_music_Artist != ""
                    || activity.bluetoothBindService.bt_service_music_Title != "") {
                bt_Music_Info = activity.bluetoothBindService.bt_service_music_Title + "		"
                        + activity.bluetoothBindService.bt_service_music_Artist;
                tv_Bt_Music_Title.setText(bt_Music_Info);
            }
            bt_Music_IsPlaying = activity.bluetoothBindService.bt_service_music_isplay;
            if ("1".equals(bt_Music_IsPlaying)) {
                btn_Bt_Music_Play.setBackground(activity.getResources().getDrawable(R.drawable.img_bt_music_play2));
                EventBus.getDefault().post(new EventAudioRequest("AUDIO","audioBtMusic"));
            } else {
                btn_Bt_Music_Play.setBackground(activity.getResources().getDrawable(R.drawable.img_bt_music_play1));
            }
        }
    }

    private void initBtMusicView() {
        btn_Bt_Music_Forward = (Button) view.findViewById(R.id.btn_bt_music_forward);
        btn_Bt_Music_Next = (Button) view.findViewById(R.id.btn_bt_music_next);
        btn_Bt_Music_Play = (Button) view.findViewById(R.id.btn_bt_music_play);
        tv_Bt_Music_Title = (TextView) view.findViewById(R.id.tv_bt_music_title);
        progress_Bt_Music = (ProgressBar) view.findViewById(R.id.pbar_bt_music);
    }

    private void initListener() {
        // 上一曲
        btn_Bt_Music_Forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("BtMusic".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    activity.bluetoothBindService.btService_Music_Forward();
                }
            }
        });
        // 下一曲
        btn_Bt_Music_Next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("BtMusic".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    activity.bluetoothBindService.btService_Music_Next();
                }
            }
        });
        // 播放
        btn_Bt_Music_Play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("BtMusic".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                    activity.bluetoothBindService.btService_Music_Play();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
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


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void btEvent(EventBTRequest event) {
        switch (event.getKey()) {
            case "BTDISCONN":
                //蓝牙断开连接
                progress_Bt_Music.setProgress(0);
                tv_Bt_Music_Title.setText("");
                break;
            case "BTMUSICINFO":
                // 蓝牙音乐歌手 歌名 歌曲时长 播放状态
                bt_Music_Title = event.getArg1();
                bt_Music_Artist = (String) event.getArg2();
                bt_Music_IsPlaying = event.getArg3();
                bt_Music_Time = (int) event.getArg4();
                if ("1".equals(bt_Music_IsPlaying)) {
                    btn_Bt_Music_Play.setBackground(activity.getResources().getDrawable(R.drawable.img_bt_music_play2));
                    // activity.sendAudioBradcast("audioBtMusic");
                } else {
                    btn_Bt_Music_Play.setBackground(activity.getResources().getDrawable(R.drawable.img_bt_music_play1));
                }
                bt_Music_Info = bt_Music_Title + "		" + bt_Music_Artist;
                tv_Bt_Music_Title.setText(bt_Music_Info);
                break;
            case "BTMUSICPOS":
                // 蓝牙音乐播放位置
                bt_Music_IsPlaying = event.getArg1();
                bt_Music_Pos = (int) event.getArg2();
                if ("1".equals(bt_Music_IsPlaying)) {
                    btn_Bt_Music_Play.setBackground(activity.getResources().getDrawable(R.drawable.img_bt_music_play2));
                    // activity.sendAudioBradcast("audioBtMusic");
                } else {
                    btn_Bt_Music_Play.setBackground(activity.getResources().getDrawable(R.drawable.img_bt_music_play1));
                }
                progress_Bt_Music.setMax(bt_Music_Time);
                progress_Bt_Music.setProgress(bt_Music_Pos);
                progress_Bt_Music.invalidate();
                break;
        }
    }

    // 跑马灯
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_Bt_Music_Title.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
        tv_Bt_Music_Title.setMarqueeRepeatLimit(-1);
        tv_Bt_Music_Title.setFocusable(true);
        tv_Bt_Music_Title.setFocusableInTouchMode(true);
        tv_Bt_Music_Title.setSelected(true);

    }

    /*
     * 时间格式转换
     */
    private String toTime(int time) {
        // time /= 1000;
        // int minute = time / 60;
        int minute = time / 60000;
        // int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
}
