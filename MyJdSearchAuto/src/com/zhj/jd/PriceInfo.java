package com.zhj.jd;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.zhj.Connect;
/**
 * 查询商品的价格
 * @author ZhangHuijun
 * 2015-03-06
 *
 */
public class PriceInfo {
	private Document doc;
	private String mStartUrl = "http://p.3.cn/prices/mgets?skuIds=J_";
	private String mEndUrl = "&type=1";
	private String mURL = "http://p.3.cn/prices/mgets?skuIds=J_100111.html";
	private Logger mLogger = Logger.getLogger(Connect.class.getName());
	private Connect mConnect = new Connect();

	/**
	 * 通过商品编号获取商品的价格
	 * 
	 * @param id
	 * @return
	 */
	public float getPrice(String id) {
		mURL = mStartUrl + id + mEndUrl;
		doc = mConnect.isConnect(mURL);
		float mPrice = getPriceStr(doc);
//		mLogger.debug("商品" + id + "的价格：" + mPrice);
		return mPrice;
	}

	public float getPriceStr(Document doc) {
		String[] str = (doc.text().split(","))[1].split(":");
		float price = Float.parseFloat(str[1].replace("\"", ""));
		return price;
	}
}
