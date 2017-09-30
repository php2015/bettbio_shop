package com.bettbio.shop.web.controller.brand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.upload.FileUploadUtils;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.service.ProductBrandService;
import com.github.pagehelper.PageInfo;
/**
 * 后台品牌管理
 * @author simon
 *
 */
@Controller
@RequestMapping("/manager/brand")
public class ProductBrandController extends BaseController{
	
	@Autowired
	ProductBrandService productBrandService;
	
	/**
	 * 品牌列表
	 * @param model
	 * @param sProductBrand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/list")
	public String loadSProductBrands(Model model, SProductBrand sProductBrand, HttpServletRequest request){
		paramMap = getRequestParameter(request);
		Page<SProductBrand> pageInfo = productBrandService.selectByPage(paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("name",sProductBrand.getName());
		return "/manager/brand/list";
	}
	
	/**
	 * 单个删除
	 * @param model
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteBrand")
	public int deleteSProductBrand(Model model,int id){
		int flag = productBrandService.delete(id);
		return flag;
	}
	
	/**
	 * 批量删除
	 */
	@ResponseBody
	@RequestMapping("/deleteBrands")
	public int deleteSProductBrands(Model model,String ids){
		int flag = 0;
		try {
			flag = productBrandService.deleteBrandList(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 删除全部
	 */
	@ResponseBody
	@RequestMapping("/delBrandAll")
	public int deleteAllBrands(Model model){
		int flag = 0;
		try {
			flag = productBrandService.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@RequestMapping("/save")
	public String saveBrand(HttpServletRequest request,Model model,@Valid SProductBrand sProductBrand,MultipartFile file){
		
		try {
			String brandLogo = "";
			if(file.getSize()!=0){
				brandLogo = FileUploadUtils.uploadImg(request, file);
			}
			sProductBrand.setBrandLogo(brandLogo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i = 0;
		sProductBrand.setIsDisable(1);
		if(sProductBrand.getId() == null){
			sProductBrand.setCode(CodeUtils.getCode());
			sProductBrand.setCreateDate(new Date());
			i = productBrandService.save(sProductBrand);
		}else{
			sProductBrand.setUpdateDate(new Date());
			i = productBrandService.updateNotNull(sProductBrand);
		}
		
		if(i==1){
			model.addAttribute("mess","操作成功");
		}else{
			model.addAttribute("mess","操作失败");
		}
		model.addAttribute("sProductBrand",sProductBrand);
		return "manager/brand/create";
	}
	
	@RequestMapping("/info")
	public String info(Model model,int id){
		SProductBrand sProductBrand = new SProductBrand();
		sProductBrand.setId(id);
		sProductBrand = productBrandService.selectByKey(sProductBrand);
		model.addAttribute("sProductBrand", sProductBrand);
		return "manager/brand/create";
	}
}
