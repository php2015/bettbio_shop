package com.bettbio.shop.web.controller.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.mongo.model.ShoppingCartList;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.core.service.OrderService;
import com.bettbio.core.service.UserAddressService;
import com.bettbio.shop.base.controller.BaseShopController;

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
	public String createOrder(Integer userAddressId, Integer invoiceId,ShoppingCartVo shoppingCartVo,HttpServletRequest request)throws NotLoginEXception {
		SUserAddress userAddress = userAddressService.selectByKey(userAddressId);// 获取收货地址
		InvoiceInfo invoiceInfo = invoiceInfoService.selectByKey(invoiceId);// 获取发票地址
		ShoppingCartList shoppingCartList = (ShoppingCartList) request.getSession().getAttribute("shoppingCartList");//获取购物车集合
		String order=orderService.createOrder(userAddress,invoiceInfo,shoppingCartList,getCurrentUserCode(request),shoppingCartVo);
		StringBuffer sb=new StringBuffer();
		sb.append("恭喜您,您的订单已提交。订单编号为").append(order).append(",我们将通知商家尽快为您发货!");
		return sb.toString();
	}
}
