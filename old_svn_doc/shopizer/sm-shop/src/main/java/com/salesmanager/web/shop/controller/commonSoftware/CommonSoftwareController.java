package com.salesmanager.web.shop.controller.commonSoftware;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.common.model.CommonSoftware;
import com.salesmanager.core.business.common.service.CommonSoftwareService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;

@Controller
@RequestMapping("/shop/software")
public class CommonSoftwareController extends AbstractController {
	
	
	@Autowired
	private CommonSoftwareService commonSoftwareService;

	
	@RequestMapping(value="/list.html", method={RequestMethod.POST, RequestMethod.GET})
	public String list(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		//Language language = (Language)request.getAttribute("LANGUAGE");
		HashMap<String, String> orders = new HashMap<String, String>();
		orders.put("torder", "asc");
		List<CommonSoftware> list = commonSoftwareService.findByProperties(new String[]{}, new String[]{}, orders);
		model.addAttribute("softwares",list);
		
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Software.softwarelist).append(".").append(store.getStoreTemplate());

		return template.toString();
	}
	
	@RequestMapping(value={"/click.html"}, method=RequestMethod.POST)
	public @ResponseBody String click(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		if (!StringUtils.isBlank(id)) {
			CommonSoftware software = commonSoftwareService.getById(Long.valueOf(id));
			software.setClicknum(software.getClicknum()+1);
//			commonSoftwareService.saveOrUpdate(software);
			commonSoftwareService.update(software);
//			commonSoftwareService.refresh(software);
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			ajaxResponse.setStatusMessage(software.getClicknum().toString());
		} else {
			ajaxResponse.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return ajaxResponse.toJSONString();
	}
	
		
}
