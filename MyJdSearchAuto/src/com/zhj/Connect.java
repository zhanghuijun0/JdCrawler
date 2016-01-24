package com.zhj;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Connect {
	private Document doc;
	private Logger mLogger = Logger.getLogger(Connect.class.getName());

	public synchronized Document isConnect(String urlStr) {
		int counts = 0;
		if (urlStr == null || urlStr.length() <= 0) {
			return null;
		}
		while (counts < 20) {
			try {
				doc = Jsoup.connect(urlStr).timeout(5000).ignoreContentType(true).get();
				counts = 0;
				break;
			} catch (Exception e) {
				// TODO: handle exception
				mLogger.debug(urlStr + "第" + counts + "次连接超时!"+e);
				counts++;
				if (counts == 20) {
					counts = 0;
					mLogger.error("["+urlStr+"time out]-"+e);
					e.printStackTrace();
					break;
				}
				continue;
			}
		}
		return doc;
	}
}
