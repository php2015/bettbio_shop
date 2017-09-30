var manMutiName = new Array();
var qualityName = new Array();
var qualitys = new Array();
var choiceOne =false;
var products = new Array();

function manBindOP(obj){
	$("."+obj).bind("click", function(){
		$(this).removeClass("mutiManCheck");
		filterByMan($(this).attr("id"),$(this).html(),obj);
	});
}

function removeManById(ezyArry,id){
	if(ezyArry.length>0){
		for(var i=0;i<ezyArry.length;i++){
			if(ezyArry[i]==id){
				ezyArry.splice(i, 1);  
			}
		}
	}
}

function showButton(obj,arrylist){
	if(arrylist.length>0){
		$('#okbutton_'+obj).removeAttr("disabled");
	}else{
		$('#okbutton_'+obj).attr('disabled','true');
	}
}

function manMuti(obj){
	if (obj=="man"){
		enalbeBrandsMultiselection();
		return;
	}
	//aready open man Muti
	if(choiceOne == true){
		if(obj=="man"){
			choiceOne=false;	
			$("#"+"qua"+"CatePanel").addClass("search_fixed_height");
			$("#"+"qua"+"CatePanel").removeClass("search_auto_height");
			$('#confirmPanel_'+"qua").hide();
			$(".mutiMan").removeClass("mutiManCheck");
			$('#panel_'+"qua").removeClass("choice-border")
			$('#mutiButton_'+"qua").show();
			qualityName = new Array();
			qualitys = new Array();
			manBindOP("qua");
//			manMutiCancel("qua");
		}else  if(obj=="qua"){
			$("#"+"man"+"CatePanel").addClass("search_fixed_height");
			$("#"+"man"+"CatePanel").removeClass("search_auto_height");
			$('#confirmPanel_'+"man").hide();
			$(".mutiMan").removeClass("mutiManCheck");
			$('#panel_'+"man").removeClass("choice-border")
			$('#mutiButton_'+"man").show();
			manufacturers = new Array();
			manMutiName = new Array();
			manBindOP("man");
//			manMutiCancel("man");
		}
	}
	choiceOne = true;
	
	if(obj=="man"){
		manufacturers = new Array();
		manMutiName=new Array();
		//添加复选框
		var list = $("#manCatePanel search_bar");
		for (i=0;i<list.length;i++) {
			var search_bar = list[i];
			var span = search_bar.innerHTML;
			search_bar.innerHTML="<input type='checkbox' style='width:12px !important;height:12px !important;margin-top:2px;'/>"+span;
		}
		showButton(obj,manufacturers);
	}else if(obj=="qua"){
		qualityName = new Array();
		qualitys = new Array();
		//添加复选框
		var list = $("#panel_qua search_bar");
		for (i=0;i<list.length;i++) {
			var search_bar = list[i];
			var span = search_bar.innerHTML;
			search_bar.innerHTML="<input type='checkbox' style='width:12px !important;height:12px !important;margin-top:2px;'/>"+span;
		}
		showButton(obj,qualitys);
	}
	$("."+obj).unbind();
	//$('#collapseMan').collapse('show');
	$("#"+obj+"CatePanel").removeClass("search_fixed_height");
	$("#"+obj+"CatePanel").addClass("search_auto_height");
	$('#panel_'+obj).addClass("choice-border")
	$('#confirmPanel_'+obj).show();
	$('#mutiButton_'+obj).hide();
	$("."+obj).bind("click", function(){
		 var checkbox = this.previousSibling;
		if(checkbox.checked){
			 if(obj=="man"){
				 removeManById(manufacturers,$(this).attr("id"));
				 removeManById(manMutiName,$(this).html());
			 } else if(obj=="qua"){
				 removeManById(qualitys,$(this).attr("id"));
				 removeManById(qualityName,$(this).html());
			 }
			 //$(this).removeClass("mutiManCheck");
			checkbox.checked = false;
	      }else{
	    	  if(obj=="man"){
	    		  /*if(manufacturers.length>4){
		    		  alertWarning();
		    	  }else{*/
	    		  //$(this).addClass("mutiManCheck");
	    		  manufacturers[manufacturers.length]=$(this).attr("id");
	    		  manMutiName[manMutiName.length]=$(this).html();
		    	  /*}*/
	    	  }else  if(obj=="qua"){
	    		/*  if(qualitys.length>4){
		    		  alertWarning();
		    	  }else{*/
//	    		  $(this).addClass("mutiManCheck");
	    		  qualitys[qualitys.length]=$(this).attr("id");
	    		  qualityName[qualityName.length]=$(this).html();
		    	 /* }*/
	    	  }
	    	  checkbox.checked = true;
	      }
		if(obj=="man"){
			showButton(obj,manufacturers);
		}else if(obj=="qua"){
			showButton(obj,qualitys);
		}
			
	});
	$("search_bar :checkbox").bind("click", function(){
		 var flag = this.checked;
		 var span = this.nextElementSibling;
		if(!flag){
			 if(obj=="man"){
				 removeManById(manufacturers,span.id);
				 removeManById(manMutiName,span.innerHTML);
			 } else if(obj=="qua"){
				 removeManById(qualitys,span.id);
				 removeManById(qualityName,span.innerHTML);
			 }
	      }else{
	    	  if(obj=="man"){
	    		  /*if(manufacturers.length>4){
		    		  alertWarning();
		    	  }else{*/
	    		  manufacturers[manufacturers.length]=span.id;
	    		  manMutiName[manMutiName.length]=span.innerHTML;
		    	  /*}*/
	    	  }else  if(obj=="qua"){
	    		/*  if(qualitys.length>4){
		    		  alertWarning();
		    	  }else{*/
//	    		  $(this).addClass("mutiManCheck");
//	    		  qualitys[qualitys.length]=$(this).attr("id");
//	    		  qualityName[qualityName.length]=$(this).html();
	    		  qualitys[qualitys.length]=span.id;
	    		  qualityName[qualityName.length]=span.innerHTML;
		    	 /* }*/
	    	  }	    	  
	      }
		if(obj=="man"){
			showButton(obj,manufacturers);
		}else if(obj=="qua"){
			showButton(obj,qualitys);
		}
			
	});
}


