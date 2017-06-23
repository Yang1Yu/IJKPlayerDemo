package com.hejia.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.hejia.eventbus.EventMediaRequest;
import com.hejia.fragment.FragmentMedia;
import com.hejia.media.FilePerate;
import com.hejia.media.GridViewAdapter;
import com.hejia.media.Media;
import com.hejia.media.OrderComparator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hejia.tp_launcher_v3.R;

import de.greenrobot.event.EventBus;

/**
 * @author mengxiangru
 * @date 2016-10-29---上午11:09:07
 * @parameter
 * @return
 */

public class FileChooseDialog extends Dialog {
	private Context context = null;
	MyDialogListener listener;
	private FragmentMedia mFragmentMedia;
	private GridView media_grid_view = null;// gridView对象
	private TextView media_empty_text = null;// 当文件夹为空时显示

	private FilePerate filePerate = null;// 文件操作对象
	List<String> fileList = null;// 当前路径下的所有子文件列表
	List<String> fileListpare = null;// 当前路径下的所有子文件列表
	GridViewAdapter gridAdapter = null;// GridView的适配器
	public static ArrayList<Media> checkedAllList; //选中的所有音乐列表
	List<String> fileParentList = null;// 当前路径下父目录的所有子文件列表
	private Button btn_media_back;
	private Button btn_media_ok;
	private Button btn_media_cancel;
	private Button btn_media_all;
	public static String name;
	public static String url;
	public static int tag = 1;
	public static int tog = 1;
	private int paper = 0;

	private LinearLayout ll_device;
	private LinearLayout ll_card;
	private LinearLayout ll_upan;
	private FrameLayout fl_second_device;
	private LinearLayout ll_root_device;


	public static int SelectedNotification = 0;//提醒适配器0：无状态，1代表全选，2，返回
	private boolean tagButtonAll = true;

	private CheckBox checkBox;
	private File filePath;

	private boolean isCard = false;
	private boolean isUpan = false;


	int checkNum;// 记录选中的条目数量
	// private MyListAdapter myListAdapter;
	public static boolean kecaozuo = false;

	public FileChooseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
	}

	public FileChooseDialog(Context context, MyDialogListener listener) {
		super(context);
		this.context = context;
		this.listener = listener;
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					btn_media_all.setVisibility(View.VISIBLE);
					break;
				case 1:
					btn_media_all.setVisibility(View.GONE);
					ll_root_device.setVisibility(View.VISIBLE);
					fl_second_device.setVisibility(View.GONE);
					break;

				default:
					break;
			}
		}

		;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.media_grid_view);
		filePerate = new FilePerate();// 得到一个文件操作对象
		checkedAllList = new ArrayList<Media>();

		mFragmentMedia = FragmentMedia.getInstance();
		fileList = filePerate.getAllFile(filePerate.getStorageRootFolder());// 得到根目录下的所有子目录
		fileListpare = fileList;
		fileParentList = new ArrayList<String>();
//		String path = filePerate.getCurrentPath();
		media_empty_text = (TextView) findViewById(R.id.media_empty_text);// TextView对象，当文件夹为空时显示“文件夹为空"
		media_grid_view = (GridView) findViewById(R.id.media_grid_view);
		btn_media_back = (Button) findViewById(R.id.btn_media_back);
		btn_media_ok = (Button) findViewById(R.id.btn_media_ok);
		btn_media_cancel = (Button) findViewById(R.id.btn_media_cancel);
		btn_media_all = (Button) findViewById(R.id.btn_media_all);
		btn_media_all.setVisibility(View.GONE);
		ll_device = (LinearLayout) findViewById(R.id.ll_device);
		ll_card = (LinearLayout) findViewById(R.id.ll_card);
		ll_upan = (LinearLayout) findViewById(R.id.ll_upan);
		ll_root_device = (LinearLayout) findViewById(R.id.ll_root_device);
		fl_second_device = (FrameLayout) findViewById(R.id.fl_second_device);
		fl_second_device.setVisibility(View.GONE);
		List<String> paths = filePerate.getCardRootFolder();

		if ((paths != null) && (paths.contains("/mnt/media_rw/extsd"))) {
			isCard = true;
			ll_card.setVisibility(View.VISIBLE);
		}
		if ((paths != null) && (paths.contains("/mnt/media_rw/udisk"))) {
			isUpan = true;
			ll_upan.setVisibility(View.VISIBLE);
		}

		if (tagButtonAll) {
			btn_media_all.setText("全选");
		} else {
			btn_media_all.setText("取消全选");
		}

		gridAdapter = new GridViewAdapter(filePerate, fileList, context, checkedAllList, handler);// 得到适配器
		media_grid_view.setAdapter(gridAdapter);// 为GridView添加适配器
		// 为gridView的子对象设置监听器，即当点击文件时的动作
		media_grid_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				// 得到选中文件的目录

				String path = filePerate.getCurrentPath() + "/"
						+ filePerate.getFileList().get(position);
				fileList = filePerate.getAllFile(path);// 得到选择的文件路径下的文件List
				filePath = new File(path);

				boolean value = filePath.isFile();
				if (value) {// 如果点击的是文件
					tog = 0;
					checkBox = (CheckBox) view
							.findViewById(R.id.checkbox_folder);

					checkBox.setChecked(!checkBox.isChecked());
					if (checkBox.isChecked()) {
						url = filePath.getAbsolutePath();
						name = filePerate.getFileList().get(position);
						checkedAllList.add(new Media(checkedAllList.size(), name, url));

					} else {

						url = filePath.getAbsolutePath();
						name = filePerate.getFileList().get(position);
						for (int i = 0; i < checkedAllList.size(); i++) {
							Media media = checkedAllList.get(i);
							if (media.getUrl().equals(url)) {
								checkedAllList.remove(media);
							}
						}
					}
				}

				if (filePath.isDirectory()) {// 即如果点击的不是文件而是文件夹，更新适配器的数据源
					btn_media_all.setVisibility(View.GONE);
					paper++;
					if (fileList != null) {

						fileParentList = fileList;
						SelectedNotification = 2;//通知适配器全部为未选中状态

						boolean isAllChecked = false;
						isAllChecked = isAllChecked();
						if (isAllChecked) {
							tagButtonAll = false;//回归按钮全选状态
							btn_media_all.setText("取消全选");
						} else {
							tagButtonAll = true;//回归按钮全选状态
							btn_media_all.setText("全选");
						}

						setEmptyTextState(fileList.size());// 设置TextView的状态

						gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变
					}

				}
			}
		});

		ll_device.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				paper++;
				ll_root_device.setVisibility(View.GONE);
				fl_second_device.setVisibility(View.VISIBLE);
