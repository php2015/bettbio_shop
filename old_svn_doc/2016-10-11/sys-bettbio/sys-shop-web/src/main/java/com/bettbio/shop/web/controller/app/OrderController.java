package com.bettbio.shop.web.controller.app;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.email.constant.EmailConstants;
import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.mongo.model.ShoppingCartList;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.core.service.OrderService;
import com.bettbio.core.service.SysEmailService;
import com.bettbio.core.service.UserAddressService;
import com.bettbio.shop.base.controller.BaseShopController;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

/**
 * 订单控制器
 * 
 * @author chang
 *
 */
@Controller("appOrderController")
@RequestMapping("/app/order")
public class OrderController extends BaseShopController {

	@Autowired
	UserAddressService userAddressService;
	@Autowired
	InvoiceInfoService invoiceInfoService;
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
	 * @param shoppingCartVo 购物车业务类
	 * @return
	 */
	@RequestMapping("/createOrder")
	@ResponseBody 
	public String createOrder(Integer userAddressId, Integer invoiceId,String order,HttpServletRequest request)throws NotLoginEXception {
		
		ShoppingCartVo shoppingCartVo = new Gson().fromJson(order, new TypeToken(ShoppingCartVo.class) {}.getType());
		
		SUserAddress userAddress = userAddressService.selectByKey(userAddressId);// 获取收货地址
		InvoiceInfo invoiceInfo = invoiceInfoService.selectByKey(invoiceId);// 获取发票地址
		ShoppingCartList shoppingCartList = (ShoppingCartList) request.getSession().getAttribute("shoppingCartList");//获取购物车集合
		String orderDes=orderService.createOrder(userAddress,invoiceInfo,shoppingCartList,getCurrentUserCode(request),shoppingCartVo);
		StringBuffer sb=new StringBuffer();
		sb.append("恭喜您,您的订单已提交。订单编号为").append(orderDes).append(",我们将通知商家尽快为您发货!");
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
		return sb.toString();
	}
}
