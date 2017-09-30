package com.bettbio.core.model.search;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

public class IndexProduct implements JSONAware {
	
	private String pname;
	private String enname;
	private List<Long> categories;//Category name
	private Long manufacturer;//manufacturer name	
	private String lang;
	private String id;
	private String simpledesc;
	private String image;
	private Long price;//最低价格
	private String store;
	private Integer quality=0;
	private boolean isFree;
	private String[][] certificates;
	private String[][] proofs;
	private String[][] thirdproofs;
	private String[][] testreports;
	private String friendlyUrl;
	private String keyword;
	private String enKey;
	private String code;
	//授权书
	private String[][] auth;
	private Integer period=0;//货期
	
	private String storeCode;//商家唯一Code
	private String producCode;//产品唯一code
	private String brandName;//品牌名称
	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		JSONObject obj = new JSONObject();
		obj.put("pname", this.getPname());
		if(this.getEnname() != null)
		obj.put("penname", this.getEnname());		
		if(this.getManufacturer() != null)
		obj.put("manufacturer", this.getManufacturer());
		obj.put("lang", this.getLang());
		obj.put("id", this.getId());
		if(this.getSimpledesc() !=null)
		obj.put("simpledesc",this.getSimpledesc());
		obj.put("friendlyUrl",this.getFriendlyUrl());		
		if(this.getImage() != null)
		obj.put("image", this.getImage());
		obj.put("store", this.getStore());
		obj.put("isfree", this.isFree);
		obj.put("chkey", this.keyword);
		obj.put("code", this.code);
		if(this.enKey !=null){
			obj.put("enkey", this.enKey);
		}
		if(this.getQuality() != null)
			obj.put("quality", this.getQuality());
		
		if(categories!=null) {
			JSONArray catsArray = new JSONArray();
			for(Long cat : categories) {
				catsArray.add(cat);
			}
			obj.put("category", catsArray);
		}
		//add 第三方凭证等
		if(certificates != null){
			JSONArray categoriesArray = getJsonString(certificates);
			obj.put("certificates", categoriesArray);
		}
		
		if(proofs != null){
			JSONArray categoriesArray = getJsonString(proofs);
			obj.put("proofs", categoriesArray);
		}
		
		if(thirdproofs != null){
			JSONArray categoriesArray = getJsonString(thirdproofs);
			obj.put("thirdproofs", categoriesArray);
		}
		
		if(testreports !=null){
			JSONArray categoriesArray = getJsonString(testreports);
			obj.put("testreports", categoriesArray);
		}
		
		if (auth != null) {
			JSONArray categoriesArray = new JSONArray();
			for(int i=0; i<auth.length;i++){
				JSONObject obj1 = new JSONObject();
				obj1.put("auth_id",auth[i][0]);						
				obj1.put("auth_type",auth[i][1]);
				categoriesArray.add(obj1);
			}
			obj.put("auth", categoriesArray);
		}
		obj.put("price", price);
		obj.put("period", period);
		obj.put("storeCode", this.getStoreCode());
		obj.put("producCode", this.getProducCode());
		obj.put("brandName", this.getBrandName());
		return obj.toJSONString();

	}

	
	private JSONArray getJsonString (String[][] proofs){
		JSONArray categoriesArray = new JSONArray();
		for(int i=0; i<proofs.length;i++){
			JSONObject obj1 = new JSONObject();
			obj1.put("pname",proofs[i][0]);						
			obj1.put("description",proofs[i][1]);
			categoriesArray.add(obj1);
		}
		return categoriesArray;
	}
	
	
	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setManufacturer(Long manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Long getManufacturer() {
		return manufacturer;
	}

	public String getSimpledesc() {
		return simpledesc;
	}

	public void setSimpledesc(String simpledesc) {
		this.simpledesc = simpledesc;
	}


	public String getPname() {
		return pname;
	}


	public void setPname(String pname) {
		this.pname = pname;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getFriendlyUrl() {
		return friendlyUrl;
	}


	public void setFriendlyUrl(String friendlyUrl) {
		this.friendlyUrl = friendlyUrl;
	}


	public List<Long> getCategories() {
		return categories;
	}


	public void setCategories(List<Long> categories) {
		this.categories = categories;
	}


	public Long getPrice() {
		return price;
	}


	public void setPrice(Long price) {
		this.price = price;
	}


	public String getStore() {
		return store;
	}


	public void setStore(String store) {
		this.store = store;
	}


	public String getEnname() {
		return enname;
	}


	public void setEnname(String enname) {
		this.enname = enname;
	}


	public Integer getQuality() {
		return quality;
	}


	public void setQuality(Integer quality) {
		this.quality = quality;
	}


	public boolean isFree() {
		return isFree;
	}


	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	public String[][] getCertificates() {
		return certificates;
	}


	public void setCertificates(String[][] certificates) {
		this.certificates = certificates;
	}


	public String[][] getProofs() {
		return proofs;
	}
	
	public String[][] getThirdproofs() {
		return thirdproofs;
	}


	public void setThirdproofs(String[][] thirdproofs) {
		this.thirdproofs = thirdproofs;
	}


	public void setProofs(String[][] proofs) {
		this.proofs = proofs;
	}


	public String[][] getTestreports() {
		return testreports;
	}


	public void setTestreports(String[][] testreports) {
		this.testreports = testreports;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getEnKey() {
		return enKey;
	}


	public void setEnKey(String enKey) {
		this.enKey = enKey;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String[][] getAuth() {
		return auth;
	}


	public void setAuth(String[][] auth) {
		this.auth = auth;
	}
	
	
	public Integer getPeriod() {
		return period;
	}
	
	public void setPeriod(Integer period) {
		this.period = period;
	}


	public String getStoreCode() {
		return storeCode;
	}


	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}


	public String getProducCode() {
		return producCode;
	}


	public void setProducCode(String producCode) {
		this.producCode = producCode;
	}


	public String getBrandName() {
		return brandName;
	}


	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
}
