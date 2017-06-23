package com.hejia.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

public class FragmentSetManage extends Fragment {
	private View view;
	private TextView tvManageSpeed;
	private TextView tvManageServer;
	private TextView tvManagePort;

	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_manage, container, false);
		tvManageSpeed = (TextView) view.findViewById(R.id.tv_set_manage_speed_limit);
		tvManagePort = (TextView) view.findViewById(R.id.tv_set_manage_port);
		tvManageServer = (TextView) view.findViewById(R.id.tv_set_manage_remote_server);

		tvManageSpeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetManage".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetManageSpeedDialog dialog = new MySetManageSpeedDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tvManagePort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetManage".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetManagePortDialog dialog = new MySetManagePortDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tvManageServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetManage".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetManageServerDialog dialog = new MySetManageServerDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
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
}
