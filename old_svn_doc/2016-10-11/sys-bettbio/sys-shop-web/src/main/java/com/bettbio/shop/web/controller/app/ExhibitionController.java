package com.bettbio.shop.web.controller.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.Exhibition;
import com.bettbio.core.model.ExhibitionRecord;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.service.ExhibitionRecordService;
import com.bettbio.core.service.ExhibitionService;
import com.bettbio.shop.base.controller.BaseShopController;

/**
 * 展会
 *
 */
@Controller("appExhibitionController")
@RequestMapping("app")
public class ExhibitionController extends BaseShopController{
	
	@Autowired
	ExhibitionRecordService  exhibitionRecordService;
	
	@Autowired
	ExhibitionService  exhibitionService;
	
	/**
	 * 展会首页
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exhibition/show")
	public Exhibition apply(HttpServletRequest request,Model model,Exhibition exhibition,HttpServletResponse hsr){
		
		
		
		ExhibitionRecord exhibitionRecord = new ExhibitionRecord();
		try {
			exhibition = exhibitionService.selectByKey(exhibition);
			SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
			exhibitionRecord.setStoreCode(sStoreUser.getStoreCode());
			exhibitionRecord.setExhibitionCode(exhibition.getCode());
			exhibitionRecord = exhibitionRecordService.selectRecordByCode(exhibitionRecord);
			if(null != exhibitionRecord){
				model.addAttribute("exhibitionRecord", exhibitionRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exhibition;
	}
	
	/**
	 * 展会列表
	 * 查询条件 
	 * 	* title = 可选 
	 * 分页参数
	 * 	* page = 1  可选
	 * 	* rows = 10 可选
	 */
	@ResponseBody
	@RequestMapping("/exhibition/list")
	public Page<Exhibition> adminExhibitionList(Model model,Exhibition exhibition,HttpServletRequest request,
			HttpServletResponse hsr){
		
		paramMap = getRequestParameter(request);
			Page<Exhibition> pageInfo = exhibitionService.selectByPage(paramMap);
		return pageInfo;
	}
}
