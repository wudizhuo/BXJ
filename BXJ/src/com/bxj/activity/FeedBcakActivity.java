package com.bxj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bxj.App;
import com.bxj.R;
import com.bxj.common.BaseActivity;
import com.bxj.utils.ToastUtil;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;

/**
 * 欢迎页 显示图片
 * 
 * @author SunZhuo
 * 
 */
public class FeedBcakActivity extends BaseActivity {

	private static final String FEEDBACK = "feedback";
	private static final String KEY_FOR_SUGGESTION = "key_for_suggestion";
	private static final String KEY_UMENG_CONTACT_INFO_PLAIN_TEXT = "plain";
	public static final String INVAILD_STR = "";
	/**
	 * 意见输入
	 */
	private EditText mSuggestionInput = null;
	/**
	 * 输入法管理器
	 */
	private InputMethodManager mImm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		mSuggestionInput = (EditText) findViewById(R.id.suggestion_input);
		init();
	}

	private void init() {
		setTitleText("意见反馈");
		setRightBtnText("提交");
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		String saveContent = getData();
		if (!saveContent.equals(INVAILD_STR)) {
			mSuggestionInput.setText(saveContent);
			mSuggestionInput.setSelection(saveContent.length());
		}
		getRightBtn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});
	}

	public void onSubmit() {
		hideImm();
		if (!isVaildInput()) {
			ToastUtil.show(R.string.input_invaild);
			return;
		}
		String suggestion = mSuggestionInput.getText().toString();
		FeedbackAgent agent = new FeedbackAgent(this);
		Conversation defaultConversation = agent.getDefaultConversation();
		defaultConversation.addUserReply(suggestion);
		agent.sync();
		ToastUtil.show(R.string.feedback_sussced);
		onBackPressed();
	}

	/**
	 * 保存数据
	 * 
	 * @param data
	 */
	public static void saveData(String data) {
		Editor edit = App.getContext()
				.getSharedPreferences(FEEDBACK, Context.MODE_PRIVATE).edit();

		edit.putString(KEY_FOR_SUGGESTION, data);
		edit.commit();
	}

	/**
	 * 获取数据
	 */
	public static String getData() {
		SharedPreferences sharePre = App.getContext()
				.getSharedPreferences(FEEDBACK, Context.MODE_PRIVATE);
		return sharePre.getString(KEY_FOR_SUGGESTION, INVAILD_STR);
	}

	/**
	 * 清空数据
	 */
	public static void clearData() {
		saveData(INVAILD_STR);
	}

	/**
	 * 隐藏输入法
	 */
	public void hideImm() {
		if (mSuggestionInput != null && mImm.isActive()) {
			mImm.hideSoftInputFromWindow(mSuggestionInput.getWindowToken(), 0);
		}
	}

	/**
	 * 验证输入是否有效
	 * 
	 * @return
	 */
	private boolean isVaildInput() {
		if (null == mSuggestionInput) {
			return false;
		}

		String input = mSuggestionInput.getText().toString();
		if (null == input || input.equals("")) {
			return false;
		}

		return true;
	}
}
