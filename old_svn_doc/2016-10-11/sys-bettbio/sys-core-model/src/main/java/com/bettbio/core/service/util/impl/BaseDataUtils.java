package com.bettbio.core.service.util.impl;

import java.util.HashMap;
import java.util.Map;

import com.bettbio.core.model.BasedataType;

public class BaseDataUtils {

	public static Map<String, Object> baseDataMap = new HashMap<String, Object>();

	static {
		baseDataMap.put("BTYPE_CERTIFICATE", 60);
		baseDataMap.put("BTYPE_CERTIFICATE_INDEX", 0.8);
		baseDataMap.put("BTYPE_CERTIFICATE_ADD", 0.1);
		baseDataMap.put("BTYPE_THIRDPROOF", 40);
		baseDataMap.put("BTYPE_THIRDPROOF_INDEX", 1);
		baseDataMap.put("BTYPE_THIRDPROOF_ADD", 0.1);
		baseDataMap.put("BTYPE_SELFPROOF", 10);
		baseDataMap.put("BTYPE_SELFPROOF_INDEX", 1);
		baseDataMap.put("BTYPE_SELFPROOF_ADD", 0.1);
		baseDataMap.put("BTYPE_PROOF", 30);
		baseDataMap.put("BTYPE_PROOF_INDEX", 0.8);
		baseDataMap.put("BTYPE_PROOF_ADD", 0.1);
		baseDataMap.put("AUDIT", 40);
		baseDataMap.put("BTYPE_SELFPROOF_NOFREE", 15);
		baseDataMap.put("BTYPE_CERTIFICATE_SERVICE", 0);
		baseDataMap.put("BTYPE_THIRDPROOF_SERVICE", 0);
		baseDataMap.put("BTYPE_SELFPROOF_SERVICE", 0);
		baseDataMap.put("BTYPE_PROOF_SERVICE", 0);
		baseDataMap.put("BTYPE_CERTIFICATE_INSTRUMENT", 0);
		baseDataMap.put("BTYPE_THIRDPROOF_INSTRUMENT", 50);
		baseDataMap.put("BTYPE_THIRDPROOF_INSTRUMENT_INDEX", 1);
		baseDataMap.put("BTYPE_THIRDPROOF_INSTRUMENT_ADD", 0.1);
		baseDataMap.put("BTYPE_SELFPROOF_INSTRUMENT", 0);
		baseDataMap.put("BTYPE_PROOF_INSTRUMENT", 70);
		baseDataMap.put("BTYPE_PROOF_INSTRUMENT_INDEX", 0.8);
		baseDataMap.put("BTYPE_PROOF_INSTRUMENT_ADD", 0.1);
		baseDataMap.put("BTYPE_CERTIFICATE_CONSUMABLE", 0);
		baseDataMap.put("BTYPE_THIRDPROOF_CONSUMABLE", 50);
		baseDataMap.put("BTYPE_THIRDPROOF_CONSUMABLE_INDEX", 1);
		baseDataMap.put("BTYPE_THIRDPROOF_CONSUMABLE_ADD", 0.1);
		baseDataMap.put("BTYPE_SELFPROOF_CONSUMABLE", 0);
		baseDataMap.put("BTYPE_PROOF_CONSUMABLE", 70);
		baseDataMap.put("BTYPE_PROOF_CONSUMABLE_INDEX", 0.8);
		baseDataMap.put("BTYPE_PROOF_CONSUMABLE_ADD", 0.1);
	}

	/**
	 * 
	 * @param point
	 * @param b
	 * @return
	 */
	public static int getProductQuality(int point, BasedataType b) {

		float score = point;

		float weight = Float.parseFloat(baseDataMap.get(b.getType()).toString());
		// add index
		float index = new Float(0.1);
		try {
			index = Float.parseFloat(baseDataMap.get(b.getType() + "_ADD").toString());
		} catch (Exception e) {

		}
		score += weight * index;
		// score += (weight/2)*Float.parseFloat(b.getValue());
		// if(score>weight) score = weight;
		return (int) (score);
	}
	
	/**
	 * 增加第一基础分
	 * @param point
	 * @param b
	 * @return
	 */
	public static int addFirstProductQuality(int point, BasedataType b) {

		float score = point;
		float weight = Float.parseFloat(baseDataMap.get(b.getType()).toString());
		score += weight * Float.parseFloat(b.getValue());
		// add index
		float index = new Float(0.1);
		try {
			index = Float.parseFloat(baseDataMap.get(b.getType() + "_ADD").toString());
		} catch (Exception e) {

		}
		// 减去第一篇的0.1
		score -= weight * index;
		if (score > weight) score = weight;
		
		return (int) (score);
		// return (int) Float.parseFloat(PropertiesUtils.getPropertiesValue(3,b.getType()));
	}
}
