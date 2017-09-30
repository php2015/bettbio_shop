package com.bettbio.shop.web.controller.evaluate;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.exception.NotLoginEXception;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SProductEvaluate;
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.SProductEvaluateService;
import com.bettbio.shop.base.controller.BaseShopController;
/**
 * 商品评价
 * @author simon
 *
 */
@Controller
@RequestMapping("/evaluate")
public class EvaluateController extends BaseShopController{

	@Autowired
	SProductEvaluateService sProductEvaluateService;
	
	/**
	 * 保存评论
	 * @param request
	 * @param sProductEvaluate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public AjaxResponse save(HttpServletRequest request,SProductEvaluate sProductEvaluate)throws NotLoginEXception{
		SUser sUser = getCurrentUser(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		sProductEvaluate.setCreateDate(new Date());
		sProductEvaluate.setUserName(sUser.getName());
		sProductEvaluate.setUserCode(sUser.getCode());
		int i = sProductEvaluateService.save(sProductEvaluate);
		if(i == 1){
			ajaxResponse.setMessage("保存成功");
			ajaxResponse.setSuccess(Boolean.TRUE);
		}else{
			ajaxResponse.setMessage("保存失败");
			ajaxResponse.setSuccess(Boolean.FALSE);
		}
		return ajaxResponse;
	}
}
