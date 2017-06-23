package com.hejia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hejia.tp_launcher_v3.R;
import com.hejia.bean.PairDevice;
import com.hejia.fragment.FragmentBtCTD;

import java.util.ArrayList;

/**
 * 
 * @author wyn
 * 
 */
public class AdapterBtCTDLv extends BaseAdapter {
	public ArrayList<PairDevice> list;
	private LayoutInflater inflater;
	private FragmentBtCTD fragmentBtCTD;

	public AdapterBtCTDLv(Context context, ArrayList<PairDevice> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		fragmentBtCTD = FragmentBtCTD.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.lv_item_btctd, null);
			holder.tvPairDeviceName = (TextView) convertView.findViewById(R.id.tv_bt_ctd_name);
			holder.ibtnDelete = (ImageButton) convertView.findViewById(R.id.ibtn_bt_ctd_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPairDeviceName.setText(list.get(position).getPairDeviceName());
		// 删除某条数据
		holder.ibtnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position > (list.size() - 1)) {
					return;
				}
				PairDevice pairDevice = list.get(position);
				String pairDeviceBD = pairDevice.getPairDeviceBd();
				// 删除数据库中内容
				fragmentBtCTD.activity.toDoDB.deletePairDevice(pairDeviceBD);
				// 发送蓝牙命令
				fragmentBtCTD.deletePairDeviceBt(pairDeviceBD);
				// 更新list列表
				list.remove(position);
				notifyDataSetChanged();
			}

		});
		return convertView;
	}

	private class ViewHolder {
		TextView tvPairDeviceName;
		ImageButton ibtnDelete;
	}


}
