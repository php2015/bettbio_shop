$(document).ready(function() {
	navmenu.loadMenu(function(){
		
		$("#navMenu .resp-tabs-list li").hover(function(){
			var _this = $(this);
			navmenu.selectMenu(_this.index());
		})
		navmenu.selectMenu();
	})
});

var navmenu = {
	loadMenu:function(_callback){
		var _this = this;
		$.ajax({  
			 type: 'post',
			 url: ctxPath + '/navmenu',
			 dataType:"json",
			 error: function(e) { },
			 success: function(navmenu) {
				 if(navmenu.menus!=null && navmenu.menus.length>0) {
					 _this.createMenu(navmenu.menus, _callback)
				 } 
			} 
		});
	},
	createMenu:function(menus,_callback){
		var navmenu = '<div id="navTabs" class="resp-vtabs"><ul class="resp-tabs-list">{nav_items}</ul><div class="resp-tabs-container">{nav_contents}</div></div>';
		var navItems = "";
		var navContents = "";
		for(var i in menus){
			var menu = menus[i];
			navItems += '<li class="resp-tab-item"><a href="{url}"><img src="{img_url}">{menu_name}</a></li>';
			navItems = navItems.replace("{url}",ctxPath+"/category/"+menu.id+".html")
							.replace("{img_url}",ctxPath+"/static/img/nav_"+menu.id+".png")
							.replace("{menu_name}",menu.name);
			navContents += '<div class="row  resp-tab-content">{nav_content}</div>';
			var navContent = "";
			for(var j in menu.sonCategory){
				var subMenu = menu.sonCategory[j];
				navContent += '<div class="row s_nav_item_hd"><div class="col-sm-3 col-md-2"><a href="{url}"><img src="{img_url}"><strong >{menu_name}</strong></a></div><div class="col-sm-9 col-md-10">{thr_menus}</div></div>';
				
				navContent = navContent.replace("{url}",ctxPath+"/category/"+subMenu.id+".html")
								.replace("{img_url}",ctxPath+"/static/img/iconfont-damuzhi.png")
								.replace("{menu_name}",subMenu.name);
				var thrMenus = "";
				for(var k in subMenu.sonCategory){
					var thrMenu = subMenu.sonCategory[k];
					thrMenus += '<div class="col-sm-4 col-md-3"><a href="{url}">{menu_name}</a></div>'

					thrMenus = thrMenus.replace("{url}",ctxPath+"/category/"+thrMenu.id+".html")
									.replace("{menu_name}",thrMenu.name);
				}
				navContent = navContent.replace("{thr_menus}",thrMenus);
			}
			navContents = navContents.replace("{nav_content}",navContent);
		}
		$("#navMenu").append(navmenu.replace("{nav_items}",navItems)
									.replace("{nav_contents}",navContents));
		if(_callback) _callback();
	},
	selectMenu : function(i){
		i = i ? i : 0;
		$("#navMenu .resp-tabs-list li").eq(i).addClass('resp-tab-active').siblings().removeClass('resp-tab-active');
		$("#navMenu .resp-tabs-container .resp-tab-content").eq(i).addClass('resp-tab-content-active').siblings().removeClass('resp-tab-content-active')
	}
}