package com.test.mode;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.service.CountryService;
import com.bettbio.core.service.OrderService;
import com.bettbio.core.service.ProductBrandService;
import com.bettbio.core.service.ProductClassificationService;
import com.bettbio.core.service.SUserService;
import com.test.mode.base.BaseT;

public class TestProductCase extends BaseT{

	@Autowired
	CountryService countryService;
		
	@Autowired
	ProductClassificationService productClassificationService;
	
	@Autowired
	ProductBrandService productBrandService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SUserService sUserService;
	
	@Test
	public void select(){
		//productService.selectByPage(null, 1, 10);
		//countryService.selectByPage(null, 1, 10);
		//SProduct  sProduct = new SProduct();
		//sProduct.setCode("code");
		//sProduct.setId(123212);
		//int j = productService.save(sProduct);
		//int i = productService.delete("1");
		//System.out.println(i);
	}
	
	@Test
	public void deleteProduct(){
	}
	
	@Test
	public void getImg(){
	}
	
	@Test
	public void saveProductClassification(){
		SProductClassification sProductClassification = new SProductClassification();
		//sProductClassification.setId(222);
		sProductClassification.setName("nn");
		//productClassificationService.save(sProductClassification);
		try {
			List<SProductClassification> sProductClassificationList = productClassificationService.selectByPage(sProductClassification, 1, 6);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getBrand(){
		SProductBrand sProductBrand = new SProductBrand();
		sProductBrand.setName("name");
		//List<SProductBrand> sProductBrandlist =	productBrandService.selectByPage(sProductBrand, 2, 1);
		try {
			List<SProductBrand> sProductBrandlist = productBrandService.selectPagebyNameOrCode("", 1, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void select1(){
		System.out.println("mm");
		//List<SProductClassification> sProductClassification = productClassificationService.selectPagebyNameOrCode("2", 1, 3);
		//List<SOrderBo> sOrderList = orderService.selectPagebyProductOrcode("1","year",3, 1, 2);
		
	}
	
	@Test
	public void login(){
		
	}
	public static void main(String[] args) {
		String name="helloworld";
	    System.out.println(name.substring(1,name.length()));//输出d
	}
}
