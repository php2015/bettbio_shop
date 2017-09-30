package com.bettbio.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.BaseService;
import com.bettbio.core.common.service.async.AsyncTaskService;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.mapper.SOrderDetailsMapper;
import com.bettbio.core.mapper.SOrderMapper;
import com.bettbio.core.mapper.SStoreMapper;
import com.bettbio.core.mapper.SSubOrderMapper;
import com.bettbio.core.mapper.SUserMapper;
import com.bettbio.core.mapper.SUserPointsMapper;
import com.bettbio.core.model.InvoiceInfo;
import com.bettbio.core.model.SOrder;
import com.bettbio.core.model.SOrderDatails;
import com.bettbio.core.model.SStore;
import com.bettbio.core.model.SSubOrder;
import com.bettbio.core.model.SUser;
import com.bettbio.core.model.SUserAddress;
import com.bettbio.core.model.SUserPoints;
import com.bettbio.core.model.vo.OrderVo;
import com.bettbio.core.model.vo.ShoppingCartVo;
import com.bettbio.core.model.vo.SubOrderVo;
import com.bettbio.core.mongo.model.ShoppingCart;
import com.bettbio.core.mongo.model.ShoppingCartList;
import com.bettbio.core.mongo.service.ShoppingCartService;
import com.bettbio.core.service.OrderService;

@Service
public class OrderServiceImpl extends BaseService<SOrder> implements OrderService{

	SOrderMapper getSOrderMapper(){
		return (SOrderMapper)mapper;
	}
	
