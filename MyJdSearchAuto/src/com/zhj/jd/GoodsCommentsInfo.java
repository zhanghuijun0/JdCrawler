package com.zhj.jd;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhj.Connect;
import com.zhj.helper.CommentsHelper;

public class GoodsCommentsInfo {
	private String mJdCommentsInfoUrl = "http://club.jd.com/review/1000080-3-1-0.html";
	private Document mDocComments;
	private int mCommentsNum = 0;
	private int mCommentsPageSize = 0;
	private int mCommentsPageNum = 0;
	Connect mConnect = new Connect();
	CommentsHelper mCommentsHelper = new CommentsHelper();
	private Logger mLogger = Logger
			.getLogger(GoodsCommentsInfo.class.getName());

	public int init(String id) {
		mJdCommentsInfoUrl = "http://club.jd.com/review/" + id + "-3-1-0.html";
		mDocComments = mConnect.isConnect(mJdCommentsInfoUrl);
		mCommentsNum = mCommentsHelper.getCommentsNum(mDocComments);
		mCommentsPageSize = mCommentsHelper.getPageSize(mDocComments);
		mCommentsPageNum = mCommentsHelper.getPageNum(mCommentsNum,
				mCommentsPageSize);
		mLogger.info("评论总数：" + mCommentsNum + "-总页数" + mCommentsPageNum
				+ "-第一页数量" + mCommentsPageSize);
		getContent(id, mCommentsPageNum);
		return mCommentsNum;
	}

	/**
	 * 
	 * @param gid 商品编号
	 * @param pageNum	评论总页数
	 */
	public void getContent(String gid, int pageNum) {
		String pageUrl;
		// 利用循环，解析每一页的商品评论
		for (int i = 1; i <= pageNum; i++) {
			pageUrl = "http://club.jd.com/review/" + gid + "-3-" + i
					+ "-0.html";
			mLogger.debug("商品编号:" + gid + "第" + i + "页-URL:" + pageUrl);
			Document doc = mConnect.isConnect(pageUrl);
			Elements elements = doc
					.select("div[id=comments-list] div[class=mc] div[class=item]");
			String id = gid;
			String uname;// 用户名
			String grade;// 用户级别
			String area;// 用户地区
			String time;// 评论时间
			String label;// 评论标签
			String content;// 评论心得
			String topic;// 评论主题
			int star;// 星级
			for (Element e : elements) {
				uname = mCommentsHelper.getuName(e);
				grade = mCommentsHelper.getuGrade(e);
				area = mCommentsHelper.getuArea(e);
				time = mCommentsHelper.getCommentTime(e);
				label = mCommentsHelper.getCommentLabel(e);
				content = mCommentsHelper.getCommentContent(e);
				topic = mCommentsHelper.getCommentTopic(e);
				star = mCommentsHelper.getCommentStar(e);
				if (uname == "" || time == "") {
					continue;
				}
				int check = mCommentsHelper.CheckExists(id, uname, time,content);//存在:返回数据库中存在的主键id
				if (check == 0) {
					int m = mCommentsHelper.insertComment(id, uname, grade,
							area, time, label, content, topic, star,pageUrl);
					//商品评论插入失败
					if (m != 1) {
						mLogger.error("[insert comments fail]-pageUrl:"+pageUrl+"-time:"+time+"-uname:"+uname);
						mCommentsHelper.showMessage(id, uname, grade, area,
								time, label, content, topic, star);
					}else {
						mLogger.debug("[insert comments success]-pageUrl:"+pageUrl+"-time:"+time+"-uname:"+uname);
//						mCommentsHelper.showMessage(id, uname, grade, area,
//								time, label, content, topic, star);
					}
				} else {
					mLogger.warn("[insert comments exists]-ExistId："+check+"-pageUrl:"+pageUrl+"-time:"+time+"-uname:"+uname);
				}
			}
		}
	}

}
