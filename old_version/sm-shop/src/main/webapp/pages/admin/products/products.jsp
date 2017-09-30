<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>	
			
<script src="<c:url value="/resources/laydate/laydate.js"/>"></script>

<script src="<c:url value="/resources/js/tree/bootstrap-treeview.js" />"></script>  
<script src="<c:url value="/resources/js/products.js" />"></script>  
<script src="<c:url value="/resources/js/pagingation.js" />"></script> 
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
 <!--[if IE]>
		<script type="text/javascript" src="/resources/js/tree/ietree.js"></script>
	<![endif]-->
<script type="text/javascript">
$(function () {
	getTreeData('<s:message code="label.category.root" text="Root" />');
	$("#p-title").hide();	
	var $check = $("#availableall"),	el;

	$check
	.data('checked', 0)
	.click(function(e) {

	  el = $(this);

	  switch (el.data('checked')) {
	    case 0:
	      el.data('checked', 1);
	      el.prop('indeterminate', true);
	      getbystatus(null);
	      break;

	      // indeterminate, going checked
	    case 1:
	      el.data('checked', 2);
	      el.prop('indeterminate', false);
	      el.prop('checked', true);
	      getbystatus(true);
	      break;

	      // checked, going unchecked
	    default:
	      el.data('checked', 0);
	      el.prop('indeterminate', false);
	      el.prop('checked', false);
	      getbystatus(false);
	  }	   
	});
	
});
var productURL='<c:url value="/admin/products/editProduct.html" />?id=';
var qualitystar='<c:url value="/resources/img/stars/star-on.png" />';
var qualityhalfstar='<c:url value="/resources/img/stars/star-half.png" />';
var deltile='<s:message code="label.generic.remove" text="Del" />';
function writeptile(offset,countByPage,totalCount){
	$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
}
var categoryId=-1;
var page=1;
var count=0;
var avstatus=null;
var findholder ='<s:message	code="label.generic.product.find.holde" text="holde" />';
function getAudit(au){
	switch (au){
		case -2:return '<s:message code="label.bettbio.product.failed.audit" text="Failed audit" />';
		case -1:return '<s:message code="label.bettbio.product.fail.audit" text="Failed audit" />';
		case 1: return '<s:message code="label.bettbio.product.after.audit" text="Audited" />';
		default : return '<s:message code="label.bettbio.product.pre.audit" text="Wait audit" />';
	}
		
}

function editProduct(id,audit,isService) {
	$("#edit_pid").val(id);
	$("#edit_audit").val(audit);
	$("#editproduct").modal("show");
	$('#myTab a:first').tab("show");
	var eaudit=$("#edit_audit").val();
	
	$("#editAudit").css("display","block");
	$("#editAudit").html(' 	<a href="javascript:void(0);"  onclick=""  aria-controls="new_audit" role="tab" data-toggle="tab" id="auditHref"><s:message code="button.label.submit2" text="Submit" /><s:message code="menu.audit" text="Audit" /></a>');
	$("#auditHref").bind('click', function(){
		confrom($("#edit_pid").val());
	});
	if(isService==3){
		$("#thirdproofs").css('display','none');
		$("#selfproofs").css('display','');
		$("#certificates").css('display','');
		$("#prices").css('display','none');
		$("#pricesli").hide();		
		$("#certsli").show();
		$("#thirdli").hide();
		$("#selfsli").show();
		//$("#proofsa").html('<s:message code="label.service.proofs" text="Product proofs" />');
	}else if(isService==1){
		$("#prices").css('display','');
		$("#thirdproofs").css('display','');
		$("#certificates").css('display','');
		$("#selfproofs").css('display',''); 
		$("#certsli").show();
		$("#selfsli").show();
		$("#thirdli").show();
		$("#pricesli").show();		
		//$("#proofsa").html('<s:message code="label.product.proofs" text="Product proofs" />');
	}else{
		$("#prices").css('display','');
		$("#thirdproofs").css('display','');
		$("#certificates").css('display','none');
		$("#selfproofs").css('display','none');
		$("#thirdli").show();
		$("#certsli").hide();
		$("#selfsli").hide();
		$("#pricesli").show();		
		//$("#proofsa").html('<s:message code="label.product.proofs" text="Product proofs" />');
	}
	
	$("#details_iframe").attr("src",'<c:url value="/admin/products/editProduct.html" />?id='+id);
}

