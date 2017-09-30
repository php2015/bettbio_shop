package com.bettbio.core.common.utils;

import java.lang.reflect.Field;

import com.bettbio.core.common.page.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 将对象转换成DBObjcet类型
 * 
 * @author chang
 *
 */
public class ConvertHelper {

	/**
	 * 将对象非空字段构造DBObject对象
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static DBObject convertNotNull(Object obj) {
		DBObject dbObject = new BasicDBObject();
		try {
			Class objClass = obj.getClass();
			Field[] fs = objClass.getDeclaredFields();
			dbObject = createDBObject(dbObject, fs, obj);
			if (objClass.getSuperclass() != null) {
				fs = objClass.getSuperclass().getDeclaredFields();
				dbObject = createDBObject(dbObject, fs, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbObject;
	}

	static DBObject createDBObject(DBObject dbObject, Field[] fs, Object obj) {
		try {
			for (Field field : fs) {
				field.setAccessible(true);
				Object objectValue = field.get(obj);
				if (java.util.List.class == field.getType()) {
					String[] objs = objectValue.toString().split("");
					if (objs.length == 2) {
						objectValue = null;
					}
				}
				if (objectValue != null) {
					dbObject.put(field.getName(), objectValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbObject;
	}
	
	public static String[] getObjectFieldsToArray(Class c){
		String fields [] = null;
		try {
			Class objClass = Class.forName(c.getName());
			Field[] fs = objClass.getDeclaredFields();
			if(fs!=null&&fs.length>0){
				fields = new String[fs.length];
				for (int i = 0; i < fs.length; i++) {
					Field f= fs[i];
					fields[i] = f.getName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fields;
	}
	
	public static void main(String[] args) {
		String fields [] = getObjectFieldsToArray(Page.class);
		for (String string : fields) {
			System.out.println(string);
		}
	}
}
