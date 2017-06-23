package com.hejia.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class FragmentSetVol extends Fragment {
	private View view;
	private Button btnVolCla;
	private Button btnVolDance;
	private Button btnVolJazz;
	private Button btnVolMbass;
	private Button btnVolNorm;
	private Button btnVolPop;
	private Button btnVolRock;
	private Button btnVolSoft;
	private MainActivity activity;
	private String tag;// 记录当前音色

	private ImageButton iv;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_vol, container, false);
		btnVolCla = (Button) view.findViewById(R.id.btn_set_vol_cla);
		btnVolDance = (Button) view.findViewById(R.id.btn_set_vol_dance);
		btnVolJazz = (Button) view.findViewById(R.id.btn_set_vol_jazz);
		btnVolMbass = (Button) view.findViewById(R.id.btn_set_vol_mbass);
		btnVolNorm = (Button) view.findViewById(R.id.btn_set_vol_norm);
		btnVolPop = (Button) view.findViewById(R.id.btn_set_vol_pop);
		btnVolRock = (Button) view.findViewById(R.id.btn_set_vol_rock);
		btnVolSoft = (Button) view.findViewById(R.id.btn_set_vol_soft);
		// 初始化设置音色选中按键
		// 获得当前音色
		// 提取出的音色按键
		tag = activity.toDoDB.getSetTone();
		switch (tag) {
			case "cla":
				btnVolCla.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolCla.setTextColor(Color.WHITE);
				break;
			case "dance":
				btnVolDance.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolDance.setTextColor(Color.WHITE);
				break;
			case "jazz":
				btnVolJazz.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolJazz.setTextColor(Color.WHITE);
				break;
			case "mbass":
				btnVolMbass.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolMbass.setTextColor(Color.WHITE);
				break;
			case "norm":
				btnVolNorm.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolNorm.setTextColor(Color.WHITE);
				break;
			case "pop":
				btnVolPop.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolPop.setTextColor(Color.WHITE);
				break;
			case "rock":
				btnVolRock.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolRock.setTextColor(Color.WHITE);
				break;
			case "soft":
				btnVolSoft.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolSoft.setTextColor(Color.WHITE);
				break;
			default:
				btnVolCla.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolCla.setTextColor(Color.WHITE);
				break;
		}

		btnVolCla.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolCla);
				}
			}
		});
		btnVolDance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolDance);
				}
			}
		});
		btnVolJazz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolJazz);
				}
			}
		});
		btnVolMbass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolMbass);
				}
			}
		});
		btnVolNorm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolNorm);
				}
			}
		});
		btnVolPop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolPop);
				}
			}
		});
		btnVolRock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolRock);
				}
			}
		});
		btnVolSoft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetVol".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					changeButton(btnVolSoft);
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	// 按键复位
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void changeButton(Button btn) {
		switch (tag) {
			case "cla":
				btnVolCla.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolCla.setTextColor(Color.BLACK);
				break;
			case "dance":
				btnVolDance.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolDance.setTextColor(Color.BLACK);
				break;
			case "jazz":
				btnVolJazz.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolJazz.setTextColor(Color.BLACK);
				break;
			case "mbass":
				btnVolMbass.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolMbass.setTextColor(Color.BLACK);
				break;
			case "norm":
				btnVolNorm.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolNorm.setTextColor(Color.BLACK);
				break;
			case "pop":
				btnVolPop.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolPop.setTextColor(Color.BLACK);
				break;
			case "rock":
				btnVolRock.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolRock.setTextColor(Color.BLACK);
				break;
			case "soft":
				btnVolSoft.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolSoft.setTextColor(Color.BLACK);
				break;
			default:
				btnVolCla.setBackgroundResource(R.drawable.img_set_vol_btnbg1);
				btnVolCla.setTextColor(Color.BLACK);
				break;
		}
		switch (btn.getId())

		{
			case R.id.btn_set_vol_cla:
				btnVolCla.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolCla.setTextColor(Color.WHITE);
				tag = "cla";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "cla", "table_set");
				break;
			case R.id.btn_set_vol_dance:
				btnVolDance.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolDance.setTextColor(Color.WHITE);
				tag = "dance";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "dance", "table_set");
				break;
			case R.id.btn_set_vol_jazz:
				btnVolJazz.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolJazz.setTextColor(Color.WHITE);
				tag = "jazz";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "jazz", "table_set");
				break;
			case R.id.btn_set_vol_mbass:
				btnVolMbass.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolMbass.setTextColor(Color.WHITE);
				tag = "mbass";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "mbass", "table_set");
				break;
			case R.id.btn_set_vol_norm:
				btnVolNorm.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolNorm.setTextColor(Color.WHITE);
				tag = "norm";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "norm", "table_set");
				break;
			case R.id.btn_set_vol_pop:
				btnVolPop.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolPop.setTextColor(Color.WHITE);
				tag = "pop";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "pop", "table_set");
				break;
			case R.id.btn_set_vol_rock:
				btnVolRock.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolRock.setTextColor(Color.WHITE);
				tag = "rock";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "rock", "table_set");
				break;
			case R.id.btn_set_vol_soft:
				btnVolSoft.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolSoft.setTextColor(Color.WHITE);
				tag = "soft";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "soft", "table_set");
				break;
			default:
				btnVolCla.setBackgroundResource(R.drawable.img_set_vol_btnbg2);
				btnVolCla.setTextColor(Color.WHITE);
				tag = "cla";
				// 更新数据库
				activity.toDoDB.update_setting("set_vol_tone", "cla", "table_set");
				break;
		}
	}
}
