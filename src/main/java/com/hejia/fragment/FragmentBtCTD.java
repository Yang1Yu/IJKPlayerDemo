package com.hejia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hejia.adapter.AdapterBtCTDLv;
import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.bean.PairDevice;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentBtCTD extends Fragment {
    private View view;
    public MainActivity activity;
    private ListView lvBtCTD;
    private TextView tvPairDeviceNow;
    private TextView tvTitleAnother;
    private ImageView ivBTCTDLine;

    private Cursor pairDeviceCursor;
    public ArrayList<PairDevice> pairDeviceList;
    // private Iterator<PairDevice> pairDeviceListIterator;
    private AdapterBtCTDLv adapterBtCTDLv;
    private String deletePairDeviceBD;

    public static FragmentBtCTD fragmentBtCTD = null;

    public static FragmentBtCTD getInstance()

    {
        if (fragmentBtCTD == null) {
            fragmentBtCTD = new FragmentBtCTD();
        }
        return fragmentBtCTD;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_bt_ctd, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除EventBus
        EventBus.getDefault().unregister(this);
    }

    private int i = 0;

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void btEvent(EventBTRequest event){
        switch (event.getKey()){
            case "BTCONN":
                //蓝牙已连接
                if (activity.bluetoothBindService.device_name != "") {
                    tvPairDeviceNow.setText(activity.bluetoothBindService.device_name);
                }
                adapterBtCTDLv.list.clear();
                // pairDeviceList.clear();
                pairDeviceCursor = activity.toDoDB.getPairDevice();
                if (pairDeviceCursor != null) {
                    while (pairDeviceCursor.moveToNext()) {
                        if (!activity.bluetoothBindService.isConnPairDeviceNow(pairDeviceCursor.getString(0))) {
                            adapterBtCTDLv.list
                                    .add(new PairDevice(pairDeviceCursor.getString(0), pairDeviceCursor.getString(1)));
                        }
                    }
                }
                pairDeviceCursor.close();
                if (adapterBtCTDLv.list.size() <= 0) {
                    tvTitleAnother.setVisibility(View.GONE);
                    ivBTCTDLine.setVisibility(View.GONE);
                } else {
                    tvTitleAnother.setVisibility(View.VISIBLE);
                    ivBTCTDLine.setVisibility(View.VISIBLE);
                }
                // adapterBtCTDLv.list.addAll(pairDeviceList);
                adapterBtCTDLv.notifyDataSetChanged();
                break;
            case "BTDISCONN":
                //蓝牙断开连接
                adapterBtCTDLv.list.clear();
                adapterBtCTDLv.notifyDataSetChanged();
                tvPairDeviceNow.setText("");
                break;
        }
    }

    // 蓝牙中删除配对设备
    public void deletePairDeviceBt(String pairDeviceBD) {
        activity.bluetoothBindService.deletePairDevice(pairDeviceBD);
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            lvBtCTD = (ListView) view.findViewById(R.id.lv_bt_ctd);
            tvPairDeviceNow = (TextView) view.findViewById(R.id.tv_bt_ctd_nowpair);
            tvTitleAnother = (TextView) view.findViewById(R.id.tv_bt_ctd_title_another);
            ivBTCTDLine = (ImageView) view.findViewById(R.id.iv_bt_ctd_line3);

            if (activity.bluetoothBindService.device_name != "") {
                tvPairDeviceNow.setText(activity.bluetoothBindService.device_name);
            }
            pairDeviceList = new ArrayList<>();

            pairDeviceCursor = activity.toDoDB.getPairDevice();
            if (pairDeviceCursor != null) {
                while (pairDeviceCursor.moveToNext()) {
                    // 判断是否为正在连接的设备
                    if (!activity.bluetoothBindService.isConnPairDeviceNow(pairDeviceCursor.getString(0))) {
                        pairDeviceList.add(new PairDevice(pairDeviceCursor.getString(0), pairDeviceCursor.getString(1)));
                    }
                }

            }
            if (pairDeviceList.size() <= 0) {
                tvTitleAnother.setVisibility(View.GONE);
                ivBTCTDLine.setVisibility(View.GONE);
            } else {
                tvTitleAnother.setVisibility(View.VISIBLE);
                ivBTCTDLine.setVisibility(View.VISIBLE);
            }
            pairDeviceCursor.close();
            // pairDeviceListIterator = pairDeviceList.iterator();
            adapterBtCTDLv = new AdapterBtCTDLv(activity, pairDeviceList);
            lvBtCTD.setAdapter(adapterBtCTDLv);
        }
    }
}
