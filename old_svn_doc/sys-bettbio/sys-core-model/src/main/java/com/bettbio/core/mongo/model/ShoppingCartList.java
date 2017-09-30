package com.bettbio.core.mongo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bettbio.core.model.vo.ShoppingCartVo;

/**
 * 购物车集合
 * @author chang
 *
 */
public class ShoppingCartList extends ArrayList<ShoppingCart> {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(ShoppingCart e) {
		boolean isTrue=false;
		if(super.size()>0){
			for (int i = 0; i < super.size(); i++) {
				if(super.get(i).productEquals(e)){
					isTrue=true;
					super.get(i).setNumber(super.get(i).getNumber()+e.getNumber());
				}
			}
			if(!isTrue){
				return super.add(e);
			}
		}else{
			return super.add(e);
		}
		return isTrue;
	}
	
	/**
	 * 产品数量变更
	 * @return
	 */
	public boolean changeNumber(ShoppingCart e){
		if(super.size()>0){
			for (int i = 0; i < super.size(); i++) {
				if(super.get(i).productEquals(e)){
					super.get(i).setNumber(e.getNumber());
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据ID删除购物车产品
	 * @param productCode
	 * @return
	 */
	public Boolean remove(ShoppingCart e) {
		if(super.size()>0){
			for (int i = 0; i < super.size(); i++) {
				if(super.get(i).productEquals(e)){
					super.remove(super.get(i));
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 批量删除
	 * @param productCode
	 * @return
	 */
	public Boolean removes(ShoppingCartVo shoppingCartVo) {
		if(super.size()>0&&shoppingCartVo!=null&&shoppingCartVo.getCarts().size()>0){
                for (ShoppingCart e : shoppingCartVo.getCarts()) {
                	remove(e);
				}
                return true;
			}
		return false;
	}
	
	/**
	 * 根据产品编号重新生成集合
	 * @param productCode
	 * @return
	 */
	public ShoppingCartList getListByProductCodes(ShoppingCartVo shoppingCartVo){
		ShoppingCartList shoppingCartList=new ShoppingCartList();
		if(shoppingCartVo!=null){
			List<ShoppingCart> carts=shoppingCartVo.getCarts();
			for (ShoppingCart shoppingCart : carts) {
				for (int i = 0; i < super.size(); i++) {
					if(super.get(i).productEquals(shoppingCart)){
						shoppingCartList.add(super.get(i));
					}
				}
			}
			
		}
		return shoppingCartList;
	}
	
	/**
	 * 购物车集合转Map
	 * @return
	 */
	public Map<String,List<ShoppingCart>> thisToMap(){
		Map<String,List<ShoppingCart>> shopcarts=new HashMap<String,List<ShoppingCart>>();
		
		for (int i = 0; i < super.size(); i++) {
			List<ShoppingCart> shoppingCarts=shopcarts.get(super.get(i).getStoreName());
			if(shoppingCarts==null){
				shoppingCarts=new ArrayList<ShoppingCart>();
			}
			shoppingCarts.add(super.get(i));
			shopcarts.put(super.get(i).getStoreName(),shoppingCarts);
		}
		return shopcarts;
	}
	
	/**
	 * 获取总金额
	 * @return
	 */
	public Double getTotalAmount(){
		Double totalAmount=0D;
		for (int i = 0; i < super.size(); i++) {
			ShoppingCart shoppingCart=super.get(i);
			totalAmount+=shoppingCart.getUnitPrice()*shoppingCart.getNumber();
		}
		return totalAmount;
	}
}
