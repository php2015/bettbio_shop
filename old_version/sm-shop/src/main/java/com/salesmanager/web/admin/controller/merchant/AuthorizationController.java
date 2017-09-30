package com.salesmanager.web.admin.controller.merchant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.authorization.service.AuthorizationService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.controller.customers.CustomerController;
import com.salesmanager.web.admin.controller.products.facade.ManufactureFacade;
import com.salesmanager.web.admin.entity.catalog.Product;
import com.salesmanager.web.admin.entity.products.ReadableManufactureList;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.ImageMarkLogoByIcon;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.PageBuilderUtils;
import com.salesmanager.web.utils.PinyinUtils;

@Controller
public class AuthorizationController {
	
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CoreConfiguration configuration;
	
	@Autowired 
	ManufactureFacade manufactureFacade ; 
	
	@Autowired
	MerchantStoreService merchantStoreService;
	
	@Autowired
	AuthorizationService authorizationService;
	
	@Autowired
	private ProductService productService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/authorization.html", method={RequestMethod.GET,RequestMethod.POST})
	public String getManufacturers(@Valid @ModelAttribute("criteria") ManufactureCriteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PaginationData paginaionData=createPaginaionData(page,15);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		if(criteria == null) criteria = new ManufactureCriteria();	
		
		if (!store.getCode().equals(Constants.DEFAULT_STORE)) {
			criteria.setStoreId(store.getId()); //如果是非系统管理，则只允许查看到自己创建的品牌
		}
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		//Language language = (Language)request.getAttribute("LANGUAGE");
		ReadableManufactureList olist = manufactureFacade.getByManufacturer();
		List<Authorization> aList = authorizationService.findByPropertiesHQL(new String[]{"merchantStore.id","auth_type"}, new Object[]{store.getId(),1}, null);
		model.addAttribute( "manus", olist);
		if(olist!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, olist.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		Authorization auth = new Authorization();
		Product product = new Product();
		model.addAttribute( "product", product);
		model.addAttribute( "auth", auth);
		model.addAttribute( "aList", aList);
		model.addAttribute( "store", store);
		this.setMenu(model, request,"authorization");
		
		return "admin-authorization";
	}
	/*
	 * 代理认证
	 */
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/agentAuthorization.html", method={RequestMethod.GET,RequestMethod.POST})
	public String agentAuthorization(@Valid @ModelAttribute("criteria") ManufactureCriteria criteria,@RequestParam(value = "page", defaultValue = "1") final int page,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PaginationData paginaionData=createPaginaionData(page,15);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		if(criteria == null) criteria = new ManufactureCriteria();
		
//		if (!store.getCode().equals(Constants.DEFAULT_STORE)) {
//			criteria.setStoreId(store.getId()); //如果是非系统管理，则只允许查看到自己创建的品牌
//		}
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		//Language language = (Language)request.getAttribute("LANGUAGE");
		//查询出厂家的品牌
		ReadableManufactureList olist = manufactureFacade.getByManufacturer();
		//查询厂家的详细信息
		List<Authorization> aList = authorizationService.findByPropertiesHQL(new String[]{"merchantStore.id","auth_type"}, new Object[]{store.getId(),2}, null);
		//List<Authorization> aList = authorizationService.findByPropertiesHQL(new String[]{"merchantStore.id"}, new Long[]{store.getId()}, null);
		model.addAttribute( "manus", olist);
		if(olist!=null) {
        	model.addAttribute( "paginationData", PageBuilderUtils.calculatePaginaionData(paginaionData, Constants.MAX_ORDERS_PAGE, olist.getTotalCount()));
        } else {
        	model.addAttribute( "paginationData", null);
        }
		Authorization auth = new Authorization();
		Product product = new Product();
		model.addAttribute( "product", product);
		model.addAttribute( "auth", auth);
		model.addAttribute( "aList", aList);
		model.addAttribute( "store", store);
		this.setMenu(model, request,"agentAuthorization");
		
		return "admin-agentAuthorization";
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	/*
	 * 添加授权书
	 */
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/addAuth.html")
	@ResponseBody
	public String addAuth(@Valid @ModelAttribute("auth") Authorization auth, Model model,
	    HttpServletRequest request, HttpServletResponse response) {
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Long merchantId = (Long.parseLong(request.getParameter("merchantId")));//厂家id

		try{
			Set<Manufacturer> manSet = new HashSet<Manufacturer>();
			String idsId = request.getParameter("idsid");
			String [] idsStoreId = idsId.split(",");
			List<ManufacturerDescription> oldmanList = manufacturerService.queryByManufacturerId(store.getId());
			List<Long> lonparam = new ArrayList<Long>();
		    for(int i=0;i<idsStoreId.length;i++)
		    {
		    	Manufacturer m = new Manufacturer();
				m.setId(Long.valueOf(idsStoreId[i].toString().trim()));
				manSet.add(m);
				lonparam.add(Long.valueOf(idsStoreId[i].toString().trim()));
		    }
		    for(int m=0;m<lonparam.size();m++){
		    	List<Long> lonparam1 = new ArrayList<Long>();
		    	lonparam1.add(lonparam.get(m));
		    	//判断是否已经添加过授权(对已经授权的不进行加分)
			    List<Authorization> authList = productService.getAuthorizationbyMerchantId(lonparam1,merchantId);
			    int auth_type_1 = 0;//厂家认证
			    int auth_type_2 = 0;//代理认证
			    if(1 == auth.getAuth_type()){
			    	auth_type_1 = 20;
			    	for(int i=0;i<authList.size();i++){
				    	if(1==authList.get(i).getAuth_type()){
				    		auth_type_1 = 0;
				    	}
				    }
			    }else if(2 == auth.getAuth_type()){
			    	auth_type_2 = 20;
			    	for(int i=0;i<authList.size();i++){
				    	if(2==authList.get(i).getAuth_type()){
				    		auth_type_2 = 0;
				    	}
				    }
			    }
			    
			    
				List<com.salesmanager.core.business.catalog.product.model.Product> proList =
						productService.getProductsByMan(lonparam1, merchantId);
				//质量分数
				for(int i=0;i<idsStoreId.length;i++)
			    {
					for(int j=0;j<oldmanList.size();j++){
			    		
			    		if(Long.valueOf(idsStoreId[i].toString().trim())==oldmanList.get(i).getId()){
							return "errorx";
			    		 }
				    }
			    }
				for (com.salesmanager.core.business.catalog.product.model.Product pro : proList) {
					com.salesmanager.core.business.catalog.product.model.Product p = new com.salesmanager.core.business.catalog.product.model.Product();
					p.setQualityScore(pro.getQualityScore()+auth_type_1+auth_type_2);
					pro.setQualityScore(p.getQualityScore());
					//最高分
					if (pro.getQualityScore() > 99) {
						pro.setQualityScore(99);
					}
					//productService.update(pro);(有问题)
				}
		    }
		    
			auth.setManufacturer(manSet);
			auth.setMerchantStore(store);
//			auth.setStartTime(sdf.parse(startTime));
//			auth.setEndTime(sdf.parse(endTime));
			
			authorizationService.save(auth);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		model.addAttribute("store", store);
		return "success";
	}
	
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value="/admin/store/delAuth.html")
	@ResponseBody
	public String delAuth(String id,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			//Authorization au = authorizationService.getById(Long.valueOf(id));
			authorizationService.delete(authorizationService.getEntity(Authorization.class, Long.valueOf(id)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}	
		return "success";
	}
	
	@RequestMapping(value="/shop/authorizationDetail.html", method={RequestMethod.GET,RequestMethod.POST})
	public String getById(String id,Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (id!=null&&id!="") {
			Authorization auth = authorizationService.getById(Long.valueOf(id));
			model.addAttribute( "auth", auth);
			model.addAttribute("store", auth.getMerchantStore());
		}
		
		return "authDetail.bootstrap3";
	}
	
	
	

	private void setMenu(Model model, HttpServletRequest request,String cur) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("store", "store");
		activeMenus.put(cur, cur);
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("store");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	@RequestMapping(value="/admin/store/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = manufacturerService.getStoreName();
			List<String> ch = new ArrayList<String>();
			ch.add("b");
			ch.add("g");
			ch.add("k");
			ch.add("n");
			ch.add("r");
			ch.add("s");
			ch.add("w");
			ch.add("z");
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores,ch);
			return pinyin;
		}catch (Exception e){
			return null;
		}
		
	}
	//商家和代理凭证
		@RequestMapping(value="/admin/store/fileupload.html",method={RequestMethod.GET,RequestMethod.POST})
		public @ResponseBody String uploadAndStroeImg(@RequestParam MultipartFile[] myfiles, HttpServletRequest request, HttpServletResponse response) throws IOException{
	    	AjaxResponse rep = new AjaxResponse();
			String fileName = "" ,savePath="";
			String originalFilename = myfiles[0].getOriginalFilename();

			fileName = DateUtil.generateTimeStamp() + new Random().nextInt(10000) + "."
					+ originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			String realPath = "C:/upload" + "/" + DateUtil.generateTimeStamp();
			savePath = realPath + "/" + fileName;
	        response.setContentType("text/plain; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        for(MultipartFile myfile : myfiles){
	        	// 文件写入磁盘
				File dirToMake = new File(realPath);
				if (!dirToMake.exists()) {
					dirToMake.mkdirs();
				}
	            if(myfile.isEmpty()){
	                out.print("1`请选择文件后上传");
	                out.flush();
	            }else{
	                originalFilename = myfile.getOriginalFilename();
	                try {
	                    //此处也可以使用Spring提供的MultipartFile.transferTo(File dest)方法实现文件的上传
	                	ImageMarkLogoByIcon icon = new ImageMarkLogoByIcon();
	        			String auth_path="img/suiyin.png";
	        			InputStream in = myfile.getInputStream();
	        			InputStream out_in = icon.markImageByIcon(in, auth_path, originalFilename.substring(originalFilename.indexOf("."),originalFilename.length() ));
	        		    System.out.println(out_in);
	                    FileUtils.copyInputStreamToFile(out_in, new File(savePath));
	                    //FileCopyUtils.copy(myfile.getBytes(), new File(savePath));
	                } catch (IOException e) {
	                    System.out.println("文件[" + originalFilename + "]上传失败,堆栈轨迹如下");
	                    e.printStackTrace();
	                    out.print("1`文件上传失败，请重试！！");
	                    out.flush();
	                }
	            }
	        }
	        return rep.toJSONString();
	    }
}
