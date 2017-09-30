package com.test.mode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.model.permission.bo.Resource;
import com.bettbio.core.model.permission.bo.Role;
import com.bettbio.core.model.permission.bo.StoreUser;
import com.bettbio.core.model.permission.bo.User;
import com.bettbio.core.service.permission.PStoreUserService;
import com.bettbio.core.service.permission.ResourceService;
import com.bettbio.core.service.permission.RoleService;
import com.test.mode.base.BaseT;

public class PermissionTest extends BaseT {

	@Autowired
	PStoreUserService pStoreUserService;

	@Autowired
	ResourceService resourceService;

	@Autowired
	RoleService roleService;

	Resource resource1;
	Resource resource2;
	Resource resource3;
	Resource resource4;
	Role role1;
	Role role2;
	User user1;
	User user2;

	@Test
	public void add() {
		/*========================R E S O U R C E=========================*/
		resource1 = new Resource("1", "STORE MGT", "/admin/store", 1, "0");
		resource2 = new Resource("2", "USER MGT", "/admin/user", 1, "0");
		resource3 = new Resource("3", "ADD STORE", "/admin/store/add", 1, "1");
		resource4 = new Resource("4", "DEL STORE", "/admin/store/del", 1, "1");
		
		resourceService.addResource(resource1);
		resourceService.addResource(resource2);
		resourceService.addResource(resource3);
		resourceService.addResource(resource4);
		
		/*========================R O L E=========================*/
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		list1.add(4);

		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(2);

		Role role1 = new Role("ADMIN MGT", "01", 1, list1);
		Role role2 = new Role("USER MGT", "02", 1, list2);

		roleService.addRole(role1);
		roleService.addRole(role2);

		/*========================U S E R=========================*/
		StoreUser storeUser1 = new StoreUser(1, "1");
		StoreUser storeUser2 = new StoreUser(2, "2");

		int i = pStoreUserService.updateUserRoles(storeUser1);
		Assert.assertTrue(i > 0);
		
		i = pStoreUserService.updateUserRoles(storeUser2);
		Assert.assertTrue(i > 0);
	}

	@Test
	public void findUserRoles() {
		StoreUser storeUser = pStoreUserService.findByAccount("admin");
		Assert.assertNotNull(storeUser);
		
		Set<String> adminRoles = pStoreUserService.findRoles(storeUser.getAccount());
		System.out.println(adminRoles.toString());
		
		Set<String> testRoles = pStoreUserService.findRoles("test");
		System.out.println(testRoles.toString());
		
	}

	@Test
	public void findPermissions(){
		StoreUser storeUser = pStoreUserService.findByAccount("admin");
		Assert.assertNotNull(storeUser);
		
		Set<String> adminPermissions = pStoreUserService.findResources(storeUser.getAccount());
		System.out.println(adminPermissions.toString());
		

		Set<String> testPermissions = pStoreUserService.findResources("test");
		System.out.println(testPermissions.toString());
		
	}
	
	@Test
	public void findUserMenu(){
		StoreUser storeUser = pStoreUserService.findByAccount("admin");
		Assert.assertNotNull(storeUser);
		
		Set<Resource> adminMenu = pStoreUserService.findUserMenu(storeUser.getAccount());
		for (Iterator iterator = adminMenu.iterator(); iterator.hasNext();) {
			Resource resource = (Resource) iterator.next();
			System.out.println(resource);
		}
		
		Set<Resource> testMenu = pStoreUserService.findUserMenu(storeUser.getAccount());
		for (Iterator iterator = testMenu.iterator(); iterator.hasNext();) {
			Resource resource = (Resource) iterator.next();
			System.out.println(resource);
		}
	}
	
	@Test
	public void matchPath(){
		String path = "/admin/user.do?name=123&pass=123";
		path = path.substring(0, path.indexOf("."));
		Assert.assertEquals(path, "/admin/user");
		
		Set<String> adminResources = pStoreUserService.findResources("admin");
		boolean flag = adminResources.contains(path);
		Assert.assertTrue(flag);
	}
}
