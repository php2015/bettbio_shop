package com.bettbio.shop.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

public class GlobalExt implements WebRenderExt{

	static long version = System.currentTimeMillis();
	public void modify(Template template, GroupTemplate arg1, HttpServletRequest arg2, HttpServletResponse response) {
		response.setBufferSize(1024*24);
		template.binding("version",version);
	}

}
