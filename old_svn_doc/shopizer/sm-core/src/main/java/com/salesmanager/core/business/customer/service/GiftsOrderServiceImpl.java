package com.salesmanager.core.business.customer.service;



import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salesmanager.core.business.customer.dao.GiftsOrderDAO;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.GftsOrderCriteria;
import com.salesmanager.core.business.customer.model.GiftOrder;
import com.salesmanager.core.business.customer.model.GiftOrdersList;
import com.salesmanager.core.business.customer.model.GiftStatus;
import com.salesmanager.core.business.customer.model.Gifts;
import com.salesmanager.core.business.customer.model.MemberPoints;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;

@Service("giftOrderImpl")
public class GiftsOrderServiceImpl extends SalesManagerEntityServiceImpl<Long, GiftOrder> implements GiftsOrderService{
	@Autowired
	private GiftsOrderDAO giftsOrderDAO;
	@Autowired
	private GiftsService giftsService;  //鍟嗗搧鍏戞崲
	@Autowired
	private MemberPointsService memberPointsService;
	@Autowired
	private CustomerAddressService customerAddressService;
	
	@Autowired
	public GiftsOrderServiceImpl(GiftsOrderDAO giftsOrderDAO) {
		super(giftsOrderDAO);
		this.giftsOrderDAO=giftsOrderDAO;
	}

	@Override
	public GiftOrdersList getByCriteria(GftsOrderCriteria giftOderCriteria) {
		return giftsOrderDAO.getByCriteria(giftOderCriteria);
	}

	@Override
	public void saveOrUpdate(GiftOrder giftOrder)  throws ServiceException {
		// TODO Auto-generated method stub
		if(giftOrder.getId() ==null){
			super.create(giftOrder);
		}else{
			super.update(giftOrder);
		}
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public int saveGift(Customer customer, long cid, Gifts gift,int num) throws ServiceException{
		// TODO Auto-generated method stub
		long point = gift.getPoints();
		List<MemberPoints> mems = memberPointsService.getLeftPoint(customer.getId());
		   if(mems !=null && mems.size()>0){
			   for(MemberPoints mem:mems){
				   if(mem.getLtfePoint()>=point){    					   
					   mem.setLtfePoint(mem.getLtfePoint()-point);
					  memberPointsService.save(mem);
					  break;
				   }else{
					   //mem.setLtfePoint(point-mem.getLtfePoint());
					   point= point- mem.getLtfePoint();
					   mem.setLtfePoint(0L);
					   memberPointsService.save(mem);
				   }
			   }
		   }
		   //积分兑换插入数据
		   MemberPoints points = new MemberPoints();
		   points.setStatas(0);
		   points.setUpdateDate(new Date());//更新日期
		   points.setValue(gift.getPoints()+"");
		   points.setCustomer(customer);
		   points.setType("EXCHANGE_SCORE");
		   memberPointsService.save(points);
		   
		   GiftOrder giftOrder = new GiftOrder();
		   giftOrder.setCustomer(customer);
		   giftOrder.setCustomerName(customer.getNick());
		   giftOrder.setGifName(gift.getName());
		   giftOrder.setGifPoint(gift.getPoints().intValue());
		   giftOrder.setGifImge(gift.getPictureSrc());
		   giftOrder.setGitfid(gift.getId());
		  // giftOrder.setGifts(gift);
		   giftOrder.setNumber(num);
		   giftOrder.setCreateDate(new Date());
		   giftOrder.setStatus(GiftStatus.ORDERED);
		   CustomerAddress customerAdress =customerAddressService.getById(cid);
		   //giftOrder.setCustomerAddress(customerAdress);
		   giftOrder.setCustomerName(customerAdress.getName());
		   //giftOrder.setPhoneNumber(customerAdress.getTelephone());
		   giftOrder.setCustomerCity(customerAdress.getCity());
		   giftOrder.setShippingAddress(customerAdress.getAddress());
		   giftOrder.setCustomerAddressId(customerAdress.getId());
		   
		   super.save(giftOrder);
		return 0;
	}

}
