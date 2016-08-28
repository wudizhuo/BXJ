package com.bxj.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bxj.AppConstants;
import com.bxj.domain.BXJListData;
import com.bxj.domain.WebData;

public class BXJDataParseMgr {

	/**
	 * 唯一实例
	 */
	private static BXJDataParseMgr mInstance;

	private BXJDataParseMgr() {
	}

	public static BXJDataParseMgr getInstance() {
		if (mInstance == null) {
			mInstance = new BXJDataParseMgr();
		}
		return mInstance;
	}

	public List<? extends WebData> parse(File saveHtmlFile) throws IOException {
		List<WebData> result = new ArrayList<WebData>();
		FileInputStream in = new FileInputStream(saveHtmlFile);
		Document doc = Jsoup.parse(in, "gb2312", AppConstants.URL_BXJ);
		// Document doc = Jsoup.connect(URL_MAIN).get();
		Element table = doc.getElementById("pl");// BXJ table id为pl
		Element tbody = table.child(1);// BXJ 里有2个table 第一个为帖子内容
		Elements trs = tbody.children();// 所有的表行
		for (Element tr : trs) {// 遍历
			// 获取p_title 包括所有的帖子信息
			Element titleUrl = tr.getElementsByClass("p_title")
					.select("a[id][href]").get(0);
			Element replyCount = tr.getElementsByClass("p_re").get(0);// 回复数 标签
			// 亮贴数 标签
			Elements light_r = tr.getElementsByClass("light_r").select(
					"a[title]");
			BXJListData data = new BXJListData();
			if (light_r != null && light_r.size() > 0) {
				// 设置亮贴数 得到的字符格式为“有2个亮了的回帖” 需要截取
				String strLight = light_r.get(0).attr("title");
				strLight = strLight.substring(1, strLight.indexOf("个"));
				data.setLightCount(strLight);
			} else {
				data.setLightCount("0");
			}
			// 电脑版网址
			// data.setUrl("http://bbs.hupu.com" +
			// tmp.attr("href"));
			// 手机版网址
			data.setUrl("http://m.hupu.com/bbs" + titleUrl.attr("href"));
			data.setTitle(titleUrl.text());
			// 回复的格式为 12/36 回复/浏览数 现在只取回复数
			data.setReplyCount(replyCount.text().split("/")[0]);
			result.add(data);
		}
		return result;
	}
}
