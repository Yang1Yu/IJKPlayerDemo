package com.hejia.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.hejia.eventbus.EventBTRequest;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.bean.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressLint("NewApi")
public class BluetoothService extends Service {
	// 蓝牙供电
	static {
		System.loadLibrary("gpio_set");
	}

	private static native int BtPowerOn();

	private static native int BtReset();

	private int fd_bluetooth = -1;
	private static final String DEV = "/dev/btdevice";// 1
	// private stati myThread;
	private MyRunnable myRunnable;

	SerialService serialService = new SerialService();
	// 设备地址
	private String device_address = "";
	// 蓝牙是否连接
	private int hfconn;
	// 查找设备名
	private int grdn;
	// 设备名
	public String device_name = "";
	// 蓝牙无响应相关
	private int btNullCount = 0;
	private int btReceive = 0;
	private boolean tagBtReceive = false;
	private int gver = -1;
	// TODO 网络是否可用
	// private Intent netIsConnIntent;
	// private Bundle netIsConnBundle;
	// 多媒体是否连接 为boolean_a2dpdisc0标志位做判断
	private int a2dpconn0;
	// TODO 待验证
	private int a2dpconn1;

	// 未开启调用电话簿授权
	private int pbcconn1;
	// 开启调用电话簿授权
	private int pbcconn0;

	private String pbcTime;//不为空表示通话记录 为空表示电话簿
	// 判断是否为配对设备
	private int pair;

	// 蓝牙电话簿相关
	// 判断是否有电话簿内容进入
	private int pbcinfo;
	private String pbc_Name;
	private String pbc_Number;
	private String temp_Pbc_Name_And_Number;
	//	private String pbc_Name_And_Number;
	private String history_name;
	// 判断电话簿是否结束
	private int pbcend;
	// 电话簿结束与通话记录结束标志相同 另加标志位作区分 以防死循环 是否可以发送获取通话记录
	private boolean tag_history = true;

	// 对端手机远离设备相关
	private int avrcpdisc9;
	private int hfconn4;
	private boolean tag_Avrcpdisc9 = false;
	private boolean tag_Hfconn4 = false;

	// 蓝牙音乐相关
	// 判断是否正在播放
	private int playstatus;
	// 判断歌曲名
	private int avrcptitle;
	// 判断歌手
	private int avrcpartist;
	// 判断歌曲时长
	private int avrcptime;
	// 判断当前播放位置
	private int avrcppos;

	public String bt_service_music_Title = "";// 歌名
	public String bt_service_music_Artist = "";// 歌手

	public int bt_service_music_Pos = 0;// 播放位置
	public int bt_service_music_Time = 10000000;// 歌曲时长
	public String bt_service_temp_music_pos;
	public String bt_service_temp_music_time;
	public String bt_service_music_isplay = "0";// 是否正在播放歌曲

	// 判断是否主动播出电话
	private int hfccin;
	public String str_hfccin_number;
	private Iterator<User> userSetIterator;

	// 拨出电话状态
	private int hfstat;
	private String str_hfstat = "9";
	private String hfccin_status;
	// 来电姓名显示
	private User user;

	// 是否有电话拨入的tag
	public int tag_CID = 1;

	// 打入电话的电话号码
	public String HFCLIP_NUMBER;

	// 配对码
	private String str_SSPPIN;

	// 设置升级
	private Intent popUpgradeIntent;
	public static boolean startUpgrade = false;

	private StringBuffer strBuffer;
	// private String bd;// 远程设备地址

	// 判断设备是否与蓝牙断开连接
	private int pbcdisc0 = -1;// 电话簿与蓝牙断开连接
	private int hfdisc0 = -1;// 通话与蓝牙断开连接
	private int a2dpdisc0 = -1;// 多媒体与蓝牙断开连接
	private boolean boolean_pbcdisc0 = false;
	private boolean boolean_hfdisc0 = false;
	private boolean boolean_a2dpdisc0 = false;
	// private boolean boolean_avrcpdisc0;


	// 与主界面连接绑定的接口
	@Override
	public IBinder onBind(Intent arg0) {

		return new MyBluetoothBinder();
	}

