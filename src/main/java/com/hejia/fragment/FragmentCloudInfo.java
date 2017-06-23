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
import com.hejia.fragment.FragmentCloudInfoBatteryManage;
import com.hejia.fragment.FragmentCloudInfoBusCAN;
import com.hejia.fragment.FragmentCloudInfoBusControl;
import com.hejia.fragment.FragmentCloudInfoCharger;
import com.hejia.fragment.FragmentCloudInfoDCAC;
import com.hejia.fragment.FragmentCloudInfoElecMech;
import com.hejia.fragment.FragmentCloudInfoHighVoltage;
import com.hejia.fragment.FragmentCloudInfoInstrument;
import com.hejia.util.IsFastChangeFragmentUtil;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCloudInfo extends Fragment {

    private RadioGroup rgCloudInfo;
    private RadioButton rbCloudInfo01;
    private RadioButton rbCloudInfo02;
    private RadioButton rbCloudInfo03;
    private RadioButton rbCloudInfo04;
    private RadioButton rbCloudInfo05;
    private RadioButton rbCloudInfo06;
    private RadioButton rbCloudInfo07;
    private RadioButton rbCloudInfo08;
    private LinearLayout fragmentCloudInfo;

    private FragmentCloudInfoBusControl fmCloudInfo01;
    private FragmentCloudInfoElecMech fmCloudInfo02;
    private FragmentCloudInfoBatteryManage fmCloudInfo03;
    private FragmentCloudInfoInstrument fmCloudInfo04;
    private FragmentCloudInfoHighVoltage fmCloudInfo06;
    private FragmentCloudInfoDCAC fmCloudInfo05;
    private FragmentCloudInfoCharger fmCloudInfo07;
    private FragmentCloudInfoBusCAN fmCloudInfo08;

    // 设置界面的子碎片管理
    private FragmentManager managerCloudInfo;
    private Fragment mContentFragment;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fm_cloud_info, container, false);
        // 注册EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    // EventBus
    @Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
    public void initSystem(EventSystem event) {
        if ("INIT".equals(event.getKey())) {
            // 界面初始化
            managerCloudInfo = getChildFragmentManager();
            initFragment();
            initView();
            initListener();
            managerCloudInfo.beginTransaction().replace(R.id.fragment_cloud_info, fmCloudInfo02).commit();
            mContentFragment = fmCloudInfo02;
            rbCloudInfo02.setChecked(true);
        }
    }

    private void initView() {
        rgCloudInfo = (RadioGroup) view.findViewById(R.id.rg_cloud_info);
        rbCloudInfo01 = (RadioButton) view.findViewById(R.id.rb_cloud_info_01);
        rbCloudInfo02 = (RadioButton) view.findViewById(R.id.rb_cloud_info_02);
        rbCloudInfo03 = (RadioButton) view.findViewById(R.id.rb_cloud_info_03);
        rbCloudInfo04 = (RadioButton) view.findViewById(R.id.rb_cloud_info_04);
        rbCloudInfo05 = (RadioButton) view.findViewById(R.id.rb_cloud_info_05);
        rbCloudInfo06 = (RadioButton) view.findViewById(R.id.rb_cloud_info_06);
        rbCloudInfo07 = (RadioButton) view.findViewById(R.id.rb_cloud_info_07);
        rbCloudInfo08 = (RadioButton) view.findViewById(R.id.rb_cloud_info_08);
        fragmentCloudInfo = (LinearLayout) view.findViewById(R.id.fragment_cloud_info);
    }

    private void initFragment() {
        fmCloudInfo01 = new FragmentCloudInfoBusControl();
        fmCloudInfo02 = new FragmentCloudInfoElecMech();
        fmCloudInfo03 = new FragmentCloudInfoBatteryManage();
        fmCloudInfo04 = new FragmentCloudInfoInstrument();
        fmCloudInfo05 = new FragmentCloudInfoDCAC();
        fmCloudInfo06 = new FragmentCloudInfoHighVoltage();
        fmCloudInfo07 = new FragmentCloudInfoCharger();
        fmCloudInfo08 = new FragmentCloudInfoBusCAN();

    }


    private void initListener() {
        rgCloudInfo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (IsFastChangeFragmentUtil.isFastChangeFragment()) {

                } else {

                    switch (checkedId) {
                        case R.id.rb_cloud_info_01:
                            rbCloudInfo01.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo01);
                            break;
                        case R.id.rb_cloud_info_02:
                            rbCloudInfo02.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo02);
                            break;
                        case R.id.rb_cloud_info_03:
                            rbCloudInfo03.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo03);
                            break;
                        case R.id.rb_cloud_info_04:
                            rbCloudInfo04.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo04);
                            break;
                        case R.id.rb_cloud_info_05:
                            rbCloudInfo05.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo05);
                            break;
                        case R.id.rb_cloud_info_06:
                            rbCloudInfo06.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo06);
                            break;
                        case R.id.rb_cloud_info_07:
                            rbCloudInfo07.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo07);
                            break;
                        case R.id.rb_cloud_info_08:
                            rbCloudInfo08.setChecked(true);
                            changeFragmentCloudInfo(fmCloudInfo08);
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
            FragmentTransaction transaction = managerCloudInfo.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContentFragment).add(R.id.fragment_cloud_info, to).commit();
            } else {
                transaction.hide(mContentFragment).show(to).commit();
            }
            mContentFragment = to;
        }
    }

    private void rbtnUnable() {
        rbCloudInfo01.setEnabled(false);
        rbCloudInfo02.setEnabled(false);
        rbCloudInfo03.setEnabled(false);
        rbCloudInfo04.setEnabled(false);
        rbCloudInfo05.setEnabled(false);
        rbCloudInfo06.setEnabled(false);
        rbCloudInfo07.setEnabled(false);
        rbCloudInfo08.setEnabled(false);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rbCloudInfo01.setEnabled(true);
        rbCloudInfo02.setEnabled(true);
        rbCloudInfo03.setEnabled(true);
        rbCloudInfo04.setEnabled(true);
        rbCloudInfo05.setEnabled(true);
        rbCloudInfo06.setEnabled(true);
        rbCloudInfo07.setEnabled(true);
        rbCloudInfo08.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 解除EventBus
        EventBus.getDefault().unregister(this);
    }
}
