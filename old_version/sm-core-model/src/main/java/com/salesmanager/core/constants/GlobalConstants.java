package com.salesmanager.core.constants;

public class GlobalConstants {

	public static final String EMAIL_SERVICE = "service@bettbio.com";
	public static final String EMAIL_SERVICE_SENDER_NAME = "百图生物";
	public static final String EMAIL_SYS_SERVER = "server@bettbio.com";

	public static final int SMS_LOGIN_RETRY_TIMES = 5;

	/** 买家账户状态 **/
	public static final String AccountState_NotCertified = "not_certified"; // 未认证
	public static final String AccountState_Certified = "certified"; // 已认证

	/** 买家类型 **/
	public static final String BuyerType_ResearchWorker = "research"; // 科研用户
	public static final String BuyerType_Dealer = "dealer"; // 经销商
	public static final String BuyerType_IndustrialCustomer = "industrial"; // 工业客户

	/** 订单处理方式 **/
	public static final String OrderProcessType_PayFirst = "pay_first"; // 先付款
	public static final String OrderProcessType_ShipFirst = "ship_first"; // 先发货

	/** 订单发起的操作 **/
	public static final String OrderAction_PlaceOrder = "place_order";
	/** 订单允许的操作 **/
	public static final String OrderAction_Cancel = "cancel_order"; // 取消订单
	public static final String OrderAction_UploadPayProof = "upload_pay_proof"; // 上传支付凭证
	public static final String OrderAction_ConfirmPay = "confirm_pay"; // 确认收到货款
	public static final String OrderAction_Ship = "ship_order"; // 发货
	public static final String OrderAction_ConfirmReceipt = "confirm_receipt"; // 确认收货
	public static final String OrderAction_Close = "close_order"; // 关闭订单
	public static final String OrderAction_AdjustPrice = "adjust_price"; // 改价
	public static final String OrderAction_Reset = "reset_order"; // 重置订单，变成新订单
	public static final String OrderAction_Review = "review_order"; // 评价
	public static final String OrderAction_ConfirmOrder = "confirm_order"; //确认订单

	/** 改价类型 **/
	public static final String OrderAdjustType_Total = "order_total"; // 改过订单总价。目前尚不支持
	public static final String OrderAdjustType_SubTotal = "suborder_total"; // 改过子订单总价。
	public static final String OrderAdjustType_ItemTotal = "item_total"; // 改过某个商品的总价。
	public static final String OrderAdjustType_ItemUnit = "item_unit"; // 改过某个商品的单价。

	/** 下单渠道 **/
	public static enum OrderCommitChannel {
		MOBILE, // 使用移动设备下的单
		PC, // 使用电脑下的单
		GROUP // 普通团购
	}

	/**
	 * 订单中的价格是如何得到的.<br/> 注意，同一项之间，只能有一个价格是调整过的。
	 * <p/>
	 * <ul>
	 * <li>商品层：
	 * <ul>
	 * <li>调整过单价，则 type=CALCULATED。finalPrice不为空，finalTotal也不为空.</li>
	 * <li>调整过总价，则 type=ADJUSTED。finalPrice为空，finalTotal不为空.</li>
	 * <li>未调整过,则 type=NORMAL。然后finalPrice和finalTotal为空.</li>
	 * </ul>
	 * </li>
	 * <li>子订单层：
	 * <ul>
	 * <li>调整过，则type=ADJUSTED。finalTotal不为空</li>
	 * <li>商品调整过，本身未调整.则type=CALCLATED。finalTotal不为空</li>
	 * <li>商品和本身都未调整过，type=NORMAL.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 如果子订单总价调整过，会自动把所有商品条目调整的价格删除，即只留下原始价格。
	 **/
	public static enum OrderPriceType {
		NORMAL, // 正常未经任何调价的价格
		CALCULATED, // 由于底层数据调整而发生变化，但是本身未经过调整的
		ADJUSTED // 调整过的
	}
	
	// 关于图像资源的几个名词定义
	public static final String IMAGE_URI_PREFIX_COMMON_IMAGE = "comimg"; // URI起始前缀
	public static final String IMAGE_URI_TYPE_BRAND_LOGO = "brand/logo"; // 品牌的logo
	
	// 未填写的字段，填写的默认值
	public static final String UI_FIELD_VALUE_NOT_SET = "未填写";
	
	// 和订单相关的积分状态
	/**
	 * 0：已到账
	 * 1：未到账
	 * 2：已撤销
	 */
	public static final int MEMBER_POINT_STATE_CONFIRMED = 0;
	public static final int MEMBER_POINT_STATE_PREPARED = 1;
	public static final int MEMBER_POINT_STATE_CANCELLED = 2;

}
