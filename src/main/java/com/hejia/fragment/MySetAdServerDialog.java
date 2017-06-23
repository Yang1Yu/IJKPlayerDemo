package com.hejia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FragmentSet.MySetDialogListener;

public class MySetAdServerDialog extends Dialog {
	private Context context = null;
	MySetDialogListener listener;
	private EditText editTextAdServerMain;
	private EditText editTextAdServerAnother;
	private Button btnAdServerYes;
	private Button btnAdServerNo;

	public MySetAdServerDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetAdServerDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_ad_server);
		initView();
		initClickListener();
	}

	private void initClickListener() {
		btnAdServerYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btnAdServerNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		editTextAdServerMain = (EditText) findViewById(R.id.et_dialog_set_ad_server_main);
		editTextAdServerMain.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode actionMode) {

			}
		});
		editTextAdServerAnother = (EditText) findViewById(R.id.et_dialog_set_ad_server_another);
		editTextAdServerAnother.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
			@Override
			public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode actionMode) {

			}
		});
		btnAdServerNo = (Button) findViewById(R.id.btn_dialog_set_ad_server_no);
		btnAdServerYes = (Button) findViewById(R.id.btn_dialog_set_ad_server_yes);
	}
}
