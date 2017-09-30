package com.salesmanager.core.modules.sms;

/**
 * 163772 确认收货商家通知
 * 【百图生物】{1},您好!用户{2}已经对{3}商品进行了确认收货,订单号:{4}.感谢您的配合!详情请登录www.bettbio.com <br/>
 * 163771 付款商家通知
 * 【百图生物】{1},您好.用户{2}已上传{3}商品的付款凭证,订单号:{4},请您尽快确认并进行后续处理.感谢您的配合!详情请登录www.bettbio
 * .com. <br/>
 * 163726 发货用户通知
 * 百图生物】{1},您好.您订购的{2}产品，商家{3}已发货.承运方:{4},单号:{5},订单号:{6}.请注意查收.详情请登录www.bettbio.
 * com <br/>
 * 163724 改价用户通知
 * 【百图生物】{1},您好!您订购的{2}产品,商家{3}已将价格改为{4},订单号:{5},请您确认，感谢您的配合!详情请登录www.bettbio.
 * com. <br/>
 * 163723
 * 下订单商家通知【百图生物】{1},您好！用户{2}已订购您公司的{3}产品，订单号:{4},请尽快安排发货，感谢您的配合！详情请登录www.bettbio
 * .com.
 * 
 * @author clari
 *
 */
public interface TextShortMessageService {
	public static final String SMSTPL_NOTIFY_MERCHANT_RECIEPT = "163772";
	public static final String SMSTPL_NOTIFY_MERCHANT_PAID = "163771";
	public static final String SMSTPL_NOTIFY_CUSTOMER_SHIPPED = "163726";
	public static final String SMSTPL_NOTIFY_CUSTOMER_ADJUSTPRICE = "163724";
	public static final String SMSTPL_NOTIFY_MERCHANT_PLACEORDER = "163723";

	boolean sendNotifySms(String phone, String smsTemplate, String[] smsParams);
}
