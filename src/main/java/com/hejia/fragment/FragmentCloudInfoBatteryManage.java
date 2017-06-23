package com.hejia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FragmentCloudInfoBatteryManageBasic;
import com.hejia.fragment.FragmentCloudInfoBatteryManageModule;
import com.hejia.fragment.FragmentCloudInfoBatteryManageTrouble;
import com.hejia.util.IsFastChangeFragmentUtil;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCloudInfoBatteryManage extends Fragment {
    private RadioGroup rgCloudInfoBattery;
    private RadioButton rbCloudInfoBattery01;
    private RadioButton rbCloudInfoBattery02;
    private RadioButton rbCloudInfoBattery03;
    private LinearLayout llCloudInfoBattery;

    private FragmentCloudInfoBatteryManageBasic fmCloudInfoBattery01;
    private FragmentCloudInfoBatteryManageTrouble fmCloudInfoBattery02;
    private FragmentCloudInfoBatteryManageModule fmCloudInfoBattery03;

    private FragmentManager managerCloudInfoBattery;
    private Fragment mContentFragment;
    private FragmentManager fragmentManager;

    private Fragment showFg = null;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_cloud_info_battery, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);

        return view;
    }
    // EventBus
    @Subscribe(threadMode= ThreadMode.MainThread,sticky = true)
    public void initSystem(EventSystem event){
        if ("INIT".equals(event.getKey())){
            // 界面初始化
            managerCloudInfoBattery = getChildFragmentManager();
            initFragment();
            initView();
            initListener();
            mContentFragment = fmCloudInfoBattery01;
            managerCloudInfoBattery.beginTransaction().replace(R.id.ll_cloud_info_battery, fmCloudInfoBattery01).commit();
        }
    }
    private void initView() {
        rgCloudInfoBattery = (RadioGroup) view.findViewById(R.id.rg_cloud_info_battery);
        rbCloudInfoBattery01 = (RadioButton) view.findViewById(R.id.rb_cloud_info_battery_01);
        rbCloudInfoBattery02 = (RadioButton) view.findViewById(R.id.rb_cloud_info_battery_02);
        rbCloudInfoBattery03 = (RadioButton) view.findViewById(R.id.rb_cloud_info_battery_03);
        llCloudInfoBattery = (LinearLayout) view.findViewById(R.id.ll_cloud_info_battery);
        rbCloudInfoBattery01.setChecked(true);

    }


    private void initFragment() {
        fmCloudInfoBattery01 = new FragmentCloudInfoBatteryManageBasic();
        fmCloudInfoBattery02 = new FragmentCloudInfoBatteryManageTrouble();
        fmCloudInfoBattery03 = new FragmentCloudInfoBatteryManageModule();

    }

    private void initListener() {
        rgCloudInfoBattery.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (IsFastChangeFragmentUtil.isFastChangeFragment()) {

                } else {

                    switch (checkedId) {
                        case R.id.rb_cloud_info_battery_01:
                            rbCloudInfoBattery01.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfoBattery01);
                            break;
                        case R.id.rb_cloud_info_battery_02:
                            rbCloudInfoBattery02.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfoBattery02);
                            break;
                        case R.id.rb_cloud_info_battery_03:
                            rbCloudInfoBattery03.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfoBattery03);
                            break;

                        default:
                            break;
                    }
                }
            }
        });
    }

    private synchronized void changeFragmentCloudInfo(Fragment to) {
        rbtnUnable();
        if (mContentFragment != null && mContentFragment != to) {
            FragmentTransaction transaction = managerCloudInfoBattery.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContentFragment).add(R.id.ll_cloud_info_battery, to).commit();
            } else {
                transaction.hide(mContentFragment).show(to).commit();
            }
            mContentFragment = to;
        }
    }

    private void rbtnUnable() {
        rbCloudInfoBattery01.setEnabled(false);
        rbCloudInfoBattery02.setEnabled(false);
        rbCloudInfoBattery03.setEnabled(false);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rbCloudInfoBattery01.setEnabled(true);
        rbCloudInfoBattery02.setEnabled(true);
        rbCloudInfoBattery03.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除EventBus
        EventBus.getDefault().unregister(this);
    }
}
