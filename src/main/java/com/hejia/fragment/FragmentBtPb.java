package com.hejia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.hejia.adapter.AdapterBtLv;
import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.bean.User;
import com.hejia.fragment.*;

import java.util.TreeSet;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentBtPb extends Fragment {
    private View view;
    private MainActivity activity;
    private TreeSet<User> treeSet;
    private ListView lvBtPb;
    private AdapterBtLv adapterBtPb;
    private Intent changeIntent;
    private String number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_bt_pb, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            treeSet = activity.user_TreeSet;
            adapterBtPb = new AdapterBtLv(activity, treeSet);
            lvBtPb = (ListView) view.findViewById(R.id.lv_bt_pb);
            lvBtPb.setAdapter(adapterBtPb);
            lvBtPb.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("BtPB".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {
                        number = adapterBtPb.list.get(position).getNumber();
                        activity.bluetoothBindService.send_Command("AT+B HFDIAL 0," + number + "\r\n");
                        activity.sendBroadcast(changeIntent);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeIntent = new Intent();
        changeIntent.setAction("CHANGEDAILFRAGMENT");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除EventBus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void btEvent(EventBTRequest event) {
        switch (event.getKey()) {
            case "BTDISCONN":
                //蓝牙断开连接
                adapterBtPb.list.clear();
                adapterBtPb.notifyDataSetChanged();
                break;
            case "PB":
                //接收电话簿信息
                treeSet = activity.user_TreeSet;
                adapterBtPb.list.clear();
                adapterBtPb.list.addAll(treeSet);
                adapterBtPb.notifyDataSetChanged();
                break;


        }
    }
}
