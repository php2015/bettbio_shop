package com.bettbio.core.mapper;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.mapper.MapperSupport;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SUser;

public interface SUserMapper extends MapperSupport<SUser> {
	
	public SUser login(SUser sUser);
	
	public SUser selectByPhone(SUser sUser);
	
	public String verifyPhone(String phone);
	
	public String verifyEmail(String email);
	
	public void updateUserPoints(SUser sUser);
	
	public int  updatePwd(Map<String, Object> map);//更新密码 - 重置密码
	
	List<String> selectUserEmail(Map<String, Object> map); //查询用户邮箱key->ids=>new Integer()[]{};
	
	public int updateGrade(SUser user);//更新用户等级
	
	public int updateActivate(Map<String, Object> map);//激活-冻结用户

	SUser fastLogin(SUser sUser);//手机快速登录
	
	int updatePhoneCode(SUser sUser); //更新手机验证码
	
	public SUser selectByCode(String userCode);//根据Code获取用户
	
}