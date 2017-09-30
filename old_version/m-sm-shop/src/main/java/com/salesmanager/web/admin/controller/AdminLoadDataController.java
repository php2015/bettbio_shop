package com.salesmanager.web.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.utils.LoadDataThread;

/**
 * 商品数据加载、导入类
 * elasticsearch索引更新类
 * 图片批量上传
 * @class com.salesmanager.web.admin.controller.AdminLoadDataController.java
 * @author sam
 * @date 2015年8月22日
 */
@Controller
public class AdminLoadDataController {
	
	@Autowired
	LoadDataThread loadDataThread;
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value={"/admin/loaddata.html"}, method=RequestMethod.GET)
	public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ControllerConstants.Tiles.adminLoadData;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/loaddata/impproducts.html", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public @ResponseBody String impproducts(@RequestParam(value="filename", required=false) String filename, @RequestParam(value="flag", required=false) String flag, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().getServletContext();
		AjaxResponse resp = new AjaxResponse();
		Language language = (Language)request.getAttribute("LANGUAGE");
		resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		resp.setStatusMessage("正在处理中，请耐心等待服务端处理...");
		loadDataThread.setFilename(filename);
		loadDataThread.setLanguage(language);
		loadDataThread.setFlag(flag);
		loadDataThread.setContext(request.getSession().getServletContext());
		Thread t = new Thread(loadDataThread);
		t.start();
		String returnString = resp.toJSONString();
		System.out.println(returnString);
		return returnString;
	}
}
