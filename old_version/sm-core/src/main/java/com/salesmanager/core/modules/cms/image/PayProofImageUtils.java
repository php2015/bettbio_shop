package com.salesmanager.core.modules.cms.image;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayProofImageUtils {
	public static final String ORDER_ID = "order-id";
	public static final String SUBORDER_ID = "suborder-id";
	public static final String POSTFIX = "postfix";
	private PayProofImageUtils(){}
	
	public static String getImageUri(long orderId, long subOrderId, String postfix){
		return String.format("payproof/O%d/so%d/payproof.%s", orderId, subOrderId, postfix);
	}
	
	private static Pattern ptnImageUri = Pattern.compile(".*?payproof/O(\\d+)/so(\\d+)/payproof\\.(.*)");
	public static Map<String, Object> parseImageUri(String imageUri){
		Matcher m = ptnImageUri.matcher(imageUri);
		if (!m.find()){
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(ORDER_ID, m.group(1));
		result.put(SUBORDER_ID, m.group(2));
		result.put(POSTFIX, m.group(3));
		return result;
	}
}
