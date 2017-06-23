package com.hejia.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.serialport.DataConvert;
import com.hejia.serialport.SendDataAnalysis;

public class SerialPortWriteService extends Service {

    private SerialService mSerialService = null;
    private WriteOneHundredThread mWriteOneHundredThread = null;
    private WriteOneThousandThread mWriteOneThousandThread = null;
    private SendDataAnalysis mSendDataAnalysis = null;

    @Override
    public IBinder onBind(Intent arg0) {

        return new SerialPortWriteBinder();
    }


    public class SerialPortWriteBinder extends Binder {
        public SerialPortWriteService getSerialPortService() {
            return new SerialPortWriteService();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    // 打开串口   打开成功返回true，失败返回false。
    public boolean openSeralPort() {
        mSerialService = new SerialService();
        mWriteOneHundredThread = new WriteOneHundredThread();
        mWriteOneThousandThread = new WriteOneThousandThread();
        mSendDataAnalysis = new SendDataAnalysis();
        if (MainActivity.fdData == 0) {
            MainActivity.fdData = mSerialService.open(MainActivity.PATH, 115200);
        }
        if (MainActivity.fdData < 0) {
            Log.e("---SerialPortOpen---", "open error!");
            return false;
        } else {
            //mWriteOneHundredThread.start();//开启发送频度为100ms语句的线程
            //mWriteOneThousandThread.start();//开启发送频度为1000ms语句的线程
            return true;
        }
    }

    // 关闭串口   打开成功返回true，失败返回false。
    private boolean closeSerialPort() {
        if (!(MainActivity.fdData < 0)) {
            mSerialService.close(MainActivity.fdData);
        }
        return true;
    }


    // 该线程负责发送频度为1000ms的语句
    private class WriteOneThousandThread extends Thread {


        @Override
        public void run() {
            super.run();
            while (true) {

                try {


                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }


        }
    }

    // 该线程负责发送频度为100ms的语句
    private class WriteOneHundredThread extends Thread {
        private int heartbeatNum = 0;
        private byte[] dataSendHeartbeat = new byte[10];
        private byte[] dataSendPowerManager = new byte[10];
        private DataConvert mDataConvert = new DataConvert();

        @Override
        public void run() {
            super.run();
            while (true) {

                try {
                    // 发送心跳数据
                    dataSendHeartbeat = mSendDataAnalysis.processSendHeartbeatData(heartbeatNum);
//                    String a = mDataConvert.getHexString(dataSendHeartbeat);
                    mSerialService.send(MainActivity.fdData, dataSendHeartbeat);
                    if (heartbeatNum < 255) {
                        heartbeatNum++;
                    } else {
                        heartbeatNum = 0;
                    }

                    // 发送温度信息数据

                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
