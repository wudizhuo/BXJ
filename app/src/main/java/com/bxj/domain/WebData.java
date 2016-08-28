package com.bxj.domain;

import java.io.Serializable;

public abstract class WebData implements Serializable {
	public abstract String getUrl();

	public abstract String getUrlType();
}
