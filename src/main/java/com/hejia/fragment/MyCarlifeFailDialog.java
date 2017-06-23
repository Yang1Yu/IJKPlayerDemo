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

import com.hejia.fragment.FragmentSet.MySetDialogListener;
import com.hejia.tp_launcher_v3.R;

public class MyCarlifeFailDialog extends Dialog {
	private Context context = null;
	private Button btnCarlifeFailIKnow;

	public MyCarlifeFailDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_carlife_fail);
		initView();
		initClickListener();
	}

	private void initClickListener() {
		btnCarlifeFailIKnow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		btnCarlifeFailIKnow= (Button) findViewById(R.id.btn_carlife_fail_iknow);
	}
}
