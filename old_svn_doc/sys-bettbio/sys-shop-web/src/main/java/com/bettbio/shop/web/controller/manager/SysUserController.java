package com.bettbio.shop.web.controller.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SysUser;
import com.bettbio.core.service.SysUserService;

/**
 * 系统员工管理
 * 
 * @author GuoChunbo
 *
 */
@Controller
@RequestMapping("/manager/sysuser")
public class SysUserController extends BaseController {

	@Autowired
	SysUserService sysUserService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap modelMap) {
		paramMap = getRequestParameter(request);
		Page<SysUser> pageInfo = sysUserService.selectByPage(paramMap);

		modelMap.addAttribute(Constants.PAGE_INFO, pageInfo);
		modelMap.addAttribute(Constants.PARAM_MAP, paramMap);
		return viewName("list");
	}

	@RequestMapping("/create")
	public String create(HttpServletRequest request, ModelMap modelMap) {
		return viewName("create");
	}

	@RequestMapping("/save")
	@ResponseBody
	public AjaxResponse save(SysUser user) {
		int r = sysUserService.save(user);
		if (r > 0) {
			return AjaxResponse.success("保存成功!");
		}
		return AjaxResponse.fail("保存失败!");
	}

	@RequestMapping("/checkAccount")
	@ResponseBody
	public AjaxResponse checkAccount(String account) {
		SysUser user = sysUserService.findByAccount(account);
		if (user == null) {
			return AjaxResponse.success("该帐号可用!");
		}
		return AjaxResponse.fail("帐号已存在!");
	}

	@RequestMapping("/checkJobNumber")
	@ResponseBody
	public AjaxResponse checkJobNumber(String jobNumber) {
		SysUser user = sysUserService.findByJobNumber(jobNumber);
		if (user == null) {
			return AjaxResponse.success("该工号可用!");
		}
		return AjaxResponse.fail("工号已存在!");
	}

	@RequestMapping("/delete")
	@ResponseBody
	public AjaxResponse delete(Integer id) {
		int r = sysUserService.deleteSysUser(id);
		if (r > 0) {
			return AjaxResponse.success("删除成功!");
		}
		return AjaxResponse.fail("操作失败!");
	}

	@RequestMapping("/disable")
	@ResponseBody
	public AjaxResponse disable(Integer[] ids) {
		int r = sysUserService.disableSysUser(ids);
		if (r > 0) {
			return AjaxResponse.success("禁用成功!");
		}
		return AjaxResponse.fail("操作失败!");
	}

	@RequestMapping("/undisable")
	@ResponseBody
	public AjaxResponse undisable(Integer[] ids) {
		int r = sysUserService.unDisableSysUser(ids);
		if (r > 0) {
			return AjaxResponse.success("启用成功!");
		}
		return AjaxResponse.fail("操作失败!");
	}
}
