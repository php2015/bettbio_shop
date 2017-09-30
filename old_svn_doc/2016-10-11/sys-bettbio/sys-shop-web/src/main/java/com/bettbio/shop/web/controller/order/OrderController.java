package com.bettbio.shop.web.controller.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bettbio.core.common.email.constant.EmailConstants;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.mongo.model.ShoppingCartList;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.core.service.OrderService;
import com.bettbio.core.service.SubOrderService;
import com.bettbio.core.service.SysEmailService;
import com.bettbio.core.service.UserAddressService;
import com.bettbio.shop.base.controller.BaseShopController;

/**
 * 订单控制器
 * 
 * @author chang
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseShopController {

	@Autowired
	UserAddressService userAddressService;
	@Autowired
	InvoiceInfoService invoiceInfoService;
	@Autowired
	SubOrderService subOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	SysEmailService sysEmailService;
	/**
	 * 生成订单
	 * 
	 * @param userAddressId
	 * @param invoiceId
	 * @param httpServletRequest
	 * @return
	 */
	@RequestMapping("/createOrder")
	public String createOrder(Integer userAddressId, Integer invoiceId, HttpServletRequest request,ModelMap model)throws NotLoginEXception {
		SUserAddress userAddress = userAddressService.selectByKey(userAddressId);// 获取收货地址
		InvoiceInfo invoiceInfo = invoiceInfoService.selectByKey(invoiceId);// 获取发票地址
		ShoppingCartList shoppingCartList = (ShoppingCartList) request.getSession().getAttribute("shoppingCartList");//获取购物车集合
		String order=orderService.createOrder(userAddress,invoiceInfo,shoppingCartList,getCurrentUserCode(request),null);
		StringBuffer sb=new StringBuffer();
		sb.append("恭喜您,您的订单已提交。订单编号为").append(order).append(",我们将通知商家尽快为您发货!");
		model.put("message",sb.toString());
		//发送邮件
		Map<String, String> templateTokens =new HashMap<String, String>();
		templateTokens.put(EmailConstants.LABEL_HI, "Hi");
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, "Bettbio");
		templateTokens.put(EmailConstants.EMAIL_CUSTOMER_NAME,"下单人姓名："+userAddress.getUserName());
		templateTokens.put(EmailConstants.EMAIL_TEXT_ORDER_NUMBER, "订单编号："+order);
		templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_ORDERED, "订购于："+(new Date()));
		/*templateTokens.put(EmailConstants.EMAIL_TEXT_STATUS_COMMENTS, );
		templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_UPDATED, );*/
		templateTokens.put("EMAIL_FOOTER_COPYRIGHT", "Copyright @ Shopizer 2016, All Rights Reserved!");
		templateTokens.put("EMAIL_DISCLAIMER", "Disclaimer text goes here...");
		templateTokens.put("EMAIL_SPAM_DISCLAIMER", "Spam Disclaimer text goes here...");
		sysEmailService.sendOrderEmail(templateTokens);
		return "/common/message";
	}
	
	/**
	 * 商家更新订单状态
	 * @param orderCode 订单号
	 * @param state 要更新的订单状态
	 * @param type 0商家 1用户
	 * @return
	 */
	@RequestMapping("/updateOrderState")
	public String updateOrderState(String orderCode,String state,int type){
		try {
		 subOrderService.updateOrderState(state, orderCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String address=type==0?"/adminHome":"/findOrders";
		return "redirect:"+address;
	}
	
	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping("/orderDetail")
	public String orderDetail(String orderCode,ModelMap model){
		model.addAllAttributes(subOrderService.selectSubOrderByCode(orderCode));
		return "/admin/order/orderDetail";
	}
	
}
