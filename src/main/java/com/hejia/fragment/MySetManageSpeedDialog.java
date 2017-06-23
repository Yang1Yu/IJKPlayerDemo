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

public class MySetManageSpeedDialog extends Dialog {
	private Context context = null;
	MySetDialogListener listener;
	private RadioGroup rgSetManageSpeed;
	private RadioButton rb40;
	private RadioButton rb50;
	private RadioButton rb60;
	private RadioButton rb70;
	private RadioButton rb80;
	private RadioButton rb90;
	private RadioButton rb100;
	private RadioButton rb110;
	private RadioButton rb120;
	private Button btnManageSpeedYes;
	private Button btnManageSpeedNo;
	private int tag;

	public MySetManageSpeedDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetManageSpeedDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_manage_speed);
		initView();
		initClickListener();
	}

	private void initClickListener() {
		rgSetManageSpeed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_dialog_set_manage_speed_40:
					rb40.setChecked(true);
					tag = R.id.rb_dialog_set_moni_d1;
					break;
				case R.id.rb_dialog_set_manage_speed_50:
					rb50.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_moni_720p;
					break;
				case R.id.rb_dialog_set_manage_speed_60:
					rb60.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_70;
					break;
				case R.id.rb_dialog_set_manage_speed_70:
					rb70.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_70;
					break;
				case R.id.rb_dialog_set_manage_speed_80:
					rb80.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_70;
					break;
				case R.id.rb_dialog_set_manage_speed_90:
					rb90.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_90;
					break;
				case R.id.rb_dialog_set_manage_speed_100:
					rb100.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_70;
					break;
				case R.id.rb_dialog_set_manage_speed_110:
					rb110.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_70;
					break;
				case R.id.rb_dialog_set_manage_speed_120:
					rb120.setChecked(true);
					// TODO �������ݿ�
					tag = R.id.rb_dialog_set_manage_speed_70;
					break;
				default:
					break;
				}

			}
		});
		btnManageSpeedYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ���ȷ���¼� �������ݿ�
				dismiss();
			}
		});
		btnManageSpeedNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		rgSetManageSpeed = (RadioGroup) findViewById(R.id.rg_dialog_set_manage_speed);
		rb40 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_40);
		rb50 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_50);
		rb60 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_60);
		rb70 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_70);
		rb80 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_80);
		rb90 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_90);
		rb100 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_100);
		rb110 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_110);
		rb120 = (RadioButton) findViewById(R.id.rb_dialog_set_manage_speed_120);
		btnManageSpeedYes = (Button) findViewById(R.id.btn_dialog_set_manage_speed_yes);
		btnManageSpeedNo = (Button) findViewById(R.id.btn_dialog_set_manage_speed_no);
	}
}