</script>
 					
<jsp:include page="/common/adminTabs.jsp" />
<div class ="row" >
    <div class="col-lg-2 col-sm-3 padding-left padding-right">
	      <div id="cataTree" class="padding-left padding-right">
	      </div>
     </div>
     <div >
	  	 <div class="col-lg-10 col-sm-9 padding-left " id="p-title">
	  	 <ul class="nav nav-pills pull-right form-inline">
	  	 		<li><a id="deleteDate" href="javascript:void(0);">根据商品创建时间单个或多个删除商品</a></li>
	  	 		<li><a href="javascript:void(0);" onclick="deleteall()"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.all" text="All"/></a></li>
	  	 		<li><a href="javascript:void(0);" onclick="deleteps()"><s:message code="label.generic.remove" text="Delete"/><s:message code="label.product.list.selecte" text="Selected"/></a></li>
				 <li >
				     <div class="input-group" style="padding-top:3px;width:250px">      
					      <input  id="findbyname" type="text" class="form-control " placeholder=<s:message	code="label.generic.product.find.holde" text="holde" />>
					      <a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" ><span class="glyphicon glyphicon-search" aria-hidden="true"  ></span></a>
				    </div>
				</li>
				
			</ul>
			<script type="text/javascript">
			   var start = {
					    elem: '#beginTime',
					    format: 'YYYY/MM/DD hh:mm:ss',
					    istime: true,
					    istoday: false,
					    festival: true, //是否显示节日
				        min: '1900-01-01 00:00:00', //最小日期
				        max: '2099-12-31 23:59:59', //最大日期
				        start: '2014-6-15 23:00:00',    //开始日期
				        fixed: false, //是否固定在可视区域
				        zIndex: 99999999, //css z-index
					    choose: function(datas){
					         end.min = datas; //开始日选好后，重置结束日的最小日期
					         end.start = datas //将结束日的初始值设定为开始日
					    }
					};
					var begin = {
					    elem: '#start',
					    format: 'YYYY/MM/DD hh:mm:ss',
					    istime: true,
					    istoday: false,
					    festival: true, //是否显示节日
				        min: '1900-01-01 00:00:00', //最小日期
				        max: '2099-12-31 23:59:59', //最大日期
				        start: '2014-6-15 23:00:00',    //开始日期
				        fixed: false, //是否固定在可视区域
				        zIndex: 99999999, //css z-index
					    choose: function(datas){
					         end.min = datas; //开始日选好后，重置结束日的最小日期
					         end.start = datas //将结束日的初始值设定为开始日
					    }
					};
					var end = {
					    elem: '#endTime',
					    format: 'YYYY/MM/DD hh:mm:ss',
					    istime: true,
					    istoday: false,
					    festival: true, //是否显示节日
				        min: '1900-01-01 00:00:00', //最小日期
				        max: '2099-12-31 23:59:59', //最大日期
				        start: '2014-6-15 23:00:00',    //开始日期
				        fixed: false, //是否固定在可视区域
				        zIndex: 99999999, //css z-index
					    choose: function(datas){
					        start.max = datas; //结束日选好后，重置开始日的最大日期
					    }
					};
					laydate(start);
					laydate(begin);
					laydate(end);
			</script>
			<script type="text/javascript">
			   $(function(){
			      $(window).load(function(){
			          $('#tr_tr').hide();
			      });
			      //点击事件
			      var index = 0;
			      $("#deleteDate").click(function(){
			          if(index == 0)
			          {
				          $("#tr_tr").show();
				          $("#manay_tr").hide();
				          index = index+1;
			          }
			          else
			          {
			              $("#tr_tr").hide();
				          index = index-1;
			          }
			      });
			      var i = 0;
			      $("#onlyOne").click(function(){
			          if(i == 0)
			          {
				          $("#one_tr").show();
				           $("#manay_tr").hide();
				          i = i+1;
			          }
			          else
			          {
			              $("#one_tr").hide();
				          i = i-1;
			          }
			      });
			      var j = 0;
			      $("#ManayProduct").click(function(){
			          if(j == 0)
			          {
			              $("#one_tr").hide();
				          $("#manay_tr").show();
				          j = j+1;
			          }
			          else
			          {
			              $("#manay_tr").hide();
				          j = j-1;
			          }
			      });
			      //ajax单条删除产品事件
			      
			      $("#delBut").click(function(){
			         var dateBeginStr = $("#beginTime").val();
				      if(dateBeginStr == '')
				      { 
				           alert("请输入要删除商品的创建时间!");
				      }
				      else
				      {
			         
			             $.ajax({
			                url:'<c:url value="/admin/products/deletePro.html"/>',
			                type:"post",
			                data:
			                {
			                    beginTime:""+dateBeginStr+"",
			                    endTime:""   
			                }
			                ,
			                dataType:"post",
			                cache:true,
			                async:true,
			                dataType: 'json',
			                success:function(data){
			                   if(data.response.status == 1 )
			                   {
			                       alert("该商品删除失败!");
			                   }
			                   else
			                   {
			                       alert("该商品删除成功!");
			                       window.location.href=""+window.location+"";
			                       $("#tr_tr").show();
				                   $("#manay_tr").hide();
			                   } 
			                }
			                
			             
			             });   
				         
			         }
			      });
			      
			      //重置事件
			      $("#resetBut").click(function(){
			           $("#beginTime").val("");
			      });
			      
			      //时间段删除商品
			      $("#manayBut").click(function(){
			      	 var dateEndStr = $("#endTime").val();
			      	 var dateBeginStr = $("#start").val();
			         if(dateBeginStr == '' || dateEndStr == '')
			         {
			             alert("请输入要删出的商品的起止时间!");
			         }
			         else
			         {
			           
			           $.ajax({
			                url:'<c:url value="/admin/products/deletePro.html"/>',
			                type:"post",
			                data:
			                {
			                 beginTime:dateBeginStr,
			                 endTime:dateEndStr
			                 
			                },
			                cache:true,
			                async:true,
			                dataType:'json',
			                success:function(data){
			                   if(data.response.status == 1 )
			                   {
			                       alert("多个商品删除失败!");
			                   }
			                   else
			                   {
			                       alert("多个商品删除成功!");
			                       window.location.href=""+window.location+"";
			                       $("#tr_tr").show();
				                   $("#manay_tr").hide();
			                   } 
			                }
			             
			             });   
			         }
			      
			      });
			      //重置事件
			      $("#resetBut2").click(function(){
			           $("#beginTime").val("");
			           $("#endTime").val("");
			      });
			      
			   });
		</script>
		<table class="table table-striped table-bordered table-hover ">	
		  <tr id="tr_tr">
		   <td colspan="12">
			<table id="table_delete" border="1" class="table table-striped table-bordered table-hover" style="width: 100%;height: auto;">
			   <tr>
			     <td colspan="5" style="vertical-align: middle;"> 
			        <a href="javascript:void(0);" id="onlyOne">
			           <span style="font-size:14px;font-weight:bold;">单个产品删除</span>
			        </a>
			     </td>
			   </tr>
			   <tr id="one_tr">
			     <td style="vertical-align: middle;width: 15%">
			          <span style="font-size:13px;font-weight:bold;">单个商品创建时间:</span>
			     </td>
			     <td>
			         <input id="beginTime" name="beginTime" type="text" class="laydate-icon form-control"  onclick="laydate(start)"/>
			     </td>
			     <td>
			        <button class="btn btn-default" id="delBut" >单个删除</button>
			        <button class="btn btn-default"  id="resetBut" style="width: 25%">重置</button>
			     </td>
			   </tr>
			   <tr>
			     <td colspan="5">
			        <a href="javascript:void(0);" id="ManayProduct">
			           <span style="font-size:14px;font-weight:bold;">按生产时间批量删除产品</span>
			        </a>
			     </td>
			   </tr>
			   <tr id="manay_tr">
			     <td style="width: 16%;vertical-align: middle;">
			       <span style="font-size:13px;font-weight:bold;">按商品生产时间批量删除:</span>
			     </td>
			     <td>
			          <input id="start" name="beginTime" type="text" class="laydate-icon form-control"  onclick="laydate(begin)"/>
			     </td>
			     <td style="width:2%;vertical-align: middle;">
			       <span>to</span>
			     </td>
			     <td>
			        <input id="endTime" name="endTime" type="text" class="laydate-icon form-control"  onclick="laydate(end)"/>
			     </td>
			     <td>
			        <button class="btn btn-default" id="manayBut">多个删除</button>
			        <button class="btn btn-default"  id="resetBut2" style="width: 30%">重置</button>
			     </td>
			   </tr>
			</table>
	   	    </td>
	   	    </tr>
	   	    <tr>
	   	     <td>
	   	    <table class="table table-striped table-bordered table-hover " id="CustmoersTable">	
				<thead id ="prodcutTable">											 
					<tr class="cubox">
						<th width="2%"><input type="checkbox" id="selectall"> </th>
						<th width="10%"><s:message code="label.entity.name" text="Name"/></th>
						<th width="10%"><s:message code="label.productedit.productenname" text="English Name"/></th>
						<th width="10%"><s:message code="label.product.sku" text="Sku"/></th>
						<th width="10%"><s:message code="label.productedit.code" text="Code"/></th>
						<th width="7%"><s:message code="label.bettbio.product.audit" text="Audit"/></th>
						<th width="9%"><s:message code="label.product.quality" text="Audit"/></th>
						<th width="9%"><s:message code="label.product.available" text="Available"/><div class="pull-right"><input type="checkbox" id="availableall"></div></th>
						<th width="9%"><s:message code="label.product.availabledate" text="Date available"/></th>
						<th width="10%"><s:message code="label.storename" text="Store"/></th>
						<th width="6%"><s:message code="label.generic.remove" text="Del"/></th>
						<th width="9%"><s:message code="label.bettbio.product.create" text="Del"/></th>
					</tr>
				</thead>
				<tbody id ="prodcutTbody" >
				</tbody>
			</table>
			</td>
			</tr>
			</table>
			<div class="">
					<div class="col-lg-3 padding-top-20" >
						<span class="p-title-text" id="ptitle"></span>
						
					</div>
					<div id="pagination" class="pull-right " ></div>
			</div>
	     </div>
     </div>
               	 
