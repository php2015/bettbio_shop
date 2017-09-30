package com.salesmanager.web.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberUtils {

	private static SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmssSSS");
	public static String getNumByTime(String prefix) {
		return prefix + sf.format(new Date());
	}
	
	public static void main(String[] args) {
		//System.out.println(getNumByTime("1"));
		System.out.println(getNumber("5","2",1));
		System.out.println(getNumber("5.4","2",2));
		System.out.println(getNumber("5","",3));
	}
	
	/**
	 * 
	 * @param one
	 * @param two
	 * @param operator 如果为1，则进行加法操作，如果为2，则进行减法操作，其他的则默认采用加法操作
	 * @return
	 */
	public static String getNumber(String one, String two, int operator) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal one_big = new BigDecimal(getZeroForNull(one));
		BigDecimal two_big = new BigDecimal(getZeroForNull(two));
		switch (operator) {
		case 1:
			result = one_big.add(two_big);
			break;
		case 2:	
			result = one_big.subtract(two_big);
			break;
		default:
			result = one_big.add(two_big);
			break;
		}
		return result.toString();
	}
	
	/**
	 * 如果为空，则返回“0”字符串
	 * @param s
	 * @return
	 */
	private static String getZeroForNull(String s) {
		if (s==null || s.trim().equals("")) {
			return "0";
		} else return s;
	}
}
