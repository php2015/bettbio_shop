package com.bettbio.shop.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.search.vo.ProductPriceVo;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.service.ProductService;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.StoreUserService;
import com.bettbio.core.service.util.BreadCrumbService;

/**
 * 产品详情
 *
 */
@Controller("ShopProductController")
public class ShopProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	SStoreService sStoreService;
	
	@Autowired
	StoreUserService storeUserService;

	@Autowired
	BreadCrumbService breadCrumbService;
	
	@RequestMapping("product-{id}")
	public String view(@PathVariable Integer id,Model model){
		Product product = productService.selectProductById(id);
		String classIfy = product.getProductClass().getCode();
		
		SStore sStore = new SStore();
		SStoreUser sStoreUser = new SStoreUser();
		sStore.setCode(product.getStoreCode());
		try {
			sStore = sStoreService.selectByCode(sStore);
			sStoreUser = storeUserService.selectByStroeCode(product.getStoreCode());
			model.addAttribute("sStore", sStore);
			model.addAttribute("sStoreUser", sStoreUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("product", product);
		model.addAttribute("breadCrumb", breadCrumbService.buildProductBreadCrumb(product));
		
		if(classIfy.startsWith("01")){
			//试剂
			return "/product/reaProduct";
		}else if(classIfy.startsWith("02") || classIfy.startsWith("03")){
		    //耗材、仪器
			return "/product/conProduct";
		}else {//if(classIfy.startsWith("04")){
			//服务
			return "/product/serProduct";
		}
	}
	
	@ResponseBody
	@RequestMapping("/price")
	public Map<Integer, Product> getPrice(ProductPriceVo priceVo){
		
		if(priceVo.getIds()!=null&&priceVo.getIds().size()>0)
			return productService.getPrdouctPrice(priceVo.getIds());
		
		return null;
	}
}
