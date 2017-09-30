package com.bettbio.core.common.utils;

import java.text.DecimalFormat;

public class DecimalFormatUtils {

	public static String formatDoubleSplitThreeReTwo(Double d){

		DecimalFormat format = new DecimalFormat();
		format.applyPattern("##,###.00");
		return format.format(d);
	}
	
	
	public static void main(String[] args) {
		System.out.println(formatDoubleSplitThreeReTwo(11123234.12));
	}
}