function manMutiCancel(obj){
	choiceOne=false;	
	$("#"+obj+"CatePanel").addClass("search_fixed_height");
	$("#"+obj+"CatePanel").removeClass("search_auto_height");
	$('#confirmPanel_'+obj).hide();
	$(".mutiMan").removeClass("mutiManCheck");
	$('#panel_'+obj).removeClass("choice-border")
	$('#mutiButton_'+obj).show();
	if(obj=="man"){
		manufacturers = new Array();
		manMutiName=new Array();
		//去掉复选框
		var list = $("#manCatePanel search_bar");
		for (i=0;i<list.length;i++) {
			var search_bar = list[i];
			var span = search_bar.innerHTML.substring(92, search_bar.innerHTML.length);
			search_bar.innerHTML= span;
		}
	}else if(obj=="qua"){
		qualityName = new Array();
		qualitys = new Array();
		//去掉复选框
		var list = $("#panel_qua search_bar");
		for (i=0;i<list.length;i++) {
			var search_bar = list[i];
			var span = search_bar.innerHTML.substring(92, search_bar.innerHTML.length);
			search_bar.innerHTML= span;
		}
	}
	manBindOP(obj);
}
function manMutiOk(obj){
	$(".mutiMan").removeClass("mutiManCheck");
	$("#"+obj+"CatePanel").addClass("search_fixed_height");
	$("#"+obj+"CatePanel").removeClass("search_auto_height");
	$('#confirmPanel_'+obj).hide();
	$('#panel_'+obj).removeClass("choice-border");
	$("#panel_"+obj).hide();
	var mutiNmae ="";
	if(obj=="man"){
		for(var i=0;i<manMutiName.length;i++){
			mutiNmae +=manMutiName[i]+'&nbsp;';
		}
		//去掉复选框
		var list = $("#manCatePanel search_bar");
		for (i=0;i<list.length;i++) {
			var search_bar = list[i];
			var span = search_bar.innerHTML.substring(92, search_bar.innerHTML.length);
			search_bar.innerHTML= span;
		}
	}else if(obj=="qua"){
		for(var i=0;i<qualityName.length;i++){
			mutiNmae +=qualityName[i]+'&nbsp;';
		}
		//去掉复选框
		var list = $("#panel_qua search_bar");
		for (i=0;i<list.length;i++) {
			var search_bar = list[i];
			var span = search_bar.innerHTML.substring(92, search_bar.innerHTML.length);
			search_bar.innerHTML= span;
		}
	}
	
	$("#name_"+obj).html('<div class="mutiMan" id="choiceMan_'+obj+'">'+mutiNmae+'<span class="glyphicon glyphicon-remove ico-style" aria-hidden="true"/></div>');
	$("#chiocedPanel_"+obj).show();
	$("#choiceMan_"+obj).bind("click", function(){
		$(this).remove();
		if(obj=="man"){
			manufacturers=new Array();
			manMutiName=new Array();
		}else if(obj=="qua"){
			qualityName = new Array();
			qualitys = new Array();
			//getMan(getcriteria());
		}
		loadProducts(getcriteria());
		$("#panel_"+obj).show();
		$("#chiocedPanel_"+obj).hide();		
	});
	$('#mutiButton_'+obj).show();	
	manBindOP(obj);	
	loadProducts(getcriteria());
}

