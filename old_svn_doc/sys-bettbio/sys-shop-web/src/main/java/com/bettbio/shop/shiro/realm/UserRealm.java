package com.bettbio.shop.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.model.permission.bo.StoreUser;
import com.bettbio.core.service.permission.PStoreUserService;

/**
 * Shiro 只走商家用户
 * 
 * @author GuoChunbo
 * 
 *
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	PStoreUserService pStoreUserService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 不做任何操作
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
		String account = (String) token.getPrincipal();
		String password = "";
        if (token.getPassword() != null) {
            password = new String(token.getPassword());
        }
        
		StoreUser storeUser = pStoreUserService.findByAccount(account);

		if (storeUser == null) {
			throw new UnknownAccountException();// 没找到帐号
		}
		
		if(storeUser.getIsDeleted()==0){
			throw new DisabledAccountException();//已删除 禁用
		}
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				account, // 用户名
				password, // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}

}
