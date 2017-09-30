package com.bettbio.shop.web.controller.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
@Controller
public class InvoiceInfoController extends BaseShopController{
	@Autowired
	InvoiceInfoService invoiceInfoService;

	/**
	 * 新增开票地址
	 * 
	 * @return
	 */
	@RequestMapping("/saveInvoiceInfo")
	public String saveInvoiceInfo(InvoiceInfo invoiceInfo,HttpServletRequest request,Integer invoiceInfoId) throws NotLoginEXception{
		invoiceInfo.setId(invoiceInfoId);
		invoiceInfo.setIsDefault(invoiceInfo.getIsDefault()==null?1:0);
		if(invoiceInfo.getId()==null){
			invoiceInfo.setCode(CodeUtils.getCode());
			invoiceInfo.setUserCode(getCurrentUserCode(request));
		}
		invoiceInfoService.saveOrUpdateInvoiceInfo(invoiceInfo);
		return "redirect:/billing";
	}
	
	
	/**
	 * 异步添加开票地址并返回开票地址信息
	 * @return
	 */
	@RequestMapping("/ajaxSaveInvoiceInfo")
	@ResponseBody
	public InvoiceInfo ajaxSaveInvoiceInfo(InvoiceInfo invoiceInfo,HttpServletRequest request,Integer invoiceInfoId)throws NotLoginEXception{
		invoiceInfo.setId(invoiceInfoId);
		invoiceInfo.setIsDefault(invoiceInfo.getIsDefault()==null?1:0);
		if(invoiceInfo.getId()==null){
			invoiceInfo.setCode(CodeUtils.getCode());
			invoiceInfo.setUserCode(getCurrentUserCode(request));
		}
		invoiceInfoService.saveOrUpdateInvoiceInfo(invoiceInfo);
		return invoiceInfo;
	}
	
	/**
	 * Ajax返回开票信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/findInvoiceInfoByCode")
	@ResponseBody
	public Map<String,Object> findInvoiceInfoByCode(HttpServletRequest request)throws NotLoginEXception{
		return invoiceInfoService.selectInvoiceInfoByCode(getCurrentUserCode(request));
	}

	/**
	 * 刪除开票地址
	 * 
	 * @return
	 */
	@RequestMapping("/deleteInvoiceInfo")
	@ResponseBody
	public AjaxResponse deleteInvoiceInfo(String code) {
		AjaxResponse ajaxResponse=new AjaxResponse();
		invoiceInfoService.deleteInvoiceInfo(code);
		ajaxResponse.setMessage("开票地址删除成功！");
		ajaxResponse.setSuccess(Boolean.TRUE);
		return ajaxResponse;
	}
	
	/**
	 * 设置默认开票地址
	 * @param invoiceInfoCode
	 * @return
	 */
	@RequestMapping("settingDefaultInvoiceInfo")
	@ResponseBody
	public AjaxResponse settingDefaultInvoiceInfo(String invoiceInfoCode,HttpServletRequest request)throws NotLoginEXception{
		AjaxResponse ajaxResponse = new AjaxResponse();
		invoiceInfoService.settingDefaultInvoiceInfo(getCurrentUserCode(request),invoiceInfoCode);
		ajaxResponse.setMessage("默认开票地址设置成功！");
		ajaxResponse.setSuccess(Boolean.TRUE);
		return ajaxResponse;
	}
	/**
	 * 根据ID获取信息
	 * @param id
	 * @return
	 */
	@RequestMapping("findInvoiceInfoById")
	@ResponseBody
	public InvoiceInfo findInvoiceInfoById(Integer id){
		return invoiceInfoService.selectByKey(id);
	}
}
