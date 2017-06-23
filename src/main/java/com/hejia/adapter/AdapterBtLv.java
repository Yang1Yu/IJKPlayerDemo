package com.hejia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hejia.tp_launcher_v3.R;
import com.hejia.bean.User;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author wyn
 */
public class AdapterBtLv extends BaseAdapter {
    public List<User> list;
    private LayoutInflater inflater;

    public AdapterBtLv(Context context, TreeSet<User> treeSet) {
        list = new ArrayList<User>();
        list.addAll(treeSet);
        this.inflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.lv_item_btpb, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_bt_pcb_name);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_bt_pcb_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_number.setText(list.get(position).getNumber());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;
        TextView tv_number;
    }

}
