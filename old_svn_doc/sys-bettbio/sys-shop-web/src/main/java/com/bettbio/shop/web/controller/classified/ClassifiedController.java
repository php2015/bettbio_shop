package com.bettbio.shop.web.controller.classified;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.common.web.validate.AjaxResponse;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.ClassifyZtreeBo;
import com.bettbio.core.service.ProductClassificationService;

@Controller
@RequestMapping("/manager/classified")
public class ClassifiedController extends BaseController{

	@Autowired
	ProductClassificationService productClassificationService;
	
	@RequestMapping("/create")
	public String categoryExpress(Model model) {
	List<SProductClassification> sProductClassificationlist = productClassificationService.selectAll();
		model.addAttribute("sProductClassificationlist", sProductClassificationlist);
		return "/manager/classified/create";
	}
	
	@RequestMapping("/tree")
	public String categoryTree() {
		
		return "/manager/classified/tree";
	}
	
	/**
	 * 加载类别清单列表
	 * @param model
	 * @param nameOrCode
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/list")
	public String loadClassifiedList(Model model,SProductClassification sProductClassification, HttpServletRequest request){
		paramMap = getRequestParameter(request);
			Page<SProductClassification> pageInfo = productClassificationService.selectByPage(paramMap);
			model.addAttribute("pageInfo", pageInfo);
			model.addAttribute("paramMap", paramMap);
			model.addAttribute("name",sProductClassification.getName());
			return "/manager/classified/list";
	}
	
	/**
	 * 单个删除
	 * @param model
	 * @param id
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delclassify")
	public int delclassify(Model model,int id){
			int i =	productClassificationService.delete(id);
			return i;
	}
	
	/**
	 * 批量删除
	 * @param model
	 * @param ids
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delclassifys")
	public int delclassifys(Model model,String ids){
			int i = 0;
			try {
				i = productClassificationService.deleteClassificationList(ids);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return i;
	}
	
	/**
	 * 删除全部
	 * @param model
	 * @param ids
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delclassifyAll")
	public int delClassifyAll(Model model,String ids){
			int i = 0;
			try {
				i = productClassificationService.deleteAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return i;
	}
	
	@RequestMapping("/save")
	public String saveClassify(Model model,@Valid SProductClassification sProductClassification){
		String checkCode = sProductClassification.getParentCode();
		int i = 0;
		if(sProductClassification.getId() == null){
			sProductClassification.setCreateDate(new Date());
			String[] idCode = sProductClassification.getParentCode().split(",");
			String code = productClassificationService.selectMaxCode(idCode[1]);
			sProductClassification.setCode((Integer.parseInt(code)+1)+"");
			sProductClassification.setParentCode(idCode[0]);
			i = productClassificationService.save(sProductClassification);
		}else{
			sProductClassification.setUpdateDate(new Date());
			i = productClassificationService.updateNotNull(sProductClassification);
		}
		
		if(i==1){
			model.addAttribute("mess","操作成功");
		}else{
			model.addAttribute("mess","操作失败");
		}
		List<SProductClassification> sProductClassificationlist = productClassificationService.selectAll();
		model.addAttribute("sProductClassificationlist", sProductClassificationlist);
		model.addAttribute("checkCode", checkCode);
		return "manager/classified/create";
	}
	
	@RequestMapping("/info")
	public String info(Model model,int id){
		SProductClassification sProductClassification = new SProductClassification();
		sProductClassification.setId(id);
		sProductClassification = productClassificationService.selectByKey(sProductClassification);
		model.addAttribute("sProductClassification", sProductClassification);
		List<SProductClassification> sProductClassificationlist = productClassificationService.selectAll();
		model.addAttribute("sProductClassificationlist", sProductClassificationlist);
		return "manager/classified/create";
	}
	
	/**
	 * 加载分类树
	 * @param parentCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectTree")
	public List<ClassifyZtreeBo> selectTree(String parentCode){
		List<ClassifyZtreeBo> classifyZtreeBoList = new ArrayList<ClassifyZtreeBo>();
		try {
			classifyZtreeBoList  = productClassificationService.selectClassByparentCode(parentCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classifyZtreeBoList;
	}
	
	/**
	 * 更新分类树
	 */
	@ResponseBody
	@RequestMapping("/update")
	public AjaxResponse update(int id,String parentCode){
		AjaxResponse ajaxResponse = new AjaxResponse();
		SProductClassification sProductClassification =new SProductClassification();
		sProductClassification.setId(id);
		sProductClassification = productClassificationService.selectByKey(sProductClassification);
		sProductClassification.setParentCode(parentCode);
		int i = productClassificationService.updateAll(sProductClassification);
		if(i == 1){
			ajaxResponse.setSuccess(true);
		}else{
			ajaxResponse.setSuccess(false);
		}
		return ajaxResponse;
	}
}
