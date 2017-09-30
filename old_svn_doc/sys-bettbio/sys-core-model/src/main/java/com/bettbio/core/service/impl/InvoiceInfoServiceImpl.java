package com.bettbio.core.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.InvoiceInfoMapper;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.service.InvoiceInfoService;

@Service
public class InvoiceInfoServiceImpl extends BaseService<InvoiceInfo> implements InvoiceInfoService {
	
	InvoiceInfoMapper getInvoiceInfoMapper(){
		return (InvoiceInfoMapper)mapper;
	}
	
	@Override
	public void settingDefaultInvoiceInfo(String userCode, String invoiceInfoCode) {
		getInvoiceInfoMapper().settingNoDefaultInvoiceInfo(userCode);
		getInvoiceInfoMapper().settingDefaultInvoiceInfo(userCode, invoiceInfoCode);
	}

	@Override
	public void deleteInvoiceInfo(String addressCode) {
		getInvoiceInfoMapper().deleteInvoiceInfo(addressCode);
	}

	@Override
	public InvoiceInfo selectDefaultInvoiceInfo(String userCode) {
		return getInvoiceInfoMapper().selectDefaultInvoiceInfo(userCode);
	}

	@Override
	public Map<String, Object> selectInvoiceInfoByCode(String userCode) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("invoiceInfoList", getInvoiceInfoMapper().selectInvoiceInfoByCode(userCode));
		map.put("invoiceInfoCount",getInvoiceInfoMapper().selectCountByCode(userCode));
		return map;
	}

	@Override
	public void saveOrUpdateInvoiceInfo(InvoiceInfo invoiceInfo) {
		if(invoiceInfo.getId()!=null){
			getInvoiceInfoMapper().updateByPrimaryKeySelective(invoiceInfo);
			invoiceInfo=getInvoiceInfoMapper().selectByPrimaryKey(invoiceInfo.getId());
		}else{
			getInvoiceInfoMapper().insert(invoiceInfo);
	    }
		if(invoiceInfo.getIsDefault()==0){//判断默认收货地址
			settingDefaultInvoiceInfo(invoiceInfo.getUserCode(),invoiceInfo.getCode());
		}
		
	}
	
}
