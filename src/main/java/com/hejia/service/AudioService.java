package com.hejia.service;

public class AudioService {
    public native int RadioAudioInit();

    public native int AudioSetSource(int AudioSource);

    public native int AudioSetVolume(int AudioVolLev);

    public native int AudioSetMute(int MuteFlag);// 0 MuteOff 1 MuteOn

    private int tag_InitAudio = -1;
    private int tag_Volume = -1;
    private int tag_Source = -1;

    static {
        System.loadLibrary("tef663x_audio_jni");
    }

    // 初始化
    public void initAudio() {
        tag_InitAudio = RadioAudioInit();
        if (tag_InitAudio >= 0) {
        } else {
        }
    }

    // 音量调节
    public void setVolume(int volume) {
        if (volume >= 0 && volume <= 16) {
            tag_Volume = AudioSetVolume(volume);
            if (tag_Volume >= 0) {
            } else {
            }
        }

    }

    // 通道选择
    public void setSource(int source) {
        switch (source) {
            // Radio
            case 0:
                tag_Source = AudioSetSource(source);
                break;
            // BT
            case 1:
                tag_Source = AudioSetSource(source);
                break;
            // CPU
            case 2:
                tag_Source = AudioSetSource(source);
                break;
            default:
                break;
        }
        if (tag_Source >= 0) {
            // 音源初始化成功
        } else {
            // 音源初始化失败
        }
    }
}
