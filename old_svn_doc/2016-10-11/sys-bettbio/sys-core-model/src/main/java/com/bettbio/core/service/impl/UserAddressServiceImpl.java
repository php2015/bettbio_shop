package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.mapper.SUserAddressMapper;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.service.UserAddressService;
@Service()
public class UserAddressServiceImpl extends BaseService<SUserAddress> implements UserAddressService {

	SUserAddressMapper getSUserAddressMapper() {
		return (SUserAddressMapper) mapper;
	}

	@Override
	public void settingDefaultAddress(String userCode, String addressCode) {
		getSUserAddressMapper().settingNoDefaultAddress(userCode);
		getSUserAddressMapper().settingDefaultAddress(userCode, addressCode);
	}

	@Override
	public void deleteAddress(String addressCode) {
		getSUserAddressMapper().deleteAddress(addressCode);
	}

	@Override
	public SUserAddress selectDefaultAddress(String userCode) {
		return getSUserAddressMapper().selectDefaultAddress(userCode);
	}

	@Override
	public Map<String, Object> selectAddressByCode(String userCode) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userAddressList", getSUserAddressMapper().selectAddressByCode(userCode));
		map.put("userAddressCount",  getSUserAddressMapper().selectCountByCode(userCode));
		return map;
	}

	@Override
	public void settingDefaultDistributionAddress(String userCode, String addressCode) {
		getSUserAddressMapper().settingNoDefaultDistributionAddress(userCode);
		getSUserAddressMapper().settingDefaultDistributionAddress(userCode, addressCode);
		
	}

	@Override
	public void saveUserAddress(SUserAddress userAddress) {
	    if(userAddress.getId()!=null){
	    	getSUserAddressMapper().updateByPrimaryKeySelective(userAddress);
	    	userAddress=getSUserAddressMapper().selectByPrimaryKey(userAddress.getId());
	    }else{
	    	getSUserAddressMapper().insert(userAddress);
	    }
		if(userAddress.getIsDefault()==0){//判断默认收货地址
			settingDefaultAddress(userAddress.getUserCode(),userAddress.getCode());
		}
		if(userAddress.getIsDistributionAddress()==0){//判断默认接受货物地址
			settingDefaultDistributionAddress(userAddress.getUserCode(),userAddress.getCode());
		}
	}

}
