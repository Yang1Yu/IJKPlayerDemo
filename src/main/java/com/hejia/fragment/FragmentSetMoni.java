package com.hejia.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;
import com.hejia.fragment.FragmentSet;

public class FragmentSetMoni extends Fragment {
	private View view;
	private TextView tv_set_moni_px;
	private TextView tv_set_moni_quality;
	private TextView tv_set_moni_passage;
	private MainActivity activity;

	private Dialog dialog_px;
	private Window dialogPxWindow;
	private WindowManager.LayoutParams dialogPxLp;

	private RelativeLayout rvPx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_moni, container, false);
		tv_set_moni_px = (TextView) view.findViewById(R.id.tv_set_moni_px);
		tv_set_moni_quality = (TextView) view.findViewById(R.id.tv_set_moni_quality);
		tv_set_moni_passage = (TextView) view.findViewById(R.id.tv_set_moni_passage);
		initLayout();
		initView();
		initClickListener();
		initDialog();
		setDialog();
		tv_set_moni_px.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetMoni".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					com.hejia.fragment.MySetPxDialog dialog = new com.hejia.fragment.MySetPxDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tv_set_moni_quality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetMoni".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetQualityDialog dialog = new MySetQualityDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tv_set_moni_passage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetMoni".equals(FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetPassageDialog dialog = new MySetPassageDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		return view;
	}

	private void initClickListener() {
	}

	private void initView() {
	}

	private void initLayout() {
		rvPx = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dialog_set_moni_px, null);
	}

	private void initDialog() {
		dialog_px = new Dialog(activity, R.style.MyDialog);
		dialog_px.setContentView(R.layout.dialog_set_moni_px);
	}

	private void setDialog() {
		dialogPxWindow = dialog_px.getWindow();
		dialogPxLp = dialogPxWindow.getAttributes();
		setDialogDetail(dialogPxWindow, dialogPxLp, 200, 300);
	}

	private void setDialogDetail(Window dialogWindow, WindowManager.LayoutParams dialogLp, int height, int width) {
		dialogWindow.setGravity(Gravity.CENTER);
		dialogLp.width = width;
		dialogLp.height = height;
		dialogWindow.setAttributes(dialogLp);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}
}
