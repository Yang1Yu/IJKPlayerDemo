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

public class FragmentSetAd extends Fragment {
	private View view;
	private TextView tvAdServer;
	private TextView tvAdPort;
	private TextView tvAdHttp;
	private TextView tvAdLevel;

	private MainActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_ad, container, false);
		tvAdServer = (TextView) view.findViewById(R.id.tv_set_ad_video_server);
		tvAdPort = (TextView) view.findViewById(R.id.tv_set_ad_port);
		tvAdHttp = (TextView) view.findViewById(R.id.tv_set_ad_http);
		tvAdLevel = (TextView) view.findViewById(R.id.tv_set_ad_level);
		tvAdServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetAD".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetAdServerDialog dialog = new MySetAdServerDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tvAdPort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetAD".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetAdPortDialog dialog = new MySetAdPortDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tvAdHttp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetAD".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetAdHttpDialog dialog = new MySetAdHttpDialog(activity);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});
		tvAdLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetAD".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetAdLevelDialog dialog = new MySetAdLevelDialog(activity);
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