	public class MyBluetoothBinder extends Binder {
		public BluetoothService getBluetoothService() {
			return BluetoothService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// timer = new Timer();
		myRunnable = new MyRunnable();
		new Thread(myRunnable).start();
		// 设置升级
		popUpgradeIntent = new Intent();
		// TODO 网络是否连接
		// netIsConnIntent = new Intent();
		// netIsConnBundle = new Bundle();
		BtPowerOn();
	}

	// 开启蓝牙
	public void openBluetooth() {
		fd_bluetooth = serialService.open(DEV, 115200);
		if (fd_bluetooth < 0) {
			// serialService.send(fd_bluetooth, "AT+B GPRL\r\n");
			Log.e("---BLUETOOTH---", "open dev error");
			return;
		}
		// 打开蓝牙后默认可连接但不可被搜索
		serialService.send(fd_bluetooth, "AT+B SCAN 2\r\n");
		send_Command("AT+B SMCE 1\r\n");
	}

	// 设置蓝牙设备名
	public void updateBTName(String name) {
		if (!name.contains("'") && !name.isEmpty()) {
			send_Command("AT+B SLDN " + name + "\r\n");
		}
	}

	// 调用电话簿
	public void openPhoneBook() {
		if (pbcconn1 >= 0) {
			// 开启电话簿权限
			serialService.send(fd_bluetooth, "AT+B PBCCONN " + device_address + "\r\n");
		} else if (pbcconn0 >= 0) {
			// 调取手机电话簿
			serialService.send(fd_bluetooth, "AT+B PBCPULLPB 1,1,65535,0\r\n");
			// 调取SIM卡电话簿
			// serialService.send(fd_bluetooth, "AT+B PBCPULLPB
			// 2,1,65535,0\r\n");
			// 更改标志位 为boolean_pbcconndisc0标志位作判断
			boolean_pbcdisc0 = false;
		}
	}

	// 关闭蓝牙
	public void closeBluetooth() {
		if (fd_bluetooth > 0) {
			serialService.close(fd_bluetooth);
			fd_bluetooth = -1;
			return;
		}
	}

	// 配对设备相关
	// 删除配对信息
	public void deletePairDevice(String pairDeviceBD) {
		serialService.send(fd_bluetooth, "AT+B DPRD " + pairDeviceBD + "\r\n");
	}

	// 删除配对设备成功
	private void sendSuccDeletePairDeviceBroadcast() {
	}

	// 判断是否为正在连接的设备
	public boolean isConnPairDeviceNow(String pairDeviceBD) {
		if (device_address == null) {
			return false;
		}
		if (pairDeviceBD.equals(device_address)) {
			return true;
		} else {
			return false;
		}
	}

	// 删除所有配对信息
	public void deleteAllPairDevice() {
		serialService.send(fd_bluetooth, "AT+B DPRD 000000000000\r\n");
	}

	// 蓝牙音乐相关方法
	// 上一曲
	public void btService_Music_Forward() {
		serialService.send(fd_bluetooth, "AT+B AVRCPBACKWARD\r\n");
	}

	// 下一曲
	public void btService_Music_Next() {
		serialService.send(fd_bluetooth, "AT+B AVRCPFORWARD\r\n");
	}

	// 播放
	public void btService_Music_Play() {
		if ("1".equals(bt_service_music_isplay)) {
			// 如正在播放则停止
			serialService.send(fd_bluetooth, "AT+B AVRCPPAUSE\r\n");
		} else {
			serialService.send(fd_bluetooth, "AT+B AVRCPPLAY\r\n");
		}

	}

	// 停止
	public void btService_Music_Stop() {

		serialService.send(fd_bluetooth, "AT+B AVRCPPAUSE\r\n");

	}

	// 增大音量
	public void btService_Music_Vol_Add() {
		serialService.send(fd_bluetooth, "AT+B AVRCPVOLUMEUP\r\n");
	}

	// 减小音量
	public void btService_Music_Vol_Sub() {
		serialService.send(fd_bluetooth, "AT+B AVRCPVOLUMEDOWN\r\n");
	}

	// 给蓝牙发送命令
	public void send_Command(String command) {
		serialService.send(fd_bluetooth, command);
	}

	// 获得当前拨入的电话号码
	public String get_CID() {
		return HFCLIP_NUMBER;
	}

	// TODO 发送网络已连接
	// private void sendNetIsConnBroadcast(String netIsConn) {
	// netIsConnBundle.putString("NETISCONN", netIsConn);
	// netIsConnIntent.putExtras(netIsConnBundle);
	// netIsConnIntent.setAction("NETISCONNBROADCAST");
	// sendBroadcast(netIsConnIntent);
	// }

