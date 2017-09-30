var menul=new Array();

function getNavMenu(url){
	
	$.ajax({  
		 type: 'post',
		 cache:false,
		 url: url + 'navmenu.html',  
		 error: function(e) { 
			// console.log('error ' + e);
		 },
		 success: function(navmenu) {
			 if(navmenu!=null && navmenu.length>0) {
				 createMenu(navmenu);
			 } 
		} 
	});
}
function createMenu(ms){
	$("#navMenu").html("");
	// 下面这个 return 是为了满足百度推广增加的。正式产品没有这句
	//恢复了，取消return： 
	return;
	var ultxt='<ul class="resp-tabs-list">';
	var pltxr='<div class="resp-tabs-container">';
	$.each(ms, function(i, m) {
		ultxt +='<li><a href="'+getContextPath()+'/shop/category/category/'+m.categoryID+'.html"  ><img  style="padding-right:15px;"  src="'+getContextPath()+'/resources/img/nav_'+i+'.png" >'+ m.categoryName+'</a></li>';
		pltxr +='<div class="row " >';
		if(m.categorys !=null && m.categorys.length>=0){
			
			$.each(m.categorys, function(k, s){
				if(s.categorys !=null && s.categorys.length>0){					
	    			   if(k!=0){
	    				   pltxr += '<div class="col-sm-3 col-md-2" style="padding-top:20px;"><a href="'+getContextPath()+'/shop/category/category/'+s.categoryID+'.html"><img style="padding-left:5px;" src="'+getContextPath()+'/resources/img/iconfont-damuzhi.png" ><strong style="padding-left:15px;">'+s.categoryName+'</strong></a></div>';
	    				   pltxr += '<div class="col-sm-9 col-md-10" style="padding-top:20px;"><div class="row" > ';
	    			   }else{
	    				   pltxr += '<div class="col-sm-3 col-md-2"><a href="'+getContextPath()+'/shop/category/category/'+s.categoryID+'.html"><img style="padding-left:5px;" src="'+getContextPath()+'/resources/img/iconfont-damuzhi.png" ><strong style="padding-left:15px;">'+s.categoryName+'</strong></a></div>';
	    				   pltxr += '<div class="col-sm-9 col-md-10" ><div class="row" > ';
	    			   }
	    			  
	    			   $.each(s.categorys, function(j, t){    				   
	    				   pltxr += '<div class="col-sm-4 col-md-3"';
	    				   //if(j<3){
	    					 //  pltxr +='';
	    				   //}
	    				   pltxr +='><a href="' +getContextPath()+'/shop/category/category/'+t.categoryID+'.html" role="button" data-toggle="modal">'+t.categoryName+'</a></div>';
	    			   });
	    			   pltxr +='</div></div>';
				}else{
					pltxr += '<div class="col-sm-12 col-md-12" style="padding-top:20px;"><a href="'+getContextPath()+'/shop/category/category/'+s.categoryID+'.html"><img style="padding-left:5px;" src="'+getContextPath()+'/resources/img/iconfont-damuzhi.png" ><strong style="padding-left:15px;">'+s.categoryName+'</strong></a></div>';
				}
			});
		}
		pltxr +='</div>';
	});
	
	ultxt +='</ul>';
	pltxr +='</div>'; 
	pltxr = '<div id="navTabs">'+ultxt+pltxr+'</div>';
	$("#navMenu").append(pltxr);
	var browser_width = $(document.body).width();
	var left = $('#nav-image').position().left;
	browser_width = browser_width -2*left;
	$("#navMenu").width(browser_width);
	$('#navTabs').easyResponsiveTabs({
	    type: 'vertical',
	    width: 'auto',
	    fit: true
	});
}

$(window).resize(function(){
		var browser_width = $(document.body).width();
		var left = $('#nav-image').position().left;
		browser_width = browser_width -2*left;
		$("#navMenu").width(browser_width);
	});
