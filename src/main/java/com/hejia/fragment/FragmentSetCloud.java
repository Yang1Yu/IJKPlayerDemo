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

public class FragmentSetCloud extends Fragment {
	private View view;
	private TextView tvCloudServer;
	private TextView tvCloudPort;
	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_cloud, container, false);
		tvCloudServer = (TextView) view.findViewById(R.id.tv_set_cloud_server);
		tvCloudPort = (TextView) view.findViewById(R.id.tv_set_cloud_port);
		tvCloudServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetCloud".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetCloudServerDialog dialog = new MySetCloudServerDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tvCloudPort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetCloud".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetCloudPortDialog dialog = new MySetCloudPortDialog(activity);
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
