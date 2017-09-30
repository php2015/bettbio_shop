package com.bettbio.core.service.util;

import java.util.ArrayList;
import java.util.List;

import com.bettbio.core.model.bo.PinYinName;
import com.bettbio.core.model.util.PinYin;

public class PinyinUtils
{
	public static List<PinYin> getPinyinList(List<Object[]> objs,List<String> ch){
		List<PinYin> lists = new ArrayList<PinYin>();
		if(objs !=null && objs.size()>0){	
			
			PinYin pinYin = new PinYin();
			//pinYin.setCode("A");
			String lett ="A";
			int lastLett =(int) ch.get(0).charAt(0);
			for(int i=0;i<objs.size();i++){
				int letter = (int)objs.get(i)[2].toString().toLowerCase().charAt(0);
				if(letter>lastLett){
					
					if(lett.equalsIgnoreCase(String.valueOf((char)(letter)).toUpperCase())){
						pinYin.setCode(lett+"-"+String.valueOf((char)(letter)).toUpperCase());
					}else{
						pinYin.setCode(lett+"-"+String.valueOf((char)(letter-1)).toUpperCase());
					}
					
					if(pinYin.getLists()!=null && pinYin.getLists().size()>0)
					lists.add(pinYin);
					//lists = new ArrayList<PinYin>();
					/**
					if(ch.size()>0){
						ch.remove(0);
					}*/
					
					for(int k=0;k<ch.size();k++){
						lastLett =(int) ch.get(k).charAt(0);
						if(letter>lastLett) {
							ch.remove(0);
							 k--;
						}else{
							break;
						}
					}
					lett=objs.get(i)[2].toString().toUpperCase();
					pinYin = new PinYin();
					//pinYin.setCode(objs.get(i)[2].toString().toUpperCase());
					PinYinName pinYinName = new PinYinName();
					pinYinName.setId(Integer.parseInt(objs.get(i)[0].toString()));
					pinYinName.setName(objs.get(i)[1].toString());
					pinYinName.setPinyin(objs.get(i)[2].toString().toUpperCase());
					List<PinYinName> plist = new ArrayList<PinYinName>(); 
					plist.add(pinYinName);
					pinYin.setLists(plist);
					
				}else{
					PinYinName pinYinName = new PinYinName();
					pinYinName.setId(Integer.parseInt(objs.get(i)[0].toString()));
					pinYinName.setName(objs.get(i)[1].toString());
					pinYinName.setPinyin(objs.get(i)[2].toString().toUpperCase());
					List<PinYinName> plist = pinYin.getLists();
					if(plist == null) plist = new ArrayList<PinYinName>();
					plist.add(pinYinName);
					pinYin.setLists(plist);					
				}
				
			}
			
			//if(lists !=null && lists.size()>0){
				int letter = (int)objs.get(objs.size()-1)[2].toString().toLowerCase().charAt(0);
				//maps.put(lett+"-"+String.valueOf((char)letter).toUpperCase(), lists);
				pinYin.setCode(lett+"-"+String.valueOf((char)letter).toUpperCase());
				lists.add(pinYin);
			//}
		}
		
		return lists;
	}
	
}
