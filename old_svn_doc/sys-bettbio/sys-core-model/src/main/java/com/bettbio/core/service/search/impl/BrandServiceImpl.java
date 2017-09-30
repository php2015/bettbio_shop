package com.bettbio.core.service.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet;
import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet.LongEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.mapper.SProductBrandMapper;
import com.bettbio.core.model.search.Brand;
import com.bettbio.core.service.search.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	SProductBrandMapper sProductBrandMapper;

	@Override
	public List<Brand> getBrandByIndex(Object object) {
		if (object instanceof InternalLongTermsFacet) {
			InternalLongTermsFacet ff = (InternalLongTermsFacet) object;
			List<Brand> brs = new ArrayList<Brand>();
			Map<Integer, Brand> brandMap = new HashMap<Integer, Brand>();
			if(ff.getEntries().size() > 0){
				brandMap = this.selectAllBrandToMap();
			}
			for (LongEntry o : ff.getEntries()) {
				Brand bt = new Brand();
				Integer id = Integer.parseInt(o.getTerm().string());
//				Brand ba = this.selectBrandById(id);
				Brand ba = brandMap.get(id);
				if (ba != null) {
					bt.setId(ba.getId());
					bt.setName(ba.getName());
					bt.setProductCount(o.getCount());
					brs.add(bt);
				}
			}
			return brs;
		}
		return null;
	}

	@Override
	public Brand selectBrandById(Integer id) {
		return sProductBrandMapper.selectBrandById(id);
	}

	@Override
	public Map<Integer, Brand> selectAllBrandToMap() {
		Map<Integer, Brand> map = new HashMap<>();
		List<Brand> brands = sProductBrandMapper.selectAllBrand();
		for (Brand brand : brands) {
			map.put(brand.getId(), brand);
		}
		return map;
	}
}
