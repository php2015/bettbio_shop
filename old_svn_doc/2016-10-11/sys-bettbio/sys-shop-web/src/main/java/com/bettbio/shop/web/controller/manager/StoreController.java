package com.bettbio.shop.web.controller.manager;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.utils.PasswordHelper;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.upload.FileUploadUtils;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.vo.StoreAndUserVo;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.StoreUserService;

/**
 * 商家管理
 * @author simon
 *
 */
@Controller("managerStoreController")
@RequestMapping("/manager/store")
public class StoreController extends BaseController{

	@Autowired
	StoreUserService storeUserService;
	
	@Autowired
	SStoreService sStoreService;
	
	/**
	 * 创建商家
	 * @param model
	 * @return
	 */
	@RequestMapping("/create")
	public String create(Model model){
		StoreAndUserVo storeAndUser = new StoreAndUserVo();
		model.addAttribute("storeAndUser", storeAndUser);
		return "/manager/store/create";
	}
	
	/**
	 * 商家详情
	 * @param model
	 * @return
	 */
	@RequestMapping("/store")
	public String StoreInfo(HttpServletRequest request, Model model){
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		StoreAndUserVo storeAndUser = new StoreAndUserVo();
		try {
			storeAndUser = sStoreService.getStoreAndUser(sStoreUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("storeAndUser", storeAndUser);
		return "/manager/store/store";
	}
	
	/**
	 * 商家详情
	 * @param model
	 * @return
	 */
	@RequestMapping("/info")
	public String info(String code, Model model){
		SStore sStore = new SStore();
		sStore.setCode(code);
		StoreAndUserVo storeAndUser = new StoreAndUserVo();
		try {
			storeAndUser = sStoreService.getStoreAndDefaultUser(sStore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("storeAndUser", storeAndUser);
		return "/manager/store/store";
	}
	
	/**
	 * 保存商家
	 */
	@RequestMapping("/save")
	public String save(HttpServletRequest request,Model model,StoreAndUserVo storeAndUser){
		SStore sStore = new SStore();
		String code = CodeUtils.getCode();
		sStore.setCode(code);
		sStore.setName(storeAndUser.getName());
		sStore.setIsSeedUser(Integer.parseInt(storeAndUser.getIsSeedUser()));
		sStore.setEmail(storeAndUser.getEmail());
		String address = ","+storeAndUser.getProvincial()+"@,"+storeAndUser.getCity()+"@,"+storeAndUser.getArea()+"@,"+storeAndUser.getAddress();
		sStore.setAddress(address);
		sStore.setChineseAbout(storeAndUser.getChineseAbout());
		sStore.setEnglishAbout(storeAndUser.getEnglishAbout());
		sStore.setStoreUrl(storeAndUser.getStoreUrl());
		sStore.setCreateDate(new Date());
		sStore.setLandline(storeAndUser.getLandline());
		int storeFlag = 0;
		String licenseImageUrl = "";
		try {
			if(storeAndUser.getFile().getSize()!=0){
				licenseImageUrl = FileUploadUtils.uploadImg(request, storeAndUser.getFile());
				sStore.setLicenseImageUrl(licenseImageUrl);
				storeAndUser.setLicenseImageUrl(licenseImageUrl);
			}else{
				sStore.setLicenseImageUrl("");
				storeAndUser.setLicenseImageUrl("");
			}
			storeFlag = sStoreService.save(sStore);
		} catch (Exception e) {
				e.printStackTrace();
			} 
		if(storeFlag == 1){
			model.addAttribute("storeAndUser", storeAndUser);
			model.addAttribute("sucMess", "保存成功");
		}else{
			model.addAttribute("storeAndUser", storeAndUser);
			model.addAttribute("errMess", "保存失败");
		}
		return "/manager/store/create";
	}
	
	/**
	 * 保存商家用户
	 */
	@RequestMapping("/saveUser")
	public String saveUser(HttpServletRequest request,Model model,SStoreUser sStoreUser,int storeId){
		String password = sStoreUser.getPassword();
		String storeCode = sStoreService.selectByKey(storeId).getCode();
		sStoreUser.setStoreCode(storeCode);
		sStoreUser.setCode(CodeUtils.getCode());
		sStoreUser.setIsDeleted(1);
		sStoreUser.setPassword(PasswordHelper.newPassword(sStoreUser.getPassword()));
		sStoreUser.setCreateDate(new Date());
		int storeUserFlag = 0;
		try {
			if("default".equals(sStoreUser)){
				SStoreUser storeUser = storeUserService.getStoreUserByParCode("default",storeCode);
				storeUser.setParentCode(storeCode);
				storeUserService.updateNotNull(storeUser);
			}else{
				SStoreUser storeUser = storeUserService.getStoreUserByParCode("default",storeCode);
				sStoreUser.setParentCode(storeUser.getCode());
			}
			storeUserFlag = storeUserService.save(sStoreUser);
		} catch (Exception e) {
				e.printStackTrace();
			} 
		sStoreUser.setPassword(password);
		if(storeUserFlag == 1){
			model.addAttribute("sStoreUser", sStoreUser);
			model.addAttribute("sucMess", "保存成功");
		}else{
			model.addAttribute("sStoreUser", sStoreUser);
			model.addAttribute("errMess", "保存失败");
		}
		return "/manager/store/createUser";
	}
	
	/**
	 * 商家更新
	 */
	@RequestMapping("/update")
	public String update(Model model,StoreAndUserVo storeAndUser,HttpServletRequest request){
		SStore sStore = new SStore();
		sStore.setId(storeAndUser.getStoreId());
		sStore = sStoreService.selectByKey(sStore);
		sStore.setLandline(storeAndUser.getLandline());
		sStore.setName(storeAndUser.getName());
		sStore.setIsSeedUser(Integer.parseInt(storeAndUser.getIsSeedUser()));
		sStore.setUpdateDate(new Date());
		sStore.setEmail(storeAndUser.getEmail());
		String address = ","+storeAndUser.getProvincial()+"@,"+storeAndUser.getCity()+"@,"+storeAndUser.getArea()+"@,"+storeAndUser.getAddress();
		sStore.setAddress(address);
		sStore.setChineseAbout(storeAndUser.getChineseAbout());
		sStore.setEnglishAbout(storeAndUser.getEnglishAbout());
		sStore.setStoreUrl(storeAndUser.getStoreUrl());
		try {
			String licenseImageUrl = "";
			if(storeAndUser.getFile().getSize()!=0){
				licenseImageUrl = FileUploadUtils.uploadImg(request, storeAndUser.getFile());
				sStore.setLicenseImageUrl(licenseImageUrl);
				storeAndUser.setLicenseImageUrl(licenseImageUrl);
			}else{
				sStore.setLicenseImageUrl("");
				storeAndUser.setLicenseImageUrl("");
			}
			int sStoreFlag = sStoreService.updateNotNull(sStore);
			if(sStoreFlag == 1){
				model.addAttribute("storeAndUser", storeAndUser);
				model.addAttribute("sucMess", "更新成功");
			}else{
				model.addAttribute("storeAndUser", storeAndUser);
				model.addAttribute("errMess", "更新失败");
			}
		} catch (Exception e) {
			model.addAttribute("storeAndUser", storeAndUser);
			model.addAttribute("errMess", "更新失败");
		}
		return "/manager/store/store";
	}
	
	/**
	 * 商铺列表
	 */
	@RequestMapping("/list")
	public String list(Model model,SStore sStore, HttpServletRequest request){
		paramMap = getRequestParameter(request);
		Page<SStore> pageInfo = sStoreService.selectByPage(paramMap);
		model.addAttribute("pageInfo",  pageInfo);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("sStore", sStore);
		return "/manager/store/list";
	}
	
	/**
	 * 单个删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxResponse delete(SStore sStore,HttpServletRequest request){
		AjaxResponse ajaxResponse = new AjaxResponse();
		int i;
		try {
			i = sStoreService.delete(sStore);
			if(i==1){
				ajaxResponse.setSuccess(true);
				ajaxResponse.setMessage("删除成功");
			}else{
				ajaxResponse.setSuccess(false);
				ajaxResponse.setMessage("删除失败");
			}
		} catch (Exception e) {
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("删除失败");
		}
		return ajaxResponse;
	}
	
	/**
	 * 批量删除
	 */
	@ResponseBody
	@RequestMapping("/deletes")
	public AjaxResponse delstores(Model model,String ids){
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			int	i = sStoreService.deleteStoreList(ids);
				if(i != 0){
					ajaxResponse.setMessage("删除成功");
					ajaxResponse.setSuccess(true);
				}else{
					ajaxResponse.setMessage("删除失败");
					ajaxResponse.setSuccess(false);
				}
			} catch (Exception e) {
				ajaxResponse.setMessage("删除失败");
				ajaxResponse.setSuccess(false);
			}
			return ajaxResponse;
	}
	
	/*
	 * 全部删除
	 */
	@ResponseBody
	@RequestMapping("/deleteAll")
	public AjaxResponse deleteAll(){
		AjaxResponse ajaxResponse = new AjaxResponse();
		try {
			int i =	sStoreService.deleteAll();
			if(i != 0){
				ajaxResponse.setMessage("删除成功");
				ajaxResponse.setSuccess(true);
			}else{
				ajaxResponse.setMessage("删除失败");
				ajaxResponse.setSuccess(false);
			}
		} catch (Exception e) {
			ajaxResponse.setMessage("删除失败");
			ajaxResponse.setSuccess(false);
		}
		return ajaxResponse;
		
	}
	
	/**
	 * 商家logo
	 */
	@RequestMapping("/logo")
	public String logo(HttpServletRequest request, Model model){
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		SStore sStore = new SStore();
		sStore.setCode(sStoreUser.getStoreCode());
		try {
			sStore = sStoreService.selectByCode(sStore);
		} catch (Exception e) {
			e.printStackTrace();
		} 
			model.addAttribute("logoUrl", sStore.getLogoUrl());
			return "/manager/store/logo";
		}
	
	/**
	 * 删除商家logo
	 */
	@RequestMapping("/deleteLogo")
	public String deleteLogo(HttpServletRequest request, Model model){
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		SStore sStore = new SStore();
		sStore.setCode(sStoreUser.getStoreCode());
		try {
			sStore = sStoreService.selectByCode(sStore);
			sStore.setLogoUrl("");
			sStoreService.updateNotNull(sStore);
		} catch (Exception e) {
			e.printStackTrace();
		} 
			model.addAttribute("logoUrl", sStore.getLogoUrl());
			return "/manager/store/logo";
		}
	
	/**
	 * 商家logo上传
	 */
	@RequestMapping("/logoUpdate")
	public String logoUpdate(HttpServletRequest request,MultipartFile file, Model model){
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		SStore sStore = new SStore();
		sStore.setCode(sStoreUser.getStoreCode());
		try {
			sStore = sStoreService.selectByCode(sStore);
			String logoUrl = FileUploadUtils.uploadImg(request, file);
			sStore.setLogoUrl(logoUrl);
			sStoreService.updateNotNull(sStore);
	} catch (Exception e) {
		e.printStackTrace();
	} 
		model.addAttribute("logoUrl", sStore.getLogoUrl());
		return "/manager/store/logo";
	}
}
