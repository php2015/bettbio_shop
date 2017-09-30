package com.bettbio.shop.web.controller.product;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.excel.ProductExcelService;


@Controller
@RequestMapping("admin/product")
public class ProductExcelController extends BaseController{

	@Autowired
	SStoreService storeService;

	@Autowired
	ProductExcelService productExcelService;
	
	@RequestMapping("/import")
	public String importView(){
		return viewName("import");
	}

	@RequestMapping("/importProduct")
	public String importProduct(
            @RequestParam("type") Integer type,
            @RequestParam("file") CommonsMultipartFile file,
            Model model,
            HttpServletRequest request){

        if(!canImport(file, model)) {
    		return viewName("import");
        }
		try {
			
			SStoreUser sStoreUser = (SStoreUser) request.getSession().getAttribute(Constants.CURRENT_USER);
			SStore store = new SStore();
			store.setCode(sStoreUser.getStoreCode());
			store = storeService.selectByCode(store);
			
			InputStream is = file.getInputStream();
			productExcelService.importProduct(store, is, type);
			
			model.addAttribute(Constants.MESSAGE, "表格已提交，后台正在执行导入");
		} catch (Exception e) {
			model.addAttribute(Constants.ERROR, "操作失败！");
		}

		return viewName("import");
	}
	

    private boolean canImport(final MultipartFile file, final Model model) {
        if(file == null || file.isEmpty()) {
            model.addAttribute(Constants.ERROR, "请选择要导入的文件");
            return false;
        }

        String filename = file.getOriginalFilename().toLowerCase();
        if(!(filename.endsWith("xls") || filename.endsWith("xlsx"))) {
            model.addAttribute(Constants.ERROR, "导入的文件格式错误，允许的格式：xls、xlsx");
            return false;
        }

        return true;
    }
}