function writeCate(cateList){
	if(cateList !=null && cateList.length>0){
		
		var tbBody ='<div class="search_nav5">';		
		$.each(cateList, function(i, p) {			
			tbBody+='<search_bar style="color:#333; margin-right: 65px;"><a  style="color:#333 !important;" href="javascript:void(0);" onclick="addFilter('+p.id+',&apos;'+p.name+'&apos;)">'+p.name;
			tbBody +=	'</a>&nbsp;<span class="countItems">('+p.productCount+')</span>';			 
			 tbBody +="</search_bar>";			
		});
		
		tbBody +='</div >';
		$("#subCatePanel").html(tbBody);
		if($("#subCatePanel").height()>25){
			$("#subCatePanel").addClass("search_fixed_height")
			$("#catMore").html( '<a role="button" style="border: 1px solid #E5E7E9;padding: 4px 6px;" href="javascript:void(0);" onclick="showMore(&quot;subCatePanel&quot;)">'+more+'<span class="glyphicon glyphicon-menu-down ico-style" aria-hidden="true"/></a>');
		}
		$("#categoryPanel").show();
	}else{
		$("#categoryPanel").hide();
	}
}

function filterByMan(id,pname,obj){	
	if(obj=="man"){
		manufacturers = new Array();
		manufacturers[0]=id;
	}else if(obj=="qua"){
		qualitys = new Array();
		qualitys[0]=id;
	}
	//getMan(getcriteria());
	loadProducts(getcriteria());
	$("#panel_"+obj).hide();
	$("#name_"+obj).html('<div class="mutiMan" id="chioceMan_'+obj+id+'">'+pname+'<span class="glyphicon glyphicon-remove ico-style" aria-hidden="true"/></div>');
	$("#chiocedPanel_"+obj).show();
	$("#chioceMan_"+obj+id).bind("click", function(){
		$(this).remove();	
		if(obj=="man"){
			manufacturers = new Array();
		}else if(obj=="qua"){
			qualitys = new Array();
			//getMan(getcriteria());
		}
		loadProducts(getcriteria());
		$("#panel_"+obj).show();
		$("#chiocedPanel_"+obj).hide();
	});
	//showMore("manCatePanel");
	$("#manCatePanel").addClass("search_brand_fixed_height");
	$("#manCatePanel").removeClass("search_auto_height");
}

function showMore(panel){
	if($("#"+panel).is('.search_fixed_height')){
		$("#"+panel).addClass("search_auto_height");
		$("#"+panel).removeClass("search_fixed_height");
		if (panel=="manCatePanel") {
			var span = $("#manMore a span");
			span.addClass("glyphicon-menu-up");
			span.removeClass("glyphicon-menu-down");
		} else if (panel == "subCatePanel") {
			var span = $("#catMore a span");
			span.addClass("glyphicon-menu-up");
			span.removeClass("glyphicon-menu-down");
		}
	}else {
		$("#"+panel).addClass("search_fixed_height");
		$("#"+panel).removeClass("search_auto_height");
		if (panel=="manCatePanel") {
			var span = $("#manMore a span");
			span.addClass("glyphicon-menu-down");
			span.removeClass("glyphicon-menu-up");
		} else if (panel == "subCatePanel") {
			var span = $("#catMore a span");
			span.addClass("glyphicon-menu-down");
			span.removeClass("glyphicon-menu-up");
		}
	}
}