	@Autowired
	SStoreMapper storeMapper;
	@Autowired
	SSubOrderMapper sSubOrderMapper;
	@Autowired
	SOrderDetailsMapper sOrderDetailsMapper;
	@Autowired
	AsyncTaskService asyncTaskService;
	@Autowired
	ShoppingCartService shoppingCartService;
	@Autowired
	SUserPointsMapper sUserPointsMapper;
	@Autowired
	SUserMapper sUserMapper;
	@Override
	public String createOrder(SUserAddress userAddress, InvoiceInfo invoiceInfo, ShoppingCartList shoppingCartList,String userCode,ShoppingCartVo shoppingCartVo) {
		
		SOrder order = new SOrder();// 订单主表
		order.setCode(CodeUtils.getOrderCode());
		order.setUserCode(userCode);
		order.setUserName(userAddress.getUserName());
		order.setUserPhone(userAddress.getUserPhone());
		order.setCorporateName(userAddress.getCompanyName());
		order.setPostcode(userAddress.getPostcode());
		// 设置收货地址
		StringBuffer address = new StringBuffer();
		if (!"".equals(userAddress.getProvince())) {
			address.append(userAddress.getProvince());
		}
		if (!"".equals(userAddress.getCity())) {
			address.append(userAddress.getCity());
		}
		address.append(userAddress.getStreet());
		order.setUserAddress(address.toString());

		// 设置发票信息
		order.setRise(invoiceInfo.getRise());
		order.setBankAccount(invoiceInfo.getBankAccount());
		order.setCompanyRegisteredName(invoiceInfo.getCorporateName());
		order.setIdentificationCode(invoiceInfo.getIdentificationCode());
		order.setRegisterAddress(invoiceInfo.getRegisterAddress());
		order.setRegisteredPhone(invoiceInfo.getRegisteredPhone());
		order.setOpenAccountBank(invoiceInfo.getOpenAccountBank());
		order.setInvoiceType(invoiceInfo.getInvoiceType());
		// 获取订购产品集合
		order.setTotalAmount(shoppingCartList.getTotalAmount());
		//留言集合
		Map<String,String> remarks=new HashMap<String,String>();
		if(shoppingCartVo!=null){
		    remarks=shoppingCartVo.toMap();
		}
		// 创建订单详情
		//记录商家数量
		 Map<String, List<ShoppingCart>> shopCartMap=shoppingCartList.thisToMap();
		int storeNum=shopCartMap.size();
		if(storeNum>1){
			order.setOrderState("已拆单");
		}
		for (Map.Entry<String, List<ShoppingCart>> entry : shopCartMap.entrySet()) {
			storeNum++;
			SSubOrder sSubOrder = new SSubOrder();
			sSubOrder.setOrderState("未发货");
			sSubOrder.setParentOrderCode(order.getCode());
			if(storeNum==1){
				sSubOrder.setOrderCode(order.getCode());
			}else{
				sSubOrder.setOrderCode(CodeUtils.getOrderCode());
			}
			sSubOrder.setStoreName(entry.getKey());
			sSubOrder.setOrderCountPrice(shoppingCartsPrice(entry.getValue()));
			try {
				for (int i = 0; i < entry.getValue().size(); i++) {
					ShoppingCart shoppingCart=entry.getValue().get(i);
					if (i == 0) {//完善商家信息
						SStore sStore = new SStore();
						sStore.setCode(entry.getValue().get(i).getStoreCode());
						sStore = storeMapper.selectByCode(sStore);
						sSubOrder.setStoreCode("-1");
						sSubOrder.setOldStoreCode(sStore.getCode());
						sSubOrder.setStorePhone(sStore.getLandline());
						sStore.setRemarks(remarks.get(sStore.getCode()));
					}
					SOrderDatails sOrderDatail = new SOrderDatails();//产品详情
					sOrderDatail.setSubOrderCode(sSubOrder.getOrderCode());
					sOrderDatail.setProductCountPrice(shoppingCart.getUnitPrice()*shoppingCart.getNumber());
					sOrderDatail.setProductNameEn(shoppingCart.getProductNameEn());
					sOrderDatail.setProductNameCh(shoppingCart.getProductNameCh());
					sOrderDatail.setProductNum(shoppingCart.getNumber());
					sOrderDatail.setProductPrice(shoppingCart.getUnitPrice());
					sOrderDatail.setProductSpce(shoppingCart.getProductSpec());
					sOrderDatail.setImgUrl(shoppingCart.getProductImg());
					sOrderDetailsMapper.insert(sOrderDatail);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			sSubOrderMapper.insert(sSubOrder);
		}
		getSOrderMapper().insert(order);
		
		asyncTaskService.run(new Runnable() {//异步执行，保证订单响应速度
			
			@Override
			public void run() {
			//删除购物车
			shoppingCartService.removes(userCode,new ShoppingCartVo(shoppingCartList));
			//发送邮件
			SUser suser=sUserMapper.selectByCode(userCode);
			//更改额度
			Double totalAmount=shoppingCartList.getTotalAmount();
			suser.setBalance(suser.getBalance()-totalAmount);
		    //下单积分
			SUserPoints sUserPoints = new SUserPoints();
			sUserPoints.setCode(userCode);
			Integer orderPoint=Integer.parseInt(Math.round(totalAmount/10)+"");
			sUserPoints.setPoints(orderPoint);
			sUserPoints.setPointsType(Constants.POINTS_ORDER);
			if (sUserPointsMapper.insert(sUserPoints) == 1) {
				suser.setIntegral(suser.getIntegral()+orderPoint);
				sUserMapper.updateByPrimaryKey(suser);
			}
			//判断首单
			SOrder order=new SOrder();
			order.setUserCode(userCode);
			if(getSOrderMapper().selectCount(order)==1){//首单激励
				//首单送积分
				sUserPoints=new SUserPoints();
				sUserPoints.setCode(userCode);
				sUserPoints.setPoints(Constants.DEFAULT_FIRST_ORDER_POINTS);
				sUserPoints.setPointsType(Constants.POINTS_FIRST_ORDER);
				if (sUserPointsMapper.insert(sUserPoints) == 1) {
					suser.setIntegral(suser.getIntegral()+Constants.DEFAULT_FIRST_ORDER_POINTS);
					sUserMapper.updateUserPoints(suser);
				}
			}
			}
		});
		
		return order.getCode();
	}
	
	
	/**
	 * 计算总价
	 * 
	 * @return
	 */
	public Double shoppingCartsPrice(List<ShoppingCart> shoppingCarts) {
		Double countPrice = 0D;
		for (ShoppingCart shoppingCart : shoppingCarts) {
			countPrice += shoppingCart.getUnitPrice() * shoppingCart.getNumber();
		}
		return countPrice;
	}
	
	
	@Override
	public Page<OrderVo> selectOrders(Map<String, Object> map) {
		Page<OrderVo> orderVos = new Page<OrderVo>(map, getSOrderMapper().orderCount(map));
		List<OrderVo> orders=getSOrderMapper().selectOrders(orderVos.getMap());
		for (OrderVo orderVo : orders) {
			List<SubOrderVo> subOrders=sSubOrderMapper.selectSubOrders(orderVo.getCode());
			for (SubOrderVo subOrderVo : subOrders) {
				SOrderDatails sOrderDatails=new SOrderDatails();
				sOrderDatails.setSubOrderCode(subOrderVo.getOrderCode());
				List<SOrderDatails> orderDatails=sOrderDetailsMapper.select(sOrderDatails);
				subOrderVo.setOrderDatails(orderDatails);
			}
			orderVo.setSubOrderVos(subOrders);
		}
		orderVos.setList(orders);
		return orderVos;
	}
	
}
