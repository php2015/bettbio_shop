package com.bettbio.core.service;

import java.util.Map;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.InvoiceInfo;

/**
 * 开票信息
 * 
 * @author chang
 *
 */
public interface InvoiceInfoService extends IService<InvoiceInfo> {
	/**
	 * 设置默认开票信息
	 * 
	 * @param userCode
	 * @param addressCode
	 */
	void settingDefaultInvoiceInfo(String userCode, String invoiceInfoCode);

	/**
	 * 删除开票信息
	 * 
	 * @param addressCode
	 */
	void deleteInvoiceInfo(String addressCode);

	/**
	 * 获取默认开票信息
	 * 
	 * @param userCode
	 * @return
	 */
	InvoiceInfo selectDefaultInvoiceInfo(String userCode);
	/**
	 * 获取用户开票信息
	 * @param userCode
	 * @return
	 */
	Map<String,Object> selectInvoiceInfoByCode(String userCode);
	
	/**
	 * 添加或更新开票信息
	 * @param invoiceInfo
	 */
	void saveOrUpdateInvoiceInfo(InvoiceInfo invoiceInfo);
	
	
}
