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

public class MySetPxDialog extends Dialog {
	private Context context = null;
	MySetDialogListener listener;
	private RadioGroup rgSetPx;
	private RadioButton rbSetPxD1;
	private RadioButton rbSetPx720P;
	private Button btnPxYes;
	private Button btnPxNo;
	private int tag;

	public MySetPxDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetPxDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_moni_px);
		// TODO 从数据库中提取数据状态
		// TODO 添加单选初始化 tag赋值
		initView();
		initClickListener();
	}

	private void initClickListener() {
		rgSetPx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_dialog_set_moni_d1:
					rbSetPxD1.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_d1;
					break;
				case R.id.rb_dialog_set_moni_720p:
					rbSetPx720P.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_720p;
					break;
				default:
					break;
				}

			}
		});
		btnPxYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 添加确定事件 更新数据库
				dismiss();
			}
		});
		btnPxNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		rgSetPx = (RadioGroup) findViewById(R.id.rg_dialog_set_moni_px);
		rbSetPxD1 = (RadioButton) findViewById(R.id.rb_dialog_set_moni_d1);
		rbSetPx720P = (RadioButton) findViewById(R.id.rb_dialog_set_moni_720p);
		btnPxYes = (Button) findViewById(R.id.btn_dialog_set_moni_px_yes);
		btnPxNo = (Button) findViewById(R.id.btn_dialog_set_moni_px_no);
	}
}
