package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.SProductBrandMapper;
import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.service.ProductBrandService;

@Service
public class ProductBrandServiceImpl extends BaseService<SProductBrand> implements ProductBrandService {

	SProductBrandMapper getSProductBrandMapper(){
		return ((SProductBrandMapper)mapper);
	}
	
	@Override
	public List<SProductBrand> getSProductBrandByCode(String code) throws Exception{
		return getSProductBrandMapper().getSProductBrandByCode(code);
	}

	@Override
	public Page<SProductBrand> selectByPage(Map<String, Object> map){
		Page<SProductBrand> page = new Page<SProductBrand>(
				map,
				getSProductBrandMapper().getBrandAllCount(map));
		
		page.setList(getSProductBrandMapper().selectByPage(page.getMap()));
		return page;
	}
	
	@Override
	public int deleteBrandList(String ids) throws Exception{
		String[] brandIds = ids.split(",");
		int[] brandId =new int[brandIds.length];
		for(int i=0;i<brandIds.length;i++){
			brandId[i] = Integer.parseInt(brandIds[i]);
		}
		return getSProductBrandMapper().deleteBrandList(brandId);
	}

	@Override
	public List<SProductBrand> selectPagebyNameOrCode(String nameOrCode, int page, int rows) throws Exception{
		page = (page-1)*rows;
		Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("nameOrCode", nameOrCode);
        params.put("page", page);
        params.put("rows", rows);
		return getSProductBrandMapper().selectPagebyNameOrCode(params);
	}

	@Override
	public int deleteAll() throws Exception{
		return getSProductBrandMapper().deleteAll();
	}

	@Override
	public List<PinYinName> brandList() {
		return getSProductBrandMapper().brandList();
	}
	
	@Override
	public SProductBrand selectByBrandName(String brandName) {
		return getSProductBrandMapper().selectByBrandName(brandName);
	}
}
