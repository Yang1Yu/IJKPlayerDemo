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

public class MySetPassageDialog extends Dialog {
	private Context context = null;
	MySetDialogListener listener;
	private RadioGroup rgSetPassage;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;
	private RadioButton rb4;
	private Button btnPassageYes;
	private Button btnPassageNo;
	private int tag;

	public MySetPassageDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetPassageDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_moni_passage);
		// TODO 从数据库中提取数据状态
		// TODO 添加单选初始化 tag赋值
		initView();
		initClickListener();
	}

	private void initClickListener() {
		rgSetPassage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_dialog_set_moni_passage_moni1:
					rb1.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_d1;
					break;
				case R.id.rb_dialog_set_moni_passage_moni2:
					rb2.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_720p;
					break;
				case R.id.rb_dialog_set_moni_passage_moni3:
					rb3.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_720p;
					break;
				case R.id.rb_dialog_set_moni_passage_moni4:
					rb4.setChecked(true);
					// TODO 更新数据库
					tag = R.id.rb_dialog_set_moni_720p;
					break;
				default:
					break;
				}

			}
		});
		btnPassageYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 添加确定事件 更新数据库
				dismiss();
			}
		});
		btnPassageNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		rgSetPassage = (RadioGroup) findViewById(R.id.rg_dialog_set_moni_passage);
		rb1 = (RadioButton) findViewById(R.id.rb_dialog_set_moni_passage_moni1);
		rb2 = (RadioButton) findViewById(R.id.rb_dialog_set_moni_passage_moni2);
		rb3 = (RadioButton) findViewById(R.id.rb_dialog_set_moni_passage_moni3);
		rb4 = (RadioButton) findViewById(R.id.rb_dialog_set_moni_passage_moni4);
		btnPassageYes = (Button) findViewById(R.id.btn_dialog_set_moni_passage_yes);
		btnPassageNo = (Button) findViewById(R.id.btn_dialog_set_moni_passage_no);
	}
}
