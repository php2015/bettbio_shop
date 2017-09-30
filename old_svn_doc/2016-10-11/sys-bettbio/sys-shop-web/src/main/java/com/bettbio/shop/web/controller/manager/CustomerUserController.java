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
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.UserGradeService;
import com.bettbio.core.service.manager.CustomerService;

/**
 * 会员管理
 * 
 * @author GuoChunbo
 *
 */
@Controller
@RequestMapping("/manager/customer")
public class CustomerUserController extends BaseController {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	UserGradeService userGradeService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, ModelMap modelMap) {
		paramMap = getRequestParameter(request);
		Page<SUser> pageInfo = customerService.selectByPage(paramMap);
		modelMap.addAttribute(Constants.PAGE_INFO, pageInfo);
		modelMap.addAttribute(Constants.PARAM_MAP,paramMap);
		
		modelMap.addAttribute("userGrades", userGradeService.selectGradeByType(UserGradeService.USER_GREAD));
		return viewName("list");
	}

	
	/**
	 * 设置等级
	 * @param user
	 * @return
	 */
	@RequestMapping("/setGrade")
	@ResponseBody
	public AjaxResponse setGrade(SUser user){
		int r = customerService.setGrade(user);
		if(r>0){
			return AjaxResponse.success("操作成功！用户等级已更新！");
		}
		return AjaxResponse.fail("操作失败！请稍候重试！");
	}
	
	/**
	 * 激活
	 * @param ids
	 * @return
	 */
	@RequestMapping("/activate")
	@ResponseBody
	public AjaxResponse activate(Integer [] ids){
		int r = customerService.activate(ids);
		if (r > 0) {
			return AjaxResponse.success("操作成功！账户激活成功！");
		}
		return AjaxResponse.fail("操作失败！请稍候重试！");
	}
	
	/**
	 * 冻结
	 * @param ids
	 * @return
	 */
	@RequestMapping("/unactivate")
	@ResponseBody
	public AjaxResponse unactivate(Integer [] ids){
		int r = customerService.unactivate(ids);
		if (r > 0) {
			return AjaxResponse.success("操作成功！帐号冻结成功！");
		}
		return AjaxResponse.fail("操作失败！请稍候重试！");
	}
	
	/**
	 * 重置密码
	 * @param ids
	 * @return
	 */
	@RequestMapping("/resetPwd")
	@ResponseBody
	public AjaxResponse resetPwd(Integer [] ids){
		int r = customerService.resetPwd(ids);
		if (r > 0) {
			return AjaxResponse.success("操作成功！密码已重置！");
		}
		return AjaxResponse.fail("操作失败！请稍候重试！");
	}
}
