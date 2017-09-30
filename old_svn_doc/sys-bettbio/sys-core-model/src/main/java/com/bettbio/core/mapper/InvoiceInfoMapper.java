package com.bettbio.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.InvoiceInfo;

public interface InvoiceInfoMapper extends MapperSupport<InvoiceInfo> {
	/**
	 * 设置默认开票信息
	 * 
	 * @param userCode
	 * @param addressCode
	 */
	void settingDefaultInvoiceInfo(@Param("userCode")String userCode, @Param("addressCode")String addressCode);
	void settingNoDefaultInvoiceInfo(String userCode);

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
	 * 根据用户编号查找用户开票信息
	 * @param userCode
	 * @return
	 */
	List<InvoiceInfo> selectInvoiceInfoByCode(String userCode);
	/**
	 * 统计用户开票信息地址总数
	 * @param userCode
	 * @return
	 */
	Integer selectCountByCode(String userCode);
}