//				if(fileList!=null)
//				{
//					fileList.clear();
				fileList = filePerate.getAllFile(filePerate.getStorageRootFolder());// 得到根目录下的所有子目录
				fileListpare = fileList;
				gridAdapter = new GridViewAdapter(filePerate, fileList, context, checkedAllList, handler);// 得到适配器
				media_grid_view.setAdapter(gridAdapter);// 为GridView添加适配器
				gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变
				fileParentList = fileList;
				SelectedNotification = 2;//通知适配器全部为未选中状态
				boolean isAllChecked = false;
				isAllChecked = isAllChecked();
				if (isAllChecked) {
					tagButtonAll = false;//回归按钮全选状态
					btn_media_all.setText("取消全选");
				} else {
					tagButtonAll = true;//回归按钮全选状态
					btn_media_all.setText("全选");
				}
//				}
			}
		});
		ll_card.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				paper++;
				ll_root_device.setVisibility(View.GONE);
				fl_second_device.setVisibility(View.VISIBLE);
//				if(fileList!=null)
//				{
//					fileList.clear();
				fileList = filePerate.getAllFile(filePerate.getCardRootFolder().get(1));// 得到根目录下的所有子目录
				fileListpare = fileList;
				gridAdapter = new GridViewAdapter(filePerate, fileList, context, checkedAllList, handler);// 得到适配器
				media_grid_view.setAdapter(gridAdapter);// 为GridView添加适配器
				gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变
				fileParentList = fileList;
				SelectedNotification = 2;//通知适配器全部为未选中状态
				boolean isAllChecked = false;
				isAllChecked = isAllChecked();
				if (isAllChecked) {
					tagButtonAll = false;//回归按钮全选状态
					btn_media_all.setText("取消全选");
				} else {
					tagButtonAll = true;//回归按钮全选状态
					btn_media_all.setText("全选");
				}
//				}


			}
		});

		ll_upan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (filePerate.existUSB()) {
					paper++;
					ll_root_device.setVisibility(View.GONE);
					fl_second_device.setVisibility(View.VISIBLE);
//				if(fileList!=null)
//				{
//					fileList.clear();
					if (isCard)
						fileList = filePerate.getAllFile(filePerate.getCardRootFolder().get(2));// 得到根目录下的所有子目录
					else
						fileList = filePerate.getAllFile(filePerate.getCardRootFolder().get(1));// 得到根目录下的所有子目录
					fileListpare = fileList;
					gridAdapter = new GridViewAdapter(filePerate, fileList, context, checkedAllList, handler);// 得到适配器
					media_grid_view.setAdapter(gridAdapter);// 为GridView添加适配器
					gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变
					fileParentList = fileList;
					SelectedNotification = 2;//通知适配器全部为未选中状态
					boolean isAllChecked = false;
					isAllChecked = isAllChecked();
					if (isAllChecked) {
						tagButtonAll = false;//回归按钮全选状态
						btn_media_all.setText("取消全选");
					} else {
						tagButtonAll = true;//回归按钮全选状态
						btn_media_all.setText("全选");

					}
				}
