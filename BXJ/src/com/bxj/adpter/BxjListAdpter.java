package com.bxj.adpter;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bxj.AppConstants;
import com.bxj.R;
import com.bxj.activity.WebContentActivity;
import com.bxj.domain.BXJListData;
import com.bxj.utils.LogUtil;

/**
 * 此页面待办事项 1.换掉按下颜色
 * 
 * @author SunZhuo
 * 
 */

public class BxjListAdpter extends BaseAdapter {

	private List<BXJListData> list;
	private LayoutInflater mInflater;
	private Context mContext;

	public BxjListAdpter(Context mContext, List<BXJListData> list) {
		this.list = list;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
		filterContent();
	}

	@Override
	public void notifyDataSetChanged() {
		filterContent();
		super.notifyDataSetChanged();
	}

	/**
	 * 过滤内容 只看亮贴等过滤条件
	 */
	private void filterContent() {
		LogUtil.s("---filterContent----");
		// 如果设置为只看亮贴 则过滤内容 只显示亮贴
		if (AppConstants.SETTING_BXJ_LIGHT) {
			Iterator<BXJListData> iterator = list.iterator();
			while (iterator.hasNext()) {
				BXJListData data = (BXJListData) iterator.next();
				if (data.getLightCount().equals("0")) {
					iterator.remove();
				}
			}
		}
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
		final BXJListData info = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.item_bxj_list, null);

			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.tv_lightCount = (TextView) convertView
					.findViewById(R.id.tv_lightCount);
			holder.tv_replyCount = (TextView) convertView
					.findViewById(R.id.tv_replyCount);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_title.setText(info.getTitle());
		holder.tv_lightCount.setText(info.getLightCount() + "");
		holder.tv_replyCount.setText(info.getReplyCount() + "");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(WebContentActivity.getIntent2Me(
						mContext, info));
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView tv_title;
		TextView tv_lightCount;
		TextView tv_replyCount;
	}

}
