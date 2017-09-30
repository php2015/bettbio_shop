package com.bettbio.shop.web.controller.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@Controller("appUserAddressController")
@RequestMapping("app")
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
	@RequestMapping("/userAddress")
	@ResponseBody
	public Map<String, Object> userAddress(HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		setHeder(hsr);
			return userAddressService.selectAddressByCode(getCurrentUserCode(request));
				}
	
	/**
	 * 收货地址维护
	 * 
	 * @return
	 */
	@RequestMapping("/invoiceInfo")
	@ResponseBody
	public Map<String, Object> invoiceInfo(HttpServletRequest request,HttpServletResponse hsr)throws NotLoginEXception {
		setHeder(hsr);
		return invoiceInfoService.selectInvoiceInfoByCode(getCurrentUserCode(request));
	}

	/**
	 * 添加用户地址
	 * 
	 * @param userAddress
	 * @param request
	 * @return
	 */
	@RequestMapping("/addUserAddress")
	@ResponseBody
	public AjaxResponse addUserAddress(SUserAddress userAddress, HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception{
		setHeder(hsr);
		if(userAddress.getId()==null){
			userAddress.setCode(CodeUtils.getCode());
			userAddress.setUserCode(getCurrentUserCode(request));
		}
		try {
			userAddressService.saveUserAddress(userAddress);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("操作成功");
	}

	/**
	 * 刪除用戶地址
	 * 
	 * @return
	 */
	@RequestMapping("/deleteUserAddress")
	@ResponseBody
	public AjaxResponse deleteUserAddress(String addressCode,HttpServletResponse hsr) {
		setHeder(hsr);
		try {
			userAddressService.deleteAddress(addressCode);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("操作成功");
	}

	/**
	 * 根据ID获取地址信息
	 * 
	 * @param addressId
	 * @return
	 */
	@RequestMapping("/findUserAddresById")
	@ResponseBody
	public SUserAddress findUserAddresById(int addressId,HttpServletResponse hsr) {
		setHeder(hsr);
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
	public AjaxResponse settingDefaultAddress(String addressCode,HttpServletRequest request,HttpServletResponse hsr) {
		setHeder(hsr);
		try {
			userAddressService.settingDefaultAddress(getCurrentUserCode(request), addressCode);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("默认收货地址设置成功！");
	}

	/**
	 * 设置默认接收地址
	 * 
	 * @return
	 */
	@RequestMapping("/settingDefaultDistribution")
	@ResponseBody
	public AjaxResponse settingDefaultDistribution(String addressCode,HttpServletRequest request,HttpServletResponse hsr) {
		setHeder(hsr);
		try {
			userAddressService.settingDefaultDistributionAddress(getCurrentUserCode(request), addressCode);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("默认收货地址设置成功！");
	}
}
