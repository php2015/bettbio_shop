package com.salesmanager.web.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.generic.service.SalesManagerEntityService;
import com.salesmanager.core.utils.ajax.AjaxResponse;

public class DeleteAll {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAll.class);
	private SalesManagerEntityService service;
	private String []sid;
	private HttpServletRequest request;		
	private AjaxResponse resp;
	private Criteria criteria ;
	

	public SalesManagerEntityService getService() {
		return service;
	}

	public String[] getSid() {
		return sid;
	}

	public void setSid(String[] sid) {
		this.sid = sid;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public AjaxResponse getResp() {
		return resp;
	}

	public void setResp(AjaxResponse resp) {
		this.resp = resp;
	}

	public void setService(SalesManagerEntityService service) {
		this.service = service;
	}
	
	
	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	private AjaxResponse setReturnDate(ArrayList<String> erros,int total){
		AjaxResponse resp  = new AjaxResponse();
		if(erros.size()==total){
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}else{
			HashMap<String, String> erronames = new HashMap<String, String>();
			//报错错误的ID
			if(erros.size()>0){
				StringBuffer errorNams = new StringBuffer();
				errorNams.append(erros.get(0));
				for(int k=1;k<erros.size();k++){
					errorNams.append(",").append(erros.get(k));
				}
				erronames.put("erronames", errorNams.toString());
				resp.setDataMap(erronames);
				resp.setStatus(1);
			}else{
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			}
		}
		return resp;
	}
	public AjaxResponse call() throws Exception {
		// TODO Auto-generated method stub
		if(sid !=null && sid.length>0){
			ArrayList<String> erros= new ArrayList<String>();
			for(int i =0 ;i<sid.length;i++){
				try {
					Long storeId = Long.parseLong(sid[i]);
					SalesManagerEntity entity = service.getById(storeId);
					service.delete(entity);	
				} catch (Exception e) {
					LOGGER.error("Error while deleting product", e);
					erros.add(sid[i]);
					continue;
				}
			}
			resp = this.setReturnDate(erros, sid.length);
		}else{
			
			criteria.setStartIndex(0);
			List<SalesManagerEntity> plist = service.getListByCriteria(criteria);
			int total = plist.size();
			if(plist != null ){
				ArrayList<String> erros= new ArrayList<String>();
				int index =0;
				for (SalesManagerEntity p:plist){
					try {
						service.delete(p);							
					} catch (Exception e) {
						LOGGER.error("Error while deleting product", e);
						erros.add(p.getId().toString());
						continue;
					}
					index++;
					NumberFormat numberFormat = NumberFormat.getInstance();  
					String result = numberFormat.format(index / total* 100);
					HttpSession session = request.getSession();
					session.setAttribute("percentnum", result);
				}
				resp = this.setReturnDate(erros, total);
				HttpSession session = request.getSession();
				session.removeAttribute("percentnum");
			}
		}
		return resp;
	}
}
