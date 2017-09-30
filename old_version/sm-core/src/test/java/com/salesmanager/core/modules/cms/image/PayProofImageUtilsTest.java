package com.salesmanager.core.modules.cms.image;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class PayProofImageUtilsTest {

	@Test
	public void testGetImageUri() {
		String uri = PayProofImageUtils.getImageUri(2000, 2100, "jpeg");
		System.out.println(uri);
	}

	@Test
	public void testParseImageUri() {
		String uri = "images/payproof/O2000/so2100/payproof.jpeg";
		Map<String, Object> map = PayProofImageUtils.parseImageUri(uri);
		System.out.println(map);
	}

}
