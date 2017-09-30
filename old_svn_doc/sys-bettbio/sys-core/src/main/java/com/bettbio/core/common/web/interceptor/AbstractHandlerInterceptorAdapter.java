package com.bettbio.core.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bettbio.core.common.constant.Constants;

public class AbstractHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

	private final PathMatcher pathMatcher = new AntPathMatcher();

	private String[] includeUrlPatterns = null; // 拦截
	private String[] excludeUrlPatterns = null; // 排除

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (isInclude(request)) {
			// logic
			if (isExclude(request)) {
				// logic
			}
			// logic
		}
		return super.preHandle(request, response, handler);
	}

	protected boolean isExclude(final HttpServletRequest request) {
		if (excludeUrlPatterns == null) {
			return false;
		}
		for (String pattern : excludeUrlPatterns) {
			if (pathMatcher.match(pattern, request.getServletPath())) {

				return true;
			}
		}
		return false;
	}

	protected boolean isInclude(final HttpServletRequest request) {
		if (includeUrlPatterns == null) {
			return false;
		}
		for (String pattern : includeUrlPatterns) {
			if (pathMatcher.match(pattern, request.getServletPath())) {

				return true;
			}
		}
		return false;
	}

	protected boolean isLogin(final HttpServletRequest request) {
		return request.getSession().getAttribute(Constants.CURRENT_USER) != null;
	}

	public String[] getIncludeUrlPatterns() {
		return includeUrlPatterns;
	}

	public void setIncludeUrlPatterns(String[] includeUrlPatterns) {
		this.includeUrlPatterns = includeUrlPatterns;
	}

	public String[] getExcludeUrlPatterns() {
		return excludeUrlPatterns;
	}

	public void setExcludeUrlPatterns(String[] excludeUrlPatterns) {
		this.excludeUrlPatterns = excludeUrlPatterns;
	}

	public static void main(String[] args) {
		PathMatcher pathMatcher = new AntPathMatcher();
		boolean b = pathMatcher.match("/app/**", "/**Login*");
		System.out.println(b);

		b = pathMatcher.match("/app/*Login*", "/app/Login");
		System.out.println(b);
	}
}
