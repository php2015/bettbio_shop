package com.salesmanager.web.admin.controller.configurations;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.MerchantConfig;
import com.salesmanager.core.business.system.service.SepcialConfigureService;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;


@Controller
public class SpecialConfigurationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpecialConfigurationController.class);
	
	@Autowired
	private SepcialConfigureService sepcialConfigureService;

	@Autowired
	Environment env;
	

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/configuration/special.html", method=RequestMethod.GET)
	public String displaySysyemConfgurations(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		model.addAttribute("configuration",sepcialConfigureService.getAllConfigure());
		return com.salesmanager.web.admin.controller.ControllerConstants.Tiles.Configuration.special;
	}
	
	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value="/admin/configuration/saveSpecialConfiguration.html", method=RequestMethod.POST)
	public String saveSystemConfigurations(Model model, HttpServletRequest request, Locale locale) throws Exception
	{
		setMenu(model, request);
		Map<String, Object> configData = new HashMap<String, Object>();
		Map<String, Object> oldCfg = sepcialConfigureService.getAllConfigure();
		Enumeration names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = (String) names.nextElement();
			if (!key.startsWith("configuration.")){
				continue;
			}
			String propKey = key.substring("configuration.".length());
			Object oldValue = oldCfg.get(propKey);
			Object newValue = request.getParameter(key);
			if (oldValue == null){
				configData.put(propKey, newValue);
				continue;
			}
			if (oldValue.equals(newValue)){
				continue;
			}
			configData.put(propKey, newValue);
		}
		sepcialConfigureService.saveConfigure(configData);
		model.addAttribute("success","success");
		model.addAttribute("configuration",sepcialConfigureService.getAllConfigure());
		return com.salesmanager.web.admin.controller.ControllerConstants.Tiles.Configuration.special;
		
	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("special-configurations", "special-configurations");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("configuration");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
	}
}
