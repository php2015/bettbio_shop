package com.bettbio.core.service.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet;
import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet.LongEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.mapper.SProductClassificationMapper;
import com.bettbio.core.model.search.Category;
import com.bettbio.core.service.search.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	SProductClassificationMapper sProductClassificationMapper;

	@Override
	public Category selectCategoryByCode(String code) {
		return sProductClassificationMapper.selectCategoryByCode(code);
	}

	@Override
	public Category selectCategoryById(Integer id) {
		return sProductClassificationMapper.selectCategoryById(id);
	}

	@Override
	public List<Category> getSubCategory(Integer id) {
		return sProductClassificationMapper.getSubCategory(id);
	}

	@Override
	public List<Category> selectTopCategory() {
		return sProductClassificationMapper.selectTopCategory();
	}

	@Override
	public List<Category> selectAllSubCategary(Integer id) {
		return sProductClassificationMapper.selectAllSubCategary(id);
	}

	@Override
	public List<Category> getCategoryByIndex(Object categoryObject) {
		if (categoryObject instanceof InternalLongTermsFacet) {
			InternalLongTermsFacet ff = (InternalLongTermsFacet) categoryObject;
			
			List<Category> cates = new ArrayList<Category>();
			
			Map<Integer, Category> cateMap = new HashMap<Integer, Category>();
			if (ff.getEntries().size() > 0) {
				cateMap = selectAllCategoryToMap();
			}
			
			// 四个分类
			Category reagent = cateMap.get(1);
			Category resumeable = cateMap.get(2);
			Category instrument = cateMap.get(3);
			Category server = cateMap.get(4);

			for (LongEntry o : ff.getEntries()) {
				Integer id = Integer.parseInt(o.getTerm().string());
//				Category cat = this.selectCategoryById(Integer.parseInt(o.getTerm().string()))
				Category cat = cateMap.get(id);
				Category re = new Category();
				if (cat != null) {
					String cCode = cat.getCode().substring(0, 2);
					switch (cCode) {
					case Constants.CATEGORY_REAGENT_CODE:
						// 试剂
						reagent.setProductCount(reagent.getProductCount() + o.getCount());
						if (!cat.getCode().equalsIgnoreCase(Constants.CATEGORY_REAGENT_CODE)) {
							re.setProductCount(o.getCount());
							re.setName(cat.getName());
							re.setId(cat.getId());
							reagent.getSonCategory().add(re);
						}
						break;
					case Constants.CATEGORY_CONSUMERGOODS:
						// 耗材
						resumeable.setProductCount(resumeable.getProductCount() + o.getCount());
						if (!cat.getCode().equalsIgnoreCase(Constants.CATEGORY_REAGENT_CODE)) {
							re.setProductCount(o.getCount());
							re.setName(cat.getName());
							re.setId(cat.getId());
							resumeable.getSonCategory().add(re);
						}
						break;
					case Constants.CATEGORY_INSTRUMENT_CODE:
						// 仪器
						instrument.setProductCount(instrument.getProductCount() + o.getCount());
						if (!cat.getCode().equalsIgnoreCase(Constants.CATEGORY_REAGENT_CODE)) {
							re.setProductCount(o.getCount());
							re.setName(cat.getName());
							re.setId(cat.getId());
							instrument.getSonCategory().add(re);
						}
						break;
					case Constants.CATEGORY_SERVICE_CODE:
						// 服务
						server.setProductCount(server.getProductCount() + o.getCount());
						if (!cat.getCode().equalsIgnoreCase(Constants.CATEGORY_REAGENT_CODE)) {
							re.setProductCount(o.getCount());
							re.setName(cat.getName());
							re.setId(cat.getId());
							server.getSonCategory().add(re);
						}
						break;
					}
				}
			}
			cates.add(reagent);
			cates.add(resumeable);
			cates.add(instrument);
			cates.add(server);

			return cates;
		}
		return null;
	}

	@Override
	public List<Category> getCategoryByIndex(Object object, Category category) {
		Map<Integer, Category> reMap = this.getSubCategoryToMap(category.getId());
		if (reMap == null) return null;
		if (object instanceof InternalLongTermsFacet) {
			InternalLongTermsFacet ff = (InternalLongTermsFacet) object;
			
			Map<Integer, Category> cateMap = new HashMap<Integer, Category>();
			if (ff.getEntries().size() > 0) {
				cateMap = this.selectAllCategoryToMap();
			}

			List<Category> rCates = new ArrayList<Category>();
			
			for (LongEntry o : ff.getEntries()) {
				Integer id = Integer.parseInt(o.getTerm().string());
				Category re = reMap.get(id);
				Category cat = cateMap.get(id);
//				Category cat = this.selectCategoryById(Integer.parseInt(o.getTerm().string()));
				// 子集
				if (re != null && cat != null && o.getCount() > 0) {
					re.setProductCount(o.getCount());
					re.setName(cat.getName());
					rCates.add(re);
				}
				// 子集的子集
				
			}

			return rCates;
		}
		return null;
	}

	public Map<Integer, Category> getSubCategoryToMap(Integer id) {
		List<Category> subCate = getSubCategory(id);
		if (subCate == null || subCate.size() == 0) return null;
		
		HashMap<Integer, Category> reMap = new HashMap<Integer, Category>();

		for (Category c : subCate) {
			c.setProductCount(0);
			reMap.put(c.getId(), c);
		}
		return reMap;
	}

	@Override
	public Map<Integer, Category> selectAllCategoryToMap() {
		Map<Integer, Category> map = new HashMap<Integer, Category>();
		List<Category> categories = sProductClassificationMapper.selectAllCategory();
		for (Category category : categories) {
			category.setProductCount(0);
			category.setSonCategory(null);
			map.put(category.getId(), category);
		}
		return map;
	}
}
