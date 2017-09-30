package com.bettbio.core.service;

import java.util.List;

import com.bettbio.core.common.service.IService;
import com.bettbio.core.model.Country;

public interface CountryService extends IService<Country> {

    /**
     * 根据条件分页查询
     *
     * @param country
     * @param page
     * @param rows
     * @return
     */
    List<Country> selectByPage(Country country, int page, int rows);
}
