package com.bettbio.shop.web.controller.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.UserGrade;
import com.bettbio.core.service.UserGradeService;

@Controller
@RequestMapping("/manager/config")
public class GradeController {

	@Autowired
	UserGradeService userGradeService;
	
	/**
	 * 积分配置列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model){
		List<UserGrade> userGradeList = userGradeService.selectAll();
		model.addAttribute("userGradeList", userGradeList);
		return "/manager/config/list";
	}
	
	/**
	 * 修改积分配置
	 * @param userGrade
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public AjaxResponse update(UserGrade userGrade){
		AjaxResponse ajaxResponse = new AjaxResponse();
		int i = userGradeService.updateNotNull(userGrade);
		if(i==1){
			ajaxResponse.setMessage("保存成功");
			ajaxResponse.setSuccess(Boolean.TRUE);
		}else{
			ajaxResponse.setMessage("保存失败");
			ajaxResponse.setSuccess(Boolean.FALSE);
		}
		return ajaxResponse;
	}
	
}
