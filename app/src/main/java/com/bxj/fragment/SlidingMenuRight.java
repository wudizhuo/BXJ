package com.bxj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bxj.AppConstants;
import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.activity.SettingActivity;
import com.bxj.common.BaseActivity;
import com.bxj.common.BaseFragment;
import com.bxj.utils.LogUtil;
import com.bxj.utils.StatServiceUtil;
import com.bxj.utils.ToastUtil;

public class SlidingMenuRight extends BaseFragment implements OnClickListener{
    private TextView tv_setting;
    private ImageView iv_setting_night;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slidingright, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        tv_setting = (TextView) view.findViewById(R.id.tv_setting);
        iv_setting_night = (ImageView) view.findViewById(R.id.iv_setting_night);
        tv_setting.setOnClickListener(this);
        view.findViewById(R.id.feedback).setOnClickListener(this);
        view.findViewById(R.id.check_update).setOnClickListener(this);
        view.findViewById(R.id.setting_night).setOnClickListener(this);
        initBtn();
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.setting_night:
                clickNightMode();
                break;
            case R.id.check_update:
                checkUpdate();
                break;
            case R.id.feedback:
                ToastUtil.show("请您反馈到我的微博'无敌卓'，谢谢支持");
                break;
            default:
                break;
        }
    }

    private void checkUpdate() {
        showProgressDialog("正在检查更新...");
        StatServiceUtil.trackEvent("检查更新");
    }

    private void clickNightMode() {
        AppConstants.SETTING_MODE_NIGHT = !AppConstants.SETTING_MODE_NIGHT;
        initBtn();
        AppPreferences.setSettingModeNight(AppConstants.SETTING_MODE_NIGHT);
        ((BaseActivity) getActivity()).setTheme();
        AppConstants.isNeedRestore = true;
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().startActivity(intent);
        if (AppConstants.SETTING_MODE_NIGHT) {
            LogUtil.s("夜间模式-开");
            StatServiceUtil.trackEvent("夜间模式-开");
        } else {
            StatServiceUtil.trackEvent("夜间模式-关");
        }
    }

    private void initBtn() {
        if (AppConstants.SETTING_MODE_NIGHT) {
            iv_setting_night.setBackgroundResource(R.drawable.setting_open);
        } else {
            iv_setting_night.setBackgroundResource(R.drawable.setting_close);
        }
    }

}
