package com.hejia.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hejia.tp_launcher_v3.R;
import com.hejia.receiver.SpeechReceiver;
import com.hejia.speech.ApkInstaller;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

/**
 * @author wwl
 *         语音合成
 *         默认在线语音合成，如当前网络无连接，自动更改为本地语音合成
 */
public class SpeechTtsService extends Service {
    private Context mcontext;

    // 语音合成对象
    private static SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 语记安装助手类
    ApkInstaller mInstaller;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    private String[] mCloudVoicersEntries;
    private String[] mCloudVoicersValue;

    // 默认发音人
    private String voicer = "xiaoyu";

    private Toast mToast;

    // 初始化结束标识
    private static boolean initTag = false;

    StartTtsThread mStartTtsThread;
    private static boolean isBusy = false;
    int priorityNow = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mToast != null) {
                        Bundle b = msg.getData();
                        mToast.setText(b.getString("content"));
                        mToast.show();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };


    @Override
    public void onCreate() {
        mcontext = SpeechTtsService.this;
        if (SpeechReceiver.mcontext != null) {
            // 云端发音人名称列表
            mCloudVoicersEntries = SpeechReceiver.mcontext.getResources().getStringArray(R.array.voicer_cloud_entries);
            mCloudVoicersValue = SpeechReceiver.mcontext.getResources().getStringArray(R.array.voicer_cloud_values);
            mToast = Toast.makeText(SpeechReceiver.mcontext, "", Toast.LENGTH_SHORT);
            mInstaller = new ApkInstaller(SpeechReceiver.mcontext);
            /**
             * 选择本地合成
             * 判断是否安装语记，未安装则跳转到提示安装页面
             */
            if (!SpeechUtility.getUtility().checkServiceInstalled()) {
                showTip("语记未安装!");
            }
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        int priority = 5;
        String content = "";
        if (intent != null) {
            priority = intent.getIntExtra("Priority", 0);
            content = intent.getStringExtra("SpeechString");
            if ((priority < priorityNow) && (isBusy)) {
                isBusy = false;//如果优先级比正在播报的优先级高，则立刻播报优先级高的，如果优先级低则等待当前执行结束后再播报
            }
            //语音播报线程
            if (SpeechUtility.getUtility().checkServiceInstalled()) {
                mStartTtsThread = new StartTtsThread(content, priority);
                mStartTtsThread.start();
            } else {
                showTip("语记未安装!");
            }

        }


        return super.onStartCommand(intent, flags, startId);
    }

    //初始化语音合成对象
    public void InitTtsService() {
        mTts = SpeechSynthesizer.createSynthesizer(SpeechReceiver.mcontext, mTtsInitListener);


    }

    public class StartTtsThread extends Thread {
        private String mcontent;
        private int mpriority;

        public StartTtsThread(String content, int priority) {
            mcontent = content;
            mpriority = priority;
        }

        @Override
        public void run() {
            super.run();

            StartTtsPlay(mcontent, mpriority);
            try {
                Thread.sleep(50); // sleep ms
            } catch (Exception e) {
            }
        }
    }

    //开始播报
    private synchronized void StartTtsPlay(String content, int priority) {
        boolean isNetConnect;
        int code;

//        setPresonSelect(mEngineType);
        // 设置参数
        setParam();

        if (initTag) {

            isBusy = true;
            priorityNow = priority;
            code = mTts.startSpeaking(content, mTtsListener);

            if (code != ErrorCode.SUCCESS) {
                if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                    showTip("语记未安装!");
                    //未安装则跳转到提示安装页面
//	                mInstaller.install();
                } else {
                    showTip("语音合成失败，错误码: " + code);
                }
            }

            while (true) {
                isNetConnect = checkNetwork();
                if ((mEngineType.equals(SpeechConstant.TYPE_CLOUD)) && (!isNetConnect)) {
                    isBusy = false;
                    mEngineType = SpeechConstant.TYPE_LOCAL;
                }
                if (!isBusy) {
                    return;
                }
            }

        }
//      /** 
//       * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//       * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//      */
//      String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//      int code = mTts.synthesizeToUri(text, path, mTtsListener);


    }

    //提示
    private void showTip(final String str) {

        Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("content", str);
        msg.what = 0;
        msg.setData(b);
        handler.sendMessage(msg);
    }

    /**
     * 初始化监听
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("SpeechTtsService-->mTtsInitListener", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码:" + code);
            } else {
                initTag = true;
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    /**
     * 合成回调监听
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            isBusy = false;//播放完成结束播放线程
            if (error == null) {
                showTip("播放完成");

            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //  if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //      String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //      Log.d(TAG, "session id =" + sid);
            //  }
        }
    };


    //设置引擎类型
    private void setEngineType(int index) {
        switch (index) {
            case 0:
                mEngineType = SpeechConstant.TYPE_CLOUD;
                break;
            case 1:
                mEngineType = SpeechConstant.TYPE_LOCAL;
                /**
                 * 选择本地合成
                 * 判断是否安装语记,未安装则跳转到提示安装页面
                 */
                if (!SpeechUtility.getUtility().checkServiceInstalled()) {
                    mInstaller.install();
                }
                break;

            default:
                break;
        }
    }


    private void setParam() {

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 发音人选择。
     */
    private void setPresonSelect(int index) {
        if (index > (mCloudVoicersValue.length - 1)) {
            showTip("所选发音人已超出设定范围");
            return;
        }
        switch (index) {
            case 0:
                mEngineType = SpeechConstant.TYPE_CLOUD;
                voicer = mCloudVoicersValue[index];
                break;
            case 1:
                mEngineType = SpeechConstant.TYPE_LOCAL;
                if (!SpeechUtility.getUtility().checkServiceInstalled()) {
                    mInstaller.install();
                } else {
                    SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_TTS);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTts.destroy();
    }

    //检查当前网络连接情况
    private boolean checkNetwork() {

        // 实例化ConnectivityManager

        ConnectivityManager manager = (ConnectivityManager) this

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获得当前网络信息
        NetworkInfo info = manager.getActiveNetworkInfo();

        // 判断是否连接

        if (info == null || !info.isConnected()) {
            return false;
        }
        return true;
    }


}
