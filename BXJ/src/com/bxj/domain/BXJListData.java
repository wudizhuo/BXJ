package com.bxj.domain;

import com.bxj.AppConstants;

public class BXJListData extends WebData {
	private String url = "";
	private String title = "";
	private String lightCount = "";
	private String replyCount;

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLightCount() {
		return lightCount;
	}

	public void setLightCount(String lightCount) {
		this.lightCount = lightCount;
	}

	public String getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(String replyCount) {
		this.replyCount = replyCount;
	}

	@Override
	public String getUrlType() {
		return AppConstants.TYPE_BXJ;
	}

}
