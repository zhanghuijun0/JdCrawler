package com.zhj.helper;

/**
 * 向数据库中插入商品编号为id的评论的数量
 * @author Administrator
 *
 */
public class CommentsCountsHelper {
	public int InsertCommentsCounts(String id, int mCommentsCounts) {
		String sql = "insert into jd_comments_counts(gid,counts)values('" + id
				+ "','" + mCommentsCounts + "');";
		int m = DataBaseUnit.execteUpdate(sql);
		DataBaseUnit.close();
		return m;
	}
}
