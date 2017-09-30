package com.bettbio.core.service.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.model.search.IndexProduct;
import com.bettbio.core.model.search.SearchEntry;
import com.bettbio.core.model.search.page.SearchPage;
import com.bettbio.core.mongo.model.AuthorityCertification;
import com.bettbio.core.mongo.model.ExperimentReport;
import com.bettbio.core.mongo.model.Literature;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductPrice;
import com.bettbio.core.mongo.model.PurchaseVoucher;
import com.bettbio.core.service.search.SearchService;
import com.shopizer.search.services.SearchRequest;
import com.shopizer.search.services.SearchResponse;

/**
 * 搜索
 * 
 * @author GuoChunbo
 *
 */
@Service("productSearchService")
public class SearchServiceImpl implements SearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	private com.shopizer.search.services.SearchService searchService;

	private static final String COLLECTION_NAME = "product_zh_default";

	@Override
	public void initService() {
		searchService.initService();
	}

	public SearchServiceImpl() {
		System.out.println(
				"--------------------------------------------------------------------------------------------");
		System.out.println(
				"| -------------------------------E L A S T I C S E A R C H-------------------------------- |");
		System.out.println(
				"--------------------------------------------------------------------------------------------");
	}

	@Override
	public void addIndex(Product product) {

		try {
			String json = convertIndexProduct(product).toJSONString();
			searchService.index(json, COLLECTION_NAME, "product_zh");
			LOGGER.info("Add Add Index For Product Id : " + product.getId());
		} catch (Exception e) {
			LOGGER.error("Cannot Add Add Index  For Product Id [" + product.getId() + "], ", e);
		}
	}

	@Override
	public void deleteIndex(Integer productId) {
		try {
			searchService.deleteObject(COLLECTION_NAME, "product_zh", String.valueOf(productId));
		} catch (Exception e) {
			LOGGER.error("Cannot delete index for product id [" + productId + "], ", e);
		}
	}

	@Override
	public void deleteIndex(List<Integer> productIds) {
		for (Integer productId : productIds) {
			deleteIndex(productId);
		}
	}

	@Override
	public SearchPage<IndexProduct> search(String jsonString, final SearchPage<IndexProduct> searchPage) {

		SearchRequest request = new SearchRequest();
		request.setCollection(COLLECTION_NAME);
		request.setJson(jsonString);
		request.setSize(searchPage.getPageSize());
		request.setStart(searchPage.getStartRow());

		return search(request, searchPage);
	}

	@Override
	public SearchPage<IndexProduct> search(SearchRequest request, final SearchPage<IndexProduct> searchPage) {

		try {
			SearchResponse response = searchService.search(request);
			SearchHits hits = response.getSearchHits();

			List<IndexProduct> indexProducts = new ArrayList<IndexProduct>();
			for (SearchHit hit : hits) {
				SearchEntry entry = new SearchEntry();
				Map<String, Object> metaEntries = hit.getSource();
				Map<String, HighlightField> fields = hit.getHighlightFields();
				if (fields != null) {
					for (HighlightField field : fields.values()) {

						Text[] text = field.getFragments();
						metaEntries.put(field.getName(), text[0]);
					}

				}

				IndexProduct indexProduct = new IndexProduct();

				if (!StringUtils.isEmpty(metaEntries.get("id")))
					indexProduct.setId(metaEntries.get("id").toString());

				if (!StringUtils.isEmpty(metaEntries.get("store")))
					indexProduct.setStore(metaEntries.get("store").toString());

				if (!StringUtils.isEmpty(metaEntries.get("code")))
					indexProduct.setCode(metaEntries.get("code").toString());

				if (!StringUtils.isEmpty(metaEntries.get("pname")))
					indexProduct.setPname(metaEntries.get("pname").toString());

				if (!StringUtils.isEmpty(metaEntries.get("penname")))
					indexProduct.setEnname(metaEntries.get("penname").toString());

				if (!StringUtils.isEmpty(metaEntries.get("image")))
					indexProduct.setImage(metaEntries.get("image").toString());

				if (!StringUtils.isEmpty(metaEntries.get("quality")))
					indexProduct.setQuality((Integer) metaEntries.get("quality"));

				if (!StringUtils.isEmpty(metaEntries.get("price")))
					indexProduct.setPrice(Long.parseLong(metaEntries.get("price").toString()));

				if (!StringUtils.isEmpty(metaEntries.get("storeCode")))
					indexProduct.setStoreCode(metaEntries.get("storeCode").toString());

				if (!StringUtils.isEmpty(metaEntries.get("producCode")))
					indexProduct.setProducCode(metaEntries.get("producCode").toString());

				if (!StringUtils.isEmpty(metaEntries.get("brandName")))
					indexProduct.setBrandName(metaEntries.get("brandName").toString());

				indexProducts.add(indexProduct);
				entry.setSocre(hit.getScore());
				entry.setProduct(metaEntries);
			}
			searchPage.setList(indexProducts);
			searchPage.setTotal(response.getCount());

			Facets facets = response.getFacets();
			if (facets != null) {
				Map<String, Object> fMap = new HashMap<String, Object>();
				for (Facet facet : facets) {
					fMap.put(facet.getName(), facet);
				}
				searchPage.setIndexFacets(fMap);
			}

			return searchPage;
		} catch (Exception e) {
			LOGGER.error("Error while searching keywords " + request.getJson(), e);
			e.printStackTrace();
		}
		return searchPage;
	}

	private IndexProduct convertIndexProduct(Product product) {
		IndexProduct index = new IndexProduct();
		index.setId(String.valueOf(product.getId()));
		index.setPname(product.getProductNameCh());
		index.setEnname(product.getProductNameEn());
		index.setProducCode(product.getCode());

		index.setFriendlyUrl(product.getFriendlyUrl());
		index.setSimpledesc(product.getSimpleDescription());

		index.setKeyword(product.getProductNameCh());
		index.setCode(product.getProductNo());// 货号
		index.setEnKey(product.getProductNameEn());

		// add文献集
		List<Literature> literatures = product.getLiteratures();
		if (literatures != null && literatures.size() > 0) {

			String[][] certis = new String[literatures.size()][2];
			for (Literature literature : literatures) {
				int i = 0;
				if (null != literature.getBasedataType().getName()) {
					certis[i][0] = literature.getBasedataType().getName();
				}
				if (null != literature.getPeriodicalCode()) {
					certis[i][1] = '"' + literature.getPeriodicalCode() + '"';
					if (null != literature.getBasedataType()) {
						certis[i][1] = '"' + literature.getCreateDate().toString();
					}
					i++;
				}
				index.setCertificates(certis);
			}
		}

		// 购买凭证
		List<PurchaseVoucher> purchaseVouchers = product.getPurchaseVouchers();
		if (purchaseVouchers != null && purchaseVouchers.size() > 0) {
			String[][] ps = new String[purchaseVouchers.size()][2];
			int i = 0;
			for (PurchaseVoucher proofs : purchaseVouchers) {
				if (null != proofs.getBasedataType().getName()) {
					ps[i][0] = proofs.getBasedataType().getName();
				}
				if (null != proofs.getCreateDate()) {
					ps[i][1] = proofs.getCreateDate().toString();
				}
				i++;
			}
			index.setProofs(ps);
		}

		// add第三方认证
		List<AuthorityCertification> authorityCertifications = product.getAuthorityCertifications();
		if (authorityCertifications != null && authorityCertifications.size() > 0) {
			String[][] ts = new String[authorityCertifications.size()][2];
			int i = 0;
			for (AuthorityCertification third : authorityCertifications) {

				if (null != third.getBasedataType().getName()) {
					ts[i][0] = third.getBasedataType().getName();
				}
				if (null != third.getContent()) {
					ts[i][1] = third.getContent();
				}
				i++;
			}
			index.setThirdproofs(ts);
		}

		// 实验报告
		List<ExperimentReport> experimentReports = product.getExperimentReports();
		if (experimentReports != null && experimentReports.size() > 0) {
			String[][] ts = new String[experimentReports.size()][2];
			int i = 0;
			for (ExperimentReport third : experimentReports) {

				if (null != third.getRemarks()) {
					ts[i][0] = third.getRemarks();
				}
				if (null != third.getContent()) {
					ts[i][1] = third.getContent();
				}
				i++;
			}
			index.setTestreports(ts);
		}

		// 图片
		index.setImage(product.getDefaultImgUrl());

		// 品牌
		if (product.getProductBrand() != null){
			index.setManufacturer(product.getProductBrand().getId().longValue());
			index.setBrandName(product.getProductBrand().getName());
		}

		// 价格 - 现货
		Double prc = 0.0;// 最低价格
		int period = 0;// 现货
		List<ProductPrice> productPrices = product.getProductPrices();
		for (ProductPrice price : productPrices) {
			Double pfc = price.getFinalPrice();
			prc = pfc;
			if (prc.compareTo(pfc) == 1)
				prc = pfc;
			if ("现货".equals(price.getSupplyCycle())) {
				period = 1;
			}
		}
		index.setPrice(prc.longValue());
		index.setPeriod(period);

		// 质量分
		Integer quality = 0;
		if (product.getQualityScore() != null)
			quality = product.getQualityScore();
		index.setQuality(quality);

		// 分类
		List<Long> categoryList = new ArrayList<Long>();
		if (product.getProductClass() != null)
			categoryList.add(product.getProductClass().getId().longValue());
		index.setCategories(categoryList);

		// 商家名 - Code
		index.setStore(product.getStore().getName());
		index.setStoreCode(product.getStore().getCode());

		return index;
	}
}
