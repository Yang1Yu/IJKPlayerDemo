
package com.hejia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.hejia.bean.PicFileBean;
import com.hejia.eventbus.EventAudioRequest;
import com.hejia.eventbus.EventMediaRequest;
import com.hejia.eventbus.EventMediaResponse;
import com.hejia.eventbus.EventRadioRequest;
import com.hejia.media.FilePerate;
import com.hejia.media.Media;
import com.hejia.media.MusicService;
import com.hejia.media.MyListAdapter;
import com.hejia.media.MyTextView;
import com.hejia.media.OrderComparator;
import com.hejia.myview.ImageScaleView;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FileChooseDialog;
import com.hejia.fragment.FragmentRadio;
import com.hejia.serialport.DataConvert;
import com.hejia.util.ImageScaleUtil;
import com.hejia.util.LruCacheUtil;
import com.hejia.util.OrderPicComparatorUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Set;


interface MyDialogListener {
    public void getFilePath(String path);
}

public class FragmentMedia extends Fragment {

    //图片浏览变量
    //GridView中可见的第一张图片的下标
    private int mFirstVisibleItem = -1;
    //GridView中可见的图片的数量
    private int mVisibleItemCount;
    //记录是否是第一次进入该界面
    private boolean isFirstEnterThisActivity = true;
    private ImageScaleView isv_media_pic;
    private LinearLayout ll_pic_pager;
    private ImageView iv_pic_pre;
    private ImageView iv_pic_next;
    private ImageScaleView isv_media_big_pic;
    private LinearLayout ll_big_pic_pager;
    private ImageView iv_big_pic_pre;
    private ImageView iv_big_pic_next;
    private TextView tv_media_pic_name;
    private ImageView iv_media_pic_picnull;
    private ImageView iv_pic_rowleft;
    private ImageView iv_pic_rowright;
    private ImageView iv_pic_start_pause;
    private LinearLayout ll_media_pic_coller;
    private ListView lv_media_pic_list;
    private List<PicFileBean> picList = new ArrayList<>();//图片对象集合
    public int picPosition = -1;//当前播放的图片
    public PictureAdapter pictureAdapter;
    private boolean threadFlag = false;
    private ImageView iv_bigpic_rowleft;
    private ImageView iv_bigpic_start_pause;
    private ImageView iv_bigpic_rowright;
    private RelativeLayout rl_media_bigpic;
    private LinearLayout ll_media_bigpic_coller;
    private LinearLayout ll_media_pic_empty;
    public MediaPicDialog picDialog;
    private Bitmap bitmap, bigbitmap;
    //5s图片定时轮播
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                SystemClock.sleep(5000);
                message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    });
    ;
    private Message message;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (message.what) {
                case 1:
                    if (threadFlag) {
                        if (-1 != picPosition && picPosition == picList.size() - 1) {
                            picPosition = 0;
                        } else {
                            picPosition = picPosition + 1;
                        }
                        if (rl_media_bigpic.getVisibility() == View.VISIBLE) {
                            BigUIchange();
                        } else {
                            UIchange();
                        }
                    }
                    break;
            }
        }
    };

    // 多媒体清空列表 popwindow
    private Button bt_media_pop_ok;
    private Button bt_media_pop_no;
    private PopupWindow pop_media;
    public MainActivity activity;
    private View view;
    private MediaController mController;
    private TextView tv_now_time;
    private TextView tv_total_time;
    private TextView tv_media_nofile;
    private ImageView iv_media_nofile;
    private Button btn_media_add;
    private Button btn_media_delete;
    private LinearLayout ll_media_empty;
    private ListView lv_media_listview;
    private LinearLayout ll_media_music;
    private VideoView vv_meida_video;
    public MyTextView tv_music_name;
    private SeekBar sb_music_seekbar;
    private Button bt_music_last;
    public Button bt_music_play;
    private Button bt_music_next;
    private Button bt_music_random;
    private Button bt_music_one;
    public Boolean isplayingmp3 = false;// 暂停
    public Boolean isplayingmp4 = false;// 暂停
    public static int id = 0;
    public int tag_Order = 0;// 初始顺序播放
    public static MyListAdapter myListAdapter;
    FilePerate filePerate = null;
    public Activity mediaActivity;
    public TreeSet<Media> mediaTreeSet = new TreeSet<Media>(new OrderComparator());
    private FragmentRadio radio;
    private String filePath = null;// 文件路径，用于接收文件选择器返回的路径
    public boolean enableApply = true;// true：可播放状态 false：不可播放状态
    public boolean isStarted = false;// 判断是否已经启动过，是否需要初始化并更新UI
    public boolean isNowPaper = false;// 判断是否为当前页面，如果不是则不会自动播放mp4
    DataConvert mDataConvert = new DataConvert();
    public MusicService mMusicService = new MusicService();
    public boolean seekbarWait = false;
    FileChooseDialog dialog;
    public int progress = 0;
    private boolean isEnableDele = true;
    public boolean isEnableAdd = true;
    private RelativeLayout rl_media_music_video;
    private RelativeLayout rl_media_pic_top;
    private Button btn_media_pic;
    private FrameLayout fl_media_listmusic;
    private FrameLayout fl_media_listpic;
    private Button btn_media_video;
    private int pagerFlag = 0;

    private static FragmentMedia mFragmentMedia = null;

    private boolean fmMediaViewIsStart = false;

    public static FragmentMedia getInstance()

    {
        if (mFragmentMedia == null) {
            mFragmentMedia = new FragmentMedia();
        }
        return mFragmentMedia;
    }

    private FragmentMedia() {
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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaActivity = getActivity();
        radio = new FragmentRadio();
        mController = new MediaController(mediaActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        fmMediaViewIsStart = true;
        initMediaPlayerui();
        initRandomUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_media, container, false);

        Init();
        initView();
        setListener();
        picSetLintener();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    private void Init() {

        btn_media_add = (Button) view.findViewById(R.id.btn_media_add);
        btn_media_delete = (Button) view.findViewById(R.id.btn_media_delete);
        ll_media_empty = (LinearLayout) view.findViewById(R.id.ll_media_empty);
        lv_media_listview = (ListView) view.findViewById(R.id.lv_media_listview);
        tv_music_name = (MyTextView) view.findViewById(R.id.tv_music_name);
        ll_media_music = (LinearLayout) view.findViewById(R.id.ll_media_music);
        vv_meida_video = (VideoView) view.findViewById(R.id.vv_meida_video);
        bt_music_last = (Button) view.findViewById(R.id.bt_music_last);
        bt_music_play = (Button) view.findViewById(R.id.bt_music_play);
        bt_music_next = (Button) view.findViewById(R.id.bt_music_next);
        bt_music_random = (Button) view.findViewById(R.id.bt_music_random);
        sb_music_seekbar = (SeekBar) view.findViewById(R.id.sb_music_seekbar);
        tv_media_nofile = (TextView) view.findViewById(R.id.tv_media_nofile);
        iv_media_nofile = (ImageView) view.findViewById(R.id.iv_media_nofile);
        tv_now_time = (TextView) view.findViewById(R.id.tv_now_time);
        tv_total_time = (TextView) view.findViewById(R.id.tv_total_time);

        rl_media_music_video = (RelativeLayout) view.findViewById(R.id.rl_media_music_video);
        rl_media_pic_top = (RelativeLayout) view.findViewById(R.id.rl_media_pic_top);
        btn_media_pic = (Button) view.findViewById(R.id.btn_media_pic);
        fl_media_listmusic = (FrameLayout) view.findViewById(R.id.fl_media_listmusic);
        fl_media_listpic = (FrameLayout) view.findViewById(R.id.fl_media_listpic);
        btn_media_video = (Button) view.findViewById(R.id.btn_media_video);

        bt_music_random.setBackgroundResource(R.drawable.img_music_order1);
        bt_music_play.setBackgroundResource(R.drawable.img_music_pause);

        // 从数据库获取音乐
//        MainActivity.toDoDB.query("table_media");

        myListAdapter = new MyListAdapter(mediaActivity, mediaTreeSet);
        myListAdapter.notifyDataSetChanged();
        lv_media_listview.setAdapter(myListAdapter);

        // 删除重复的item
        TreeSet<Media> treeSet = new TreeSet<Media>(new OrderComparator());
        treeSet.addAll(mediaTreeSet);
        myListAdapter.mList = new ArrayList<Media>(treeSet);

        //  为VideoView指定MediaController
        vv_meida_video.setMediaController(mController);
        // 为MediaController指定控制的VideoView
        mController.setMediaPlayer(vv_meida_video);

        if (myListAdapter.mList.size() == 0) {
            tv_media_nofile.setVisibility(View.VISIBLE);
            iv_media_nofile.setVisibility(View.VISIBLE);
        } else {
            tv_media_nofile.setVisibility(View.GONE);
            iv_media_nofile.setVisibility(View.GONE);
        }

    }

    // 处理各控件的监听事件
    private void setListener() {
        //切换音乐视频页面
        btn_media_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerFlag = 0;
                rl_media_music_video.setVisibility(View.VISIBLE);
                rl_media_pic_top.setVisibility(View.GONE);
                fl_media_listmusic.setVisibility(View.VISIBLE);
                fl_media_listpic.setVisibility(View.GONE);
            }
        });
        //切换图片浏览页面
        btn_media_pic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pagerFlag = 3;
                rl_media_music_video.setVisibility(View.GONE);
                rl_media_pic_top.setVisibility(View.VISIBLE);
                fl_media_listmusic.setVisibility(View.GONE);
                fl_media_listpic.setVisibility(View.VISIBLE);
            }
        });
        // 播放
        bt_music_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if ((myListAdapter.mList.size() > 0) && (enableApply)) {
                        Media media = myListAdapter.mList.get(id);
                        if (checkIsMusicStyle(media)) {
                            tv_music_name.setText(media.getTitle());
                            if (!isplayingmp3) {

                                startMediaMP3(0);
                            } else {
                                pauseMediaMP3();
                            }
                        } else {
                            // 如果当前id为MP4，提示请选择歌曲并点击无效
                            EventBus.getDefault().post(new EventMediaRequest("MEDIASELECT", "default", "default"));

                        }
                    }
                } catch (Exception e) {
                }


            }
        });

        // 视频的上一曲下一曲监听
        mController.setPrevNextListeners(new OnClickListener() {
            // 下一曲
            @Override
            public void onClick(View arg0) {
                if (enableApply) {
                    if (tag_Order == 1) {
                        EventBus.getDefault().post(
                                new EventMediaRequest("MEDIALOOP", "PLAY_RADOM", "default"));
                    } else {
                        EventBus.getDefault().post(
                                new EventMediaRequest("MEDIALOOP", "PLAY_ORDER", "default"));
                    }

                }

            }
        }, new OnClickListener() {

            // 上一曲
            @Override
            public void onClick(View v) {
                try {
                    if (enableApply) {
                        if (tag_Order == 1) {
                            EventBus.getDefault().post(
                                    new EventMediaRequest("MEDIALOOP", "PLAY_RADOM", "default"));
                        } else {
                            if (id != 0) {
                                id--;
                            } else {
                                id = myListAdapter.mList.size() - 1;
                            }
                            if (id > (myListAdapter.mList.size() - 1)) {
                                id = 0;
                            }
                            if (myListAdapter.mList.size() > 0) {
                                Media media = myListAdapter.mList.get(id);
                                tv_music_name.setText(media.getTitle());
                                if (checkIsMusicStyle(media)) {

                                    startMediaMP3(1);
                                } else if (!checkIsMusicStyle(media)) {
                                    startMediaMP4();
                                }

                            }
                            activity.toDoDB.update_setting("media_id", id + "", "table_radio");
                        }
                    }
                } catch (Exception e) {
                }
            }
        });


        // 点击导入文件
        btn_media_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pagerFlag == 3) {
                    threadFlag = false;
                    iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
                    getPicDialog();
                } else {
                    if (isEnableAdd) {
                        isEnableAdd = false;
                        FileChooseDialog dialog = new FileChooseDialog(mediaActivity, dialogListener);
                        // 创建一个自定义的对话框
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }

            }
        });

        // 监听视频结束
        vv_meida_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放结束后的动作
                mMusicService.playerOverListener();
            }
        });

        vv_meida_video.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.e("wwl", "发生未知错误");

                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Log.e("wwl", "媒体服务器死机");
                        break;
                    default:
                        Log.e("wwl", "onError ");
                        break;
                }
                switch (extra) {
                    case MediaPlayer.MEDIA_ERROR_IO:
                        //io读写错误
                        Log.e("wwl", "文件或网络相关的IO操作错误");
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        //文件格式不支持
                        Log.e("wwl", "比特流编码标准或文件不符合相关规范");
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        //一些操作需要太长时间来完成,通常超过3 - 5秒。
                        Log.e("wwl", "操作超时");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
                        Log.e("wwl", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                        break;
                    default:
                        Log.e("wwl", "onError ");
                        break;
                }
                EventBus.getDefault().post(
                        new EventMediaRequest("MEDIAERR", "default", "default"));
                //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
                return false;
            }
        });


        // 进度控件监听
        sb_music_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    if (myListAdapter.mList.size() > 0) {
                        if (id > (myListAdapter.mList.size() - 1)) {
                            id = 0;
                        }
                        if (enableApply) {
                            tv_music_name.setText(myListAdapter.mList.get(id).getTitle());
                            seekbarWait = true;
                            EventBus.getDefault().post(new EventAudioRequest("AUDIO", "audioMediaOn"));
                            if (radio == null)
                                radio = new FragmentRadio();
                            EventBus.getDefault().post(new EventRadioRequest("RADIOOFF", "default", "default"));
                            EventBus.getDefault().post(new EventMediaResponse("MEDIAPLAY", "PAUSE_TO_START", "default"));
                            bt_music_play.setBackgroundResource(R.drawable.img_music_play);
                            isplayingmp3 = true;
                            isplayingmp4 = false;

                            EventBus.getDefault().post(
                                    new EventMediaResponse("SEEKPOS", seekBar.getProgress() + "", "default"));

                        } else {
                            sb_music_seekbar.setProgress(0);
                            sb_music_seekbar.invalidate();
                        }

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
        });

        // 上一曲
        bt_music_last.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    if (enableApply) {
                        if (tag_Order == 1) {
                            EventBus.getDefault().post(
                                    new EventMediaRequest("MEDIALOOP", "PLAY_RADOM", "default"));
                        } else {
                            if (id != 0) {
                                id--;
                            } else {
                                id = myListAdapter.mList.size() - 1;
                            }

                            if (id > (myListAdapter.mList.size() - 1)) {
                                id = 0;
                            }
                            if (myListAdapter.mList.size() > 0) {
                                Media media = myListAdapter.mList.get(id);
                                Log.e("media-last", media.getTitle());
                                tv_music_name.setText(media.getTitle());
                                if (checkIsMusicStyle(media)) {

                                    startMediaMP3(1);
                                } else if (!checkIsMusicStyle(media)) {
                                    startMediaMP4();
                                }

                            }
                            activity.toDoDB.update_setting("media_id", id + "", "table_radio");
                        }
                    }

                } catch (Exception e) {
                }


            }

        });
        // 下一曲
        bt_music_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (enableApply) {
                    if (tag_Order == 1) {
                        EventBus.getDefault().post(
                                new EventMediaRequest("MEDIALOOP", "PLAY_RADOM", "default"));
                    } else {
                        EventBus.getDefault().post(
                                new EventMediaRequest("MEDIALOOP", "PLAY_ORDER", "default"));
                    }
                }

            }

        });

        //循环方式控件监听
        bt_music_random.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //0顺序1随机2单曲
                switch (tag_Order) {
                    case 0://如现在为顺序播放，切换随机
                        bt_music_random.setBackgroundResource(R.drawable.img_music_random1);
                        activity.toDoDB.update_setting("media_random", "1", "table_radio");
                        tag_Order = 1;
                        break;
                    case 1:// 如现在为随机，切换单曲
                        bt_music_random.setBackgroundResource(R.drawable.img_music_one1);
                        activity.toDoDB.update_setting("media_random", "2", "table_radio");
                        tag_Order = 2;
                        break;
                    case 2:// 如现在为单曲播放，切换顺序
                        bt_music_random.setBackgroundResource(R.drawable.img_music_order1);
                        activity.toDoDB.update_setting("media_random", "0", "table_radio");
                        tag_Order = 0;
                        break;

                    default:
                        break;
                }
            }
        });

        // 清空按键监听
        btn_media_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pagerFlag == 3) {

                    MediaDeletePopwindow("请确认清空列表");
                } else {
                    if (isEnableDele) {
                        isEnableDele = false;
                        MediaDeletePopwindow("请确认清空列表");
                    }
                }

            }
        });

    }

    // 清空多媒体列表
    public void MediaDeletePopwindow(String str) {
        LayoutInflater media_inflater = (LayoutInflater) mediaActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View media_pop = media_inflater.inflate(R.layout.pop_media_delete, null);
        bt_media_pop_ok = (Button) media_pop.findViewById(R.id.bt_media_pop_ok);
        bt_media_pop_no = (Button) media_pop.findViewById(R.id.bt_media_pop_no);
        TextView tv_pop_content = (TextView) media_pop.findViewById(R.id.tv_pop_content);
        tv_pop_content.setText(str);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pop_media = new PopupWindow(media_pop, metrics.widthPixels, metrics.heightPixels);
        pop_media.setFocusable(false);

        ColorDrawable dw = new ColorDrawable(0x66111111);
        pop_media.setBackgroundDrawable(dw);

        pop_media.showAtLocation(activity.findViewById(R.id.btn_main_bottom_switchbutton), Gravity.CENTER_HORIZONTAL, 0, 140);

        bt_media_pop_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pagerFlag == 3) {
                    picPosition = 0;
                    LruCacheUtil.getInstance().evictAll();//清除全部图片内存
                    picList.clear();
                    UIchange();
                    pop_media.dismiss();
                } else {
                    clearMediaList();
                }
            }
        });
        bt_media_pop_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pop_media.dismiss();
                isEnableDele = true;
            }
        });

    }

    public class MyCompletionListner extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        }

    }

    // 开启音乐播放 0:继续播放 1：从起点播放
    private void startMediaMP3(int value) {
//		isAllFileNOExisted();
        if (enableApply) {
            if (isStarted) {
                ll_media_music.setVisibility(View.VISIBLE);
                vv_meida_video.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventAudioRequest("AUDIO", "audioMediaOn"));
                EventBus.getDefault().post(new EventRadioRequest("RADIOOFF", "default", "default"));
            }

            switch (value) {
                case 0:
                    EventBus.getDefault().post(
                            new EventMediaResponse("MEDIAPLAY", "PAUSE_TO_START", "default"));
                    break;
                case 1:
                    EventBus.getDefault().post(
                            new EventMediaResponse("MEDIAPLAY", "PLAY", "default"));
                    break;
                default:
                    break;
            }
            isplayingmp3 = true;
            isplayingmp4 = false;
            if (isStarted) {
                bt_music_play.setBackgroundResource(R.drawable.img_music_play);

            }
            saveMediaSwitch();//保存多媒体开启状态
        }
    }

    // 暂停音乐播放
    private void pauseMediaMP3() {
        if (isStarted) {
            ll_media_music.setVisibility(View.VISIBLE);
            vv_meida_video.setVisibility(View.GONE);
        }
        EventBus.getDefault().post(
                new EventMediaResponse("MEDIAPLAY", "PAUSE", "default"));

        isplayingmp3 = false;
        isplayingmp4 = false;
        if (isStarted) {
            bt_music_play.setBackgroundResource(R.drawable.img_music_pause);

        }
        saveMediaSwitch();// 保存多媒体开启状态

    }

    private void startMediaMP4() {
//		isAllFileNOExisted();
        if (enableApply) {
            if (isStarted) {

                if (isplayingmp3 == true) {
                    // 如果当前正在播放mp3，需先暂停mp3
                    EventBus.getDefault().post(
                            new EventMediaResponse("MEDIAPLAY", "PAUSE", "default"));

                    bt_music_play.setBackgroundResource(R.drawable.img_music_pause);
                    isplayingmp3 = false;
                    saveMediaSwitch();
                } else if (isplayingmp4) {
                    vv_meida_video.stopPlayback();
                }

                EventBus.getDefault().post(new EventAudioRequest("AUDIO", "audioMediaOn"));
                EventBus.getDefault().post(new EventRadioRequest("RADIOOFF", "default", "default"));
                String pathString = myListAdapter.mList.get(id).getUrl();
                File file = new File(pathString);
                if (file.exists()) {
                    ll_media_music.setVisibility(View.GONE);
                    vv_meida_video.setVisibility(View.VISIBLE);
                    // 打开视频时先将进度条归位，避免出现闪顿现象
                    sb_music_seekbar.setProgress(0);
                    sb_music_seekbar.invalidate();
                    // 设置播放视频源的路径
                    vv_meida_video.setVideoPath(file.getAbsolutePath());
                    vv_meida_video.requestFocus();
                    vv_meida_video.start();
                    isplayingmp4 = true;
                } else {
                    tv_music_name.setText("无法播放此文件");
//					mMusicService.playerOverListener();
                }
            } else {
                EventBus.getDefault().post(new EventMediaRequest("MEDIASELECT", "default", "default"));
            }
        }
    }

    //关闭视频
    private void closeMediaMp4() {
        if (isplayingmp4) {
            ll_media_music.setVisibility(View.VISIBLE);
            vv_meida_video.setVisibility(View.GONE);
            if (vv_meida_video.isPlaying()) {
                vv_meida_video.stopPlayback();
                isplayingmp4 = false;
            }
            EventBus.getDefault().post(new EventMediaRequest("MEDIASELECT", "default", "default"));
        }
    }

    public void saveMediaSwitch() {
        if (isplayingmp3) {
            activity.toDoDB.update_setting("media_switch", "on", "table_radio");
        } else {
            activity.toDoDB.update_setting("media_switch", "off", "table_radio");
        }
    }

    // 初始化播放顺序
    private void initRandomUI() {
        switch (tag_Order) {
            case 0:
                bt_music_random.setBackgroundResource(R.drawable.img_music_order1);
                break;
            case 1:
                bt_music_random.setBackgroundResource(R.drawable.img_music_random1);
                break;
            case 2:
                bt_music_random.setBackgroundResource(R.drawable.img_music_one1);
                break;
            default:
                break;
        }
    }

    // 电源重启需初始化mediaplayer，判断当前音乐是mp3/mp4觉得使用mediaplayer还是videoView
    public void initMediaPlayer(Context context) {
        try {
            MainActivity mcontext = (MainActivity) context;
            String index = mcontext.toDoDB.select_media_switch();
            String idnow = mcontext.toDoDB.select_media_id();
            String tag_Orderstr = mcontext.toDoDB.select_media_random();
            id = Integer.parseInt(idnow);
            tag_Order = Integer.parseInt(tag_Orderstr);
            //从数据库获取音乐
            mcontext.toDoDB.query("table_media");
            myListAdapter = new MyListAdapter(mediaActivity, mediaTreeSet);
            if (id > (myListAdapter.mList.size() - 1)) {
                id = 0;
            }
            if (index.equals("off"))
                isplayingmp3 = false;
            else {
                isplayingmp3 = true;
                isplayingmp4 = false;
            }
            if (myListAdapter.mList.size() > 0) {

                Media media = myListAdapter.mList.get(id);
                if (checkIsMusicStyle(media)) {

                    if (isplayingmp3) {
                        EventBus.getDefault().postSticky(
                                new EventMediaResponse("MEDIAPLAY", "PAUSE_TO_START", "default"));
                    }

                }

            }

        } catch (Exception e) {
        }

    }

    // 开机初始化多媒体UI
    private void initMediaPlayerui() {
        try {

            if (id > (myListAdapter.mList.size() - 1)) {
                id = 0;
            }
            if (myListAdapter.mList.size() > 0) {
                Media media = myListAdapter.mList.get(id);

                if (checkIsMusicStyle(media)) {
                    tv_music_name.setText(media.getTitle());

                    if (isplayingmp3) {

                        bt_music_play.setBackgroundResource(R.drawable.img_music_play);
                    } else {
                        bt_music_play.setBackgroundResource(R.drawable.img_music_pause);
                    }

                } else if (!checkIsMusicStyle(media)) {

                    closeMediaMp4();
                    EventBus.getDefault().post(new EventMediaRequest("MEDIASELECT", "default", "default"));
                }

            }

        } catch (Exception e) {
        }


    }

    //判断多媒体是音乐还是视频  返回1：音乐 2：多媒体 3：其他不作处理
    private boolean checkIsMusicStyle(Media media) {
        String name = media.getTitle();
        if (name.endsWith(".mp3")) {
            return true;
        } else if (name.endsWith(".wma")) {
            return true;
        } else if (name.endsWith(".wav")) {
            return true;
        } else if (name.endsWith(".mp4")) {
            return false;
        } else if (name.endsWith(".wmv")) {
            return false;
        } else if (name.endsWith(".avi")) {
            return false;
        }
        return true;

    }

    //如果名字和路径中包含“’”经过数据库前需更改
    public String filterMediain(String str) {
        String value = str;
        if (str.contains("'")) {
            value = str.replace("'", "/.txt./");
        }
        return value;
    }

    //如果名字和路径中包含“/.txt./”从数据库取出后需恢复
    public String filterMediaout(String str) {
        String value = str;
        if (str.contains("/.txt./")) {
            value = str.replace("/.txt./", "'");
        }

        return value;
    }


    //判断列表中的所有音乐文件是否都不存在
    private boolean isAllFileNOExisted() {

        for (int i = 0; i < myListAdapter.mList.size(); i++) {
            Media media = myListAdapter.mList.get(i);
            File file = new File(media.getUrl());
            if (file.exists()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除EventBus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(EventMediaRequest event) {
        switch (event.getKey()) {
            case "MEDIASELECT":
                enableApply = false;
                if (isStarted) {
                    tv_music_name.setText("请选中歌曲！");
                    sb_music_seekbar.setProgress(0);
                    sb_music_seekbar.invalidate();
                    tv_now_time.setText("00:00");
                    tv_total_time.setText("00:00");
                }
                break;
            case "CLOSEMEDIAMUSIC"://关闭多媒体音乐
                pauseMusicAndVideo();
                break;
            case "CLOSEMEDIAVIDEO"://关闭多媒体视频
                closeMediaMp4();
                break;
            case "MEDIAPOS"://播放进度
                if (enableApply) {
                    if (!seekbarWait) {
                        int position = Integer.valueOf(event.getValues());
                        int total = Integer.valueOf(event.getObj().toString());
                        if (total == 0) {
                            return;
                        }
                        progress = position * 100 / total;
                        if ((total >= 0) && (progress >= 0)) {
                            try {

                                sb_music_seekbar.setProgress(progress);
                                sb_music_seekbar.invalidate();
                                int nowTimeSecond = position / 1000;
                                int totalTimeSecond = total / 1000;
                                String nowTimeSecondstr = String.format("%02d", nowTimeSecond / 60) + ":"
                                        + String.format("%02d", nowTimeSecond % 60);
                                String totalTimeSecondstr = String.format("%02d", totalTimeSecond / 60) + ":"
                                        + String.format("%02d", totalTimeSecond % 60);
                                tv_now_time.setText(nowTimeSecondstr);
                                tv_total_time.setText(totalTimeSecondstr);
                                if (nowTimeSecond / 60 > 20) {
                                    Log.e("seekbar", progress + "--" + position + "--" + total);
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }

                        }
                    }
                } else {
                    sb_music_seekbar.setProgress(0);
                    sb_music_seekbar.invalidate();
                }
                break;
            case "MEDIALOOP":
                String sequence = event.getValues();
                switch (sequence) {
                    case "PLAY_ORDER":
                        if (isAllFileNOExisted()) {
                            enableApply = false;
                            isplayingmp3 = false;
                            if ((isStarted) && (isNowPaper)) {
                                bt_music_play.setBackgroundResource(R.drawable.img_music_pause);
                            }
                            tv_music_name.setText("无法播放此文件");
                            break;
                        }
                        id++;
                        if (id > (myListAdapter.mList.size() - 1)) {
                            id = 0;
                        } else if (id < 0) {
                            id = 0;
                        }
                        if (myListAdapter.mList.size() > 0) {
                            Media media = myListAdapter.mList.get(id);
                            if (isStarted) {
                                tv_music_name.setText(media.getTitle());
                            }
                            if (checkIsMusicStyle(media)) {

                                startMediaMP3(1);
                            } else if (!checkIsMusicStyle(media)) {
                                if ((isStarted) && (isNowPaper)) {
                                    startMediaMP4();
                                } else {
                                    id = 0;
                                    Media media0 = myListAdapter.mList.get(id);
                                    if (isStarted) {
                                        tv_music_name.setText(media0.getTitle());
                                    }
                                    startMediaMP3(1);
                                }
                            }

                        }
                        activity.toDoDB.update_setting("media_id", id + "", "table_radio");
                        break;
                    case "PLAY_RADOM":
                        if (isAllFileNOExisted()) {
                            enableApply = false;
                            isplayingmp3 = false;
                            if ((isStarted) && (isNowPaper)) {
                                bt_music_play.setBackgroundResource(R.drawable.img_music_pause);
                            }
                            tv_music_name.setText("无法播放此文件");
                            break;
                        }
                        if (id > (myListAdapter.mList.size() - 1)) {
                            id = 0;
                        }
                        if (myListAdapter.mList.size() - 1 >= 0) {
                            id = mDataConvert.getRandom(0, myListAdapter.mList.size() - 1);
                            if (id >= myListAdapter.mList.size() - 1) {
                                id = myListAdapter.mList.size() - 1;
                            } else if (id < 0) {
                                id = 0;
                            }
                            Media media = myListAdapter.mList.get(id);
                            if (isStarted) {
                                tv_music_name.setText(media.getTitle());
                            }
                            if (checkIsMusicStyle(media)) {

                                startMediaMP3(1);

                            } else if (!checkIsMusicStyle(media)) {
                                if ((isStarted) && (isNowPaper)) {
                                    startMediaMP4();
                                } else {
                                    id = 0;
                                    Media media0 = myListAdapter.mList.get(id);
                                    if (isStarted) {
                                        tv_music_name.setText(media0.getTitle());

                                    }
                                    startMediaMP3(1);
                                }
                            }
                        }
                        activity.toDoDB.update_setting("media_id", id + "", "table_radio");
                        break;
                    case "PLAY_ONE":
                        if (isAllFileNOExisted()) {
                            enableApply = false;
                            isplayingmp3 = false;
                            if ((isStarted) && (isNowPaper)) {
                                bt_music_play.setBackgroundResource(R.drawable.img_music_pause);
                            }
                            tv_music_name.setText("无法播放此文件");
                            break;
                        }
                        if (myListAdapter.mList.size() - 1 >= 0) {
                            if (id > (myListAdapter.mList.size() - 1)) {
                                id = 0;
                            }
                            Media media = myListAdapter.mList.get(id);
                            if (isStarted) {
                                tv_music_name.setText(media.getTitle());
                            }
                            if (checkIsMusicStyle(media)) {

                                startMediaMP3(1);

                            } else if (!checkIsMusicStyle(media)) {
                                startMediaMP4();
                            }
                        }
                        break;
                }
                break;
            case "MEDIADELETE":
                String strdele = event.getValues();
                String nowName = tv_music_name.getText().toString();
                if (nowName.equals(strdele)) {
                    // id ++;
                    if (id > (myListAdapter.mList.size() - 1)) {
                        id = 0;
                    } else if (id < 0) {
                        id = 0;
                    }
                    if (myListAdapter.mList.size() > 0) {
                        Media media = myListAdapter.mList.get(id);
                        tv_music_name.setText(media.getTitle());
                        if (checkIsMusicStyle(media)) {

                            startMediaMP3(1);
                        } else if (!checkIsMusicStyle(media)) {
                            startMediaMP4();
                        }

                    }
                }

                break;
            case "MEDIANULL":
                tv_music_name.setText("无歌曲");
                tv_media_nofile.setVisibility(View.VISIBLE);
                iv_media_nofile.setVisibility(View.VISIBLE);
                enableApply = false;
                sb_music_seekbar.setProgress(0);
                sb_music_seekbar.invalidate();
                tv_now_time.setText("00:00");
                tv_total_time.setText("00:00");
                if (isplayingmp3) {
                    pauseMediaMP3();
                    isplayingmp3 = false;

                    bt_music_play.setBackgroundResource(R.drawable.img_music_pause);
                }
                if (isplayingmp4) {
                    vv_meida_video.pause();
                    isplayingmp4 = false;
                    vv_meida_video.setVisibility(View.GONE);
                    ll_media_music.setVisibility(View.VISIBLE);

                }
                activity.toDoDB.update_setting("media_id", 0 + "", "table_radio");
                break;
            case "MEDIAADD":
                String str = tv_music_name.getText().toString();
                if (str.equals("无歌曲") && (myListAdapter.mList.size() > 0)) {
                    tv_music_name.setText("请选中歌曲！");
                    tv_media_nofile.setVisibility(View.GONE);
                    iv_media_nofile.setVisibility(View.GONE);
                    enableApply = false;
                } else if (str.equals("无法播放此文件") && (myListAdapter.mList.size() > 0)) {
                    tv_music_name.setText("请选中歌曲！");
                    tv_media_nofile.setVisibility(View.GONE);
                    iv_media_nofile.setVisibility(View.GONE);
                    enableApply = false;
                } else {
                    for (int i = 0; i < myListAdapter.mList.size(); i++) {
                        Media media = myListAdapter.mList.get(i);
                        if (media.getTitle().equals(str)) {
                            id = i;
                        }
                    }
                }
                break;
            case "MEDIAERR":
                if (id > (myListAdapter.mList.size() - 1)) {
                    id = 0;
                } else if (id < 0) {
                    id = 0;
                }
                try {

                    if (myListAdapter.mList.size() > 0) {
                        Log.e("media--error", id + "");
                        Media media = myListAdapter.mList.get(id);
                        tv_music_name.setText(media.getTitle());
                        if (checkIsMusicStyle(media)) {

                            startMediaMP3(1);
                        } else if (!checkIsMusicStyle(media)) {
                            startMediaMP4();
                        }

                    }
                } catch (Exception e) {
                }
                break;
            case "LISTENER":
                if (isNowPaper) {
                    try {
                        enableApply = true;
                        id = Integer.valueOf(event.getValues());
                        if (id > (myListAdapter.mList.size() - 1)) {
                            id = 0;
                        }
                        if (myListAdapter.mList.size() > 0) {
                            Media media = myListAdapter.mList.get(id);
                            tv_music_name.setText(media.getTitle());
                            if (checkIsMusicStyle(media)) {
                                startMediaMP3(1);
                            } else if (!checkIsMusicStyle(media)) {
                                startMediaMP4();
                            }

                        }
                        activity.toDoDB.update_setting("media_id", id + "", "table_radio");
                    } catch (Exception e) {
                    }
                }
                break;
            case "NOFILE":
                pauseMusicAndVideo();
                tv_music_name.setText("无法播放此文件");
                break;
            case "USB":
                if ("UNMOUNTED".equals(event.getValues())) {
                    if (fmMediaViewIsStart) {
                        pauseMusicAndVideo();
                        closeMediaMp4();
                        tv_music_name.setText("无法播放此文件");
                    }

                    if (pagerFlag == 3) {
                        threadFlag = false;
                        picList.clear();
                        picPosition = 0;
                        UIchange();
                    }

                }
                break;
            default:
                break;

        }
    }

    private void pauseMusicAndVideo() {
        if (isplayingmp3) {
            pauseMediaMP3();
        }
        if ((isNowPaper) && vv_meida_video.isPlaying()) {
            vv_meida_video.pause();
        }
    }

    private void clearMediaList() {
        FragmentMedia.myListAdapter.mList.clear();
        activity.toDoDB.deleteTable("table_media");
        mediaTreeSet.clear();

        FragmentMedia.myListAdapter.notifyDataSetChanged();

        if (myListAdapter.mList.size() == 0) {
            EventBus.getDefault().post(
                    new EventMediaRequest("MEDIANULL", "default", "default"));
        }
        pop_media.dismiss();
        isEnableDele = true;
    }

    // 隐藏音频界面相关
    private void hideMusicView() {
        ll_media_music.setVisibility(View.GONE);
    }

    // 隐藏视频界面相关
    private void hideVideoView() {
        closeMediaMp4();
    }

    // 隐藏图片浏览界面相关
    private void hidePicView() {
    }


    //图片浏览代码
    //图片事件监听
    public void picSetLintener() {

        //listview滚动事件监听
        lv_media_pic_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    loadBitmaps(mFirstVisibleItem, 10);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mFirstVisibleItem = firstVisibleItem;
                mVisibleItemCount = visibleItemCount;
                if (isFirstEnterThisActivity && visibleItemCount > 0) {
                    loadBitmaps(firstVisibleItem, 10);
                    isFirstEnterThisActivity = false;
                }
            }
        });

        //listview子条目点击切换图片
        lv_media_pic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                threadFlag = false;
                iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
                ll_media_pic_coller.setVisibility(View.GONE);
                ll_pic_pager.setVisibility(View.GONE);
                picPosition = i;
                UIchange();
            }
        });

        //小视图上一页
        iv_pic_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                if (-1 != picPosition && picPosition == 0) {
                    picPosition = picList.size() - 1;
                } else {
                    picPosition = picPosition - 1;
                }
                UIchange();
            }
        });

        //小视图下一页
        iv_pic_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                if (-1 != picPosition && picPosition == picList.size() - 1) {
                    picPosition = 0;
                } else {
                    picPosition = picPosition + 1;
                }
                UIchange();
            }
        });

        //大视图上一页
        iv_big_pic_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
                if (-1 != picPosition && picPosition == 0) {
                    picPosition = picList.size() - 1;
                } else {
                    picPosition = picPosition - 1;
                }
                BigUIchange();
            }
        });

        //大图下一页
        iv_big_pic_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
                if (-1 != picPosition && picPosition == picList.size() - 1) {
                    picPosition = 0;
                } else {
                    picPosition = picPosition + 1;
                }
                BigUIchange();
            }
        });

        //小图的单击事件
        isv_media_pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (ll_media_pic_coller.getVisibility() == View.VISIBLE) {
                            ll_media_pic_coller.setVisibility(View.GONE);
                            ll_pic_pager.setVisibility(View.GONE);
                        } else {
                            ll_media_pic_coller.setVisibility(View.VISIBLE);
                            ll_pic_pager.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        //小图片双击事件
        isv_media_pic.setOnDoubleClickListener(new ImageScaleView.OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                rl_media_bigpic.setVisibility(View.VISIBLE);
                rl_media_pic_top.setVisibility(View.GONE);
                threadFlag = false;
                iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
                BigUIchange();
            }
        });

        //大图片的单击事件
        isv_media_big_pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (ll_media_bigpic_coller.getVisibility() == View.VISIBLE) {
                            ll_media_bigpic_coller.setVisibility(View.GONE);
                            ll_big_pic_pager.setVisibility(View.GONE);
                        } else {
                            ll_media_bigpic_coller.setVisibility(View.VISIBLE);
                            ll_big_pic_pager.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        //大图片双击事件
        isv_media_big_pic.setOnDoubleClickListener(new ImageScaleView.OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                rl_media_pic_top.setVisibility(View.VISIBLE);
                rl_media_bigpic.setVisibility(View.GONE);
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                UIchange();
            }
        });

        //左旋图片
        iv_pic_rowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                Matrix matrix = new Matrix();
                // 设置旋转角度
                matrix.setRotate(-90);
                // 重新绘制Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                isv_media_pic.setImageBitmap(bitmap);
            }
        });

        //右旋图片
        iv_pic_rowright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                Matrix matrix = new Matrix();
                // 设置旋转角度
                matrix.setRotate(90);
                // 重新绘制Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                isv_media_pic.setImageBitmap(bitmap);
            }
        });

        //轮播的开始和暂停
        iv_pic_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread.isAlive()) {
                    if (threadFlag) {
                        threadFlag = false;
                        iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                    } else {
                        threadFlag = true;
                        iv_pic_start_pause.setImageResource(R.drawable.pic_media_pause);
                        isv_media_pic.setImageBitmap(ImageScaleUtil.ImageSrc(picList.get(picPosition).getFilePath(), isv_media_pic.getLayoutParams().width / 3, isv_media_pic.getLayoutParams().height / 3));
                    }
                } else {
                    thread.start();
                    threadFlag = true;
                    iv_pic_start_pause.setImageResource(R.drawable.pic_media_pause);
                    isv_media_pic.setImageBitmap(ImageScaleUtil.ImageSrc(picList.get(picPosition).getFilePath(), isv_media_pic.getLayoutParams().width / 3, isv_media_pic.getLayoutParams().height / 3));
                }
            }
        });

        //大图左旋图片
        iv_bigpic_rowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                Matrix matrix = new Matrix();
                // 设置旋转角度
                matrix.setRotate(-90);
                // 重新绘制Bitmap
                bigbitmap = Bitmap.createBitmap(bigbitmap, 0, 0, bigbitmap.getWidth(), bigbitmap.getHeight(), matrix, true);
                isv_media_big_pic.setImageBitmap(bigbitmap);
            }
        });

        //大图右旋图片
        iv_bigpic_rowright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadFlag = false;
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
                Matrix matrix = new Matrix();
                // 设置旋转角度
                matrix.setRotate(90);
                // 重新绘制Bitmap
                bigbitmap = Bitmap.createBitmap(bigbitmap, 0, 0, bigbitmap.getWidth(), bigbitmap.getHeight(), matrix, true);
                isv_media_big_pic.setImageBitmap(bigbitmap);
            }
        });

        //大图轮播的开始和暂停
        iv_bigpic_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread.isAlive()) {
                    if (threadFlag) {
                        threadFlag = false;
                        iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
                    } else {
                        threadFlag = true;
                        iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_pause);
                        isv_media_big_pic.setImageBitmap(ImageScaleUtil.ImageSrc(picList.get(picPosition).getFilePath(), isv_media_big_pic.getLayoutParams().width / 3, isv_media_big_pic.getLayoutParams().height / 3));
                    }
                } else {
                    thread.start();
                    threadFlag = true;
                    iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_pause);
                    isv_media_big_pic.setImageBitmap(ImageScaleUtil.ImageSrc(picList.get(picPosition).getFilePath(), isv_media_big_pic.getLayoutParams().width / 3, isv_media_big_pic.getLayoutParams().height / 3));
                }
            }
        });

    }

    //初始化视图
    public void initView() {
        picPosition = 0;
        tv_media_pic_name = (TextView) view.findViewById(R.id.tv_media_pic_name);
        iv_pic_rowleft = (ImageView) view.findViewById(R.id.iv_pic_rowleft);
        iv_pic_rowright = (ImageView) view.findViewById(R.id.iv_pic_rowright);
        iv_pic_start_pause = (ImageView) view.findViewById(R.id.iv_pic_start_pause);
        lv_media_pic_list = (ListView) view.findViewById(R.id.lv_media_pic_list);
        ll_media_pic_coller = (LinearLayout) view.findViewById(R.id.ll_media_pic_coller);
        iv_media_pic_picnull = (ImageView) view.findViewById(R.id.iv_media_pic_picnull);
        iv_bigpic_rowleft = (ImageView) view.findViewById(R.id.iv_bigpic_rowleft);
        iv_bigpic_start_pause = (ImageView) view.findViewById(R.id.iv_bigpic_start_pause);
        iv_bigpic_rowright = (ImageView) view.findViewById(R.id.iv_bigpic_rowright);
        rl_media_bigpic = (RelativeLayout) view.findViewById(R.id.rl_media_bigpic);
        rl_media_pic_top = (RelativeLayout) view.findViewById(R.id.rl_media_pic_top);
        ll_media_bigpic_coller = (LinearLayout) view.findViewById(R.id.ll_media_bigpic_coller);
        isv_media_pic = (ImageScaleView) view.findViewById(R.id.isv_media_pic);
        ll_pic_pager = (LinearLayout) view.findViewById(R.id.ll_pic_pager);
        iv_pic_pre = (ImageView) view.findViewById(R.id.iv_pic_pre);
        iv_pic_next = (ImageView) view.findViewById(R.id.iv_pic_next);
        isv_media_big_pic = (ImageScaleView) view.findViewById(R.id.isv_media_big_pic);
        ll_big_pic_pager = (LinearLayout) view.findViewById(R.id.ll_big_pic_pager);
        iv_big_pic_pre = (ImageView) view.findViewById(R.id.iv_big_pic_pre);
        iv_big_pic_next = (ImageView) view.findViewById(R.id.iv_big_pic_next);
        ll_media_pic_empty = (LinearLayout) view.findViewById(R.id.ll_media_pic_empty);

        UIchange();
    }

    //切换图片的页面变化
    public void UIchange() {
        if (null == picList || picList.isEmpty()) {
            tv_media_pic_name.setText("暂无图片");
            isv_media_pic.setVisibility(View.GONE);
            iv_media_pic_picnull.setVisibility(View.VISIBLE);
            ll_media_pic_coller.setVisibility(View.GONE);
            ll_pic_pager.setVisibility(View.GONE);
            ll_media_pic_empty.setVisibility(View.VISIBLE);
            if (null != pictureAdapter) {
                pictureAdapter.notifyDataSetChanged();
            } else {
                pictureAdapter = new PictureAdapter();
                lv_media_pic_list.setAdapter(pictureAdapter);
            }
            lv_media_pic_list.setVisibility(View.GONE);

        } else {
            lv_media_pic_list.setVisibility(View.VISIBLE);
            ll_media_pic_empty.setVisibility(View.GONE);
            bitmap = ImageScaleUtil.ImageSrc(picList.get(picPosition).getFilePath(), isv_media_pic.getLayoutParams().width / 3, isv_media_pic.getLayoutParams().height / 3);
            isv_media_pic.setVisibility(View.VISIBLE);
            isv_media_pic.setImageBitmap(bitmap);
            tv_media_pic_name.setText(picList.get(picPosition).getFileName());
            iv_media_pic_picnull.setVisibility(View.GONE);
            //暂停播放的状态
            if (threadFlag) {
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_pause);
            } else {
                iv_pic_start_pause.setImageResource(R.drawable.pic_media_start);
            }
            if (null != pictureAdapter) {
                pictureAdapter.notifyDataSetChanged();
            } else {
                pictureAdapter = new PictureAdapter();
                lv_media_pic_list.setAdapter(pictureAdapter);
            }
        }
    }

    //切换大图片页面的状态
    public void BigUIchange(){
        bigbitmap = ImageScaleUtil.ImageSrc(picList.get(picPosition).getFilePath(),isv_media_big_pic.getLayoutParams().width/3,isv_media_big_pic.getLayoutParams().height/3);
        isv_media_big_pic.setImageBitmap(bigbitmap);
        //暂停播放的状态
        if(threadFlag){
            iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_pause);
        }else {
            iv_bigpic_start_pause.setImageResource(R.drawable.pic_media_start);
        }

    }

    /**
     * 为GridView的初始化item加载图片
     */
    private void initLoadBitmaps(){
        try {
            int j = 0;
            if(picList.size()>10){
                j = 10;
            }else {
                j=picList.size();
            }
            for(int i=0;i<j;i++){
                String imagePath = picList.get(i).getFilePath();
                Bitmap bitmap = (Bitmap) LruCacheUtil.getInstance().get(imagePath);
                if (bitmap == null) {
                    bitmap=ImageScaleUtil.ImageSrc(picList.get(i).getFilePath(),60,60);
                    //将从SDCard读取的图片添加到LruCache中
                    LruCacheUtil.getInstance().put(imagePath, bitmap);
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 为GridView的item加载图片
     * @param firstVisibleItem GridView中可见的第一张图片的下标
     * @param visibleItemCount GridView中可见的图片的数量
     */
    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                String imagePath = picList.get(i).getFilePath();
                Bitmap bitmap = (Bitmap) LruCacheUtil.getInstance().get(imagePath);
                if (bitmap == null) {
                    bitmap=ImageScaleUtil.ImageSrc(picList.get(i).getFilePath(),60,60);
                    //将从SDCard读取的图片添加到LruCache中
                    LruCacheUtil.getInstance().put(imagePath, bitmap);
                }

            }
            pictureAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取文件dialog
    private void getPicDialog(){
        if(null == picDialog){
            picDialog = new MediaPicDialog(mediaActivity);
        }else{
            if(!picDialog.isShowing()){
                picDialog = new MediaPicDialog(mediaActivity);
            }
        }
        picDialog.setYesOnclickListener(new MediaPicDialog.OnYesOnclickListener() {
            @Override
            public void onYesClick(List<PicFileBean> collectPicList) {
                if(null==collectPicList||collectPicList.isEmpty()){
                    picDialog.dismiss();
                }else {
                    //添加选中展示图片
                    picList.addAll(collectPicList);
                    //创建set集合进行图片元素去重
                    Set<PicFileBean> set = new TreeSet<>(new OrderPicComparatorUtil());
                    set.addAll(picList);
                    picList.clear();
                    picList.addAll(set);
                    initLoadBitmaps();
                    UIchange();
                    picDialog.dismiss();
                }

            }
        });
        picDialog.setNoOnclickListener(new MediaPicDialog.OnNoOnclickListener() {
            @Override
            public void onNoClick() {
                picDialog.dismiss();
            }
        });
        picDialog.setCanceledOnTouchOutside(false);
        picDialog.show();
    }


    /**
     * 文件名：PictureAdapter
     * 作者：韩秋宇
     * 时间：2017/5/17
     * 功能描述：图片列表适配器
     */
    public class PictureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return picList.size();
        }

        @Override
        public Object getItem(int position) {
            return picList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mediaActivity,R.layout.lv_item_media_pic, null);
                holder.iv_pic_icon = (ImageView) convertView.findViewById(R.id.iv_item_pic_list);
                holder.tv_pic_name = (TextView) convertView.findViewById(R.id.tv_item_pic_name);
                holder.iv_pic_del = (ImageView) convertView.findViewById(R.id.iv_item_pic_del);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            setImageForImageView(picList.get(position).getFilePath(),holder.iv_pic_icon);
            holder.tv_pic_name.setText(picList.get(position).getFileName());
            holder.iv_pic_del.setImageResource(R.drawable.pic_media_del);

            //设置子目中删除按钮监听
            holder.iv_pic_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    picList.remove(position);
                    if(position>=picList.size()){
                        picPosition = position-1;
                        if(position-1<0){
                            picPosition = 0;
                        }
                    }else {
                        picPosition = position;
                    }
                    UIchange();
                }
            });

            //切换自条目背景
            if(position == picPosition){
                convertView.setBackgroundResource(R.drawable.pic_media_picbg);
            }else{
                convertView.setBackgroundResource(0);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv_pic_icon;
            TextView tv_pic_name;
            ImageView iv_pic_del;
        }
    }

    /**
     * 为ImageView设置图片(Image)
     * 1 从缓存中获取图片
     * 2 若图片不在缓存中则为其设置默认图片
     */
    private void setImageForImageView(String imagePath, ImageView imageView) {
        Bitmap bitmap = (Bitmap) LruCacheUtil.getInstance().get(imagePath);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.img_media_file);
        }
    }
}
