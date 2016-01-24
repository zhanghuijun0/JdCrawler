package com.zhj;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.zhj.jd.GoodsInfo;

/**
 * 该版本的程序设计思路 独立封装，输入开始、结束商品的编号，抓取商品的基本信息，评论信息，评论数量！
 * 
 * @author Administrator
 *
 */
public class Main {
	private static Logger mLogger = Logger.getLogger(Main.class.getName());
	private static int[] mArray;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mLogger.warn("===welcome to MyJdSearchAuto!===");
		GoodsInfo mGoodsInfo = new GoodsInfo();
		Scanner mScanner = new Scanner(System.in);
		
		System.out.println("1、区间遍历\n2、枚举遍历\n请输入你选择的编号：");
		int choice = mScanner.nextInt();
		if (choice==1) {
			mGoodsInfo.getInfo();
		} else {
			System.out.print("请输入你要输入的商品编号的个数：");
			int mNum = mScanner.nextInt();
			System.out.print("请输入商品编号,以回车结束：");
			mArray = new int[mNum];
			int s = 0;
			for (int i = 0; i < mNum; i++) {
				mArray[i] = mScanner.nextInt();
			}
			mGoodsInfo.getInfo(mArray);
		}
	}
}
