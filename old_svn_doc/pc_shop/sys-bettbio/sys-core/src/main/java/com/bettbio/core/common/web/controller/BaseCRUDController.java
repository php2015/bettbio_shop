/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.bettbio.core.common.web.controller;

import java.io.Serializable;

import javax.validation.Valid;

import org.dom4j.tree.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefaults;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bettbio.core.common.constant.Constants;
import com.bettbio.core.common.service.BaseService;

import net.sf.ehcache.config.Searchable;

/**
 * 基础CRUD 控制器
 * <p>User: Zhang Kaitao
 * <p>Date: 13-2-23 下午1:20
 * <p>Version: 1.0
 */
public abstract class BaseCRUDController<M extends Serializable, ID extends Serializable>
        extends BaseController<M, ID> {

    protected BaseService<M> baseService;

    /**
     * 设置基础service
     *
     * @param baseService
     */
    @Autowired
    public void setBaseService(BaseService<M> baseService) {
        this.baseService = baseService;
    }
}
