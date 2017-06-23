package com.hejia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hejia.fragment.FragmentMedia;
import com.hejia.media.Media;


public class ToDoDB extends SQLiteOpenHelper {

	private static String DATABASE_NAME = "db_launcher";// 数据库名
	private static int DATABASE_VERSION = 1;// 数据库版本号
	// private String TABLE_NAME;
	public String FIELD_id = "name";
	public String FIELD_TEXT = "todo_text";
	private String share_password;
	private String share_name;
	private String shareIsOpen;
	private String shareHavePassword;

	private String channel_one;
	private String channel_two;
	private String channel_three;
	private String channel_four;
	private String channel_five;
	private String channel_six;
	private String channel_current;
	private String rb_radio_on_off;

	private String media_switch;
	private String media_id;
	private String media_random;

	public ToDoDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// 建立设置表
		db.execSQL("CREATE TABLE IF NOT EXISTS table_set (name text primary key,property text)");
		// 建立空调表
		db.execSQL("CREATE TABLE IF NOT EXISTS table_ac (name text primary key,property text)");
		// 建立收音机表
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS table_radio (id integer primary key autoincrement,name text,property text)");
		// 建立多媒体音乐表
		db.execSQL("CREATE TABLE IF NOT EXISTS table_media (url  text  primary key,name text)");
		// 建立蓝牙配对设备表
		db.execSQL("CREATE TABLE IF NOT EXISTS table_bt_pair (bd  text  primary key,name text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS table_set");
		db.execSQL("DROP TABLE IF EXISTS table_ac");
		db.execSQL("DROP TABLE IF EXISTS table_radio");
		db.execSQL("DROP TABLE IF EXISTS table_media");
		db.execSQL("DROP TABLE IF EXISTS table_bt_pair");
		onCreate(db);
	}

	// 初始化数据库，仅在第一次安装程序时执行
	public void initDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		// 设置表相关数据
		// 当前界面
		db.execSQL("insert into table_set(name,property) values('set_nowfm','cloud')");
		// 屏幕亮度
		db.execSQL("insert into table_set(name,property) values('set_light','13')");
		// 触控反馈
		db.execSQL("insert into table_set(name,property) values('set_shock_switch','off')");
		// 是否静音
		db.execSQL("insert into table_set(name,property) values('set_mute_switch','off')");
		// 音量大小
		db.execSQL("insert into table_set(name,property) values('set_vol','4')");
		// 音色
		db.execSQL("insert into table_set(name,property) values('set_vol_tone','cla')");
		// 启用wifi
		db.execSQL("insert into table_set(name,property) values('set_wifi_switch','off')");
		// 启用热点
		db.execSQL("insert into table_set(name,property) values('set_share_switch','off')");
		// 热点名称
		db.execSQL("insert into table_set(name,property) values('set_share_name','WIFI')");
		// 启用热点加密
		db.execSQL("insert into table_set(name,property) values('set_share_password_switch','off')");
		// 热点加密密码
		db.execSQL("insert into table_set(name,property) values('set_share_password','12345678')");
		// 当前音源
		db.execSQL("insert into table_set(name,property) values('set_audio','media')");
		// 缓存音源
		db.execSQL("insert into table_set(name,property) values('set_audio_temp','media')");
		// 蓝牙电话音源
		db.execSQL("insert into table_set(name,property) values('set_audiobt','no')");
		// 离线地图存储
		db.execSQL("insert into table_set(name,property) values('set_map','no')");
		// Log编码
		db.execSQL("insert into table_set(name,property) values('set_log','0')");

		// 空调表相关数据
		// 温度开关
		db.execSQL("insert into table_ac(name,property) values('ac_work_mode','0')");
		// 循环方式
		db.execSQL("insert into table_ac(name,property) values('ac_mode_circul','0')");
		// 出风方式
		db.execSQL("insert into table_ac(name,property) values('ac_mode_air_out','0')");
		// 主风速
		db.execSQL("insert into table_ac(name,property) values('ac_adjust_main_air_speed','0')");
		// 主温度
		db.execSQL("insert into table_ac(name,property) values('ac_adjust_main_temp','25')");
		// 副风速
		db.execSQL("insert into table_ac(name,property) values('ac_adjust_vice_air_speed','0')");
		// 副温度
		db.execSQL("insert into table_ac(name,property) values('ac_adjust_vice_temp','25')");
		// 挡风玻璃加热
		db.execSQL("insert into table_ac(name,property) values('ac_mode_behind_glass_heat','0')");
		// 节能模式
		db.execSQL("insert into table_ac(name,property) values('ac_mode_save_energy','0')");
		// 空气净化
		db.execSQL("insert into table_ac(name,property) values('ac_mode_air_purification','0')");

		// 音乐开关
		db.execSQL("insert into table_radio(name,property) values('media_switch','off')");
		db.execSQL("insert into table_radio(name,property) values('media_id','0')");
		db.execSQL("insert into table_radio(name,property) values('media_random','0')");

		// 收音机表相关数据

		// 收音机开关
		db.execSQL("insert into table_radio(name,property) values('rb_radio_fm','off')");
		// 频道1表
		db.execSQL("insert into table_radio(name,property) values('channel_one','000')");
		// 频道2表
		db.execSQL("insert into table_radio(name,property) values('channel_two','000')");
		// 频道3表
		db.execSQL("insert into table_radio(name,property) values('channel_three','000')");
		// 频道4表
		db.execSQL("insert into table_radio(name,property) values('channel_four','000')");
		// 频道5表
		db.execSQL("insert into table_radio(name,property) values('channel_five','000')");
		// 频道6表
		db.execSQL("insert into table_radio(name,property) values('channel_six','000')");

		db.execSQL("insert into table_radio(name,property) values('channel_current','96.8')");
	}

	private String nowFragment;

	// 获取当前所在界面
	public String getNowFragment() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_set", new String[]{"property"}, "name=?",
				new String[]{"set_nowfm"}, null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		nowFragment = cursor.getString(temp);
		cursor.close();
		return nowFragment;
	}

	// 存入当前所在界面
	public void setNowFragment(String nowFragment) {
		update_setting("set_nowfm", nowFragment, "table_set");
	}

	//对数据进行查找
	public Cursor select(String TABLE_NAME) {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		cursor.close();
		return cursor;
	}

	private boolean pairDeviceExiat = false;

	// 存入配对设备信息
	public void savePairDevice(String pairDeviceBd, String pairDeviceName) {
		// 判断当前配对设备信息是否存在
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_bt_pair", null, null, null, null, null, null);
		// 判断cursor不为空
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (cursor.getString(cursor.getColumnIndex("bd")).equals(pairDeviceBd)) {
					pairDeviceExiat = true;
				}
				;
			}
		}
		if (pairDeviceExiat) {
			// 如存在则更新
			updateBtPair(pairDeviceBd, pairDeviceName, "table_bt_pair");
			Log.i("chanyeol", "table_bt_pair exist");
		} else {
			// 如不存在则插入
			db.execSQL("insert into table_bt_pair(bd,name) values('" + pairDeviceBd + "','" + pairDeviceName + "')");
		}
		// 复原tag
		pairDeviceExiat = false;
		cursor.close();
	}

	// 删除配对设备信息
	public void deletePairDevice(String pairDeviceBD) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from 'table_bt_pair' where bd='" + pairDeviceBD.trim() + "'");
	}

	// 对配对设备数据进行更新
	public void updateBtPair(String id, String text, String TABLE_NAME) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "bd" + " = ?";
		String[] whereValue = new String[1];
		whereValue[0] = id;
		ContentValues cv = new ContentValues();
		cv.put("name", text);
		db.update(TABLE_NAME, cv, where, whereValue);
		// db.close();
	}

	// 提取配对设备
	public Cursor getPairDevice() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_bt_pair", null, null, null, null, null, null);
		return cursor;
	}
	// 多媒体数据进行查找

	FragmentMedia mFragmentMedia;

	public void query(String TABLE_NAME) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		// 判断cursor不为空
		if (cursor != null) {
			// 循环遍历cursor
			while (cursor.moveToNext()) {
				// 拿到每一行url与name
				mFragmentMedia = FragmentMedia.getInstance();
				String url = cursor.getString(cursor.getColumnIndex("url"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				url = mFragmentMedia.filterMediaout(url);
				name = mFragmentMedia.filterMediaout(name);
				mFragmentMedia.mediaTreeSet.add(new Media(mFragmentMedia.mediaTreeSet.size(), name, url));
				// FragmentMedia.myListAdapter.mList.add(new
				// Media(FragmentMedia.myListAdapter.getMax()+1,name,url));

			}
			cursor.close();
		}

	}

	// 获取热点密码
	public String getSharePassword() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_set", new String[]{"property"}, "name=?",
				new String[]{"set_share_password"}, null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		share_password = cursor.getString(temp);
		cursor.close();
		return share_password;
	}

	// 获取热点名称
	public String getShareName() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_set", new String[]{"property"}, "name=?", new String[]{"set_share_name"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		share_name = cursor.getString(temp);
		cursor.close();
		return share_name;
	}

	// 获取热点开启状态
	public boolean getShareIsOpen() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_set", new String[]{"property"}, "name=?",
				new String[]{"set_share_switch"}, null, null, null);
		// int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		shareIsOpen = cursor.getString(0);
		cursor.close();
		if ("off".equals(shareIsOpen)) {
			return false;
		}
		return true;
	}

	// 获取热点密码开放状态
	public boolean getShareHavePassword() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_set", new String[]{"property"}, "name=?",
				new String[]{"set_share_password_switch"}, null, null, null);
		cursor.moveToFirst();
		shareHavePassword = cursor.getString(0);
		cursor.close();
		if ("off".equals(shareHavePassword)) {
			return false;
		}
		return true;
	}

	// 更新热点名称
	public void updateShareName(String shareName) {
		update_setting("set_share_name", shareName, "table_set");
	}

	// 更新热点密码
	public void updateSharePassword(String sharePassword) {
		update_setting("set_share_password", sharePassword, "table_set");
	}

	// 更新热点开启状态
	public void updateShareIsOpen(String shareIsOpen) {
		update_setting("set_share_switch", shareIsOpen, "table_set");
	}

	// 更新热点密码开放状态
	public void updateShareHavePassword(String shareHavePassword) {
		update_setting("set_share_password_switch", shareHavePassword, "table_set");
	}

	// 提取音色游标
	private Cursor cursorTone;
	private String strTone;

	// 获得设置表中音色
	public String getSetTone() {
		SQLiteDatabase db = this.getWritableDatabase();
		cursorTone = db.query("table_set", new String[]{"property"}, "name=?",
				new String[]{"set_vol_tone"}, null, null, null);
		cursorTone.moveToFirst();
		// 提取出的音色按键
		strTone = cursorTone.getString(0);
		return strTone;
	}

	private Cursor cursorACDataProperty;
	private String strACDataProperty;

	// 获取空调表中某项数据属性
	public String getACDataProperty(String name) {
		if (!name.isEmpty() && name != "") {
			SQLiteDatabase db = this.getWritableDatabase();
			cursorACDataProperty = db.query("table_ac", new String[]{"property"}, "name=?",
					new String[]{name}, null, null, null);
			cursorACDataProperty.moveToFirst();
			strACDataProperty = cursorACDataProperty.getString(0);
			cursorACDataProperty.close();
			return strACDataProperty;
		} else {
			return "error";
		}
	}


	private Cursor cursorSetDataProperty;
	private String strSetDataProperty;

	// 获取设置表中某项数据属性
	public String getSetDataProperty(String name) {
		if (!name.isEmpty() && name != "") {
			SQLiteDatabase db = this.getWritableDatabase();
			cursorSetDataProperty = db.query("table_set", new String[]{"property"}, "name=?", new String[]{name},
					null, null, null);
			cursorSetDataProperty.moveToFirst();
			strSetDataProperty = cursorSetDataProperty.getString(0);
			cursorSetDataProperty.close();
			return strSetDataProperty;
		} else {
			return "error";
		}
	}

	// 对数据进行插入
	public long insert(String text, String TABLE_NAME) {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(FIELD_TEXT, text);
		Log.i("chanyeol", "text:" + text);
		long row = db.insert(TABLE_NAME, null, cv);
		return row;
	}

	// 对数据进行更新
	public void update_setting(String id, String text, String TABLE_NAME) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";

		String[] whereValue = new String[1];
		whereValue[0] = id;
		ContentValues cv = new ContentValues();
		cv.put("property", text);
		db.update(TABLE_NAME, cv, where, whereValue);
	}

	// 对数据进行更新
	public void updateInfor(String id, String text, String TABLE_NAME) {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";
		// String[] whereValue = { Integer.toString(id) };
		// Log.i("chanyeol", "text:" + text);

		String[] whereValue = new String[1];
		whereValue[0] = id;
		Log.i("chanyeol", "whereValue:" + whereValue[0]);
		ContentValues cv = new ContentValues();
		cv.put("property", text);
		db.update(TABLE_NAME, cv, where, whereValue);
	}

	// 对数据进行删除
	public void delete(int id, String TABLE_NAME) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";
		String[] whereValue = {Integer.toString(id)};
		db.delete(TABLE_NAME, where, whereValue);
	}

	// 对频道进行查找
	public String select_radio_on_off() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"rb_radio_fm"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		rb_radio_on_off = cursor.getString(temp);

		return rb_radio_on_off;
	}

	// 对频道进行查找
	public String select_channel_one() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"channel_one"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_one = cursor.getString(temp);
		cursor.close();
		return channel_one;
	}

	// 对频道进行查找
	public String select_channel_two() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"channel_two"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_two = cursor.getString(temp);
		cursor.close();
		return channel_two;
	}

	// 对频道进行查找
	public String select_channel_three() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"channel_three"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_three = cursor.getString(temp);
		cursor.close();
		return channel_three;
	}

	// 对频道进行查找
	public String select_channel_four() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"channel_four"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_four = cursor.getString(temp);
		cursor.close();
		return channel_four;
	}

	// 对频道进行查找
	public String select_channel_five() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"channel_five"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_five = cursor.getString(temp);
		cursor.close();
		return channel_five;
	}

	// 对频道进行查找
	public String select_channel_six() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"channel_six"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_six = cursor.getString(temp);
		cursor.close();
		return channel_six;
	}

	// 对频道进行查找
	public String select_channel_current() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?",
				new String[]{"channel_current"}, null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		channel_current = cursor.getString(temp);
		cursor.close();
		return channel_current;
	}

	// 对当前音乐开关进行查找
	public String select_media_switch() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"media_switch"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		media_switch = cursor.getString(temp);
		cursor.close();
		return media_switch;
	}

	// 对当前音乐id进行查找
	public String select_media_id() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"media_id"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		media_id = cursor.getString(temp);
		cursor.close();
		return media_id;
	}

	// 对当前音乐播放顺序进行查找
	public String select_media_random() {
		// this.TABLE_NAME = TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("table_radio", new String[]{"property"}, "name=?", new String[]{"media_random"},
				null, null, null);
		int temp = cursor.getColumnIndex("property");
		cursor.moveToFirst();
		media_random = cursor.getString(temp);
		cursor.close();
		return media_random;
	}

	// 插入音乐播放列表内容
	public void insertMediaUrl(String url, String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("insert into table_media(url,name) values('"
				+ url
				+ "','"
				+ name + "')");
	}

	// 对某表全部内容进行删除
	public void deleteTable(String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from '" + tableName + "'");
	}

	// 删除多媒体列表中某条数据
	public void deleteMediaUrl(String url) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("delete from 'table_media' where url='" + url.toString() + "'");
	}

}
