package com.hejia.service;

import com.hejia.serialport.DataConvert;
import com.hejia.serialport.ReceiveDataAnalysis;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import static com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity.*;

public class SerialPortReadService extends Service {

	private SerialService mSerialService = null;

	private ReadSerialPortThread mReadSerialPortThread = null;
	private ReceiveDataAnalysis mReceiveDataAnalysis;
	private DataConvert mDataConvert;
	public static int receBufferSize = 0;

	@Override
	public IBinder onBind(Intent arg0) {

		return new SerialPortReadBinder();
	}

	public class SerialPortReadBinder extends Binder {
		public SerialPortReadService getSerialPortService() {
			return new SerialPortReadService();
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// ifSuccess = openSeralPort();
		// //串口打开成功，开启接受线程
		// if(ifSuccess)
		// {
		// mReadSerialPortThread.start();
		// }
	}

	// 打开串口 打开成功返回true，失败返回false。
	public boolean openSeralPort() {
		mDataConvert = new DataConvert();
		mSerialService = new SerialService();
		mReceiveDataAnalysis = ReceiveDataAnalysis.getInstance();
		mReadSerialPortThread = new ReadSerialPortThread();
		fdData = mSerialService.open(PATH, 115200);
		if (fdData < 0) {
			Log.e("---SerialPortOpen---", "open error!");
			return false;
		} else {
//			mReadSerialPortThread.start();// 开启接受线程
			return true;
		}
	}

	// 关闭串口 打开成功返回true，失败返回false。
	private boolean closeSerialPort() {
		if (!(fdData < 0)) {
			mSerialService.close(fdData);
		}
		return true;
	}

	private class ReadSerialPortThread extends Thread {

		private final int num = 1024;
		byte[] buffer;
		StringBuffer strBuf = new StringBuffer(1124);
		int startIndex = 0;
		int endIndex = 0;
		int i = 0;
		int nextStartIndex = 0;
		StringBuffer strtemp;

		@Override
		public void run() {
			while (true) {
				int startNum = 0;
				buffer = new byte[num];
				strtemp = new StringBuffer();
				buffer = mSerialService.recvByte(fdData, num); // 串口接收数据

				try {
					if(receBufferSize>0)
					{
						strtemp = mDataConvert.getHexStringBuffer(buffer);
						strtemp.delete(receBufferSize * 2, strtemp.length());
						startNum = count(strtemp, ("7e"));// 出现标志域(头)的次数
						strBuf.append(strtemp);
						strBuf.delete(0, strBuf.indexOf("7e"));

						for (i = 0; i < startNum; i++) {
							startIndex = strBuf.indexOf("7e");

							nextStartIndex = strBuf.indexOf("7e", strBuf.indexOf("7e") + 1);
							if ((nextStartIndex >= 2)&&(!strBuf.substring(nextStartIndex - 2, nextStartIndex).equals("7f")))// 如果下一个起始位的前一个不是前一个的结束位，继续获取下一个
							{
								nextStartIndex = strBuf.indexOf("7e", nextStartIndex + 1);
							}
							if (nextStartIndex < 0) {
								if ((strBuf.length() >= 2)&&(strBuf.substring(strBuf.length() - 2, strBuf.length()).equals("7f")))
									endIndex = strBuf.length() - 2;
								else {
									endIndex = strBuf.lastIndexOf("7f");
								}
							} else {
								endIndex = nextStartIndex - 2;
							}

							if ((startIndex >= 0) && (endIndex > 0)) {
								String strOne = strBuf.substring(startIndex, endIndex + 2);// 截取单条语句
								// if(strOne.contains("1881eff3"))
								// {
								// Log.e("--串口数据--", "缺失！");
								// }
								if (mReceiveDataAnalysis != null) {
									boolean value = mReceiveDataAnalysis.proceData(strOne);
									if (!value) {
										Log.e("--check_sum--", "校验错误！--" + strOne);
									}
								}

								strBuf = strBuf.delete(0, endIndex + 2);
							}
						}
					}

				} catch (Exception e2) {
					e2.printStackTrace();
				}

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}

		}
	}

	public static int count(StringBuffer text, String sub) {
		int count = 0, start = 0;
		while ((start = text.indexOf(sub, start)) >= 0) {
			start += sub.length();
			count++;
		}
		return count;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
