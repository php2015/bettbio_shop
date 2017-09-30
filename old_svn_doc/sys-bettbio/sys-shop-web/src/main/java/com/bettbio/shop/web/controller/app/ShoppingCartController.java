package com.bettbio.shop.web.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.model.SUser;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.mongo.model.ShoppingCart;
import com.bettbio.core.mongo.model.ShoppingCartList;
import com.bettbio.core.mongo.service.ShoppingCartService;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.UserAddressService;
import com.bettbio.shop.base.controller.BaseShopController;
/**
 * 购物车控制器
 * @author chang
 *
 */
@Controller("appShoppingCartController")
@RequestMapping("/app/shoppingCart")
public class ShoppingCartController extends BaseShopController{
	
	@Autowired
	ShoppingCartService ShoppingCartService;
	@Autowired
	SUserService userService;
	@Autowired
	UserAddressService userAddressService;
	@Autowired
	InvoiceInfoService invoiceInfoService;
	/**
	 * 加入购物车
	 * @param shoppingCartVo
	 * @param request
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public AjaxResponse add(ShoppingCart ShoppingCart,HttpServletRequest request,HttpServletResponse hsr) {
		setHeder(hsr);
		AjaxResponse ajaxResponse=new AjaxResponse();
		try {
			ShoppingCartService.insert(getCurrentUserCode(request),new ShoppingCartVo(ShoppingCart));
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("加入购物车成功");
			return ajaxResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResponse.fail("加入购物车失败");
		}
	}
	
	/**
	 * 移除购物车
	 * @param request
	 * @param productCode
	 * @return
	 */
	@RequestMapping("/removes")
	@ResponseBody
	public AjaxResponse remove(HttpServletRequest request,ShoppingCartVo shoppingCartVo,HttpServletResponse hsr) {
		setHeder(hsr);
		AjaxResponse ajaxResponse=new AjaxResponse();
		try {
			ShoppingCartService.removes(getCurrentUserCode(request), shoppingCartVo);
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("产品删除成功");
			return ajaxResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResponse.fail("产品删除失败");
		}
	}
	
	/**
	 * 更改数量
	 * @param request
	 * @param shoppingCart
	 * @return
	 */
	@RequestMapping("/changeNumber")
	@ResponseBody
	public AjaxResponse changeNumber(HttpServletRequest request,ShoppingCart shoppingCart,HttpServletResponse hsr) {
		setHeder(hsr);
		AjaxResponse ajaxResponse=new AjaxResponse();
		try {
			ShoppingCartService.changeShoppingCart(getCurrentUserCode(request), shoppingCart);
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("操作成功");
			return ajaxResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResponse.fail("操作失败");
		}
	}
	
	/**
	 * 获取购物车
	 * @param request
	 * @return
	 */
	@RequestMapping("/shoppingCarts")
	@ResponseBody
	public ShoppingCartList shoppingCarts(HttpServletRequest request,HttpServletResponse hsr)throws NotLoginEXception{
		setHeder(hsr);
		return isLanding(request)==true?ShoppingCartService.findShoppingCarts(getCurrentUserCode(request)).getShoppingCartList():new ShoppingCartList();
	}
	
	/**
	 * 购物车清单
	 */
	@RequestMapping("/shopcart")
	@ResponseBody
	public Map<String,Object> shopcart(HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		setHeder(hsr);
		Map<String,Object> model=new HashMap<String,Object>();
		SUser suser=userService.selectByPhone(getCurrentUser(request));
		model.put("balance",suser.getBalance());//个人授信额度
		ShoppingCartList shoppingCartList=ShoppingCartService.findShoppingCarts(suser.getCode()).getShoppingCartList();
		model.put("shopcarts",shoppingCartList.thisToMap());//产品集合
		return model;
	}
	
	/**
	 * 确认订单
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/selement")
	@ResponseBody
	public Map<String,Object> selement(HttpServletRequest request,ShoppingCartVo shoppingCartVo,HttpServletResponse hsr) throws NotLoginEXception{
		setHeder(hsr);
		Map<String,Object> model=new HashMap<String,Object>();
		ShoppingCartList shoppingCartList=ShoppingCartService.findShoppingCarts(getCurrentUserCode(request)).getShoppingCartList().getListByProductCodes(shoppingCartVo);
		model.put("shopcarts",shoppingCartList.thisToMap());
		request.getSession().setAttribute("shoppingCartList", shoppingCartList);
		SUserAddress userAddress=userAddressService.selectDefaultAddress(getCurrentUserCode(request));
		InvoiceInfo invoiceInfo=invoiceInfoService.selectDefaultInvoiceInfo(getCurrentUserCode(request));
		model.put("userAddress", userAddress);
		model.put("invoiceInfo", invoiceInfo);
		return model;
	}
}
