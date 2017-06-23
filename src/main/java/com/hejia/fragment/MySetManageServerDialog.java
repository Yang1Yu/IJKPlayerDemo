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

public class MySetManageServerDialog extends Dialog {
	private Context context = null;
	MySetDialogListener listener;
	private EditText editTextManageServerMain;
	private EditText editTextManageServerAnother;
	private Button btnManageServerYes;
	private Button btnManageServerNo;

	public MySetManageServerDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public MySetManageServerDialog(Context context, MySetDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_set_manage_server);
		initView();
		initClickListener();
	}

	private void initClickListener() {
		btnManageServerYes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btnManageServerNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void initView() {
		editTextManageServerMain = (EditText) findViewById(R.id.et_dialog_set_manage_server_main);
		editTextManageServerMain.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
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
		editTextManageServerAnother = (EditText) findViewById(R.id.et_dialog_set_manage_server_another);
		editTextManageServerAnother.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
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
		btnManageServerYes = (Button) findViewById(R.id.btn_dialog_set_manage_server_yes);
		btnManageServerNo = (Button) findViewById(R.id.btn_dialog_set_manage_server_no);
	}
}
