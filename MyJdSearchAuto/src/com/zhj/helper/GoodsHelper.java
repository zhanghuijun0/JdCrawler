package com.zhj.helper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoodsHelper {
	private Logger mLogger = Logger.getLogger(GoodsHelper.class.getName());
	/**
	 * 商品ID
	 * 
	 * @param url
	 * @return
	 */
	public String getId(String url) {
		String[] str = url.split("item.jd.com/");
		String[] aa = str[1].split(".html");
		return aa[0];
	}

	/**
	 * 商品名称
	 * 
	 * @param kind
	 * @return
	 */
	public String getName(String kind) {
		String name = null;
		if (kind != null) {
			String[] str = kind.split(">");
			name = str[str.length - 1];
		}
		return name;
	}

	/**
	 * 商品标题
	 * 
	 * @param doc
	 * @return
	 */
	public String getTitle(Document doc) {
		String title = null;
		Elements melements = doc.select("div[id=name] h1");
		for (Element aa : melements) {
			title = aa.text().toString();
		}
		return title;
	}

	/**
	 * 商品分类信息
	 * 
	 * @param doc
	 * @return
	 */
	public String getKind(Document doc) {
		String kind = null;
		Elements melements = doc.select("div[class=w] div[class=breadcrumb]");
		for (Element aa : melements) {
			kind = aa.text().toString();
		}
		return kind;
	}
	public void showData(String id, String url, String name, String title,
			String kind, float price) {
		System.out.println("商品URL:" + url + "-商品编号：" + id + "-商品名称：" + name
				+ "-商品标题：" + title + "=========商品分类：" + kind + "=========商品价格："
				+ price);
	}
	
	//=============================数据库的操作========================================//
	

	public int InsertGoodsData(String id, String url, String name,
			String title, String kind, float price) {
		String sql = "insert into jd_goods(gid, gurl, gname, gtitle,gprice,gkind)values('"
				+ id
				+ "','"
				+ url
				+ "','"
				+ name
				+ "','"
				+ title
				+ "','"
				+ price + "','" + kind + "');";
		int m = DataBaseUnit.execteUpdate(sql);
		return m;
	}

	/**
	 * 检查URL在数据库中是否存在
	 * 
	 * @param url
	 *            检测的URL
	 * @return
	 */
	public int CheckJdGoods(String id, String name) {
		String sql = "select id from jd_goods where gid = '" + id
				+ "'and gname = '" + name + "';";
		try {
			ResultSet res = DataBaseUnit.executeQuery(sql);
			ResultSetMetaData rsmd = res.getMetaData();
			// 把ResultSet的所有记录添加到Vector里
			while (res.next()) {
				return res.getInt("id");
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			mLogger.error("检测存在异常："+e);
		} finally {
			DataBaseUnit.close();
		}
		return 0;
	}
}
