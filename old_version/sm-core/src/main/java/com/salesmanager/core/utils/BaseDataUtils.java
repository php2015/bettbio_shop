package com.salesmanager.core.utils;



import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.common.model.BasedataType;


public class BaseDataUtils {
	//private doulble qualityIndex=0.1;
	
	/**
	 * 
	 */
	public static int getProductQuality(int point,BasedataType b){
		
		float score =point;
		
		float weight =Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()));
		//add index
		float index = new Float(0.1);
		try{
			index = Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()+"_ADD"));
		}catch (Exception e){
			
		}
				
		score += weight*index;
		//score += (weight/2)*Float.parseFloat(b.getValue());		
		//if(score>weight) score = weight;
		return (int)(score);
	}
	
	/**增加第一基础分*/
	public static int addFirstProductQuality(int point,BasedataType b){
		
		float score =point;		
		float weight =Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()));
		score += weight*Float.parseFloat(b.getValue());	
		//add index
		float index = new Float(0.1);
		try{
			index = Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()+"_ADD"));
		}catch (Exception e){
			
		}
		//减去第一篇的0.1
		score -= weight*index;
		if(score>weight) score = weight;
		return (int)(score);
		/*return (int) Float.parseFloat(PropertiesUtils.getPropertiesValue(3,b.getType()));*/
	}
   
   /**
    *积分
    * */
   public static int addCoustomerByOrader(String orderType){
		
		/*float score =point;		
		float weight =Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()));
		score += weight/2;	
		if(score>weight) score = weight;
		return (int)(score);*/
		return (int) Float.parseFloat(PropertiesUtils.getPropertiesValue(orderType));
	}
	/**移除所有用户的积分*/
	public static int removeProductQuality(Product p ,BasedataType b,boolean last){
		float score =0;
		if(p.getQualityScore()!=null)
			score = p.getQualityScore();
		float weight =Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()));
		float index = new Float(0.1);
		try{
			index = Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()+"_ADD"));
		}catch (Exception e){
			
		}
		if(last == true) {
			score -= weight;
		}else{
			score -= (weight)*index;	
		}
		
		if((score)<0){
			score=0;
		}
		return (int)(score);
	}

}
