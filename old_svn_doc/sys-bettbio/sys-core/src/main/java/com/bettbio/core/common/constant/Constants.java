/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.bettbio.core.common.constant;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-7 下午5:14
 * <p>Version: 1.0
 */
public interface Constants {
    /**
     * 操作名称
     */
    String OP_NAME = "op";


    /**
     * 消息key
     */
    String MESSAGE = "message";

    /**
     * 错误key
     */
    String ERROR = "error";

    /**
     * 上个页面地址
     */
    String BACK_URL = "BackURL";

    String IGNORE_BACK_URL = "ignoreBackURL";

    /**
     * 当前请求的地址 带参数
     */
    String CURRENT_URL = "currentURL";

    /**
     * 当前请求的地址 不带参数
     */
    String NO_QUERYSTRING_CURRENT_URL = "noQueryStringCurrentURL";

    String CONTEXT_PATH = "ctx";

    /**
     * 当前登录的用户
     */
    String CURRENT_USER = "user";
    String CURRENT_USERNAME = "username";

    String ENCODING = "UTF-8";

    String IMG_CODE = "imgCode";
    
    String NO_AUTHORIZED_EXCEPTION = "noAuthorizedException";
    /**
     * 默认注册积分
     */
    int DEFAULT_REGISTER_POINTS=50;
    /**
     * 默认首单积分
     */
    int DEFAULT_FIRST_ORDER_POINTS=150;
    /**
     * 注册积分类型
     */
    int POINTS_REGISTER=0;
    /**
     * 首单积分类型
     */
    int POINTS_FIRST_ORDER=1;
    /**
     * 订单积分类型
     */
    int POINTS_ORDER=2;

    /**
     * 分页信息key
     */
    String PAGE_INFO = "pageInfo";
    
    /**
     * 参数信息key
     */
    String PARAM_MAP = "paramMap";
    
    String CATEGORY_REAGENT_CODE = "01";
    String CATEGORY_CONSUMERGOODS = "02";
    String CATEGORY_INSTRUMENT_CODE = "03";
    String CATEGORY_SERVICE_CODE = "04"; 
    
    Integer NOT_LOGIN_CODE = 4103;
    Integer ADMIN_NOLOGIN_CODE = 4403;
    Integer MANAGE_NOLOGIN_CODE = 4803;
}
