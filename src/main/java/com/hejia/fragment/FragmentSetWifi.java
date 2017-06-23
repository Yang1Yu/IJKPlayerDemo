package com.hejia.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hejia.myview.SwitchButton;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

public class FragmentSetWifi extends Fragment {

	private View view;
	private SwitchButton switch_set_wifi_share;
	private SwitchButton switch_set_wifi_password;
	private ImageView iv_set_wifi_lock;
	private RelativeLayout rl_wifi;
	private static EditText etShareName;
	private static EditText etSharePassword;
	private MainActivity activity;
	private String shareName;
	private String tempShareName;
	private String tempSharePassword;
	private String sharePassword;
	private int tag = 1;
	// private int id;
	private boolean shareHavePassword;
	private boolean shareIsOpen;

	private static InputMethodManager imm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_wifi, container, false);
		// 获取热点存储状态
		shareIsOpen = activity.toDoDB.getShareIsOpen();
		// 提取热点加密密码
		sharePassword = activity.toDoDB.getSharePassword();
		// 提取热点名称
		shareName = activity.toDoDB.getShareName();
		// 热点密码开放状态
		shareHavePassword = activity.shareHavePassword;
		initView();
		initListener();
		return view;
	}

	private void initView() {
		iv_set_wifi_lock = (ImageView) view.findViewById(R.id.iv_set_wifi_lock);
		rl_wifi = (RelativeLayout) view.findViewById(R.id.rl_wifi);
		switch_set_wifi_share = (SwitchButton) view.findViewById(R.id.switch_set_wifi_share);
		switch_set_wifi_password = (SwitchButton) view.findViewById(R.id.switch_set_wifi_password);
		etShareName = (EditText) view.findViewById(R.id.et_set_wifi_share_nameing);
		etSharePassword = (EditText) view.findViewById(R.id.et_set_wifi_passwording);
		if (shareIsOpen) {
			switch_set_wifi_share.setChecked(false);
			// activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
			etShareName.setVisibility(View.VISIBLE);
			etShareName.setText(shareName);
			switch_set_wifi_password.setVisibility(View.VISIBLE);
			if (shareHavePassword) {
				switch_set_wifi_password.setChecked(false);
				etSharePassword.setVisibility(View.VISIBLE);
				etSharePassword.setText(sharePassword);
				iv_set_wifi_lock.setVisibility(View.VISIBLE);
				iv_set_wifi_lock.setVisibility(View.VISIBLE);
				// activity.setWifiApEnabled(shareIsOpen, null, null);
				// activity.setWifiApEnabled(shareIsOpen, shareName,
				// sharePassword);
			} else {
				switch_set_wifi_password.setChecked(true);
				etSharePassword.setVisibility(View.INVISIBLE);
				iv_set_wifi_lock.setVisibility(View.INVISIBLE);
			}
		} else {
			switch_set_wifi_share.setChecked(true);
			switch_set_wifi_password.setChecked(true);
			etShareName.setVisibility(View.INVISIBLE);
			etSharePassword.setVisibility(View.INVISIBLE);
			iv_set_wifi_lock.setVisibility(View.INVISIBLE);
			switch_set_wifi_password.setVisibility(View.INVISIBLE);

		}

	}

	private void initListener() {
		etSharePassword.setText(sharePassword);
		etSharePassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode actionMode) {

			}
		});
		etShareName.setText(shareName);
		etShareName.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode actionMode) {

			}
		});
		switch_set_wifi_share.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(SwitchButton button, boolean isChecked) {
				if ("SetWifi".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					if (isChecked) {
						// 更新数据库中热点开放状态 关闭热点
						activity.toDoDB.updateShareIsOpen("off");
						shareIsOpen = false;
						activity.setWifiApEnabled(shareIsOpen, null, null);
						etShareName.setVisibility(View.INVISIBLE);
						etSharePassword.setVisibility(View.INVISIBLE);
						iv_set_wifi_lock.setVisibility(View.INVISIBLE);
						switch_set_wifi_password.setVisibility(View.INVISIBLE);
						switch_set_wifi_password.setChecked(!shareIsOpen);
					} else {
						// 更新数据库中热点开放状态 开启热点
						activity.toDoDB.updateShareIsOpen("on");
						shareIsOpen = true;
						activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
						etShareName.setVisibility(View.VISIBLE);
						etShareName.setText(shareName);
						switch_set_wifi_password.setVisibility(View.VISIBLE);
					}

				}
			}
		});
		switch_set_wifi_password.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(SwitchButton button, boolean isChecked) {
				if ("SetWifi".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					if (isChecked) {
						// 更新数据库中热点密码开发状态 关闭密码
						activity.toDoDB.updateShareHavePassword("off");
						shareHavePassword = false;
						activity.shareHavePassword = false;
						etSharePassword.setVisibility(View.INVISIBLE);
						iv_set_wifi_lock.setVisibility(View.INVISIBLE);
						shareIsOpen = false;
						activity.setWifiApEnabled(shareIsOpen, null, null);
						shareIsOpen = true;
						activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
					} else {
						// 更新数据库中热点密码开发状态 开启密码
						activity.toDoDB.updateShareHavePassword("on");
						shareHavePassword = true;
						activity.shareHavePassword = true;
						etSharePassword.setVisibility(View.VISIBLE);
						// if (sharePassword.trim() != null) {
						etSharePassword.setText(sharePassword);
						// }
						iv_set_wifi_lock.setVisibility(View.VISIBLE);
						shareIsOpen = false;
						activity.setWifiApEnabled(shareIsOpen, null, null);
						shareIsOpen = true;
						activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
					}
				}
			}
		});
		etSharePassword.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (etSharePassword.length() < 8) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("错误提示").setIcon(R.drawable.png_wifi_error);
					builder.setMessage("密码的长度不足8位，请重新设置密码");
					builder.setPositiveButton("确认", null);
					etSharePassword.setText("");
					builder.create().show();
				}
				if (etSharePassword.length() > 16) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("错误提示").setIcon(R.drawable.png_wifi_error);
					builder.setMessage("密码的长度不允许超过16位，请重新设置密码");
					builder.setPositiveButton("确认", null);
					etSharePassword.setText("");
					builder.create().show();
				}
				if (etSharePassword.length() >= 8 & etSharePassword.length() <= 16) {
					sharePassword = etSharePassword.getText().toString();
					// 更新数据库中热点密码
					activity.toDoDB.updateSharePassword(sharePassword);
					activity.toDoDB.updateShareHavePassword("on");
					activity.apConfig.preSharedKey = sharePassword;
					shareHavePassword = true;
					activity.shareHavePassword = true;
					shareIsOpen = false;
					activity.setWifiApEnabled(shareIsOpen, null, null);
					shareIsOpen = true;
					activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
					etSharePassword.setText(sharePassword);

				}

				return false;
			}
		});
		etShareName.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (etShareName.length() < 1) {
					// 热点名长度小于1
					etShareName.setText(shareName);
				} else {
					shareName = etShareName.getText().toString();
					// 更新数据库中热点名称
					activity.toDoDB.updateShareName(shareName);
					activity.apConfig.SSID = shareName;
					activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
					shareIsOpen = false;
					activity.setWifiApEnabled(shareIsOpen, null, null);
					shareIsOpen = true;
					activity.setWifiApEnabled(shareIsOpen, shareName, sharePassword);
				}
				return false;
			}
		});
		iv_set_wifi_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tag == 1) {
					etSharePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					tag = 2;
				} else {
					etSharePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					tag = 1;
				}
			}
		});

		rl_wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyboard();
				// if (etSharePassword.length() < 8 || etSharePassword.length()
				// > 16) {
				// etSharePassword.setText(sharePassword);
				// } else {
				// sharePassword = etSharePassword.getText().toString();
				// // 更新数据库中热点密码及开启热点状态
				// activity.toDoDB.updateSharePassword(sharePassword);
				// activity.toDoDB.updateShareHavePassword("on");
				// // activity.apConfig.preSharedKey = sharePassword;
				// shareHavePassword = true;
				// activity.shareHavePassword = true;
				// shareIsOpen = false;
				// activity.setWifiApEnabled(shareIsOpen, null, null);
				// shareIsOpen = true;
				// activity.setWifiApEnabled(shareIsOpen, shareName,
				// sharePassword);
				// switch_set_wifi_password.setVisibility(View.VISIBLE);
				// etSharePassword.setText(sharePassword);
				// }
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	// 隐藏键盘
	public static void hideKeyboard() {
		if (imm != null) {
			imm.hideSoftInputFromWindow(etShareName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etSharePassword.getWindowToken(), 0);
			// 以防键盘未回收
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
