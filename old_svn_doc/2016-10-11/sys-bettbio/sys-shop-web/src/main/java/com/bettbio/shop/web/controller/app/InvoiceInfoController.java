package com.bettbio.shop.web.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.service.InvoiceInfoService;
import com.bettbio.shop.base.controller.BaseShopController;

/**
 * 开票信息控制器
 * 
 * @author chang
 *
 */
@Controller("appInvoiceInfoController")
@RequestMapping("app")
public class InvoiceInfoController extends BaseShopController{
	@Autowired
	InvoiceInfoService invoiceInfoService;

	/**
	 * 新增开票地址
	 * 
	 * @return
	 * @throws NotLoginEXception 
	 */
	@RequestMapping("/saveInvoiceInfo")
	@ResponseBody
	public AjaxResponse saveInvoiceInfo(InvoiceInfo invoiceInfo,HttpServletRequest request,HttpServletResponse hsr) throws NotLoginEXception {
		
		if(invoiceInfo.getId()==null){
			invoiceInfo.setCode(CodeUtils.getCode());
			invoiceInfo.setUserCode(getCurrentUserCode(request));
		}
		try {
			invoiceInfoService.saveOrUpdateInvoiceInfo(invoiceInfo);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("操作成功");

	}

	/**
	 * 刪除开票地址
	 * 
	 * @return
	 */
	@RequestMapping("/deleteInvoiceInfo")
	@ResponseBody
	public AjaxResponse deleteInvoiceInfo(String code,HttpServletResponse hsr) {
		
		try {
			invoiceInfoService.deleteInvoiceInfo(code);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("开票地址删除成功！");
	}
	
	/**
	 * 设置默认开票地址
	 * @param invoiceInfoCode
	 * @return
	 */
	@RequestMapping("settingDefaultInvoiceInfo")
	@ResponseBody
	public AjaxResponse settingDefaultInvoiceInfo(String invoiceInfoCode,HttpServletRequest request,HttpServletResponse hsr) {
		
		try {
			invoiceInfoService.settingDefaultInvoiceInfo(getCurrentUserCode(request),invoiceInfoCode);
		} catch (Exception e) {
			return AjaxResponse.fail("操作失败");
		}
		return AjaxResponse.success("默认开票地址设置成功！");
	}
	/**
	 * 根据ID获取信息
	 * @param id
	 * @return
	 */
	@RequestMapping("findInvoiceInfoById")
	@ResponseBody
	public InvoiceInfo findInvoiceInfoById(Integer id,HttpServletResponse hsr) {
		
		return invoiceInfoService.selectByKey(id);
	}
}