//				}
			}
		});


		btn_media_back.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				tagButtonAll = true;//回归按钮全选状态
				btn_media_all.setVisibility(View.GONE);
				btn_media_all.setText("全选");
				if (paper == 0) {

					dismiss();// 如果已经返回到根目录，则关闭兑换框
					mFragmentMedia.isEnableAdd = true;
					return;
				} else {

					if (paper > 1) {
						// 否则得到上级目录
						String path = filePerate.getParentFolder(filePerate.getCurrentPath());
						fileList = filePerate.getAllFile(path);// 更新数据源
						fileParentList = fileList;
						SelectedNotification = 2;
						setEmptyTextState(fileList.size());// 设置TextView的状态
						gridAdapter.notifyDataSetChanged();// 更新GridAdapte

					} else {
						handler.sendEmptyMessage(1);
					}
					paper--;
				}
				boolean isAllChecked = false;
				isAllChecked = isAllChecked();
				if (isAllChecked) {
					tagButtonAll = false;//回归按钮全选状态
					btn_media_all.setText("取消全选");
				} else {
					tagButtonAll = true;//回归按钮全选状态
					btn_media_all.setText("全选");
				}

			}
		});
		//当前两种状态，全选和取消全选
		btn_media_all.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tagButtonAll) {
					tog = 0;
					tagButtonAll = false;
					btn_media_all.setText("取消全选");
					SelectedNotification = 1;
					if (fileListpare != null) {
						fileParentList = fileListpare;
						for (int i = 0; i < fileParentList.size(); i++) {
							String path = filePerate.getCurrentPath() + "/"
									+ filePerate.getFileList().get(i);
							filePath = new File(path);
							boolean value = filePath.isFile();
							if (value) // 如果点击的是文件
							{

								url = filePath.getAbsolutePath();
								name = filePerate.getFileList().get(i);
								checkedAllList.add(new Media(checkedAllList.size(), name, url));
							}


						}
					}
					gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变
				} else {
					tagButtonAll = true;
					btn_media_all.setText("全选");
					SelectedNotification = 0;
					for (int i = (fileParentList.size() - 1); i > -1; i--) {
						String path = filePerate.getCurrentPath() + "/"
								+ filePerate.getFileList().get(i);
						filePath = new File(path);
						boolean value = filePath.isFile();
						if (value) // 如果点击的是文件
						{

							url = filePath.getAbsolutePath();
							name = filePerate.getFileList().get(i);
							for (int j = (checkedAllList.size() - 1); j > -1; j--) {
								Media media = checkedAllList.get(j);
								if (media.getUrl().equals(url)) {
									checkedAllList.remove(media);
								}
							}

						}


					}
					gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变

				}
			}
		});
		btn_media_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tog == 0) {

					SaveCheckedMusic();//将选中的音乐存于数据库
					//删除重复的item
					TreeSet<Media> treeSet = new TreeSet<Media>(new OrderComparator());
					treeSet.addAll(checkedAllList);
					treeSet.addAll(FragmentMedia.myListAdapter.mList);
					FragmentMedia.myListAdapter.mList = new ArrayList<Media>(treeSet);
					FragmentMedia.myListAdapter.notifyDataSetChanged();

					EventBus.getDefault().post(
							new EventMediaRequest("MEDIAADD", "default", "default"));
					checkedAllList.clear();
					SelectedNotification = 0;
					dismiss();
					mFragmentMedia.isEnableAdd = true;
					tog = 1;
				} else if (tog == 1) {
					dismiss();
					mFragmentMedia.isEnableAdd = true;
				}

			}
		});

		btn_media_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				filePerate.fileList.clear();
				if (fileList != null)
					fileList.clear();
				if (checkedAllList != null)
					checkedAllList.clear();
				SelectedNotification = 0;
				dismiss();
				mFragmentMedia.isEnableAdd = true;
			}
		});
	}


	// 设置文本的状态
	public void setEmptyTextState(int num) {
		// 如果该文件夹下存在文件（即num>0)则不显示textView,否则显示
		media_empty_text.setVisibility(View.VISIBLE);
		if (num > 0) {
			media_empty_text.setVisibility(View.GONE);
		}
	}

	//将选中的音乐存于数据库中
	private void SaveCheckedMusic() {
		for (int i = 0; i < checkedAllList.size(); i++) {
			Media media = checkedAllList.get(i);
			String url = media.getUrl();
			String name = media.getTitle();
			url = mFragmentMedia.filterMediain(url);
			name = mFragmentMedia.filterMediain(name);
			try {
				mFragmentMedia.activity.toDoDB.insertMediaUrl(url, name);
			} catch (Exception e) {
			}
		}
	}


	//判断当前文件目录下的所有文件是否全部出去选中状态
	private boolean isAllChecked() {
		int num = 0; //当前目录文件有多少个被选中
		int filenum = 0;//当前目录中文件的个数

		for (int i = 0; i < fileParentList.size(); i++) {
			String path = filePerate.getCurrentPath() + "/"
					+ filePerate.getFileList().get(i);
			filePath = new File(path);
			boolean value = filePath.isFile();
			if (value) {
				filenum++;
			}
			for (int j = 0; j < checkedAllList.size(); j++) {

				Media media = checkedAllList.get(j);
				if (media.getUrl().equals(path)) {
					num++;
					break;
				} else {
					continue;
				}
			}
		}
		if ((num == filenum) && (filenum != 0))
			return true;
		else {
			return false;
		}
	}

}
