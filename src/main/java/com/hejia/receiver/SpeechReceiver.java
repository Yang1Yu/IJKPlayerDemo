package com.hejia.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hejia.service.SpeechTtsService;


public class SpeechReceiver extends BroadcastReceiver{
    
    private SpeechTtsService mSpeechTts;
    public static Context mcontext;
    public SpeechReceiver(Context context){
    	mcontext = context;
    	mSpeechTts = new SpeechTtsService();
    	mSpeechTts.InitTtsService();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
                
            case "SPEECH_TTS"://语音合成
                String str = intent.getStringExtra("SpeechString");
                int priority = intent.getIntExtra("Priority", 0);
		        Intent intentTts = new Intent(mcontext,SpeechTtsService.class);
		        intentTts.putExtra("SpeechString", str);
		        intentTts.putExtra("Priority", priority);
		        mcontext.startService(intentTts);
                break;

            default:
                break;
        }
        
    }
    

}
