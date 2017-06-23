package com.hejia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hejia.myview.DateTimePickDialogUtil;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

public class FragmentSetRoutine extends Fragment {
	private View view;
	private MainActivity activity;
	private TextView tvDateAndTime;
	private String initStartDateTime = "2017年12月20日 14:44"; // 初始化时间

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_routine, container, false);
		tvDateAndTime = (TextView) view.findViewById(R.id.tv_set_routine_time);
		tvDateAndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetRoutine".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(activity, initStartDateTime);
					dateTimePicKDialog.dateTimePicKDialog();
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
