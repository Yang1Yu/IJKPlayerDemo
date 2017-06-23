package com.hejia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FragmentSet.MySetDialogListener;

public class MySetAdLevelDialog extends Dialog {
	private Context context = null;
	MySetDialogListener listener;
	private RadioGroup rgAdLevel;
	private RadioButton rbLocal;
	private RadioButton rbNetwork;
	private Button btnAdLevelYes;
	private Button btnAdLevelNo;
	private int tag;

	public MySetAdLevelDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetAdLevelDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_ad_level);
		// TODO 从数据库中提取数据状态
		// TODO 添加单选初始化 tag赋值
		initView();
		initClickListener();
	}

	private void initClickListener() {
		rgAdLevel.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_dialog_set_ad_level_local:
					rbLocal.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_d1;
					break;
				case R.id.rb_dialog_set_ad_level_network:
					rbNetwork.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_720p;
					break;

				default:
					break;
				}

			}
		});
		btnAdLevelYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 添加确定事件 更新数据库
				dismiss();
			}
		});
		btnAdLevelNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		rgAdLevel = (RadioGroup) findViewById(R.id.rg_dialog_set_ad_level);
		rbLocal = (RadioButton) findViewById(R.id.rb_dialog_set_ad_level_local);
		rbNetwork = (RadioButton) findViewById(R.id.rb_dialog_set_ad_level_network);
		btnAdLevelNo = (Button) findViewById(R.id.btn_dialog_set_ad_level_no);
		btnAdLevelYes = (Button) findViewById(R.id.btn_dialog_set_ad_level_yes);
	}
}
