function writePaging(paginationData,second){
	writePagingDiv(paginationData,second,"pagination","doAction");
}


function writePagingDiv(paginationData,second, divId, actionName){
	divId="#"+divId;
	$(divId).html("");
	if(paginationData !=null){
		var pages = '<ul class="pagination">';
		 pages += '';
		 if(paginationData.startPages !=1){
			 pages += '<li ><a href="javascript:void(0);" onclick="'+actionName+'('+1+')">首页</a></li>';
			 pages += '<li ><a href="javascript:void(0);" onclick="'+actionName+'('+(paginationData.currentPage-1)+')">上一页</a></li>';
		 }
		 for(var i =paginationData.startPages;i<=paginationData.showPages;i++){
			 if(paginationData.currentPage == i){
				 pages += '<li class="active"><a href="javascript:void(0);" class="disabled">'+i+'</a></li>';
			 }else{
				 pages += '<li ><a href="javascript:void(0);" onclick="'+actionName+'('+i+')">'+i+'</a></li>';
			 }
		 }
		 if(paginationData.startPages+4 <=paginationData.totalPages){
			 
			 pages += '<li ><a href="javascript:void(0);" onclick="'+actionName+'('+(paginationData.currentPage+1)+')">下一页</a></li>';
			 pages += '<li ><a href="javascript:void(0);" onclick="'+actionName+'('+paginationData.totalPages+')">末页</a></li>';
		 }
		 pages += '</ul>';
	}
	 if(second !=null){
		 $(divId+"1").html(pages); 
	 }
	 $(divId).html(pages);
}