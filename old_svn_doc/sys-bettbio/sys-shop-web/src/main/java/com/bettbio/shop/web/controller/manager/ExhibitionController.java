package com.bettbio.shop.web.controller.manager;

import java.text.SimpleDateFormat;
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

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.upload.FileUploadUtils;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.Exhibition;
import com.bettbio.core.model.ExhibitionRecord;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.bo.ExhibitionStoreBo;
import com.bettbio.core.service.ExhibitionRecordService;
import com.bettbio.core.service.ExhibitionService;
import com.bettbio.core.service.SStoreService;
import com.github.pagehelper.PageInfo;

/**
 * 展会
 * @author simon
 *
 */
@Controller
public class ExhibitionController extends BaseController {

	@Autowired
	ExhibitionRecordService  exhibitionRecordService;
	
	@Autowired
	ExhibitionService  exhibitionService;
	
	@Autowired
	SStoreService sStoreService;
	
	@RequestMapping("/manager/exhibition/create")
	public String create(){
		return "/manager/exhibition/create";
	}
	
	/**
	 * 商家报名
	 * @param exhibitionRecord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/manager/exhibition/saveExhibition")
	public AjaxResponse saveExhibition(ExhibitionRecord exhibitionRecord){
		AjaxResponse ajaxResponse = new AjaxResponse();
		int i =	exhibitionRecordService.save(exhibitionRecord);
		if(i == 1){
			ajaxResponse.setMessage("保存成功");
			ajaxResponse.setSuccess(true);
		}else{
			ajaxResponse.setMessage("保存失败");
			ajaxResponse.setSuccess(false);
		}
		return ajaxResponse;
	}
	
	/**
	 * 发布展会
	 * @param exhibitionRecord
	 * @return
	 */
	@RequestMapping("/manager/exhibition/save")
	public String save(Exhibition exhibition,MultipartFile file, HttpServletRequest request,Model model){
		String posterImg = "";
		exhibition.setCode(CodeUtils.getCode());
		if(file != null && file.getSize()!=0){
			try {
				posterImg = FileUploadUtils.uploadImg(request, file);
				exhibition.setPosterImg(posterImg);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else{
			exhibition.setPosterImg("");
		}
		int i= exhibitionService.save(exhibition);
		if(i == 1){
			model.addAttribute("sucMess", "保存成功");
		}else{
			model.addAttribute("errMess", "保存失败");
		}
		SimpleDateFormat dd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date begin = exhibition.getBeginDate();
		Date end = exhibition.getEndDate();
		String beginDate = dd.format(begin); 
		String endDate = dd.format(end); 
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("exhibition", exhibition);
		
		return "/manager/exhibition/create";
	}
	
	/**
	 * 展会首页
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/exhibition/apply")
	public String apply(HttpServletRequest request,Model model,Exhibition exhibition){
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
		SimpleDateFormat dd = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date begin = exhibition.getBeginDate();
		Date end = exhibition.getEndDate();
		String beginDate = dd.format(begin); 
		String endDate = dd.format(end); 
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("exhibition", exhibition);
		return "/admin/exhibition/apply";
	}
	
	/**
	 * 参加展会
	 */
	@ResponseBody
	@RequestMapping("/admin/exhibition/join")
	public AjaxResponse join(HttpServletRequest request,String code){
		AjaxResponse ajaxResponse = new AjaxResponse();
		SStoreUser sStoreUser = (SStoreUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		ExhibitionRecord exhibitionRecord = new ExhibitionRecord();
		exhibitionRecord.setStoreCode(sStoreUser.getStoreCode());
		exhibitionRecord.setExhibitionCode(code);//展会code
		exhibitionRecord.setCreateDate(new Date());
		exhibitionRecord.setIsAffirm(0);
		int i = exhibitionRecordService.save(exhibitionRecord);
		if(i == 1){
			ajaxResponse.setSuccess(true);
		}else{
			ajaxResponse.setSuccess(false);
		}
		return ajaxResponse;
	}
	
	
	/**
	 * 参展商列表
	 */
	@RequestMapping("/manager/exhibition/list")
	public String list(Model model,HttpServletRequest request, ExhibitionStoreBo exhibitionStoreBo){
		List<ExhibitionStoreBo> eExhibitionStoreBoList = new ArrayList<ExhibitionStoreBo>();
		Exhibition exhibition = new Exhibition();
		exhibition.setId(exhibitionStoreBo.getExhibitionId());
		try {
			exhibition = exhibitionService.selectByKey(exhibition);
			eExhibitionStoreBoList = sStoreService.selectStoreByExhibitionCode(exhibition.getCode(),exhibitionStoreBo.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("eExhibitionStoreBoList", eExhibitionStoreBoList);
		model.addAttribute("name", exhibitionStoreBo.getName());
		model.addAttribute("exhibitionId", exhibitionStoreBo.getExhibitionId());
		return "/manager/exhibition/list";
	}
	
	/**
	 * 确认参展
	 */
	@ResponseBody
	@RequestMapping("/manager/exhibition/affirm")
	public AjaxResponse affirm(int id,String isAffirm){
		AjaxResponse ajaxResponse =new AjaxResponse();
		ExhibitionRecord exhibitionRecord = new ExhibitionRecord();
		exhibitionRecord.setId(id);
		exhibitionRecord.setIsAffirm(isAffirm.equals("true")?0:1);
		int i = exhibitionRecordService.updateNotNull(exhibitionRecord);
		if(i == 1){
			ajaxResponse.setMessage("操作成功");
			ajaxResponse.setSuccess(true);
		}else{
			ajaxResponse.setMessage("操作失败");
			ajaxResponse.setSuccess(false);
		}
		return ajaxResponse;
	}
	
	/**
	 * 展会列表
	 */
	@RequestMapping("/admin/exhibition/list")
	public String adminExhibitionList(Model model, Exhibition exhibition,HttpServletRequest request){
		paramMap = getRequestParameter(request);
			Page<Exhibition> pageInfo = exhibitionService.selectByPage(paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("title", exhibition.getTitle());
		return "/admin/exhibition/list";
	}
	
	/**
	 * 展会列表
	 */
	@RequestMapping("/manager/exhibition/exhibitionList")
	public String managerExhibitionList(Model model,Exhibition exhibition,HttpServletRequest request){
		paramMap = getRequestParameter(request);
		Page<Exhibition> pageInfo = exhibitionService.selectByPage(paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("title", exhibition.getTitle());
		return "/manager/exhibition/exhibitionList";
	}
	
	/**
	 * 修改展会状态
	 */
	@RequestMapping("/manager/exhibition/update")
	@ResponseBody
	public AjaxResponse update(Exhibition exhibition){
		AjaxResponse ajaxResponse = new AjaxResponse();
		int i = exhibitionService.updateNotNull(exhibition);
		if(i == 1){
			ajaxResponse.setMessage("保存成功");
			ajaxResponse.setSuccess(Boolean.TRUE);
		}else{
			ajaxResponse.setMessage("保存失败");
			ajaxResponse.setSuccess(Boolean.FALSE);
		}
		return ajaxResponse;
	}
}
