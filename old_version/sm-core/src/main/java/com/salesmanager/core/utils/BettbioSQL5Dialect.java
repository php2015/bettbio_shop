package com.salesmanager.core.utils;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StringType;

public class BettbioSQL5Dialect extends MySQL5Dialect {
	public BettbioSQL5Dialect(){
		 super(); 
		 registerFunction("convert", new SQLFunctionTemplate(StringType.INSTANCE , "convert(?1 using ?2)") );
		 registerFunction("CONV", new SQLFunctionTemplate(StringType.INSTANCE , "CONV(?1,?2,?3)") );
	}
		
}