/**
function createMenu(ms,url){
	$("#topmenu").html("");
	var _w = 143;
	var _left = 0;
	$.each(ms, function(i, m) {
		_left = _w*i;
        var menus = "";
        var menuLength=0;
       if(m.categorys ==null || m.categorys.length==0){
    	   menus += '<li class="dropdown" "><a class="topa" href="'+getContextPath()+'/shop/category/category/'+m.categoryID+'.html" role="button" >'+ m.categoryName+'</a></li>';
       }else {    	   
    	   menus += '<li class="dropdown" style="z-index:999;"><a class="topa dropdown-toggle "  href="'+ getContextPath()+'/shop/category/category/'+m.categoryID+'.html" role="button" ';
    	   menus += ' data-toggle="dropdown" onclick="javascript:window.location.href=&apos;'+getContextPath()+'/shop/category/category/'+m.categoryID+'.html&apos;"> '+m.categoryName+'<b class="caret"></b></a> <div class="dropdown-menu " style="z-index:9999;box-shadow:none;border-radius:0px;border-bottom:2px solid #1abc9c;margin-left:-'+_left+'px" id="topMenu_'+m.categoryID+'">';
    	   $.each(m.categorys, function(k, s){
    		   menus += '<div class="row col-sm-12 col-md-12 col-md-offset-0 col-lg-12 col-lg-offset-0" style="padding:5px;">';
    		  
    		   if(s.categorys !=null && s.categorys.length>0){
    			   menus += '<div class="col-sm-1 col-md-2"><a href="'+getContextPath()+'/shop/category/category/'+s.categoryID+'.html"><span class="glyphicon glyphicon-thumbs-up" style="padding:5px;"></span><strong>'+s.categoryName+'</strong></a></div>';
    			   if(k!=0){
    				   menus += '<div class="col-sm-10 col-md-10"><div class="row" style="border-top:1px solid #DDDDDD;padding-top:5px;"> ';
    			   }else{
    				   menus += '<div class="col-sm-10 col-md-10"><div class="row" > ';
    			   }
    			  
    			   $.each(s.categorys, function(j, t){    				   
    				   menus += '<div class="col-sm-3 col-md-3"';
    				   if(j<3){
    					   menus +='';
    				   }
    				   menus +='><a href="' +getContextPath()+'/shop/category/category/'+t.categoryID+'.html" role="button" data-toggle="modal">'+t.categoryName+'</a></div>';
    			   });
    			   menus +='</div></div>';
    		   } else{
    			   menus += '<div class="col-sm-12 col-md-12"><a href="'+getContextPath()+'/shop/category/category/'+s.categoryID+'.html"><span class="glyphicon glyphicon-thumbs-up" style="padding:5px;"></span><strong>'+s.categoryName+'</strong></a></div>';
    		   } 
    		   menus +='</div>';
    	   });
    	   menus +='</div></li>';
       }   
       $("#topmenu").append(menus);
       var browser_width = $(document.body).width();
     
       $("#topMenu_"+m.categoryID).width(browser_width);
      
    });
}
*/
/**
 * 加载移动端的4个一级菜单
 * @returns
 */
function getMobileNavMenu(url) {
	$.ajax({  
		 type: 'post',
		 cache:false,
		 url: url + 'mobileNavMenu.html',  
		 error: function(e) { 
			// console.log('error ' + e);
		 },
		 success: function(navmenu) {
			 $("#topmenu").html('');
			 $("#topmenu").removeClass();
			 $("#topmenu").css("width","100%");
			 var menus = "";
			 menus += '<div class="container-fluid"><div class="row" style="text-align:center;margin-bottom:5px;">';
			 //menus += '<nav class="navbar navbar-primary" role="navigation" style="width:100%;border:1px solid red"><ul class="nav navbar-nav">';
			 $.each(navmenu, function(i, m){
				 //menus += '<div class="col-xs-3"><button type="button" class="btn btn-primary" style="background:#2672c8;border:1px solid #2672c8;width:100%">'+m.categoryName+'</button></div>';
				 menus += '<a href="'+getContextPath()+'/shop/category/category/'+m.categoryID+'.html"><div class="col-xs-3" style="background:#2672c8;color:#fff;padding:10px;">'+m.categoryName+'</div></a>';
				 //menus += '<li><a href="'+getContextPath()+'/shop/category/category/'+m.categoryID+'.html">'+m.categoryName+'</a></li>';
			 });
			 menus += '</div></div>';
			 //menus += '</ul></nav>';
			 $("#topmenu").append(menus);
			 
		} 
	});
}
