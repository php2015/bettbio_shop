package com.bettbio.shop.web.controller.path;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通用走一级目录，如关于我们，联系我们等
 * @author GuoChunbo
 *
 */
@Controller
public class PathController {

	@RequestMapping("/{p}")
	public String ex(@PathVariable String p){
		
		return p;
	}
}
