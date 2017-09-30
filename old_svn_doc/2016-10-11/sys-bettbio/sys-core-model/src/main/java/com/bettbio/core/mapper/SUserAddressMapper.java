package com.bettbio.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.SUserAddress;

public interface SUserAddressMapper extends MapperSupport<SUserAddress> {
	void settingDefaultAddress(@Param("userCode") String userCode, @Param("addressCode") String addressCode);
	
	void settingNoDefaultAddress(String userCode);
	
	void settingDefaultDistributionAddress(@Param("userCode") String userCode, @Param("addressCode") String addressCode);
	
	void settingNoDefaultDistributionAddress(String userCode);

	void deleteAddress(String addressCode);

	SUserAddress selectDefaultAddress(String userCode);
    
    List<SUserAddress> selectAddressByCode(String userCode);
    
    Integer selectCountByCode(String UserCode);
}