package com.hejia.tp_launcher_v3.android.tp_launcher_v3;

import com.hejia.eventbus.EventIATRequest;
import com.hejia.eventbus.EventIATResponse;
import com.hejia.speech.ApkInstaller;
import com.hejia.speech.JsonParser;
import com.hejia.tp_launcher_v3.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class SpeechIatActivity extends Activity{
    private RelativeLayout rlIatDialog;

    private static String TAG = SpeechIatActivity.class.getSimpleName();
    private static SpeechRecognizer mIat;// 语音听写对象
    private ApkInstaller mInstaller;// 语记安装助手类
    private String mEngineType;// 引擎类型
    private Intent intent;
    private Toast mToast;
    private StringBuffer strbuffer;
    private MainActivity activity;


    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_speech_iat);
        // 注册EventBus
        EventBus.getDefault().register(this);
        //初始化控件
        initView();
        //实例化相关对象
        mEngineType = SpeechConstant.TYPE_CLOUD;// 在线引擎
        mInstaller = new ApkInstaller(SpeechIatActivity.this);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        intent = new Intent();
        //监听方法
        startListener();
    }


    private void initView()
    {
        rlIatDialog =  (RelativeLayout) findViewById(R.id.rl_iat_dialog);
    }

    //实例化语音听写对象   该初始化需要提前进行，并且收到初始化成功后方可进行语音听写
    public void initIat(Context context)
    {
        activity = (MainActivity) context;
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        if(mIat==null)
            mIat = SpeechRecognizer.createRecognizer(activity, mInitListener);
    }

    int ret = 0; // 函数调用返回值
    private void startListener()
    {
        if(mIat!=null)
        {
            // 设置参数
            setParam();
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                EventBus.getDefault().post(
                        new EventIATResponse("IATOVER","default"));
                SpeechIatActivity.this.finish();
                Log.e("----", "mInitListener");
                showTip("听写失败,错误码：" + ret);
            } else {
                showTip("开始说话");
            }
        }
        else
        {
            showTip("mIat初始化失败！");
        }
    }



    //   初始化监听器。
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
                EventBus.getDefault().post(
                        new EventIATResponse("IATOVER","default"));
                SpeechIatActivity.this.finish();
            }
        }
    };


    //   听写监听器。
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
            EventBus.getDefault().post(
                    new EventIATResponse("IATOVER","default"));
            SpeechIatActivity.this.finish();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Log.e("----", "onEndOfSpeech");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            CompareResult(results);
            EventBus.getDefault().post(
                    new EventIATResponse("IATOVER","default"));
            SpeechIatActivity.this.finish();
        }


        //根据返回的音量大小更换背景图片
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if(volume<6)
            {
                rlIatDialog.setBackgroundResource(R.drawable.bg_speech_iat0);
            }
            else if((volume>=6)&&(volume<12))
            {
                rlIatDialog.setBackgroundResource(R.drawable.bg_speech_iat1);
            }
            else if((volume>=12)&&(volume<24))
            {
                rlIatDialog.setBackgroundResource(R.drawable.bg_speech_iat2);
            }
            else if((volume>=24)&&(volume<36))
            {
                rlIatDialog.setBackgroundResource(R.drawable.bg_speech_iat3);
            }
            else if(volume>=36)
            {
                rlIatDialog.setBackgroundResource(R.drawable.bg_speech_iat4);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void CompareResult(RecognizerResult results) {
        strbuffer = new StringBuffer();
        strbuffer.append(JsonParser.parseIatResult(results.getResultString()));
        if((strbuffer!=null)&&(strbuffer.length()>0))
            EventBus.getDefault().post(
                    new EventIATResponse("IATSTRING",strbuffer.toString()));

    }

    private void showTip(final String str) {
        if(mToast!=null)
        {
            mToast.setText(str);
            mToast.show();
        }
    }


     // 参数设置
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = "mandarin";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "6000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "3000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        if(mIat!=null) {
//	     	mIat.stopListening();
            mIat.cancel();
            mIat.destroy();
        }
        EventBus.getDefault().post(
                new EventIATResponse("IATOVER","default"));
        // 解除EventBus
        EventBus.getDefault().unregister(this);
        Log.e(TAG, "onDestroy");
    }

    // 实例化语音听写对象   该初始化需要提前进行，并且收到初始化成功后方可进行语音听写
    @Subscribe(threadMode = ThreadMode.Async, sticky=true)
    public void onEvent(EventIATRequest event) {
        switch (event.getKey()) {
            case "IATINIT":
                Log.e("EventIATRequest","init");
                Context activity = (MainActivity)event.getArg2();
                // 初始化识别无UI识别对象
                // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
                if(mIat==null)
                    mIat = SpeechRecognizer.createRecognizer(activity, mInitListener);
                break;
            default:
                break;
        }
    }
}
