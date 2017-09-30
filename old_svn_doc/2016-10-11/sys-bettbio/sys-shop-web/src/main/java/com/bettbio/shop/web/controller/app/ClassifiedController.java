package com.bettbio.shop.web.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.ClassifyZtreeBo;
import com.bettbio.core.service.ProductClassificationService;

@Controller("appClassifiedController")
@RequestMapping("/app/classified")
public class ClassifiedController extends BaseController{

	@Autowired
	ProductClassificationService productClassificationService;
	
	/**
	 * 加载分类树
	 * @param parentCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectTree")
	public List<ClassifyZtreeBo> selectTree(String parentCode){
		List<ClassifyZtreeBo> classifyZtreeBoList = new ArrayList<ClassifyZtreeBo>();
		try {
			classifyZtreeBoList  = productClassificationService.selectClassByparentCode(parentCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classifyZtreeBoList;
	}
	
}
