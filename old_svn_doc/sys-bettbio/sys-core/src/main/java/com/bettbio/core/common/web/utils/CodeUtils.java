package com.bettbio.core.common.web.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 编码生成类
 * 
 * @author chang
 *
 */
public class CodeUtils {
	private static long orderNum = 0l;
	private static String date;

	/**
	 * 获取code
	 * 
	 * @return
	 */
	public static String getCode() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 获取订单号
	 * @return
	 */
	public static synchronized String getOrderCode() {
		String str = new SimpleDateFormat("yyMMddHHmm").format(new Date());
		if (date == null || !date.equals(str)) {
			date = str;
			orderNum = 0l;
		}
		orderNum++;
		long orderNo = Long.parseLong((date)) * 10000;
		orderNo += orderNum;
		;
		return orderNo + "";
	}

	/**
	 * 获取产品主键
	 * @return
	 */
	public static synchronized int getProductId() {
		String no = "";
		int num[] = new int[8];
		int c = 0;
		for (int i = 0; i < 8; i++) {
			num[i] = new Random().nextInt(10);
			c = num[i];
			for (int j = 0; j < i; j++) {
				if (num[j] == c) {
					i--;
					break;
				}
			}
		}
		if (num.length > 0) {
			for (int i = 0; i < num.length; i++) {
				no += num[i];
			}
		}
		return Integer.parseInt(no);
	}

}
