package com.salesmanager.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;



public class EzybioExceptionHandler implements HandlerExceptionResolver{
	private static final Logger LOGGER = LoggerFactory.getLogger(EzybioExceptionHandler.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3) {
		// TODO Auto-generated method stub
		LOGGER.error("Catch Exception: ..... " + arg3);
		arg3.printStackTrace();
		String path=arg0.getServletPath();
		
		ModelAndView model = new ModelAndView("error/access_denied");
		//服务器错误
		if(path.indexOf("/admin")!=-1){
			model.addObject("fromadmin", true);
		}else{
			model.addObject("fromadmin", false);
		}
		return model;
	}

}
