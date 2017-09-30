package com.bettbio.core.service;

import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.SSubOrder;
import com.bettbio.core.model.vo.SubOrderVo;

public interface SubOrderService extends IService<SSubOrder>{
   /**
    * 获取子订单集合
    * @param map
    * @return
    */
   Page<SubOrderVo> selectSubOrdersByMap(Map<String,Object> map);
   /**
    * 更新订单状态
    * @param orderState
    * @param orderCode
    */
   void updateOrderState(String orderState,String orderCode);
   /**
    * 根据订单编号获取订单
    * @param code
    * @return
    */
   Map<String,Object> selectSubOrderByCode(String code);
   /**
    * 获取订单总数
    * @return
    */
   int selectOrderCountByCode(String code);
   /**
    * 获取商家总价
    * @param code
    * @return
    */
   Double selectCountPriceByCode(String code);
}
