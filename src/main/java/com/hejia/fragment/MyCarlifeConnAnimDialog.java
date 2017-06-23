package com.hejia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.hejia.tp_launcher_v3.R;

public class MyCarlifeConnAnimDialog extends Dialog {
	private Context context = null;

	public MyCarlifeConnAnimDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_carlife_anim);
		initView();
		initClickListener();
	}

	private void initClickListener() {
	}

	private void initView() {
	}
}
