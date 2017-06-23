package com.hejia.caught;

import android.content.Context;
import android.os.Environment;
import android.os.Process;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler implements UncaughtExceptionHandler {

	private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/hejia/log/1030/";
	private static final String FILE_NAME = "log_";
	private static final String FILE_NAME_SUFFIX = ".trace";
	private static CrashHandler mInstance = new CrashHandler();
	private Context mContext;
	private UncaughtExceptionHandler mDefaultCrashHandler;

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return mInstance;
	}

	public void init(Context context) {
		mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		mContext = context.getApplicationContext();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			writeExceptionToSDCard(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ex.printStackTrace();
		if (mDefaultCrashHandler != null) {
			mDefaultCrashHandler.uncaughtException(thread, ex);
		} else {
			Process.killProcess(Process.myPid());
		}
	}

	private void writeExceptionToSDCard(Throwable ex) throws IOException {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// long current = System.currentTimeMillis();
		// String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new
		// Date(current));
		// File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
		File file = new File(PATH + FILE_NAME + MainActivity.logNumber + FILE_NAME_SUFFIX);
		MainActivity.toDoDB.update_setting("set_log", Integer.toString(MainActivity.logNumber + 1), "table_set");
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			// pw.println(time);
			ex.printStackTrace(pw);
			pw.close();
		} catch (Exception e) {
		}
	}

}
