package com.salesmanager.web.shop.controller.newsContent;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.newscontent.model.NewsContent;
import com.salesmanager.core.business.newscontent.service.NewsContentService;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PageInformation;
import com.salesmanager.web.shop.controller.AbstractController;
import com.salesmanager.web.shop.controller.ControllerConstants;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

/**
 * 
 * @class com.salesmanager.web.shop.controller.newsContent.NewsContentController.java
 * @author sam
 * @date 2016年1月27日
 */
@Controller
@RequestMapping("/shop/news")
public class NewsContentController extends AbstractController {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(NewsContentController.class);
	
	@Autowired
	private LabelUtils messages;
	@Autowired
	private NewsContentService newsContentService;
	
	@RequestMapping(value="/list.html", method={RequestMethod.POST, RequestMethod.GET})
	public String list(@Valid @ModelAttribute("criteria") Criteria criteria,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestParam(value = "page", defaultValue = "1") final int page) throws Exception {
		
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.NewsContent.newslist).append(".").append(store.getStoreTemplate());
		PaginationData paginaionData=createPaginaionData(page,15);
		if(criteria == null) {
			criteria = new Criteria();
		}
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		List<NewsContent> list = newsContentService.getListByCriteria(criteria);
		model.addAttribute( "newslist", list);
		if(list!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, Long.valueOf(criteria.getTotalCount()).intValue()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		return template.toString();
	}
	
	@RequestMapping(value="/view/{newsid}.html",  method={RequestMethod.GET})
	public String view(@PathVariable final String newsid, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		if (StringUtils.isBlank(newsid)) {
			return PageBuilderUtils.build404(store);
		}
		NewsContent news = newsContentService.getById(Long.valueOf(newsid));
		if (news == null) {
			return PageBuilderUtils.build404(store);
		}
		//meta information
		PageInformation pageInformation = new PageInformation();
		//set meta descripton
		pageInformation.setPageDescription(news.getSummary());
		pageInformation.setPageKeywords(messages.getMessage("label.website.keywords", locale)+","+news.getKeywords());
		pageInformation.setPageTitle(news.getLinkText());
		pageInformation.setPageUrl(news.getLinkHref());
		
		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);
		model.addAttribute("news", news);
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.NewsContent.newsview).append(".").append(store.getStoreTemplate());
		return template.toString();
	}
}
