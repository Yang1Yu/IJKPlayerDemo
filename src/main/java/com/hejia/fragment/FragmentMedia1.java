package com.hejia.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.hejia.media.MyTextView;
import com.hejia.tp_launcher_v3.R;

import de.greenrobot.event.EventBus;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.widget.media.IjkVideoView;


/**
 * Created by dev-12 on 2017/6/23.
 */

public class FragmentMedia1 extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView iv_music_center; //多媒体中心图片
    private MyTextView tv_music_name;//歌名
    private TextView tv_now_time;//当前播放时间
    private ImageView iv_music_seekbarbg;//进度条背景
    private SeekBar sb_music_seekbar;//进度条
    private TextView tv_total_time;//歌曲总时长
    private Button btn_music_last;//上一曲
    private Button btn_music_play;//播放、暂停
    private Button btn_music_next;//下一曲
    private Button btn_music_random;//播放模式（随机，循环，单曲）
    private LinearLayout rg_music_radiogroup;//播放控制（上一曲，下一曲，播放，切换模式）
    private LinearLayout ll_media_music;//音乐播放(对应ijk视频播放器)
    private IjkVideoView ijk_meida_video;//ijk视频播放器
    private RelativeLayout rl_media_music_video;//视音频播放（对应图片播放）
    private TextView tv_media_pic_name;//图片播放器-图片名
    private ViewPager vp_media_pic_big;//图片播放的viewpager
    private ImageView iv_media_pic_picnull;//没有图片时显示默认图片
    private ImageView iv_pic_rotateleft;//图片左旋
    private ImageView iv_pic_start_pause;//播放、暂停图片（5s）
    private ImageView iv_pic_rotateright;//图片右旋
    private LinearLayout ll_media_pic_control;//图片控制条
    private RelativeLayout rl_media_pic_top;//图片播放器
    private Button btn_media_video;//切换视频按钮
    private Button btn_media_pic;//切换图片按钮
    private Button btn_media_music;//切换音乐按钮
    private Button btn_media_add;//添加文件按钮
    private Button btn_media_delete;//清空列表按钮
    private ListView lv_media_listview;//视音频、图片播放列表
    private ImageView iv_media_nofile;//无文件显示默认图片
    private TextView tv_media_nofile;//无文件时提示导入文件
    private FrameLayout fl_media_listmusic;//视音频列表
    private ListView lv_media_pic_list;//图片列表listview
    private FrameLayout fl_media_listpic;//图片列表
    private LinearLayout ll_media_main;//多媒体主页
    private ViewPager vp_media_bigpic_big;//大图播放的viewpager
    private ImageView iv_bigpic_rotateleft;//大图左旋
    private ImageView iv_bigpic_start_pause; //大图播放暂停
    private ImageView iv_bigpic_rotateright; //大图右旋
    private LinearLayout ll_media_bigpic_control;//大图控制
    private RelativeLayout rl_media_bigpic;//切换全屏大图
    private String filePath = null;// 文件路径，用于接收文件选择器返回的路径
    private boolean fmMediaViewIsStart = false;//视频是否已经开始，默认false
    private static FragmentMedia1 mFragmentMedia = null;//FragmentMeadi对象

    public static FragmentMedia1 getInstance()

    {
        if (mFragmentMedia == null) {
            mFragmentMedia = new FragmentMedia1();
        }
        return mFragmentMedia;
    }

    private FragmentMedia1() {
        // 注册EventBus
        EventBus.getDefault().register(this);
    }

    //  dialoglistener 实现了自定义的MyDialogListener类
    // 通过该类实现获取对话框的返回值
    public MyDialogListener dialogListener = new MyDialogListener() {
        @Override
        public void getFilePath(String path) {
            filePath = path;//  得到返回的文件路径，其为一个回调方法
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_media1, container, false);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fmMediaViewIsStart = true;//视频已经开始播放
    }

    private void initView() {
        //多媒体中心图片
        iv_music_center = (ImageView) view.findViewById(R.id.iv_music_center);
        //歌名
        tv_music_name = (MyTextView) view.findViewById(R.id.tv_music_name);
        //当前播放时间
        tv_now_time = (TextView) view.findViewById(R.id.tv_now_time);
        //进度条背景
        iv_music_seekbarbg = (ImageView) view.findViewById(R.id.iv_music_seekbarbg);
        //进度条
        sb_music_seekbar = (SeekBar) view.findViewById(R.id.sb_music_seekbar);
        //歌曲总时长
        tv_total_time = (TextView) view.findViewById(R.id.tv_total_time);
        //上一曲
        btn_music_last = (Button) view.findViewById(R.id.btn_music_last);
        //播放、暂停
        btn_music_play = (Button) view.findViewById(R.id.btn_music_play);
        //下一曲
        btn_music_next = (Button) view.findViewById(R.id.btn_music_next);
        //播放模式（随机，循环，单曲）
        btn_music_random = (Button) view.findViewById(R.id.btn_music_random);
        //播放控制（上一曲，下一曲，播放，切换模式）
        rg_music_radiogroup = (LinearLayout) view.findViewById(R.id.rg_music_radiogroup);
        //音乐播放(对应ijk视频播放器)
        ll_media_music = (LinearLayout) view.findViewById(R.id.ll_media_music);
        //ijk视频播放器
        ijk_meida_video = (IjkVideoView) view.findViewById(R.id.ijk_meida_video);
        //视音频播放（对应图片播放）
        rl_media_music_video = (RelativeLayout) view.findViewById(R.id.rl_media_music_video);
        //图片播放器-图片名
        tv_media_pic_name = (TextView) view.findViewById(R.id.tv_media_pic_name);
        //图片播放的viewpager
        vp_media_pic_big = (ViewPager) view.findViewById(R.id.vp_media_pic_big);
        //没有图片时显示默认图片
        iv_media_pic_picnull = (ImageView) view.findViewById(R.id.iv_media_pic_picnull);
        //图片左旋
        iv_pic_rotateleft = (ImageView) view.findViewById(R.id.iv_pic_rotateleft);
        //播放、暂停图片（5s）
        iv_pic_start_pause = (ImageView) view.findViewById(R.id.iv_pic_start_pause);
        //图片右旋
        iv_pic_rotateright = (ImageView) view.findViewById(R.id.iv_pic_rotateright);
        //图片控制条
        ll_media_pic_control = (LinearLayout) view.findViewById(R.id.ll_media_pic_control);
        //图片播放器
        rl_media_pic_top = (RelativeLayout) view.findViewById(R.id.rl_media_pic_top);
        //切换视频按钮
        btn_media_video = (Button) view.findViewById(R.id.btn_media_video);
        //切换图片按钮
        btn_media_pic = (Button) view.findViewById(R.id.btn_media_pic);
        //切换音乐按钮
        btn_media_music = (Button) view.findViewById(R.id.btn_media_music);
        //添加文件按钮
        btn_media_add = (Button) view.findViewById(R.id.btn_media_add);
        //清空列表按钮
        btn_media_delete = (Button) view.findViewById(R.id.btn_media_delete);
        //视音频、图片播放列表
        lv_media_listview = (ListView) view.findViewById(R.id.lv_media_listview);
        //无文件显示默认图片
        iv_media_nofile = (ImageView) view.findViewById(R.id.iv_media_nofile);
        //无文件时提示导入文件
        tv_media_nofile = (TextView) view.findViewById(R.id.tv_media_nofile);
        //视音频列表
        fl_media_listmusic = (FrameLayout) view.findViewById(R.id.fl_media_listmusic);
        //图片列表listview
        lv_media_pic_list = (ListView) view.findViewById(R.id.lv_media_pic_list);
        //图片列表
        fl_media_listpic = (FrameLayout) view.findViewById(R.id.fl_media_listpic);
        //多媒体主页
        ll_media_main = (LinearLayout) view.findViewById(R.id.ll_media_main);
        //大图播放的viewpager
        vp_media_bigpic_big = (ViewPager) view.findViewById(R.id.vp_media_bigpic_big);
        //大图左旋
        iv_bigpic_rotateleft = (ImageView) view.findViewById(R.id.iv_bigpic_rotateleft);
        //大图播放暂停
        iv_bigpic_start_pause = (ImageView) view.findViewById(R.id.iv_bigpic_start_pause);
        //大图右旋
        iv_bigpic_rotateright = (ImageView) view.findViewById(R.id.iv_bigpic_rotateright);
        //大图控制
        ll_media_bigpic_control = (LinearLayout) view.findViewById(R.id.ll_media_bigpic_control);
        //切换全屏大图
        rl_media_bigpic = (RelativeLayout) view.findViewById(R.id.rl_media_bigpic);

        //初始化播放器
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        AndroidMediaController controller = new AndroidMediaController(getActivity(), false);
        //为ijkplayer设置控制器
        ijk_meida_video.setMediaController(controller);

        btn_music_last.setOnClickListener(this);
        btn_music_play.setOnClickListener(this);
        btn_music_next.setOnClickListener(this);
        btn_music_random.setOnClickListener(this);
        vp_media_pic_big.setOnClickListener(this);
        iv_pic_rotateleft.setOnClickListener(this);
        iv_pic_start_pause.setOnClickListener(this);
        iv_pic_rotateright.setOnClickListener(this);
        btn_media_video.setOnClickListener(this);
        btn_media_pic.setOnClickListener(this);
        btn_media_music.setOnClickListener(this);
        btn_media_add.setOnClickListener(this);
        btn_media_delete.setOnClickListener(this);
        iv_bigpic_rotateleft.setOnClickListener(this);
        iv_bigpic_start_pause.setOnClickListener(this);
        iv_bigpic_rotateright.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //上一曲
            case R.id.btn_music_last:

                break;
            //播放、暂停
            case R.id.btn_music_play:

                break;
            //下一曲
            case R.id.btn_music_next:

                break;
            //播放模式（随机，循环，单曲）
            case R.id.btn_music_random:

                break;
            //图片播放的viewpager
            case R.id.vp_media_pic_big:

                break;
            //图片左旋
            case R.id.iv_pic_rotateleft:

                break;
            //播放、暂停图片（5s）
            case R.id.iv_pic_start_pause:

                break;
            //图片右旋
            case R.id.iv_pic_rotateright:

                break;
            //切换视频按钮
            case R.id.btn_media_video:

                break;
            //切换图片按钮
            case R.id.btn_media_pic:

                break;
            //切换音乐按钮
            case R.id.btn_media_music:

                break;
            //添加文件按钮
            case R.id.btn_media_add:

                break;
            //清空列表按钮
            case R.id.btn_media_delete:

                break;
            //大图左旋
            case R.id.iv_bigpic_rotateleft:

                break;
            //大图播放暂停
            case R.id.iv_bigpic_start_pause:

                break;
            //大图右旋
            case R.id.iv_bigpic_rotateright:

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ijk_meida_video.resume();
    }
    @Override
    public void onPause() {
        super.onPause();
        ijk_meida_video.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IjkMediaPlayer.native_profileEnd();
    }
}