/**
 * 品牌
 * */
function writeManCateV2(cateList,obj,type){
	multiBrandsSelectCancel();
	
	if(cateList !=null && cateList.length>0){
		$('#manCatePanel').removeClass('search_brand_fixed_height');
		var tbBody ='<div class="search_nav5">';
		
		//tbBody += '<div id="collapse_Man" class="panel-collapse collapse " role="tabpanel" aria-labelledby="headingOne" >';
		
		
		//tbBody+="<div id='value'>";
		tbBody =  tbBody + '<ul class="brand-show clearfix">';
		$.each(cateList, function(i, p) {
			
			 var brandHtml = '<li><a href="javascript:void(0);" onclick="selectBrand(this)" title="';
			 brandHtml += p.manName + ' ('+p.manCount+')';
			 brandHtml += '" data-bid="' + p.id + '" data-bname="' + p.manName;
			 brandHtml += '"><span>' + p.manName + '</span></a></li>';
			 
			 tbBody +=brandHtml;
		});
		
		tbBody+="</ul>";	    
	    
		tbBody +='<span  style="display: none;" id="confirmPanel_man"><div class="row" style="text-align:center"><button type="button" id="okbutton_'+obj+'" class="btn btn-danger"  disabled="disabled" onclick="multiBrandsSelectOK()">'+ok;
		tbBody +='</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-info" onclick="multiBrandsSelectCancel()">'+cancel+'</button><span></div></div>';
		$("#manCatePanel").html(tbBody);
		if($("#manCatePanel").height()>100){
			$("#manCatePanel").addClass("search_brand_fixed_height");
			$("#manMore").html( '<a role="button" style="border: 1px solid #E5E7E9;padding:4px 6px;"  href="javascript:void(0);" onclick="showMoreBrands(this)" class="pull-right">'+more+'<span class="glyphicon glyphicon-menu-down ico-style" aria-hidden="true"/></a>');
			$("#manMore").css("margin-left","10px");
			
		}
		
		$("#panel_"+obj).show();
		//manBindOP(obj);
	}else{
		$("#panel_"+obj).hide();
	}
}
function writeManCate(cateList,obj,type){
	if(cateList !=null && cateList.length>0){
		var tbBody ='<div class="search_nav5">';
		
		//tbBody += '<div id="collapse_Man" class="panel-collapse collapse " role="tabpanel" aria-labelledby="headingOne" >';
		
		
		//tbBody+="<div id='value'>";
		
		$.each(cateList, function(i, p) {
			 tbBody+='<search_bar style="color:#333;margin-right: 65px;"><span class=" mutiMan '+obj+' choice-panel-span" id="'+p.id+'">'+p.manName;
			 if(p.manCount != null && p.manCount>0) {
					if(type !=null && type==0){
						tbBody += '&nbsp;';
					}else{
						tbBody += '&nbsp;('+p.manCount+')';
					}
					
				}
			 tbBody +="</span></search_bar>";
		});
		
		//tbBody+="</ul>";	    
	    
		tbBody +='<span  style="display: none;" id="confirmPanel_'+obj+'"><div class="row" style="text-align:center"><button type="button" id="okbutton_'+obj+'" class="btn btn-danger"  disabled="disabled" onclick="manMutiOk(&apos;man&apos;)">'+ok;
		tbBody +='</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-info" onclick="manMutiCancel(&apos;man&apos;)">'+cancel+'</button><span></div></div>';
		$("#manCatePanel").html(tbBody);
		if($("#manCatePanel").height()>30){
			$("#manCatePanel").addClass("search_fixed_height");
			$("#manMore").html( '<a role="button" style="border: 1px solid #E5E7E9;padding:4px 6px;"  href="javascript:void(0);" onclick="showMore(&quot;manCatePanel&quot;)" class="pull-right">'+more+'<span class="glyphicon glyphicon-menu-down ico-style" aria-hidden="true"/></a>');
			$("#manMore").css("margin-left","10px");
			
		}
		
		$("#panel_"+obj).show();
		manBindOP(obj);
	}else{
		$("#panel_"+obj).hide();
	}
}
function addpagenum(num){
	pageNum=num;
	$('#showpagenum').html(num);
	var criteria = getcriteria() ;
	criteria += '&pageNum=' + num ;
	loadProducts(criteria);
}
function doAction(pagenum){
	page = pagenum;
	var criteria = getcriteria() ;
	criteria += '&page=' + pagenum ;
	loadProducts(criteria);
	
}
function getDiscountDisplay(discount){
	return Math.floor(discount*10000)/1000;
}
function getPrice(){
	if(products!=null&&products.length>0 || services!=null && services.length>0) {
		$.ajax({
			type: 'POST',
			dataType: "json",
			url: getContextPath()+'/services/public/products/price.html',
			data: 'prodcutId='+products+'&services='+services,
			success: function(priceList) {
				if(priceList != null){
					$.each(priceList, function(i, p) {
						if(p.sPrice !=null && p.sPrice !=''){
							var priceHtml='<div class="container-fluid" style="margin:2px 0px;border-bottom:1px dashed #ddd"><div class="row">';
							priceHtml += p.sPrice +'</div></div>';
							$("#price_"+p.id).html(priceHtml);
						}else{
							if(p.prices !=null){
								var priceHtml='';
								for(var j=0;j<p.prices.length&&j<3;j++){
									priceHtml += '<div class="container-fluid" style="margin:2px 0px;border-bottom:1px dashed #ddd"><div class="row">';
									
									priceHtml += '<div class="col-sm-3" style="padding-left:0px!important;overflow:hidden;white-space: nowrap;width:120px;padding-right:0px;">'+ p.prices[j].code;
									priceHtml += '</div>';
									
									// 目录价 折扣 和 折扣价
									// false 是根据新的价格规则，special价格已经不再生效，所以添加的
									if (false && p.prices[j].special != null && p.prices[j].special !=''){
										priceHtml += '<div class="col-sm-3" style="padding-left:0px!important;color:#FF6C00"><s>'+ p.prices[j].price + '</s>';
										priceHtml += '</div>';
										priceHtml += '<div class="col-sm-3" style="padding-left:0px!important;color:#FF6C00">'+ p.prices[j].special;
										priceHtml += '</div>';
									} else {
										var specPrice = p.prices[j];
										if (p.prices[j].priceValue != null && p.prices[j].priceValue == '0') {
											priceHtml += '<div class="col-sm-3" style="padding-left:0px!important;color:blue;width:100px;padding-right:0px;">'
											priceHtml += '<a href="javascript:void(0);" style="color:blue;" title="服务热线：021-61552750">询价</a>';
											priceHtml += '</div>';
											priceHtml += '<div class="col-sm-2" style="padding-left:0px!important;color:blue;width:90px;padding-right:0px;">';
											/**
											if (p.prices[j].discountRate != null){
												if (p.prices[j].discountRate >= 0){
													priceHtml += '<span style="border: 1px solid blue;">'+getDiscountDisplay(p.prices[j].discountRate) + '折<span>';
												}else{
													priceHtml += '<a style="color:blue;" href="' + loginPageUrl + '">登录查看</a>';
												}
											}**/
											if (specPrice.discountType == null){
												priceHtml += '<a style="color:blue;" href="' + loginPageUrl + '">登录查看</a>';
											}else if (specPrice.discountType == "list_price"){
												// nothing to do
											}else if (specPrice.discountType == "timed_promotion"){
												priceHtml += '<span style="border: 1px solid blue;">'+ '限时促销' + '<span>';
											}else{
												priceHtml += '<span style="border: 1px solid blue;">'+getDiscountDisplay(specPrice.discountRate) + '折<span>';
											}
											priceHtml += '</div>';
											
											priceHtml += '<div class="col-sm-3" style="padding-left:0px!important;color:blue;width:130px;padding-right:0px;">';
											/**
											if (p.prices[j].discountRate != null){
												if (p.prices[j].discountRate >= 0){
													priceHtml += '<a href="javascript:void(0);" style="color:blue;" title="服务热线：021-61552750">询价</a>';
												}else{
													priceHtml += '<a style="color:blue;" href="' + loginPageUrl + '">登录查看</a>';
												}
											}**/
											if (specPrice.discountType == null){
												priceHtml += '<a style="color:blue;" href="' + loginPageUrl + '">登录查看</a>';
											}else if (specPrice.discountType == "list_price"){
												// nothing to do
											}else if (specPrice.discountType == "timed_promotion"){
												priceHtml += '<span style="border: 1px solid blue;">'+ '限时促销' + '<span>';
											}else{
												priceHtml += '<span style="border: 1px solid blue;">'+getDiscountDisplay(specPrice.discountRate) + '折<span>';
											}
											priceHtml += '</div>';
											
										} else {
											priceHtml += '<div class="col-sm-3" style="padding-left:0px!important;color:#FF6C00;width:100px;padding-right:0px;">'+ p.prices[j].priceValue;
											priceHtml += '</div>';
											priceHtml += '<div class="col-sm-2 discountRate" style="padding-left:0px!important;color:#FF6C00;width:90px;padding-right:0px;">';
											/**
											if (p.prices[j].discountRate != null){
												if (p.prices[j].discountRate >= 0){
													priceHtml += '<span style="border: 1px solid #FF6C00;">' + getDiscountDisplay(p.prices[j].discountRate) + '折</span>';
												}else{
													priceHtml += '<a style="color:#FF6C00;" href="' + loginPageUrl + '">登录查看</a>';
												}
											}else{
												priceHtml += '&nbsp;';
											}**/
											if (specPrice.discountType == null){
												priceHtml += '<a style="color:#FF6C00;" href="' + loginPageUrl + '">登录查看</a>';
											}else if (specPrice.discountType == "list_price"){
												// nothing to do
											}else if (specPrice.discountType == "timed_promotion"){
												priceHtml += '<span style="border: 1px solid #FF6C00;">'+ '限时促销' + '</span>';
											}else{
												priceHtml += '<span style="border: 1px solid #FF6C00;">'+getDiscountDisplay(specPrice.discountRate) + '折</span>';
											}
											priceHtml += '</div>';
											
											priceHtml += '<div class="col-sm-3 discountPrice" style="padding-left:0px!important;color:#FF6C00;width:130px;padding-right:0px;">';
											/**
											if (p.prices[j].discountRate != null){
												if (p.prices[j].discountRate >= 0){
													priceHtml += p.prices[j].discountPrice;;
												}else{
													priceHtml += '<a style="color:#FF6C00;" href="' + loginPageUrl + '">登录查看</a>';
												}
											}else{
												priceHtml += '&nbsp;';
											}**/
											if (specPrice.discountType == null){
												priceHtml += '<a style="color:#FF6C00;" href="' + loginPageUrl + '">登录查看</a>';
											}else if (specPrice.discountType == "list_price"){
												// nothing to do
											}else{
												priceHtml += specPrice.discountPrice;
											}
											priceHtml += '</div>';
										}
									}
									
									
									if (p.prices[j].priceValue != null && p.prices[j].priceValue == '0') {
										priceHtml += '<div class="col-sm-1" style="padding-left:0px!important;color:#aaa;font-size: 1.7em;width:18px;padding-right:0px;">';
										priceHtml += '<span class="fa-shopping-cart fa" aria-hidden="true"></span>';
									} else {
										priceHtml += '<div class="col-sm-1" style="padding-left:0px!important;color:#ff313b;cursor:pointer;font-size: 1.7em;width:18px;padding-right:0px;">';
										priceHtml += '<span class="fa-shopping-cart fa" aria-hidden="true" onclick="addToCart('+p.id+','+p.prices[j].id+')"></span>';
									}
									priceHtml += '</div>';
									
									priceHtml += '</div></div>';
									
								}	
								if(p.prices.length>3){
									priceHtml +='<div class="container-fluid" style="margin:2px 0px;border-bottom:1px dashed #ddd"><div class="row" style="color:#449d44;"><a target="_blank"  href="/sm-shop/shop/product/' +p.id + '.html">更多...</a></div></div>';
								}
								
								$("#price_"+p.id).html(priceHtml);
							}
						}
						
					});
				}
				$("#categoryBody").hideLoading();
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				$("#categoryBody").hideLoading();
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
			
		});
	}
	
}

function showArrow(panel,arrow){
	showMore(panel);
	if($("#"+arrow).attr('src')=='/sm-shop/resources/img/downa.png'){
		$("#"+arrow).attr('src','/sm-shop/resources/img/upa.png');
	}else {
		$("#"+arrow).attr('src','/sm-shop/resources/img/downa.png');
	}
}

function log(value) {
}

var multiBrandsSelectionEnabled = false;
var singleSelectedBrandObj = null;

function showMoreBrands(aObj) {
	var hiddenBrands = $('#manCatePanel').hasClass('search_brand_fixed_height');
	var titleObj = $('#manMore a span');
	if (hiddenBrands){
		$('#manCatePanel').removeClass('search_brand_fixed_height');
		titleObj.removeClass('glyphicon-menu-down');
		titleObj.addClass('glyphicon-menu-up');
	}else{
		$('#manCatePanel').addClass('search_brand_fixed_height');
		titleObj.removeClass('glyphicon-menu-up');
		titleObj.addClass('glyphicon-menu-down');
	}
}
function enalbeBrandsMultiselection(){
	$('#confirmPanel_man').show();
	$('#manCatePanel').removeClass('search_brand_fixed_height');
	multiBrandsSelectionEnabled = true;
	$('#mutiButton_man').hide();
}
function selectBrand(aObj){
	if (!multiBrandsSelectionEnabled){
		if (singleSelectedBrandObj != null){
			singleSelectedBrandObj.removeClass('selectedBrand');
			singleSelectedBrandObj.css("border", "");
		}
		filterByMan($(aObj).data('bid'), $(aObj).data('bname'), "man");
		return;
	}
	var isSelected = $(aObj).hasClass('selectedBrand');
	if (isSelected){
		$(aObj).removeClass('selectedBrand');
		$(aObj).css("border", "");
		singleSelectedBrandObj = null;
	}else{
		$(aObj).addClass('selectedBrand');
		$(aObj).css("border", "3px solid rgb(125,204,0)");
		singleSelectedBrandObj = $(aObj);
	}
	var hasSeleceted = false;
	$('.brand-show li a').each(function(i, obj){
		if ($(obj).hasClass('selectedBrand')){
			hasSeleceted = true;
		}
	});
	$('#okbutton_man').attr("disabled", !hasSeleceted);
}
function multiBrandsSelectOK(){
	var selectedBNames = new Array();
	var selectedBIds = new Array();
	$('.brand-show li a').each(function(i, obj){
		if ($(obj).hasClass('selectedBrand')){
			$(obj).removeClass("selectedBrand");	
			$(obj).css("border", "");
			selectedBNames.push($(obj).data('bname'));
			selectedBIds.push($(obj).data('bid'));
		}
	});
	multiBrandsSelectionEnabled = false;
	$('#confirmPanel_man').hide();
	$('#manCatePanel').addClass('search_brand_fixed_height');
	var titleObj = $('#manMore a span');
	titleObj.removeClass('glyphicon-menu-up');
	titleObj.addClass('glyphicon-menu-down');
	$('#mutiButton_man').show();
	
	$('#panel_man').removeClass("choice-border");
	$("#panel_man").hide();
	
	var mutiNmae ="";
	
	for(var i=0;i<selectedBNames.length;i++){
		mutiNmae +=selectedBNames[i]+'&nbsp;';
	}
		
	$("#name_man").html('<div class="mutiMan" id="choiceMan_man">'+mutiNmae+'<span class="glyphicon glyphicon-remove ico-style" aria-hidden="true"/></div>');
	$("#chiocedPanel_man").show();
	$("#choiceMan_man").bind("click", function(){
		$(this).remove();
		manufacturers=new Array();
		manMutiName=new Array();
		
		loadProducts(getcriteria());
		$("#panel_man").show();
		$("#chiocedPanel_man").hide();		
	});
		
	//manBindOP(obj);	
	loadProducts(getcriteria());
}
function multiBrandsSelectCancel(){
	multiBrandsSelectionEnabled = false;
	$('#confirmPanel_man').hide();
	clearAllBrandsSelection();
	$('#manCatePanel').addClass('search_brand_fixed_height');
	var titleObj = $('#manMore a span');
	titleObj.removeClass('glyphicon-menu-up');
	titleObj.addClass('glyphicon-menu-down');
	$('#mutiButton_man').show();
}
function clearAllBrandsSelection() {
	var x = $('.brand-show li a');
	singleSelectedBrandObj = null;
	x.each(function(i, obj){
		$(obj).removeClass("selectedBrand");	
		$(obj).css("border", "");
	});
}