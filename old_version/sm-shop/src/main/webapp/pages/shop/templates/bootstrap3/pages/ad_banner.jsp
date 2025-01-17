﻿<%
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm"%>
<%@page pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/cuxiao_KT.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/dacu.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/demo.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/cuxiao/cuxiao.css'/>">

<div class="cuxiao_body" style="margin-top: 50px;">

    <div style="margin-bottom:5px;">
        <img  class="cx_img" src="/sm-shop/resources/img/adimages/img/ad_banner_wenan.jpg" />
    </div>

    <div style="width: 1340px;margin: 0 auto;"> <!-- 1290=90+1200 -->
        <div style="width: 100%; display: inline-block; margin: 16px;">
            <a href="http://www.biomanufacturingsingleuseconference.com/" target="_blank">
            <img src="/sm-shop/resources/img/adimages/img/20170930_001.jpg" width="1200px"/>
            <!-- <img src="/sm-shop/resources/img/adimages/img/20170930_001.jpg"/> -->
            </a>
        </div>
        <div comment="广告交换的文字链接" style="width: 100%; display: inline-block; margin: 16px; font-size: 24px; ">
                <a href="http://www.biomanufacturingsingleuseconference.com/news/394308" target="_ex_ad_window_">
                    <div style="width: 1200px; color: #3333FF;">
                        <u>中国制造一次性使用技术为中国生物制药快速前进助力 <span style="font-size: 12px; ">&gt;&gt;</span></u>
                    </div>
                </a>
                <a href="http://www.biomanufacturingsingleuseconference.com/news/389548/" target="_ex_ad_window_">
                    <div style="width: 1200px; color: #3333FF;">
                        <u>评估一次性使用技术在生物制药生产中的适用性和工艺经济性 <span style="font-size: 12px; ">&gt;&gt;</span></u>
                    </div>
                </a>
            </div>
    </div>
</div>