</div>
<div class="modal fade" id="showprogress" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body">  
        <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 2%;" id="precent">0% </div>  
      </div>
    </div>
  </div>
</div>
<input type="file" name="file" class="hidden" value="" />
<jsp:include page="/common/delForm.jsp"/> 

<!-- Modal -->
<div class="modal fade" id="editproduct" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static">
  <div class="modal-dialog" role="document" style="width:95%">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" id="close_window" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">
          <span id="productlabel"></span>
		  <span id="diamondFlag" class="diamond_product_flag"><s:message code="label.generic.diamondstar" text="*"/></span>&nbsp;&nbsp;
          <a target="_blank" id="producttitle" style="color:green"></a>
        </h4>
      </div>
      <div class="modal-body">
        <jsp:include page="/pages/admin/products/editproduct.jsp" />
      </div>
    </div>
  </div>
</div> 

<div class="modal fade" id="tryproduct" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static">
  <div class="modal-dialog" role="document" style="width:95%">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">
          <span id="new_productlabel"></span>&nbsp;&nbsp;
          <a target="_blank" id="producttitles" style="color:green"></a><a  target="_blank" id="sp_pro" style="font-size:14px"></a>
        </h4>
      </div>
      <div class="modal-body">
        <jsp:include page="/pages/admin/products/tryproduct.jsp" />
      </div>
    </div>
  </div>
</div> 
 	