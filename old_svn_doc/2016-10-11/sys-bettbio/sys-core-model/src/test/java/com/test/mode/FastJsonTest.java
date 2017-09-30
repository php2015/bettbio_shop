package com.test.mode;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.bettbio.core.common.web.validate.AjaxResponse;

public class FastJsonTest {

	@Test
	public void test(){
		
	}
	
	public static void main(String[] args) {
		System.out.println(JSON.toJSONString(AjaxResponse.fail("操作错误")));
	}
}