	// 获得主动播出号码（及呼入号码）
	private void sendHFCCINBROADCAST() {
		EventBus.getDefault().post(new EventBTRequest("HFCCIN", str_hfstat.trim(), str_hfccin_number.trim()));
	}

	// 有电话呼入时要发送的广播
	private void sendCIDBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("CID", HFCLIP_NUMBER, "default"));
	}

	// 有设备要与蓝牙相连的广播
	private void sendSSPPINBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("SSPPIN", str_SSPPIN, "default"));
	}

	// 接受或拒绝配对
	public void acceptOrReject(int key) {
		switch (key) {
			case 0:
				// 拒绝配对
				send_Command("AT+B SMCR 0," + device_address + "\r\n");
				break;
			case 1:
				// 接受配对
				send_Command("AT+B SMCR 1," + device_address + "\r\n");
				break;
		}
	}

	// 蓝牙音乐相关
	// 发送歌曲名 歌手 歌曲时长的广播
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void sendAVRCPINFOBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("BTMUSICINFO", bt_service_music_Title, bt_service_music_Artist, bt_service_music_isplay, bt_service_music_Time));
	}

	// 发送蓝牙音乐当前播放位置
	private void sendAVRCPPOSBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("BTMUSICPOS", bt_service_music_isplay, bt_service_music_Pos));
	}

	// 发送电话簿姓名 电话号码
	private void sendPBCInfoBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("PB", pbc_Name, pbc_Number));
	}

	private int temp_history_index = -1;

	// 发送通话记录
	private void sendHistoryBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("HISTORY", history_name, pbc_Number));
	}

	// 关闭popwindow广播
	private void sendClosePopBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("CIDCUT", "default", "default"));
		// history_all();
	}

	// 调取通话记录 全
	public void history_all() {
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 1,5,65535,0\r\n");
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 2,5,65535,0\r\n");
	}

	// 调取通话记录 呼入
	public void history_incoming() {
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 1,2,65535,0\r\n");
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 2,2,65535,0\r\n");
	}

	// 调取通话记录 呼出
	public void history_outgoing() {
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 1,3,65535,0\r\n");
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 2,3,65535,0\r\n");
	}

	// 调取通话记录 未接
	public void history_missed() {
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 1,4,65535,0\r\n");
		serialService.send(fd_bluetooth, "AT+B PBCPULLPB 2,4,65535,0\r\n");
	}

	// 发送设备名
	private void sendDeviceNameBroadcast() {
		EventBus.getDefault().post(new EventBTRequest("BTCONN", device_name, "default"));
	}

	// 关闭拨号界面显示信息
	private void sendCloseDailInfo() {
		EventBus.getDefault().post(new EventBTRequest("HFCCINCUT", "default", "default"));
	}

	// 设备与蓝牙断开连接
	private void sendDisconnectBroadcast(String pop) {
		if (pop == null) {
			pop = "pop";
		}
		EventBus.getDefault().post(new EventBTRequest("BTDISCONN", pop, "default"));
	}

	// 蓝牙主动断开与设备连接
	public void duankailianjie() {
		serialService.send(fd_bluetooth, "AT+B HFDISC\r\n");
		serialService.send(fd_bluetooth, "AT+B A2DPDISC\r\n");
		serialService.send(fd_bluetooth, "AT+B PBCDISC " + device_address + "\r\n");
		serialService.send(fd_bluetooth, "AT+B AVRVPDISC " + device_address + "\r\n");
	}

	// 发送蓝牙音乐状态广播
	public void sendBTMusicStatus() {
		EventBus.getDefault().post(new EventBTRequest("BTMUSICSTATUS", bt_service_music_isplay, "default"));
	}

	// 发送设置升级广播
	public void sendPopUpgrade() {
		popUpgradeIntent.setAction("SHOWUPGRADEPOPBROADCAST");
		sendBroadcast(popUpgradeIntent);
	}

	private class MyRunnable implements Runnable {
		@Override
		public void run() {
			// super.run();
			// TODO 循环执行的代码
			while (true) {
				try {
					// 每隔100ms执行一次
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				strBuffer = new StringBuffer(serialService.recv(fd_bluetooth, 100));
				Log.i("chanyeol", "bt ---------> " + strBuffer.toString());
				listBTPrase = parseBTCommand(strBuffer);
				if (listBTPrase.size() >= 1) {
					if ("null".equals(listBTPrase.get(0))) {
						// 接收到的为空符串
						btNullCount++;
					} else if ("err".equals(listBTPrase.get(0))) {
						// 接收到的为非法字符串
					} else if ("succ".equals(listBTPrase.get(0))) {
						// 接收到的为规范字符串
						if (listBTPrase.size() >= 2) {
							switch (listBTPrase.get(1)) {
								//判断接收到的命令
								case "GVER":
									// 接收到版本信息相关
									tagBtReceive = false;
									btReceive = 0;
									btNullCount = 0;
									break;
								case "HFCONN":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 蓝牙连接成功
											// 更改标志位 为boolean_hfdisc0标志位做判断
											boolean_hfdisc0 = false;
											// 复原标志位，防止远离设备中途返回
											tag_Hfconn4 = false;
											tag_Avrcpdisc9 = false;
											// 如有设备连接
											// 截取远程连接设备地址
											device_address = listBTPrase.get(3);
											serialService.send(fd_bluetooth, "AT+B GRDN " + device_address + "\r\n");
											// 连接手机电话簿
											serialService.send(fd_bluetooth, "AT+B PBCCONN " + device_address + "\r\n");
											// 开启多媒体权限
											serialService.send(fd_bluetooth, "AT+B A2DPCONN " + device_address + "\r\n");
											// 设置电话簿解析方式
											serialService.send(fd_bluetooth, "AT+B PBCSETPARSE 1\r\n");
										} else if ("4".equals(listBTPrase.get(2))) {
											// 对端手机远离设备
											tag_Hfconn4 = true;
										} else {
											// 更改标志位 为boolean_hfdisc0标志位做判断
											boolean_hfdisc0 = true;
										}
									}
									break;
								case "GRDN":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 查找设备名成功
											if (listBTPrase.size() >= 5) {
												device_name = listBTPrase.get(4);
											} else {
												device_name = "未获取到";
											}
											// 将此次配对设备传入数据库
											MainActivity.toDoDB.savePairDevice(device_address, device_name.trim());
											sendDeviceNameBroadcast();
										} else {
											// TODO 查找设备名失败
										}
									}
									break;
								case "PBCCONN":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 连接电话簿成功
											serialService.send(fd_bluetooth, "AT+B PBCPULLPB 1,1,65535,0\r\n");
										} else {
											// 连接电话簿失败
											boolean_pbcdisc0 = true;
										}
									}
									break;
								case "PAIR":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 首次配对成功
											if (listBTPrase.size() >= 4) {
												device_address = listBTPrase.get(3);
											}
										}
									}
									break;
								case "SSPPIN":
									// 配对码
									if (listBTPrase.size() >= 5) {
										str_SSPPIN = listBTPrase.get(2);
										device_address = listBTPrase.get(4);
										// 发送配对码
										sendSSPPINBroadcast();
									}
									break;
								case "PBCPARSEDATAIND":
									if (listBTPrase.size() >= 7) {
										// 获取电话簿信息
										pbcTime = listBTPrase.get(5);
										pbc_Number = listBTPrase.get(4);
										// 筛选出电话簿（非通话记录）
										if ("".equals(pbcTime)) {
											//电话簿
											pbc_Name = listBTPrase.get(6);
											sendPBCInfoBroadcast();
										} else {
											//通话记录
											history_name = listBTPrase.get(6);
											sendHistoryBroadcast();
										}
									}
									break;
								case "HFCCIN":
									// 判断是否为主动拨出电话(包含打入电话 方便为拨号界面右侧显示传值)
									if (listBTPrase.size() >= 9) {
										hfccin_status = listBTPrase.get(2);
										str_hfccin_number = listBTPrase.get(8).trim();
										HFCLIP_NUMBER = listBTPrase.get(8).trim();
										userSetIterator = MainActivity.user_TreeSet.iterator();
										while (userSetIterator.hasNext()) {
											user = userSetIterator.next();
											if (user.getNumber().equals(str_hfccin_number)) {
												str_hfccin_number = user.getName();
												HFCLIP_NUMBER = user.getName();
												break;
											}
										}
										if ("3".equals(hfccin_status) || "5".equals(str_hfstat)) {
											sendHFCCINBROADCAST();
										}
										if ("6".equals(str_hfstat)) {
											sendHFCCINBROADCAST();
											sendClosePopBroadcast();
										}
										if ("4".equals(str_hfstat) && tag_CID == 1) {
											// 弹出有电话呼入对话框
											// 有电话呼入 发送广播
											sendCIDBroadcast();
											sendHFCCINBROADCAST();
											// 更改tag
											tag_CID = 0;
										}

									}
									break;
								case "HFSTAT":
									// 判断拨号状态
									if (listBTPrase.size() >= 3) {
										str_hfstat = listBTPrase.get(2);
										if ("3".equals(str_hfstat)) {
											sendClosePopBroadcast();
											sendCloseDailInfo();
										}
									}
									break;
								// 蓝牙音乐相关
								case "PLAYSTATUS":
									// 音乐正在播放
									if (listBTPrase.size() >= 3) {
										bt_service_music_isplay = listBTPrase.get(2);// 1为正在播放0为停止2为暂停
										sendBTMusicStatus();
										sendAVRCPINFOBroadcast();
									}
									break;
								case "AVRCPTITLE":
									// 判断歌曲名
									bt_service_music_Title = listBTPrase.get(2);
									break;
								case "AVRCPARTIST":
									// 获取歌手
									if (listBTPrase.size() >= 3) {
										bt_service_music_Artist = listBTPrase.get(2);
									}
									break;
								case "AVRCPTIME":
									// 判断歌曲时长
									if (listBTPrase.size() >= 3) {
										if (listBTPrase.get(2).trim() != null && listBTPrase.get(2).trim().length() > 0) {
											bt_service_temp_music_time = listBTPrase.get(2).trim();
											if (!bt_service_temp_music_time.isEmpty()) {
												try {
													if (Long.parseLong(bt_service_temp_music_time) <= 2147483647) {
														bt_service_music_Time = Integer.parseInt(bt_service_temp_music_time);
													}
												} catch (NumberFormatException e) {
													Log.i("chanyeol","bt------>MusicTotalTime NumberFormatException");
												}

											}
										}
										sendAVRCPINFOBroadcast();
									}
									break;
								case "AVRCPPOS":
									// 判断当前播放位置
									if (listBTPrase.size() >= 3) {
										bt_service_temp_music_pos = listBTPrase.get(2).trim();
										if (!bt_service_temp_music_pos.isEmpty()) {
											try {
												if (Long.parseLong(bt_service_temp_music_pos) <= 2147483647) {
													bt_service_music_Pos = Integer.parseInt(bt_service_temp_music_pos);
												}
											}catch (NumberFormatException e){
												Log.i("chanyeol","bt------>MusicPos NumberFormatException");
											}

										}
										sendAVRCPINFOBroadcast();
										sendAVRCPPOSBroadcast();
									}
									break;
								case "A2DPCONN":
									// 判断多媒体是否连接
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 连接成功
											boolean_a2dpdisc0 = false;
											// 复原标志位，防止远离设备中途返回
											tag_Hfconn4 = false;
											tag_Avrcpdisc9 = false;
										} else {
											// 连接失败
											boolean_a2dpdisc0 = true;
											// 复原标志位，防止远离设备中途返回
											tag_Hfconn4 = false;
											tag_Avrcpdisc9 = false;
										}
									}
									break;
								case "PBCPULLCMTIND":
									// 电话簿中信息完毕
									if (tag_history == true) {
										history_all();
										tag_history = false;
									}
									break;
								// 判断设备是否与蓝牙断开连接
								case "PBCDISC":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 设备与蓝牙电话簿断开连接
											boolean_pbcdisc0 = true;
										}
									}
									break;
								case "HFDISC":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 设备与蓝牙通话断开连接
											boolean_hfdisc0 = true;
										} else if ("3".equals(listBTPrase.get(2))) {
											// 断开超时 对端忽略设备时见
											boolean_hfdisc0 = true;
										}
									}
									break;
								case "A2DPDISC":
									if (listBTPrase.size() >= 3) {
										if ("0".equals(listBTPrase.get(2))) {
											// 设备与多媒体断开连接
											boolean_a2dpdisc0 = true;
										}
									}
									break;
								case "AVRCPDISC":
									if (listBTPrase.size() >= 3) {
										// 对端手机远离设备相关
										if ("9".equals(listBTPrase.get(2))) {
											tag_Avrcpdisc9 = true;
										}
									}
									break;


							}
						}
					}
				}
				if (btNullCount == 5) {
					//如5次未接收到消息，发送获取蓝牙版本命令
					serialService.send(fd_bluetooth, "AT+B GVER\r\n");
					//复原计数
					btNullCount = 0;
					//开启接收命令计数
					tagBtReceive = true;
				}
				if (tagBtReceive) {
					if (btReceive < 3) {
						btReceive++;
					} else {
						//发送断开蓝牙命令
						sendDisconnectBroadcast("nopop");
						//复原标志位
						tagBtReceive = false;
						//复原计数
						btReceive = 0;
					}
				}

				// 设备与蓝牙断开连接
				if (boolean_a2dpdisc0 && boolean_hfdisc0 && boolean_pbcdisc0) {
					device_address = "";
					device_name = "";
					bt_service_music_Artist = "";
					bt_service_music_Pos = 0;
					bt_service_music_Time = 0;
					bt_service_music_Title = "";
					sendDisconnectBroadcast("pop");
					boolean_a2dpdisc0 = false;
					boolean_hfdisc0 = false;
					boolean_pbcdisc0 = false;
					tag_history = true;
				}
				if (tag_Avrcpdisc9 && tag_Hfconn4) {
					// 复原标志位，防止因未复原引起的误断开
					tag_Hfconn4 = false;
					tag_Avrcpdisc9 = false;
					//发送断开命令,使对端手机接近设备时可重新连接
					device_address = "";
					device_name = "";
					bt_service_music_Artist = "";
					bt_service_music_Pos = 0;
					bt_service_music_Time = 0;
					bt_service_music_Title = "";
					sendDisconnectBroadcast("pop");
					boolean_a2dpdisc0 = false;
					boolean_hfdisc0 = false;
					boolean_pbcdisc0 = false;
					tag_history = true;
				}
				// 设置升级
				if (startUpgrade) {
					sendPopUpgrade();
					startUpgrade = false;
				}
			}
		}
	}

	private List<String> listBTCommand;//函数中返回值
	private List<String> listBTPrase = new ArrayList<String>();//线程中解析后的值
	private int commaCommand = -1;
	private int commaIndex = -1;
	private int commaCount = 0;

	//查找字符串中含有多少个逗号
	private int getCommaCount(StringBuffer strBuf) {
		commaCount = 0;
		for (int i = 0; i < strBuf.length(); i++) {
			String getstr = strBuf.substring(i, i + 1);
			if (getstr.equals(",")) {
				commaCount++;
			}
		}
		return commaCount;
	}

	// 解析字符串公共方法
	private List<String> parseBTCommand(StringBuffer strBuf) {
		//判断是否为规范字符串
		listBTCommand = new ArrayList<String>();
		if (strBuf.indexOf("AT-B") >= 0) {
			listBTCommand.add("succ");// 第1位,角标为0
			strBuf = strBuf.delete(0, 5);//删除前缀
		} else if ("".equals(strBuf.toString())) {
			listBTCommand.add("null");
			return listBTCommand;
		} else {
			listBTCommand.add("err");
			return listBTCommand;
		}
		// strBuf.indexOf(" ")查找第一个空格位置
		if (strBuf.indexOf(" ") >= 0) {
			listBTCommand.add(strBuf.substring(0, strBuf.indexOf(" ")).trim());//第2位存放关键字,角标为1
			strBuf.delete(0, strBuf.indexOf(" "));
		} else {
			listBTCommand.add(strBuf.toString().trim());
		}
		commaCommand = getCommaCount(strBuf);
		if (commaCommand > 0) {
			for (int i = 0; i <= commaCommand; i++) {
				commaIndex = strBuf.indexOf(",");
				if (commaIndex >= 0) {
					listBTCommand.add(strBuf.substring(0, commaIndex).trim());//截出的字符串包不含逗号
					strBuf = strBuf.delete(0, commaIndex + 1);//删除已截取字符串，删除第一个分隔逗号
				} else {
					listBTCommand.add(strBuf.toString().trim());
				}
			}
		} else if (commaCommand == 0) {
			// 如所接收到的数据不含逗号
			listBTCommand.add(strBuf.toString().trim());
		}
		return listBTCommand;
	}
}
