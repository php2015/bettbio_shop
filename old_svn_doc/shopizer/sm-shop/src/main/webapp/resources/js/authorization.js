var ctx = "${pageContext.request.contextPath}";
$(document).ready(function(){
	$("#ezybioForm").css("display","none");
	$("#showpagedata").css("display","none");
});
function getlist(){
	$.ajax({
		type: 'POST',
		  url:ctx+ '/admin/store/getManList.html',
		  data: {type:1},
		 // dataType: 'json',
		  async : false,
		  success: function(data){
				alert(data);
		  },
		  error: function(xhr, textStatus, errorThrown) {
		  	alert('error ' + errorThrown);
			  alert("error11");
		  }
	});
}
//添加授权书
function saveAuth(type,idsid,idsval) {
	var com = $("#company").val();
	var merchantId = $("#merchantId").val();//商家id
	//var brand = $("#brand").val();
	var img = $("#img_url").val();
	var st = $("#startTime").val();
	var et = $("#endTime").val();
	var intro = $("#introduce").val();
	//var brand="";
	/*var objSelect = document.getElementById("brand");
    if (null != objSelect && typeof(objSelect) != "undefined") {
       for(var i=0;i<objSelect.options.length;i=i+1) {  
           if(objSelect.options[i].selected) {  
        	   brand = brand + ";" + objSelect.options[i].value;
           }
       }         
    } */
	/*while(brand.charAt(0)==';')
		brand = brand.substring(1,brand.length);
	if(brand.charAt(brand.length-1)==';')
		brand = brand.substring(0,brand.length-1);*/
	//比较时间大小
	var  stime=st.toString();
	var  etime=et.toString();
	stime =  stime.replace(/-/g,"/");
	etime =  etime.replace(/-/g,"/");
    var sdate = new Date(stime);
    var edate = new Date(etime);
	if(com=="" || com ==null){
		alert("认证单位不能为空");
		return false;
	}
	else if(idsval==""　||　idsval==null){
		alert("品牌不能为空");
		return false;
	}
	else if(st=="" || st ==null){
		alert("有效开始时间不能为空");
		return false;
	}
	else if(et=="" || et==null){
		alert("有效截止时间不能为空");
		return false;
	}
	else if(sdate>edate){
    	alert("开始时间不能大于结束时间");
    	return false;
    }
	/*else if(intro=="" || intro==null){
		alert("授权书简介不能为空");
		return false;
	}*/
	else if(img=="" || img == null){
		alert("图片不能为空");
		return false;
	}
	else
	{
		$.ajax({
			type: 'POST',
			  url:ctx+ '/admin/store/addAuth.html',
			  data: 
			  {
				  image:img,
				  idsval:idsval,
				  idsid:idsid,
				  company:com,
				  startTime:st,
				  endTime:et,
				  introduce:intro,
				  auth_type:type,
				  merchantId:merchantId
			  },
			  //dataType: 'json',
			  async : false,
			  success: function(data){
					if(data=="success"){
						alert("添加成功");
						window.location=""+window.location.href+"";
					}else if(data=="errorx"){
						alert("添加的一些或莫个品牌已存在了!");
						return false;
					}else{
						alert("添加失败了!");
						return false;
					}	
			  },
			  error: function(xhr, textStatus, errorThrown) {
				  alert("error");
			  }
		});
	}
}

function delAuth(id){
	var flag = confirm("确认删除该记录吗？");
	if(flag)
	$.ajax({
		type: 'POST',
		  url:ctx+ '/admin/store/delAuth.html',
		  data: {id:id},
		  //dataType: 'json',
		  async : false,
		  success: function(data){
				if(data=="success"){
					alert("删除成功");
					window.location=""+window.location.href+"";
				}else{
					alert("删除失败");
				}	
		  },
		  error: function(xhr, textStatus, errorThrown) {
			  alert("删除失败");
		  }
	});
}