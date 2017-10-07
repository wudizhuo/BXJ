package com.bxj.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bxj.AppConstants;
import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.activity.MainActivity;
import com.bxj.adpter.BxjListAdpter;
import com.bxj.common.BaseFragment;
import com.bxj.domain.BXJListData;
import com.bxj.manager.BXJDataParseMgr;
import com.bxj.manager.DownLoadMgr;
import com.bxj.manager.StorageManager;
import com.bxj.utils.LogUtil;
import com.bxj.utils.NetUitl;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 此页面待办事项 1.加入等待动画
 *
 * @author SunZhuo
 */

public class ContentBXJFragment extends BaseFragment implements
        OnClickListener, OnRefreshListener2 {
    public static int INDEX = 1;
    public static final String URL_MAIN = "http://bbs.hupu.com/bxj";
    // 表示是否是下拉刷新状态
    public static Boolean ISREFRESHING = false;

    private static List<BXJListData> showDataList = new ArrayList<BXJListData>();
    private List<BXJListData> dataList;
    private ListView contentList;
    private BxjListAdpter mAdpter;
    private View containerView;
    private PullToRefreshListView pullToRefreshListView;
    private View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.fragment_content, null);
        containerView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        emptyView = inflater.inflate(R.layout.view_content_empty, null);
        if (AppPreferences.getHasSlidingGuide()) {
            // 显示guide
        }
        pullToRefreshListView = (PullToRefreshListView) containerView
                .findViewById(R.id.contentList);
        pullToRefreshListView.setMode(Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);
        contentList = pullToRefreshListView.getRefreshableView();
        INDEX = 1;
        initTitle();
        return containerView;
    }

    private void initTitle() {
        TextView tv_titlebar_midtv = (TextView) containerView
                .findViewById(R.id.tv_titlebar_midtv);
        tv_titlebar_midtv.setText(AppConstants.CONTENT_TYPE);
        Button btn_title_left = (Button) containerView
                .findViewById(R.id.btn_title_left);
        View btn_title_right = containerView.findViewById(R.id.btn_title_right);
        btn_title_left
                .setBackgroundResource(R.drawable.content_left_btn_selector);
        btn_title_right
                .setBackgroundResource(R.drawable.content_right_btn_selector);
        btn_title_left.setVisibility(View.VISIBLE);
        btn_title_left.setText("");
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_left.setOnClickListener(this);
        btn_title_right.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (AppConstants.isNeedRestore) {
            LogUtil.s("--onReInitContent---");
            onReInitContent();
        } else {
            LogUtil.s("--initContent---");
            initContent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.s("onPause--暂停了");
    }

    public void settingChanged() {
        if (AppConstants.SETTING_CHANGED) {
            onReInitContent();
            AppConstants.SETTING_CHANGED = !AppConstants.SETTING_CHANGED;
        }
    }

    public void initContent() {
        showDataList.clear();
        if (NetUitl.isConnected()) {
            initContentFromNet();
        } else {
            initContentFromLocal();
        }
    }

    /**
     * 设置修改等 需要重新从本地加载新设置的内容
     */
    private void onReInitContent() {
        if (mAdpter == null) {
            mAdpter = new BxjListAdpter(mContext, showDataList);
            contentList.setAdapter(mAdpter);
        } else {
            mAdpter.notifyDataSetChanged();
        }
    }

    /**
     * 从本地文件初始数据
     */
    private void initContentFromLocal() {
        getBXJContent(false);
    }

    /**
     * 从网络初始数据
     */
    private void initContentFromNet() {
        // 第一次打开相当于是一次下拉刷新
        showProgressDialog();
        pullToRefreshListView.setRefreshing();
        onPullDownToRefresh(pullToRefreshListView);
    }

    private void getBXJContent(final Boolean isGetFromNet) {
        showProgressDialog();
        download(isGetFromNet);
    }

    Observer<Void> observer = new Observer<Void>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Void aVoid) {

        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (ISREFRESHING) {
                pullToRefreshListView.onRefreshComplete();
                ISREFRESHING = false;
            }
            dismissProgressDialog();
            contentList.setEmptyView(emptyView);
        }

        @Override
        public void onComplete() {
            dismissProgressDialog();
            updateListData();
        }
    };

    private void download(final Boolean isGetFromNet) {
        ObservableOnSubscribe<Void> fetchData = new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> emitter) throws Exception {
                if (isGetFromNet && NetUitl.isConnected()) {
                    dataList = getNetData();
                } else {
                    dataList = getLocalData();
                }
                if (ISREFRESHING) {
                    pullToRefreshListView.onRefreshComplete();
                    ISREFRESHING = false;
                }
                emitter.onComplete();
            }
        };

        Observable.create(fetchData).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private List<BXJListData> getLocalData() throws IOException {
        File saveHtmlFile = new File(StorageManager.getInstance().getTypeDir(
                AppConstants.TYPE_BXJ), AppConstants.TYPE_BXJ + "_data_"
                + INDEX);
        return (List<BXJListData>) BXJDataParseMgr.getInstance().parse(
                saveHtmlFile);
    }

    private List<BXJListData> getNetData() throws IOException {
        File saveHtmlFile = new File(StorageManager.getInstance().getTypeDir(
                AppConstants.TYPE_BXJ), AppConstants.TYPE_BXJ + "_data_"
                + INDEX);
        // URL_MAIN = "http://bbs.hupu.com/bxj-" + INDEX;
        String url = URL_MAIN + "-" + INDEX;
        DownLoadMgr.getInstance().getHtmlAndSave(url, saveHtmlFile);
        return (List<BXJListData>) BXJDataParseMgr.getInstance().parse(
                saveHtmlFile);
    }

    /**
     * 更新页面数据
     */
    public void updateListData() {
        // 适应上拉刷新和第一次启动的情况
        if (INDEX == 1) {
            showDataList.clear();
        }
        showDataList.addAll(dataList);
        if (mAdpter == null) {
            mAdpter = new BxjListAdpter(mContext, showDataList);
            contentList.setAdapter(mAdpter);
        } else {
            mAdpter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_title_left:
                ((MainActivity) getActivity()).getSlidingmenu().showMenu();
                break;
            case R.id.btn_title_right:
                ((MainActivity) getActivity()).getSlidingmenu().showSecondaryMenu();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        LogUtil.s("下拉刷新");
        INDEX = 1;
        ISREFRESHING = true;
        getBXJContent(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        LogUtil.s("上拉刷新");
        INDEX++;
        ISREFRESHING = true;
        getBXJContent(true);
    }

    ;

    /**
     * 返回键按下
     *
     * @return
     */
    public boolean onBackPressed() {
        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
            LogUtil.s("-----MainPageFragment--isRefreshing---onBackPressed-----");
            return true;
        }
        return false;
    }

}
