
function findByYear(status){	
	$("#beginDatePurchased").val(status) 
	dosubmit();
	
};
function findByName(){	
	if(findholder == $("#findbyname").val()){
		$("#findbyname").val('');
	}
	$("#findName").val($("#findbyname").val());
	dosubmit();
	
};
function dosubmit(){
	$('#ezybioForm').submit();
}

function doDelivery(oid){
	subOrderid=oid;
	$('#delivery').modal('show');
}

function getOrderData(){
	var move = false; 
	var dropFlag = false;
	var $bg = $(".droptarget"); 
	var initDiv, tarDiv,finTar, tarDivHalf = 0, wHalf = 0;  
	var initPos = {x: 0, y: 0}, relPos = {x: 0, y: 0}, temPos = {x: 0, y: 0}; 
	var dragPos = {x1: 0, x2: 0, y1: 0, y2: 0};
	var tarDivPos = {x1: 0, y1: 0, x2: 0, y2: 0};  
	$(".dropsource").each(function() { 
		$(this).mousedown(function(event) { 
			initDiv = $(this);
			tarDiv=null;
			var offset = initDiv.position();
			relPos.x = parseInt(offset.left,0)-event.pageX; 
			relPos.y = parseInt(offset.top,0)-event.pageY; 
			initDiv.parent().parent().parent().addClass("dropfoucssourcecolcor");
			move = true; 
		}); 
		$(document).mousemove(function(event) { 
			if (!move) { return false; } 
				
				initDiv.removeClass("dropsource").addClass("dropmove"); 
				
				dragPos.x1 = event.pageX + relPos.x; 
				dragPos.y1 = event.pageY + relPos.y; 
				dragPos.x2 = dragPos.x1 + initDiv.innerWidth(); 
				dragPos.y2 = dragPos.y1 + initDiv.innerHeight(); 
				initDiv.css({ left: dragPos.x1 +'px', top: dragPos.y1 + 'px',width:initDiv.innerWidth() }); 
				$bg.each(function() {
					if($(this).attr('id') != initDiv.parent().parent().attr('id')){
						tarDiv = $(this); 
						
						tarDivPos.x1 = tarDiv.parent().position().left; 
						tarDivPos.x2 = tarDivPos.x1 + tarDiv.width(); 
						//tarDivPos.y1 = tarDiv.parent().position().top; 
						//tarDivPos.y2 = tarDivPos.y1 + tarDiv.height(); 
						//tarDivHalf = tarDiv.height()/20; 
						//wHalf = tarDiv.width()/20; 
							//if (Math.abs(dragPos.x2) >= Math.abs(tarDivPos.x1) + Math.abs(wHalf) && Math.abs(dragPos.x2) <= Math.abs(tarDivPos.x2) + Math.abs(wHalf) && Math.abs(dragPos.y2) >= Math.abs(tarDivPos.y1) + Math.abs(tarDivHalf) && Math.abs(dragPos.y2) <= Math.abs(tarDivPos.y2) + Math.abs(tarDivHalf) ) { 
						if (Math.abs(dragPos.x2) >= Math.abs(tarDivPos.x1)  && Math.abs(dragPos.y2) >= Math.abs(tarDivPos.y1) ) {	
							finTar=$(this);
								//tarDiv.removeClass("droptarget").addClass("droptarget "); 
								tarDiv.parent().addClass("dropfoucscolcor");
								dropFlag = true;
							} else{
								tarDiv.parent().removeClass("dropfoucscolcor");
								dropFlag = false ;
							}
					}
							}); 
		}).mouseup (function(event) { 
			if($(this).attr('id') != initDiv.parent().parent().attr('id') && dropFlag == true){				
				
				tarDiv.parent().removeClass("dropfoucscolcor");
				initDiv.parent().parent().parent().removeClass("dropfoucssourcecolcor");
				initDiv.appendTo(finTar);
			}else{
				initDiv.parent().parent().parent().removeClass("dropfoucssourcecolcor");
			}
			dropFlag = false ;
			initDiv.removeClass("dropmove").addClass("dropsource").removeAttr("style");
			move = false; 
		}); 
	}); 
	 $('#split').modal('show');
}

function showSplit(oid){
	$('#orderlist').showLoading();
	//alert(getContextPath());
	$.ajax({  
		type: 'POST',
		  url: getContextPath()+"/admin/orders/order.html",
		  data:"orderId="+ oid,
		  success: function(data) {
			  $('#orderlist').hideLoading();
			 if(data != null && data.cartItems !=null ){
				 var targettable = document.getElementById("targettabel");
				 if(targettable.getElementsByTagName('tbody')[0] != null){
					 targettable.removeChild(targettable.getElementsByTagName('tbody')[0]);
					}
				 var table = document.getElementById("sourcetabel");
				 if(table.getElementsByTagName('tbody')[0] != null){
						table.removeChild(table.getElementsByTagName('tbody')[0]);
					}
				 
				 var tbody = document.createElement("tbody");				 
				 for (var i = 0; i < data.cartItems.length; i++) {
						var tr = tbody.insertRow(i) ;
						tr = addorder(tr,data.cartItems[i]);
						tr.className="dropsource";
				}
				 if(tbody !=null){
					 
						table.appendChild(tbody); 						
						subOrderid=oid;
						getOrderData();
				}else{
					subOrderid=null;
				}
			 }
				 
			 
		 } ,
		 error: function( textStatus, errorThrown) {
			//alert(textStatus);
			$('#orderlist').hideLoading();
		 }
		 	});
}
function doAction(){	
	 $('#page').val(cuurentPage);
	 dosubmit();
	
};
function doPage(page){
	 $('#page').val(page);
	 dosubmit();
}
function splitSubOrder(){
	 var sourcetablerow =$("#sourcetabel tbody tr");
	 var targettablerow =$("#targettabel tbody tr");
	 $('#split').modal('hide');
	 //左右两个一个为0时关闭
	 if(sourcetablerow.length==0 || targettablerow.length==0 || subOrderid ==null){
		return ;
	 }
	
	 $('#orderlist').showLoading();
	 var items = subOrderid;
	 
	 for(var i=0;i<targettablerow.length;i++){
		 items = items +"," +targettablerow[i].id;
	 }
	 $('#splitSubOrder').val(items);
	 doAction();
	 $('#splitSubOrder').val('');
	 $('#orderlist').hideLoading();
}

function deliveryOrder(_url){
	$('#delivery').modal('hide');
	$('#orderlist').showLoading();
	$.ajax({  
		type: 'POST',
		  url: _url,
		  data:"suborderid="+ subOrderid+"&dCode="+$('#searchstorename').val()+"&dNo="+$('#deliveryNo').val(),
		  success: function(response) {
			  if(response.response.status>=1) {
				  alertSuccess();
				  doAction();
				 // setStatus(response.response.statusMessage)
			  }else {
				  activeFaild();
			  }
			  $('#orderlist').hideLoading();
			 } ,
		 error: function( textStatus, errorThrown) {
			$('#orderlist').hideLoading();			 
			activeFaild();
		 }
		 	});
}
