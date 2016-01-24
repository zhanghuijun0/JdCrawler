package com.zhj.helper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CommentsHelper {
	private Logger mLogger = Logger.getLogger(CommentsHelper.class.getName());

	// =============================获取商品评论第一页的评论条数，页数================================//
	/**
	 * 解析评论的总数量，如果错误，返回-1，否则返回评论个数
	 * 
	 * @param doc
	 * @return
	 */
	public int getCommentsNum(Document doc) {
		Elements elements = doc
				.select("div[id=comments-list] ul[class=tab] li[class=curr] em");
		int commentNum = -1;
		for (Element e : elements) {
			commentNum = Integer.parseInt(e.text().replace("(", "")
					.replace(")", ""));
		}
		return commentNum;
	}

	/**
	 * 解析传入页面对象的评论数量
	 * 
	 * @param doc
	 * @return
	 */
	public int getPageSize(Document doc) {
		Elements elements = doc.select("div[id=comments-list] div[class=mc]");
		return elements.size();
	}

	/**
	 * 
	 * @param commentsNum
	 * @param pageSize
	 * @return
	 */
	public int getPageNum(int commentsNum, int pageSize) {
		if (commentsNum == 0 || pageSize == 0) {
			return -1;
		} else if (commentsNum % pageSize == 0) {
			return commentsNum / pageSize;
		} else {
			return commentsNum / pageSize + 1;
		}
	}

	// =============================解析商品评论的各个部分的信息================================//

	/**
	 * 获取用户名
	 * 
	 * @param e
	 * @return name
	 */
	public String getuName(Element e) {
		String name = "";
		name = e.select("div[class=i-item]").attr("data-nickname");
		return name;
	}

	/**
	 * 用户等级
	 * 
	 * @param e
	 * @return
	 */
	public String getuGrade(Element e) {
		String grade = "";
		grade = e.select("div[class=user] span[class=u-level] span[style]")
				.text();
		return grade;
	}

	/**
	 * 用户区域
	 * 
	 * @param e
	 * @return
	 */
	public String getuArea(Element e) {
		String area = "";
		area = e.select("div[class=user] span[class=u-level] span[class]")
				.text();
		return area;
	}

	/**
	 * 评论时间
	 * 
	 * @param e
	 * @return
	 */
	public String getCommentTime(Element e) {
		String time = "";
		time = e.select("div[class=i-item] span[class=date-comment]").text();
		return time;
	}

	/**
	 * 评论标签（满意度）
	 * 
	 * @param e
	 * @return
	 */
	public String getCommentLabel(Element e) {
		String label = "";
		label = e
				.select("div[class=i-item] div[class=comment-content] span[class=comm-tags]")
				.text();
		return label;
	}

	/**
	 * 评论内容
	 * 
	 * @param e
	 * @return
	 */
	public String getCommentContent(Element e) {
		String content = "";
		content = e.select("div[class=i-item] div[class=comment-content] dl")
				.text().replaceAll("'", "");
		return content;
	}

	/**
	 * 评论标题
	 * 
	 * @param e
	 * @return
	 */
	public String getCommentTopic(Element e) {
		String topic = "";
		topic = e.select("div[class=i-item] strong[class=topic]").text()
				.replaceAll("'", "");
		return topic;
	}

	/**
	 * 评论星级
	 * 
	 * @param e
	 * @return
	 */
	public int getCommentStar(Element e) {
		String star = "";
		star = e.select("div[class=i-item] div[class=o-topic] span").first()
				.attr("class");
		int s = -1;
		if (star.contains("5")) {
			s = 5;
		} else if (star.contains("4")) {
			s = 4;
		} else if (star.contains("3")) {
			s = 3;
		} else if (star.contains("2")) {
			s = 2;
		} else if (star.contains("1")) {
			s = 1;
		}
		return s;
	}

	// ==============================对数据库的一些操作===============================//

	/**
	 * 检测该条评论数据库知否存在
	 * 
	 * @param goodsId
	 * @param uname
	 * @param ucommentTime
	 * @return
	 */
	public int CheckExists(String goodsId, String uname, String ucommentTime,
			String ucomment) {
		String sql = "select id from jd_goods_comment where goods_id = '"
				+ goodsId + "'and uname = '" + uname + "'and ucomment_time = '"
				+ ucommentTime + "'and ucomment = '" + ucomment + "';";
		try {
			ResultSet res = DataBaseUnit.executeQuery(sql);
			// ResultSetMetaData rsmd = res.getMetaData();
			if (res == null) {
				return 0;
			}
			// 把ResultSet的所有记录添加到Vector里
			while (res.next()) {
				return res.getInt("id");
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			mLogger.error("检测存在异常：" + e);
		} finally {
			DataBaseUnit.close();
		}
		return 0;
	}

	/**
	 * 打印错误信息
	 * 
	 * @param url
	 * @param args
	 * @param count
	 */
	// public void showErrorMessage(String url, int args, int count) {
	// switch (args) {
	// case 1:
	// System.out.println("[TimeOut]第" + count + "次链接超时：" + url);
	// break;
	// case 2:
	// System.out.println("[Fail]第" + count + "次链接超时：" + url);
	// break;
	// case 3:
	// System.out.println("[Fail]数据插入失败");
	// break;
	// default:
	// System.out.println("---SUCCESS---");
	// break;
	// }
	// }

	/**
	 * 输出评论的信息
	 * 
	 * @param id
	 * @param name
	 * @param grade
	 * @param area
	 * @param time
	 * @param label
	 * @param content
	 * @param topic
	 * @param star
	 */
	public void showMessage(String id, String name, String grade, String area,
			String time, String label, String content, String topic, int star) {
		System.out.println("商品编号：" + id);
		System.out.println("用户名：" + name);
		System.out.println("用户级别：" + grade);
		System.out.println("用户区域：" + area);
		System.out.println("时间：" + time);
		System.out.println("标签：" + label);
		System.out.println("内容：" + content);
		System.out.println("主题：" + topic);
		System.out.println("星级：" + star);
		System.out.println("==============本条结束=======================");
	}

	/**
	 * 插入评论信息
	 * 
	 * @param id
	 * @param name
	 * @param grade
	 * @param area
	 * @param time
	 * @param label
	 * @param content
	 * @param topic
	 * @param star
	 * @return
	 */
	public int insertComment(String id, String name, String grade, String area,
			String time, String label, String content, String topic, int star,
			String from) {
		String sql = "insert into jd_goods_comment(goods_id,uname,ugrade,uarea,comment_star,topic,ucomment,ucomment_time,usatisfied,ufrom)values('"
				+ id
				+ "','"
				+ name
				+ "','"
				+ grade
				+ "','"
				+ area
				+ "','"
				+ star
				+ "','"
				+ topic
				+ "','"
				+ content
				+ "','"
				+ time
				+ "','"
				+ label + "','" + from + "');";
		int m = DataBaseUnit.execteUpdate(sql);
		DataBaseUnit.close();
		return m;
	}

}
