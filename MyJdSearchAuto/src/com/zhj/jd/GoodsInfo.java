package com.zhj.jd;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.zhj.Connect;
import com.zhj.helper.CommentsCountsHelper;
import com.zhj.helper.GoodsHelper;

public class GoodsInfo {
	private int mStartGoodsId = 0;
	private int mEndGoodsId = 0;
	private String mJdInfoUrl = "http://item.jd.com/1000080.html";
	private Logger mLogger = Logger.getLogger(GoodsInfo.class.getName());
	private Document mDocInfo;
	
	private String mId;//商品编号
	private String mName;//商品名称
	private String mTitle;//商品标题
	private String mKind;//商品分类
	private float mPrice;//商品价格
	
	Connect mConnect = new Connect();
	PriceInfo mPriceInfo = new PriceInfo();
	GoodsHelper mGoodsHelper = new GoodsHelper();
	GoodsCommentsInfo mGoodsCommentsInfo = new GoodsCommentsInfo();
	CommentsCountsHelper mCommentsCountsHelper = new CommentsCountsHelper();
	
	/**
	 * 通过录入商品编号区间，查询区间的商品编号信息
	 */
	public void getInfo() {
		Scanner mScanner = new Scanner(System.in);
		System.out.println("Please Input the Start Goods Id:");
		mStartGoodsId = mScanner.nextInt();
		System.out.println("Please Input the End Goods Id:");
		mEndGoodsId = mScanner.nextInt();
		if (mStartGoodsId <= mEndGoodsId) {
			for (int i = mStartGoodsId; i <= mEndGoodsId; i++) {
				getData(i);
			}
			mLogger.error("over:success");
		} else {
			mLogger.error("over:开始商品的Id必须大于结束商品的Id!");
		}
	}
	
	/**
	 * 通过列举商品的编号查询指定编号的商品信息
	 * @param array 商品编号数组
	 */
	public void getInfo(int[] array){
		for (int i = 0; i < array.length; i++) {
			System.out.println("array.length:"+array.length+",i:"+i);
			getData(array[i]);
		}
		mLogger.error("over:success");
	}
	
	/**
	 * 通过商品编号查询商品及评论信息
	 * @param i 商品编号
	 */
	public void getData(int i){
		mJdInfoUrl = "http://item.jd.com/" + i + ".html";
		mLogger.debug(mJdInfoUrl);//输出当前连接的URL
		mDocInfo = mConnect.isConnect(mJdInfoUrl);
		if (mDocInfo.baseUri().equals(mJdInfoUrl)) {
			//得到商品的编号、分类、名称、标题、价格
			mId = mGoodsHelper.getId(mJdInfoUrl);
			mKind = mGoodsHelper.getKind(mDocInfo);
			mName = mGoodsHelper.getName(mKind);
			mTitle = mGoodsHelper.getTitle(mDocInfo);
			mPrice = mPriceInfo.getPrice(mId);
//			mGoodsHelper.showData(mId, mJdInfoUrl, mName, mTitle, mKind, mPrice);
			//判断数据库中是否存在该条记录
			int check = mGoodsHelper.CheckJdGoods(mId, mName); 
			if (check == 0) {
				int m = mGoodsHelper.InsertGoodsData(mId, mJdInfoUrl, mName, mTitle,
						mKind, mPrice);
				if (m != 1) {
					mLogger.error("[insert goods info fail]-id:"+mId);
					mGoodsHelper.showData(mId, mJdInfoUrl, mName, mTitle, mKind, mPrice);
				} else {
					mLogger.debug("[insert goods info success]-id:"+mId);
//					mGoodsHelper.showData(mId, mJdInfoUrl, mName, mTitle, mKind, mPrice);
				}
			} else {
				mLogger.warn("[insert goods info exists]"+"插入的商品编号"+mId+"与数据库中的id为"+check+"的记录相同！");
//				mLogger.warn("[insert goods info exists]-ExistId:"+check+mId + "exists");
				mGoodsHelper.showData(mId, mJdInfoUrl, mName, mTitle, mKind, mPrice);
			}

			/*商品的评论*/
			int mCommentsCounts = mGoodsCommentsInfo.init(mId);
			int mark = mCommentsCountsHelper.InsertCommentsCounts(mId,mCommentsCounts);
			if (mark==-1) {
				mLogger.error("[insert comments counts fail]-id:"+mId+"-counts:"+mCommentsCounts);
			}else{
				mLogger.debug("编号为"+mId+"商品的商品评论插入成功，共"+mCommentsCounts+"条");
			}
			
		}
	}
	
	
}
