function writePaging(paginationData,second){
	$("#pagination").html("");
	if(paginationData !=null){
		var pages = '<ul class="pagination">';
		 pages += '';
		 if(paginationData.startPages !=1){
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+1+')">首页</a></li>';
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+(paginationData.currentPage-1)+')">上一页</a></li>';
		 }
		 for(var i =paginationData.startPages;i<=paginationData.showPages;i++){
			 if(paginationData.currentPage == i){
				 pages += '<li class="active"><a href="javascript:void(0);" class="disabled">'+i+'</a></li>';
			 }else{
				 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+i+')">'+i+'</a></li>';
			 }
		 }
		 if(paginationData.startPages+4 <=paginationData.totalPages){
			 
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+(paginationData.currentPage+1)+')">下一页</a></li>';
			 pages += '<li ><a href="javascript:void(0);" onclick="doAction('+paginationData.totalPages+')">末页</a></li>';
		 }
		 pages += '</ul>';
	}
	 if(second !=null){
		 $("#pagination1").html(pages); 
	 }
	 $("#pagination").html(pages);
}