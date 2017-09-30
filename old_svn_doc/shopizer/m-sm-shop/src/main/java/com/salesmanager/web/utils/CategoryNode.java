package com.salesmanager.web.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.web.constants.Constants;
@Component
public class CategoryNode {
	
	public com.salesmanager.web.admin.entity.catalog.Category getRoot(HttpServletRequest request){
		com.salesmanager.web.admin.entity.catalog.Category root = new com.salesmanager.web.admin.entity.catalog.Category();
		
			//Map<String, Category> objects = categoryService.listTwoDepthByLanguage(store,language);
			Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
			List<Category>tops = (List<Category>)request.getSession().getServletContext().getAttribute("topCateGory");
			if(cmap !=null && cmap.size()>0 && tops !=null && tops.size()>0){
				List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
				for(Category c:tops){
					nodes.add(this.getNode(cmap.get(c.getId())));
				}
				root.setCategorys(nodes);
			}
		return root;
	}
	
	public com.salesmanager.web.admin.entity.catalog.Category getRootReagent(HttpServletRequest request){
		com.salesmanager.web.admin.entity.catalog.Category root = new com.salesmanager.web.admin.entity.catalog.Category();
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		List<Category>tops = (List<Category>)request.getSession().getServletContext().getAttribute("topCateGory");
		if(cmap !=null && cmap.size()>0 && tops !=null && tops.size()>0){
			List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
			for(Category c:tops){
				if(c.getCode().equalsIgnoreCase(Constants.CATEGORY_REAGENT_CODE)){
					nodes.add(this.getNode(cmap.get(c.getId())));
				}
			}
			root.setCategorys(nodes);
		}
	return root;
	}
	
	public com.salesmanager.web.admin.entity.catalog.Category getRootInstrumentAndConsumer(HttpServletRequest request){
		com.salesmanager.web.admin.entity.catalog.Category root = new com.salesmanager.web.admin.entity.catalog.Category();
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		List<Category>tops = (List<Category>)request.getSession().getServletContext().getAttribute("topCateGory");
		if(cmap !=null && cmap.size()>0 && tops !=null && tops.size()>0){
			List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
			for(Category c:tops){
				if(c.getCode().equalsIgnoreCase(Constants.CATEGORY_INSTRUMENT_CODE) || c.getCode().equalsIgnoreCase(Constants.CATEGORY_CONSUMERGOODS)){
					nodes.add(this.getNode(cmap.get(c.getId())));
				}
			}
			root.setCategorys(nodes);
		}
	return root;
	}
	
	/**
	 * 1:试剂
	 * 2：其他
	 * 3：服务	
	 * @param category
	 * @return
	 */
	public int getProductType(Category category){
		String cCode = category.getCode();
		if(cCode.startsWith(Constants.CATEGORY_SERVICE_CODE)){
			return 3;
		}else if(cCode.startsWith(Constants.CATEGORY_REAGENT_CODE)){
			return 1;
		}else{
			return 2;
		}
	}
	/**
	 * 1:试剂
	 * 2：耗材
	 * 3：仪器
	 * 4：服务	
	 * @param category
	 * @return
	 */
	public int getCateType(Category category){
		String cCode = category.getCode();
		if(cCode.startsWith(Constants.CATEGORY_SERVICE_CODE)){
			return 4;
		}else if(cCode.startsWith(Constants.CATEGORY_REAGENT_CODE)){
			return 1;
		}else if(cCode.startsWith(Constants.CATEGORY_CONSUMERGOODS)){
			return 2;
		}else {
			return 3;
		}
	}
	public String getReturnStri(Category category){
		String cCode = category.getCode();
		if(cCode.startsWith(Constants.CATEGORY_SERVICE_CODE)){
			return "admin-serivce-edit";
		}else if(cCode.startsWith(Constants.CATEGORY_REAGENT_CODE)){
			return "admin-products-edit";
		}else{
			return "admin-instrument-edit";
		}
	}
	public com.salesmanager.web.admin.entity.catalog.Category getRootService(HttpServletRequest request){
		com.salesmanager.web.admin.entity.catalog.Category root = new com.salesmanager.web.admin.entity.catalog.Category();
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		List<Category>tops = (List<Category>)request.getSession().getServletContext().getAttribute("topCateGory");
		if(cmap !=null && cmap.size()>0 && tops !=null && tops.size()>0){
			List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
			for(Category c:tops){
				if(c.getCode().equalsIgnoreCase(Constants.CATEGORY_SERVICE_CODE)){
					nodes.add(this.getNode(cmap.get(c.getId())));
				}
			}
			root.setCategorys(nodes);
		}
	return root;
	}
	
	public List<com.salesmanager.web.admin.entity.catalog.Category> getTopList(HttpServletRequest request){
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
		List<Category>tops = (List<Category>)request.getSession().getServletContext().getAttribute("topCateGory");
		if(cmap !=null && cmap.size()>0 && tops !=null && tops.size()>0){
			List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
			for(Category c:tops){
				nodes.add(this.getNode(cmap.get(c.getId())));
			}
			return nodes;
		}
	return null;
	}
	private com.salesmanager.web.admin.entity.catalog.Category getNode(Category cat){
		com.salesmanager.web.admin.entity.catalog.Category node = new com.salesmanager.web.admin.entity.catalog.Category();
		node.setCategoryID(cat.getId());
		node.setCategoryName(cat.getDescinfo());
		node.setUrl(cat.getUrl());
		if(cat.getCategories()!=null && cat.getCategories().size()>0){
			node.setCategorys(this.getNodes(cat));
		}
		return node;
	}
	private List<com.salesmanager.web.admin.entity.catalog.Category> getNodes(Category cat){		
		List<com.salesmanager.web.admin.entity.catalog.Category> nodes = new ArrayList<com.salesmanager.web.admin.entity.catalog.Category>();
		if(cat.getCategories() !=null && cat.getCategories().size()>0){
			for(Category c :cat.getCategories()){				
				com.salesmanager.web.admin.entity.catalog.Category n = new com.salesmanager.web.admin.entity.catalog.Category();
				n.setCategoryID(c.getId());
				n.setCategoryName(c.getDescinfo());
				n.setUrl(c.getUrl());
				if(c.getCategories() != null && c.getCategories().size()>0){
					n.setCategorys(getNodes(c));
				}
				nodes.add(n);
			}
		}
		return nodes;
	}

}