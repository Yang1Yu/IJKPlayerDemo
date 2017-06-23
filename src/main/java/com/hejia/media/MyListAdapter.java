package com.hejia.media;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hejia.eventbus.EventMediaRequest;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FragmentMedia;
import com.hejia.serialport.DataConvert;
import com.hejia.util.IsFastChangeFragmentUtil;
import com.hejia.util.IsFastDoubleMediaListClickUtil;

import java.util.ArrayList;
import java.util.TreeSet;

import de.greenrobot.event.EventBus;

/**
 * @author mengxiangru
 * @date 2016-10-29---上午10:06:03
 * @parameter
 * @return
 */

public class MyListAdapter extends BaseAdapter {
	/**
	 * 上下文对象
	 */
	private Context context;
	public int tag = 0;
	TreeSet<Media> treeSet;
	// 数据的集合
	public ArrayList<Media> mList;
//	DataConvert mDataConvert = new DataConvert();

	FragmentMedia mFragmentMedia = null;

	public MyListAdapter(Context context, TreeSet<Media> set) {
		this.context = context;
		this.mList = new ArrayList<Media>();
		mFragmentMedia = FragmentMedia.getInstance();

		// 删除重复的item
		treeSet = new TreeSet<Media>(new OrderComparator());
		treeSet.addAll(set);
		mList = new ArrayList<Media>(treeSet);
	}

	/*
	 * // 移除一个项目的时候 public void remove(int position) {
	 * this.mList.remove(position); }
	 */

	/**
	 * 获取最大id
	 */
	public int getMax() {
		int num = 0;
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getId() > num) {
				num = mList.get(i).getId();
			}
		}
		return num;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_media, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_media_item_image);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_media_item_name);
			viewHolder.delete = (LinearLayout) convertView.findViewById(R.id.ll_media_delete);
//			viewHolder.listMedia = (LinearLayout) convertView.findViewById(R.id.ll_list);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// mDataConvert.sortBySuffix(mList);
		if (position <= (mList.size() - 1)) {

			Media m = mList.get(position);
			viewHolder.textView.setText(m.getTitle());

			if (m.getTitle().contains("mp3")) {
				viewHolder.imageView.setBackgroundResource(R.drawable.img_media_item_video);
			} else {
				viewHolder.imageView.setBackgroundResource(R.drawable.img_media_item_music);
			}
		}
		// 删除某条数据
		viewHolder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position > (mList.size() - 1)) {
					return;
				}
				Media media = mList.get(position);
				String url = media.getUrl();
				String name = media.getTitle();
				url = mFragmentMedia.filterMediain(url);
				mFragmentMedia.activity.toDoDB.deleteMediaUrl(url);
				url = mFragmentMedia.filterMediaout(url);
				mList.remove(position);
				mFragmentMedia.mediaTreeSet.remove(new Media(FragmentMedia.myListAdapter.getMax() + 1, name, url));

				notifyDataSetChanged();

				if (FragmentMedia.myListAdapter.mList.size() == 0) {
					EventBus.getDefault().post(
							new EventMediaRequest("MEDIANULL", "default", "default"));
				} else {

					EventBus.getDefault().post(
							new EventMediaRequest("MEDIADELETE", name, "default"));
				}
			}

		});

		viewHolder.textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IsFastDoubleMediaListClickUtil.isFastDoubleClick()) {

				} else {
					if (position > (mList.size() - 1)) {
						return;
					}
					EventBus.getDefault().post(
							new EventMediaRequest("LISTENER", position + "", "default"));
				}

			}
		});


		return convertView;
	}

	public class ViewHolder {
		ImageView imageView;
		TextView textView;
		LinearLayout delete;
	}


}
