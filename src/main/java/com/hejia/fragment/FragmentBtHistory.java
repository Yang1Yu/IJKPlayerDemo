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
import android.widget.ListView;

import com.hejia.adapter.AdapterBtHistoryLv;
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

public class FragmentBtHistory extends Fragment {
    private View view;
    private MainActivity activity;
    private TreeSet<User> treeSet_history;
    private AdapterBtHistoryLv adapterBtHistory;
    private ListView lv_bt_history;
    private String number;
    private Intent changeIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_bt_history, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            treeSet_history = activity.history_TreeSet;
            adapterBtHistory = new AdapterBtHistoryLv(activity, treeSet_history);
            lv_bt_history = (ListView) view.findViewById(R.id.lv_bt_history);
            lv_bt_history.setAdapter(adapterBtHistory);
            lv_bt_history.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    if ("BtHistory".equals(com.hejia.fragment.FragmentBt.nowBtFragment) && "BT".equals(activity.nowFragment)) {

                        number = adapterBtHistory.list.get(position).getNumber();
                        activity.bluetoothBindService.send_Command("AT+B HFDIAL 0," + number + "\r\n");
                        activity.sendBroadcast(changeIntent);
                    }
                    return false;
                }
            });
        }
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void btEvent(EventBTRequest event) {
        switch (event.getKey()) {
            case "BTDISCONN":
                //蓝牙断开连接
                adapterBtHistory.list.clear();
                adapterBtHistory.notifyDataSetChanged();
                break;
            case "HISTORY":
                //通话记录
                treeSet_history = activity.history_TreeSet;
                adapterBtHistory.list.clear();
                adapterBtHistory.list.addAll(treeSet_history);
                adapterBtHistory.notifyDataSetChanged();
                break;
        }
    }

}
