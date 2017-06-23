package com.hejia.media;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.hejia.eventbus.EventMediaRequest;
import com.hejia.eventbus.EventMediaResponse;
import com.hejia.fragment.FragmentMedia;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MusicService extends Service implements Runnable {
	public static MediaPlayer player;
	private String play; // 音乐文件路径
	private boolean isRun = true;
	public final int PLAY_MP3 = 0;        //播放音乐
	public final int PAUSE_TO_START = 1;  //由暂停切换播放
	public final int PLAYING_TO_PAUSE = 2;//暂停
	public final int NEXT_MP3 = 3;        //下一曲
	public final int LAST_MP3 = 4;        //上一曲
	public boolean isPrepare = false; //当前mediaplayer准备好
	private FragmentMedia mFragmentMedia;
	//private int position;
	//private int total;

	@Override
	public IBinder onBind(Intent arg0) {

		return new MusicBinder();
	}

	public class MusicBinder extends Binder {
		public MusicService getMusicService() {
			return MusicService.this;
		}
	}


	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("onCreate", "onCreate");
		// 注册EventBus
		EventBus.getDefault().register(this);
		mFragmentMedia = FragmentMedia.getInstance();
		new Thread(this).start();


	}


	public void playMusic(int id) throws IOException {

		if (mFragmentMedia == null) {
			mFragmentMedia = FragmentMedia.getInstance();
		}
		if (null != player) {
			player.release();
			player = null;
		}
		if (id > FragmentMedia.myListAdapter.mList.size() - 1) {
			id = 0;
		} else if (id < 0) {
			System.out.println("不播放");
		}
		Media m = FragmentMedia.myListAdapter.mList.get(id);
		String url = m.getUrl();
		Uri myUri = Uri.parse(url);
		if (fileIsExist(myUri)) {
			player = new MediaPlayer();
			player.reset();
			mFragmentMedia.seekbarWait = true;
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				player.setDataSource(myUri.toString());
				player.prepare();
				isPrepare = true;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mFragmentMedia.seekbarWait = false;
			player.start();

			player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
//				if(!error_index)
					if (mFragmentMedia == null) {
						mFragmentMedia = FragmentMedia.getInstance();
					}
					int value = mFragmentMedia.progress;
					if (value >= 98)
						playerOverListener();


				}
			});
			player.setOnErrorListener(new OnErrorListener() {

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
							//比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
							Log.e("wwl", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
							break;
						default:
							Log.e("wwl", "onError ");
							break;
					}
					EventBus.getDefault().post(
							new EventMediaRequest("MEDIAERR", "default", "default"));
					return false;
				}
			});
//		}
		} else {
			isPrepare = false;
			EventBus.getDefault().post(
					new EventMediaRequest("NOFILE", "default", "default"));
		}

	}

	@Override
	public void run() {
		if (mFragmentMedia == null) {
			mFragmentMedia = FragmentMedia.getInstance();
		}
		while (isRun) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if ((null != player)) {

				int position = 0;
				try {
					position = player.getCurrentPosition();
				} catch (Exception ex) {
				}
				if (position != 0) {
					try {
						if (!mFragmentMedia.seekbarWait) {
							int total = player.getDuration();
							EventBus.getDefault().post(
									new EventMediaRequest("MEDIAPOS", position + "", total));
						}
					} catch (Exception e) {
					}

				}
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}


	public void playerOverListener() {
		// 下一首
		if (FragmentMedia.myListAdapter.mList.size() <= 0) {
			return;
		}
		mFragmentMedia = FragmentMedia.getInstance();
		switch (mFragmentMedia.tag_Order) {
			case 0:
				EventBus.getDefault().post(
						new EventMediaRequest("MEDIALOOP", "PLAY_ORDER", "default"));
				break;
			case 1:
				// 随机播放
				EventBus.getDefault().post(
						new EventMediaRequest("MEDIALOOP", "PLAY_RADOM", "default"));
				break;
			case 2:
				EventBus.getDefault().post(
						new EventMediaRequest("MEDIALOOP", "PLAY_ONE", "default"));
				break;
		}


	}


	@Subscribe(threadMode = ThreadMode.Async, sticky = true)
	public void onEvent(EventMediaResponse event) {
		switch (event.getKey()) {
			case "SEEKPOS":
				if (mFragmentMedia == null) {
					mFragmentMedia = FragmentMedia.getInstance();
				}
				if (FragmentMedia.myListAdapter.mList.size() > 0) {
					int seekBarPosition = Integer.valueOf(event.getValues());
					int value1 = player.getDuration();
					int value2 = seekBarPosition * player.getDuration() / 100;
					player.seekTo(value2);
					player.start();
					mFragmentMedia.seekbarWait = false;
					mFragmentMedia.isplayingmp3 = true;

				}
				break;
			case "MEDIAPLAY":
				Log.e("initMediaPlayer", "3");
				String status = event.getValues();
				Log.e("initMediaPlayer", status);
				switch (status) {
					case "PLAY":
						try {
							playMusic(FragmentMedia.id);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case "PAUSE_TO_START":
						if (isPrepare) {
							player.start();
						} else {
							try {
								playMusic(FragmentMedia.id);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						break;
					case "PAUSE":
						if (null != player) player.pause();
						break;
				}
		}
	}

	private boolean fileIsExist(Uri myUri) {
		if (myUri != null) {
			File file = new File(String.valueOf(myUri));
			if (file.exists()) {
				return true;
			}
		}
		return false;
	}

}
