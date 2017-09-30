package com.bettbio.shop.web.controller.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
/**
 * 买家用户地址维护
 * @author chang
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.core.service.UserAddressService;
import com.bettbio.shop.base.controller.BaseShopController;

@Controller
public class UserAddressController extends BaseShopController {

	@Autowired
	UserAddressService userAddressService;
	
	@Autowired
	InvoiceInfoService invoiceInfoService;

	/**
	 * 收货地址维护
	 * 
	 * @return
	 */
	@RequestMapping("/billing")
	public String billing(HttpServletRequest request, ModelMap map) throws NotLoginEXception{
		map.addAllAttributes(userAddressService.selectAddressByCode(getCurrentUserCode(request)));
		map.addAllAttributes(invoiceInfoService.selectInvoiceInfoByCode(getCurrentUserCode(request)));
		return "/user/billing";
	}
	
	/**
	 * Ajax返回地址列表信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/findUserAddressByCode")
	@ResponseBody
	public Map<String,Object> findUserAddressByCode(HttpServletRequest request)throws NotLoginEXception{
		return userAddressService.selectAddressByCode(getCurrentUserCode(request));
	}
	
	/**
	 * 添加用户地址
	 * 
	 * @param userAddress
	 * @param request
	 * @return
	 */
	@RequestMapping("/addUserAddress")
	public String addUserAddress(SUserAddress userAddress, HttpServletRequest request) throws NotLoginEXception{
		if(userAddress.getId()==null){
			userAddress.setCode(CodeUtils.getCode());
			userAddress.setUserCode(getCurrentUserCode(request));
		}
		userAddressService.saveUserAddress(userAddress);
		return "redirect:/billing";
	}
	
	/**
	 * Ajax方式添加用户地址，并返回用户地址信息
	 * @param userAddress
	 * @param request
	 * @return
	 */
	@RequestMapping("/ajaxAddUserAddress")
	@ResponseBody
	public SUserAddress ajaxAddUserAddress(SUserAddress userAddress, HttpServletRequest request)throws NotLoginEXception{
		if(userAddress.getId()==null){
			userAddress.setCode(CodeUtils.getCode());
			userAddress.setUserCode(getCurrentUserCode(request));
		}
		userAddressService.saveUserAddress(userAddress);
		return userAddress;
	}

	/**
	 * 刪除用戶地址
	 * 
	 * @return
	 */
	@RequestMapping("/deleteUserAddress")
	@ResponseBody
	public AjaxResponse deleteUserAddress(String addressCode) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			userAddressService.deleteAddress(addressCode);
			ajaxResponse.setSuccess(Boolean.TRUE);
			ajaxResponse.setMessage("删除地址成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxResponse;
	}

	/**
	 * 根据ID获取地址信息
	 * 
	 * @param addressId
	 * @return
	 */
	@RequestMapping("/findUserAddresById")
	@ResponseBody
	public SUserAddress findUserAddresById(int addressId) {
		SUserAddress userAddress = userAddressService.selectByKey(addressId);
		return userAddress;
	}

	/**
	 * 设置默认地址
	 * 
	 * @return
	 */
	@RequestMapping("/settingDefaultAddress")
	@ResponseBody
	public AjaxResponse settingDefaultAddress(String addressCode,HttpServletRequest request) throws NotLoginEXception{
		AjaxResponse ajaxResponse = new AjaxResponse();
		userAddressService.settingDefaultAddress(getCurrentUserCode(request), addressCode);
		ajaxResponse.setMessage("默认收货地址设置成功！");
		ajaxResponse.setSuccess(Boolean.TRUE);
		return ajaxResponse;
	}

	/**
	 * 设置默认接收地址
	 * 
	 * @return
	 */
	@RequestMapping("/settingDefaultDistribution")
	@ResponseBody
	public AjaxResponse settingDefaultDistribution(String addressCode,HttpServletRequest request) throws NotLoginEXception{
		AjaxResponse ajaxResponse = new AjaxResponse();
		userAddressService.settingDefaultDistributionAddress(getCurrentUserCode(request), addressCode);
		ajaxResponse.setMessage("默认收货地址设置成功！");
		ajaxResponse.setSuccess(Boolean.TRUE);
		return ajaxResponse;
	}
}
