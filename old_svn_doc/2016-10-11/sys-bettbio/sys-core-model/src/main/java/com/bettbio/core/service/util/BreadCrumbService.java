package com.bettbio.core.service.util;

import com.bettbio.core.model.search.Category;
import com.bettbio.core.model.search.vo.SearchVo;
import com.bettbio.core.model.util.BreadCrumb;
import com.bettbio.core.mongo.model.Product;

public interface BreadCrumbService {

	BreadCrumb buildCategoryBreadCrumb(Category category);

	BreadCrumb buildSearchBreadCrumb(Category category, SearchVo searchVo);

	BreadCrumb buildProductBreadCrumb(Product product);
}
