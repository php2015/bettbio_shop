package com.salesmanager.core.utils;

import java.util.HashSet;
import java.util.Set;

public class ExceptionUtils {
	private ExceptionUtils() {}
	private static Set<String> validPackagePrefix = new HashSet<String>();
	static {
		validPackagePrefix.add("com.salesmanager.");
		validPackagePrefix.add("com.shopizer.");
		validPackagePrefix.add("com.mysql.");
	}
	public static String dumpSalesmanagerExceptionStack(Throwable e){
		StringBuilder sb = new StringBuilder();
		Throwable curException = e;
		sb.append("has exception:");
		while(curException != null){
			sb.append("\nCaused by: ").append(curException.toString());
			StackTraceElement[] sts = curException.getStackTrace();
			for(StackTraceElement st : sts){
				String className = st.getClassName();
				if (className.indexOf("$$") > 0){
					continue;
				}
				for(String prefix: validPackagePrefix){
					if (className.startsWith(prefix)){
						sb.append("\n    ").append(className).append('.').append(st.getMethodName()).append("():").append(st.getLineNumber());
						break;
					}
				}
			}
			curException = curException.getCause();
		}
		return sb.toString();
	}
}
