package com.hejia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FragmentSet.MySetDialogListener;

public class MySetAboutUpgradeErrDialog extends Dialog {
	private Context context = null;

	public MySetAboutUpgradeErrDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetAboutUpgradeErrDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_about_upgrade_err);
	}

}
