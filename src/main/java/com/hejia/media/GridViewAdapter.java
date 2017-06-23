
package com.hejia.media;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FileChooseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mengxiangru
 * @date 2016-10-29---����2:07:41
 * @version
 * @parameter
 * @since
 * @return
 */

public class GridViewAdapter extends BaseAdapter {

	List<String> fileList = null;
	LayoutInflater flater = null;
	FilePerate filePerate = null;
	Context context = null;
	int fileNum = 0;// 文件数
	int folderNum = 0;// 目录数
	public static ArrayList<Media> list;// 被选中项的集合
	public static int checkNum;
	public static int tag = 1;
	private OnCheckListener callBack;
	private Handler handler;
//	private static HashMap<Integer, Boolean> mIsSelected;//保存每个复选框的选中状态


	public GridViewAdapter(FilePerate filePerate, List<String> fileList, Context context, ArrayList<Media> checkedList, Handler mhandler) {
		flater = LayoutInflater.from(context);
		this.context = context;
		this.fileList = fileList;
		this.filePerate = filePerate;
		// 初始化被选中项的集合
		list = new ArrayList<Media>();
		list = checkedList;
		handler = mhandler;
	}

//	public HashMap<Integer, Boolean> getIsSelected() {
//		return mIsSelected;
//	}
//
//	public void putIsSelected(int position, boolean isSelected) {
//		mIsSelected.put(position, isSelected);
//	}

	@Override
	public int getCount() {
		return fileList.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return fileList.get(position);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		fileNum = filePerate.getFileNum();// 获取文件个数
		folderNum = filePerate.getFolderNum();// 获取文件夹个数
		filePerate.getFileList().get(position).endsWith(".mp3");
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = flater.inflate(R.layout.media_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.fileIcon);
			viewHolder.title = (MyTextView) convertView.findViewById(R.id.fileName);
			viewHolder.checkBox1 = (CheckBox) convertView.findViewById(R.id.checkbox_folder);
			viewHolder.checkBox2 = (CheckBox) convertView.findViewById(R.id.checkbox_file);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (filePerate.getFileList().get(position).endsWith(".mp3")
				|| filePerate.getFileList().get(position).endsWith(".mp4")
				||filePerate.getFileList().get(position).endsWith(".wma")
				|| filePerate.getFileList().get(position).endsWith(".wav")
				|| filePerate.getFileList().get(position).endsWith(".wmv")
				|| filePerate.getFileList().get(position).endsWith(".avi")) {
			viewHolder.image.setImageResource(R.drawable.img_media_file);// 文件的图标
			viewHolder.checkBox1.setVisibility(View.VISIBLE);
			viewHolder.checkBox2.setVisibility(View.GONE);
			handler.sendEmptyMessage(0);

		} else {
			viewHolder.image.setImageResource(R.drawable.img_media_folder);// 目录的图标
			viewHolder.checkBox1.setVisibility(View.GONE);
			viewHolder.checkBox2.setVisibility(View.GONE);
		}


		viewHolder.title.setText(fileList.get(position));// 文件名
		switch (FileChooseDialog.SelectedNotification) {
			case 0:
				viewHolder.checkBox1.setChecked(false);
				break;
			case 1:
				viewHolder.checkBox1.setChecked(true);
				break;
			case 2:
				String path = filePerate.getCurrentPath() + "/"
						+ filePerate.getFileList().get(position);
				for(int i = 0;i<FileChooseDialog.checkedAllList.size();i++)
				{
					Media media = FileChooseDialog.checkedAllList.get(i);
					if(media.getUrl().equals(path))
					{
						viewHolder.checkBox1.setChecked(true);
						return convertView;
					}
					else
					{
						viewHolder.checkBox1.setChecked(false);
					}
				}
				break;

			default:
				break;
		}

		return convertView;
	}

	OnCheckListener setOnClickListener(OnCheckListener l) {
		this.callBack = l;
		return callBack;
	}

	static class ViewHolder {
		ImageView image;
		MyTextView title;
		CheckBox checkBox1;
		CheckBox checkBox2;

	}


}


interface OnCheckListener {
	void quanxuan(boolean isAll);
}
