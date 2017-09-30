package com.bettbio.core.service;

import java.util.Map;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SUserAddress;

/**
 * 用户地址维护
 * 
 * @author chang
 *
 */
public interface UserAddressService extends IService<SUserAddress> {
	/**
	 * 设置默认地址
	 * 
	 * @param userCode
	 * @param addressCode
	 */
	void settingDefaultAddress(String userCode, String addressCode);

	/**
	 * 删除地址
	 * 
	 * @param addressCode
	 * @return
	 */
	void deleteAddress(String addressCode);

	/**
	 * 获取默认收货地址
	 * 
	 * @param userCode
	 * @return
	 */
	SUserAddress selectDefaultAddress(String userCode);

	/**
	 * 根据用户编号获取所有收货地址
	 * 
	 * @param userCode
	 * @return
	 */
	Map<String, Object> selectAddressByCode(String userCode);

	/**
	 * 设置默认接收发票地址
	 * 
	 * @param userCode
	 * @param addressCode
	 */
	void settingDefaultDistributionAddress(String userCode, String addressCode);
	
	/**
	 * 添加用户地址
	 * @param userAddress
	 */
	void saveUserAddress(SUserAddress userAddress);

}
