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
	//aready open man Muti
	if(choiceOne == true){
		if(obj=="man"){
			manMutiCancel("qua");
		}else  if(obj=="qua"){
			manMutiCancel("man");
		}
	}
	choiceOne = true;
	if(obj=="man"){
		manufacturers = new Array();
		manMutiName=new Array();
		showButton(obj,manufacturers);
	}else if(obj=="qua"){
		qualityName = new Array();
		qualitys = new Array();
		showButton(obj,qualitys);
	}
	$("."+obj).unbind();
	//$('#collapseMan').collapse('show');
	$('#panel_'+obj).addClass("choice-border")
	$('#confirmPanel_'+obj).show();
	$('#mutiButton_'+obj).hide();
	$("."+obj).bind("click", function(){
		if($(this).is('.mutiManCheck')){
			 $(this).removeClass("mutiManCheck");
			 removeManById(manufacturers,$(this).attr("id"));
			 removeManById(manMutiName,$(this).html());
	      }else{
	    	  if(obj=="man"){
	    		  if(manufacturers.length>4){
		    		  alertWarning();
		    	  }else{
		    		  $(this).addClass("mutiManCheck");
		    		  manufacturers[manufacturers.length]=$(this).attr("id");
		    		  manMutiName[manMutiName.length]=$(this).html();
		    	  }
	    	  }else  if(obj=="qua"){
	    		  if(qualitys.length>4){
		    		  alertWarning();
		    	  }else{
		    		  $(this).addClass("mutiManCheck");
		    		  qualitys[qualitys.length]=$(this).attr("id");
		    		  qualityName[qualityName.length]=$(this).html();
		    	  }
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
	$('#collapse_'+obj).collapse('hide');
	$('#confirmPanel_'+obj).hide();
	$(".mutiMan").removeClass("mutiManCheck");
	$('#panel_'+obj).removeClass("choice-border")
	$('#mutiButton_'+obj).show();
	if(obj=="man"){
		manufacturers = new Array();
		manMutiName=new Array();
	}else if(obj=="qua"){
		qualityName = new Array();
		qualitys = new Array();
	}
	manBindOP(obj);
}
function manMutiOk(obj){
	$(".mutiMan").removeClass("mutiManCheck");
	$('#collapse_'+obj).collapse('hide');
	$('#confirmPanel_'+obj).hide();
	$('#panel_'+obj).removeClass("choice-border")
	$("#panel_"+obj).hide();
	var mutiNmae ="";
	if(obj=="man"){
		for(var i=0;i<manMutiName.length;i++){
			mutiNmae +=manMutiName[i]+'&nbsp;';
		}
	}else if(obj=="qua"){
		for(var i=0;i<qualityName.length;i++){
			mutiNmae +=qualityName[i]+'&nbsp;';
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
		var tbBody ='<div class="row">';
		$.each(cateList, function(i, p) {
			if(i==6){
				tbBody += '<div id="collapse_Cat" class="panel-collapse collapse " role="tabpanel" aria-labelledby="collapse_Cat" >';
				$("#catMore").html( '<a role="button" data-toggle="collapse" href="#collapse_Cat" aria-expanded="true" aria-controls="collapse_Cat">'+more+'</a>');
			}
			tbBody +=  '<div class="col-sm-2 col-md-2 col-lg-2 choice-panel-span" ><span class="choice-panel-span"><a href="javascript:void(0);" onclick="addFilter('+p.id+',&apos;'+p.name+'&apos;)">'+p.name;
			tbBody +=	'</span>&nbsp;<span class="countItems">('+p.productCount+')</span></a></div> ';
		});
		if(cateList.length>5){
			tbBody +='</div>';
		}
		tbBody +='</div >';
		$("#subCatePanel").html(tbBody);
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
}


function writeManCate(cateList,obj){
	if(cateList !=null && cateList.length>0){
		var tbBody ='<div class="row">';
		$.each(cateList, function(i, p) {
			if(i==6){
				tbBody += '<div id="collapse_Man" class="panel-collapse collapse " role="tabpanel" aria-labelledby="headingOne">';
				$("#manMore").html( '<a role="button" data-toggle="collapse" href="#collapse_Man" aria-expanded="true" aria-controls="collapse_man" class="pull-right">'+more+'<span class="glyphicon glyphicon-menu-down" aria-hidden="true"/></a>');
			}
			tbBody +=  '<div class="col-sm-2 col-md-2 col-lg-2 choice-panel-span" ><span class=" mutiMan '+obj+' choice-panel-span" id="'+p.id+'">'+p.manName+'</span>';
			if(p.manCount != null && p.manCount>0) {
				tbBody += '&nbsp;<span class="countItems">('+p.manCount+')</span>';
			}
			tbBody += '</div>';
		});
		if(cateList.length>5){
			tbBody +='</div>';
			// $('.collapse').on('show.bs.collapse', function () {
		      //   alert('cccc');});
		}
		tbBody +='</div ><div class="row" style="padding-top:15px;text-align:center;display: none;" id="confirmPanel_'+obj+'"><button type="button" id="okbutton_'+obj+'" class="btn btn-danger"  disabled="disabled" onclick="manMutiOk(&apos;man&apos;)">'+ok;
		tbBody +='</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" class="btn btn-info" onclick="manMutiCancel(&apos;man&apos;)">'+cancel+'</button></div>';
		$("#manCatePanel").html(tbBody);
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
function getPrice(){
	if(products!=null&&products.length>0) {
		$.ajax({
			type: 'POST',
			dataType: "json",
			url: getContextPath()+'/services/public/products/price.html',
			data: 'prodcutId='+products,
			success: function(priceList) {
				if(priceList != null){
					$.each(priceList, function(i, p) {
						if(p.prices !=null){
							var priceHtml='';
							for(var j=0;j<p.prices.length&&j<3;j++){
								priceHtml += '<div class="container-fluid" style="margin:2px 0px;border-bottom:1px dashed #ddd"><div class="row">';
								
								priceHtml += '<div class="col-xs-4" style="padding-left:0px!important">'+ p.prices[j].code;
								priceHtml += '</div>';
								
								if (p.prices[j].special != null && p.prices[j].special !=''){
									priceHtml += '<div class="col-xs-3" style="padding-left:0px!important;color:#FF6C00"><s>'+ p.prices[j].price + '</s>';
									priceHtml += '</div>';
									priceHtml += '<div class="col-xs-3" style="padding-left:0px!important;color:#FF6C00">'+ p.prices[j].special;
									priceHtml += '</div>';
								} else {
									if (p.prices[j].priceValue != null && p.prices[j].priceValue == '0') {
										priceHtml += '<div class="col-xs-3" style="padding-left:0px!important;color:blue">询价';
										priceHtml += '</div>';
										priceHtml += '<div class="col-xs-3">';
										priceHtml += '</div>';
									} else {
										priceHtml += '<div class="col-xs-3" style="padding-left:0px!important;color:#FF6C00">'+ p.prices[j].price;
										priceHtml += '</div>';
										priceHtml += '<div class="col-xs-3">';
										priceHtml += '</div>';
									}
								}
								
								
								if (p.prices[j].priceValue != null && p.prices[j].priceValue == '0') {
									priceHtml += '<div class="col-xs-2" style="padding-left:0px!important;color:#aaa;font-size: 1.7em;">';
									priceHtml += '<span class="fa-shopping-cart fa" aria-hidden="true"></span>';
								} else {
									priceHtml += '<div class="col-xs-2" style="padding-left:0px!important;color:#449d44;cursor:pointer;font-size: 1.7em;">';
									priceHtml += '<span class="fa-shopping-cart fa" aria-hidden="true" onclick="addToCart('+p.id+','+p.prices[j].id+')"></span>';
								}
								priceHtml += '</div>';
								
								priceHtml += '</div></div>';
								
							}	
							if(p.prices.length>3){
								priceHtml +='<div class="container-fluid" style="margin:2px 0px;border-bottom:1px dashed #ddd"><div class="row" style="color:#449d44;">更多...</div></div>';
							}
							$("#price_"+p.id).html(priceHtml);
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
function log(value) {
}



