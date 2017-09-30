package com.bettbio.shop.web.controller.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bettbio.core.common.web.controller.BaseController;
import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.model.util.PinYin;
import com.bettbio.core.service.BasedataTypeService;
import com.bettbio.core.service.ProductBrandService;
import com.bettbio.core.service.SStoreService;
import com.bettbio.core.service.util.PinyinUtils;
@Controller
@RequestMapping("/admin/proof")
public class ProductProofController extends BaseController {
	
	@Autowired
	BasedataTypeService basedataTypeService;
	
	@Autowired
	ProductBrandService productBrandService;
	
	@Autowired
	SStoreService sStoreService;
	
	/**
	 * 获取权威买方
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("voucherList")
	public List<PinYin> voucherList(String code){
		String proofType="BTYPE_PROOF";
		switch (code.substring(0, 2)){
		case "01":
			proofType="BTYPE_PROOF";
			break;
		case "02":
			proofType="BTYPE_PROOF_CONSUMABLE";
			break;
		case "03":
			proofType="BTYPE_PROOF_INSTRUMENT";
			break;
		case "04":
			proofType="BTYPE_PROOF_SERVICE";
			break;	
		}
		List<PinYinName> proofBoList = basedataTypeService.selectBasedataByType(proofType);
		List<Object[]> list = new ArrayList<Object[]>();
		for(PinYinName pinYinName : proofBoList){
			Object[] obj=new Object[3];
            obj[0]=pinYinName.getId();
            obj[1]=pinYinName.getName();
            obj[2]=pinYinName.getPinyin();
            list.add(obj);
		}
		List<String> ch = new ArrayList<String>();
		ch.add("b");
		ch.add("g");
		ch.add("k");
		ch.add("n");
		ch.add("r");
		ch.add("s");
		ch.add("w");
		ch.add("z");
		List<PinYin> pinyin = PinyinUtils.getPinyinList(list,ch);
		return pinyin;
	}
	
	/**
	 * 文献引用集
	 */
	@ResponseBody
	@RequestMapping("literatureList")
	public List<PinYin> literatureList(String code){
		String proofType="BTYPE_CERTIFICATE";
		switch (code.substring(0, 2)){
		case "01":
			proofType="BTYPE_CERTIFICATE";
			break;
		case "02":
			proofType="BTYPE_CERTIFICATE_CONSUMABLE";
			break;
		case "03":
			proofType="BTYPE_CERTIFICATE_INSTRUMENT";
			break;
		case "04":
			proofType="BTYPE_CERTIFICATE_SERVICE";
			break;	
		}
		List<PinYinName> proofBoList = basedataTypeService.selectBasedataByType(proofType);
		List<Object[]> list = new ArrayList<Object[]>();
		for(PinYinName pinYinName : proofBoList){
			Object[] obj=new Object[3];
            obj[0]=pinYinName.getId();
            obj[1]=pinYinName.getName();
            obj[2]=pinYinName.getPinyin();
            list.add(obj);
		}
		List<String> ch = new ArrayList<String>();
		ch.add("b");
		ch.add("g");
		ch.add("k");
		ch.add("n");
		ch.add("r");
		ch.add("s");
		ch.add("w");
		ch.add("z");
		List<PinYin> pinyin = PinyinUtils.getPinyinList(list,ch);
		return pinyin;
	}
	
	/**
	 * 第三方认证
	 */
	@ResponseBody
	@RequestMapping("attestationList")
	public List<PinYin> attestationList(String code){
		String proofType="BTYPE_CERTIFICATE";
		switch (code.substring(0, 2)){
		case "01":
			proofType="BTYPE_THIRDPROOF";
			break;
		case "02":
			proofType="BTYPE_THIRDPROOF_CONSUMABLE";
			break;
		case "03":
			proofType="BTYPE_THIRDPROOF_INSTRUMENT";
			break;
		case "04":
			proofType="BTYPE_THIRDPROOF_SERVICE";
			break;	
		}
		List<PinYinName> proofBoList = basedataTypeService.selectBasedataByType(proofType);
		List<Object[]> list = new ArrayList<Object[]>();
		for(PinYinName pinYinName : proofBoList){
			Object[] obj=new Object[3];
			obj[0]=pinYinName.getId();
			obj[1]=pinYinName.getName();
			obj[2]=pinYinName.getPinyin();
			list.add(obj);
		}
		List<String> ch = new ArrayList<String>();
		ch.add("b");
		ch.add("g");
		ch.add("k");
		ch.add("n");
		ch.add("r");
		ch.add("s");
		ch.add("w");
		ch.add("z");
		List<PinYin> pinyin = PinyinUtils.getPinyinList(list,ch);
		return pinyin;
	}
	
	/**
	 * 产品品牌
	 */
	@ResponseBody
	@RequestMapping("brandList")
	public List<PinYin> brandList(){
		List<PinYinName> brandList = productBrandService.brandList();
		List<Object[]> list = new ArrayList<Object[]>();
		for(PinYinName pinYinName : brandList){
			Object[] obj=new Object[3];
            obj[0]=pinYinName.getId();
            obj[1]=pinYinName.getName();
            obj[2]=pinYinName.getPinyin();
            list.add(obj);
		}
		List<String> ch = new ArrayList<String>();
		ch.add("b");
		ch.add("g");
		ch.add("k");
		ch.add("n");
		ch.add("r");
		ch.add("s");
		ch.add("w");
		ch.add("z");
		List<PinYin> pinyin = PinyinUtils.getPinyinList(list,ch);
		return pinyin;
	}
	
	/**
	 * 商家列表
	 */
	@ResponseBody
	@RequestMapping("storeList")
	public List<PinYin> storeList(){
		List<PinYinName> storeList = sStoreService.storeList();
		List<Object[]> list = new ArrayList<Object[]>();
		for(PinYinName pinYinName : storeList){
			Object[] obj=new Object[3];
            obj[0]=pinYinName.getId();
            obj[1]=pinYinName.getName();
            obj[2]=pinYinName.getPinyin();
            list.add(obj);
		}
		List<String> ch = new ArrayList<String>();
		ch.add("b");
		ch.add("g");
		ch.add("k");
		ch.add("n");
		ch.add("r");
		ch.add("s");
		ch.add("w");
		ch.add("z");
		List<PinYin> pinyin = PinyinUtils.getPinyinList(list,ch);
		return pinyin;
	}
}
