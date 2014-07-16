package com.bxj.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bxj.R;

/**
 * 进度dialog
 * 
 * @author SunZhuo
 * 
 */
public class CustomerProgressDialog extends Dialog {
	protected Context context;
	/**
	 * dialog的contentView
	 */
	private View contentView;
	/**
	 * dialog的内容view
	 */
	protected TextView contentText;

	public CustomerProgressDialog(Context context) {
		super(context, R.style.Dialog);
		this.context = context;
		initCustomerView();
	}

	private void initCustomerView() {
		contentView = LayoutInflater.from(context).inflate(
				R.layout.view_progress_indeterminate, null);
		contentText = (TextView) contentView.findViewById(R.id.tv_msg);
		setContentView(contentView);
	}

	public void setMessage(String string) {
		if (contentText != null) {
			contentText.setText(string);
		}
	}
}
