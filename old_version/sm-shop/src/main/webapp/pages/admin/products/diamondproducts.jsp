<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>	

<script src="<c:url value="/resources/laydate/laydate.js"/>"></script>

<script src="<c:url value="/resources/js/tree/bootstrap-treeview.js" />"></script>  
<script src="<c:url value="/resources/js/diamondproducts.js" />"></script>  
<script src="<c:url value="/resources/js/pagingation.js" />"></script> 
<script src="<c:url value="/resources/js/selectcheckbox.js" />"></script> 
<script src="<c:url value="/resources/js/commonActive.js" />"></script> 
<script src="<c:url value="/resources/js/jquery.maskedinput.min.js" />"></script>
<!--[if IE]>
	<script type="text/javascript" src="/resources/js/tree/ietree.js"></script>
<![endif]-->
<script type="text/javascript">
	function writeptile(offset,countByPage,totalCount){
		$("#ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
	}
	function writediamondptile(offset,countByPage,totalCount){
		$("#diamond_ptitle").html('<s:message code="label.entitylist.paging" arguments="'+offset+';'+countByPage+';'+totalCount+'" htmlEscape="false" argumentSeparator=";" text=""/>');
	}
	$(function () {
		getDiamondProducts(getdiamondcriteria());
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
				}
			);

		}
	);
	var productURL='<c:url value="/admin/products/editProduct.html" />?id=';
	var qualitystar='<c:url value="/resources/img/stars/star-on.png" />';
	var qualityhalfstar='<c:url value="/resources/img/stars/star-half.png" />';
	var marktile='<s:message code="label.generic.mark" text="Mark" />';
	var unmarktile='<s:message code="label.generic.unmark" text="Unmark" />';
	<c:if test="${requestScope.LANGUAGE.code == 'zh'}">
	var rankName="${rankProfil.parentRank.rankChineseName}" || "普通";
	</c:if>
	<c:if test="${requestScope.LANGUAGE.code != 'zh'}">
	var rankName="${rankProfil.parentRank.rankName}" || "Junior";
	</c:if>
	
	var failTitle='<s:message code="label.product.failAddDiamond" text="Unmark" />';

	var categoryId=-1;
	var page=1;
	var diamondpage=1;
	var count=0;
	var avstatus=null;
	var findholder ='<s:message	code="label.generic.product.find.holde" text="holde" />';
	var diamond_findholder ='<s:message	code="label.generic.product.find.holde" text="holde" />';
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
		$("#auditHref").bind('click', 
			function(){
				confrom($("#edit_pid").val());
			}
		);
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

	function searchProducts(){
		$("#searchproduct").modal("show");
	}
	$(document).on('hidden.bs.modal', '.modal', function () {
		$('.modal:visible').length && $(document.body).addClass('modal-open');
	});
</script>

<jsp:include page="/common/adminTabs.jsp" />
<div class ="row" >
	<ul class="nav nav-pills pull-left form-inline">
		<li>
			<div class="add" style="width:105px; height:22px; padding-left:15px; background: url(<c:url value="/resources/img/add.png"/>) no-repeat #337ab7; background-position:90px center; -moz-border-radius: 5px;-webkit-border-radius: 5px; font:12px/22px 微软雅黑; color:#ffffff;">
				<a class="active" href="javascript:searchProducts()" style="color:white;"><s:message code="label.product.adddiamond" text="Add Diamond Product"/></a>
			</div>

			
		</li>
		<li>
			
		</li>
	</ul>

	<ul class="nav nav-pills pull-right form-inline">
		<li>
			<a href="javascript:void(0);" onclick="unmarkselected()">
				<s:message code="label.generic.unmark" text="Unmark"/><s:message code="label.product.list.selecte" text="Selected"/>
			</a>
		</li>
		<li >
			<div class="input-group" style="padding-top:3px;width:250px">      
				<input  id="diamond_findbyname" type="text" class="form-control " placeholder=<s:message code="label.generic.product.find.holde" text="holde" />>
				<a  href="javascript:void(0);" onclick="findDiamondByName()" class="input-group-addon" >
					<span class="glyphicon glyphicon-search" aria-hidden="true"  ></span>
				</a>
			</div>
		</li>

	</ul>
	<!-- diamond products list -->
	<table class="table table-striped table-bordered table-hover ">	
		<tr id="diamond_tr_tr">
			<td colspan="12">
			
			</td>
		</tr>
		<tr>
			<td>
				<table class="table table-striped table-bordered table-hover " id="CustmoersTable">	
					<thead id ="diamond_prodcutTable">											 
						<tr class="cubox">
							<th width="2%"><input type="checkbox" id="diamond_selectall"> </th>
							<th width="10%"><s:message code="label.entity.name" text="Name"/></th>
							<th width="10%"><s:message code="label.productedit.productenname" text="English Name"/></th>
							<th width="10%"><s:message code="label.product.sku" text="Sku"/></th>
							<th width="10%"><s:message code="label.productedit.code" text="Code"/></th>
							<th width="7%"><s:message code="label.bettbio.product.audit" text="Audit"/></th>
							<th width="9%"><s:message code="label.product.quality" text="Audit"/></th>
							<th width="9%"><s:message code="label.product.available" text="Available"/></th>
							<th width="9%"><s:message code="label.product.availabledate" text="Date available"/></th>
							<th width="10%"><s:message code="label.storename" text="Store"/></th>
							<th width="6%"><s:message code="label.generic.unmark" text="Unmark"/></th>
							<th width="9%"><s:message code="label.bettbio.product.create" text="Del"/></th>
						</tr>
					</thead>
					<tbody id ="diamond_prodcutTbody" >	</tbody>
				</table>
			</td>
		</tr>
	</table>
	<div class="">
		<div class="col-lg-3 padding-top-20" >
			<span class="p-title-text" id="diamond_ptitle"></span>
		</div>
		<div id="diamond_pagination" class="pull-right " ></div>
	</div>
</div>
<span style="font-size:13px;font-weight:bold;">
	<s:message code="label.product.totaldiamond" arguments="${maxDiamondNumber}" argumentSeparator="," text="Can have ${maxDiamondNumber} Diamond products"/>
</span>
		<!-- search of product -->
<div class="modal fade" id="searchproduct" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static">
	<div class="modal-dialog" role="document" style="width:95%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<span class="pull-right form-inline">&nbsp;&nbsp;&nbsp;</span>
				<ul class="nav nav-pills pull-right form-inline">
					<li>
						<a href="javascript:void(0);" onclick="markselected()">
							<s:message code="label.generic.mark" text="Mark"/><s:message code="label.product.list.selecte" text="Selected"/>
						</a>
					</li>
					<li >
						<div class="input-group" style="padding-top:3px;width:250px">      
							<input  id="findbyname" type="text" class="form-control " placeholder=<s:message code="label.generic.product.find.holde" text="holde" />>
							<a  href="javascript:void(0);" onclick="findByName()" class="input-group-addon" >
								<span class="glyphicon glyphicon-search" aria-hidden="true"  ></span>
							</a>
						</div>
					</li>

				</ul>
				<h4 class="modal-title">
					<span id="new_productlabel"><s:message code="label.generic.search" text="Search"/></span>&nbsp;&nbsp;
				</h4>

			</div>
			<div class ="row" >
				<div class="col-lg-2 col-sm-3 padding-left padding-right">
					<div id="cataTree" class="padding-left padding-right">	</div>
				</div>
				<div>
					<div class="col-lg-10 col-sm-9 padding-left " id="p-title">
						<table class="table table-striped table-bordered table-hover ">	
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
												<th width="9%"><s:message code="label.product.available" text="Available"/></th>
												<th width="9%"><s:message code="label.product.availabledate" text="Date available"/></th>
												<th width="10%"><s:message code="label.storename" text="Store"/></th>
												<th width="6%"><s:message code="label.generic.mark" text="Mark"/></th>
												<th width="9%"><s:message code="label.bettbio.product.create" text="Del"/></th>
											</tr>
										</thead>
										<tbody id ="prodcutTbody" >	</tbody>
									</table>
								</td>
							</tr>
						</table>
						<div class="">
							<div class="col-lg-3 padding-top-20" style="padding-left: 0px;padding-top: 10px;">
								<a href="javascript:void(0);" onclick="markselected()">
									<s:message code="label.generic.mark" text="Mark"/><s:message code="label.product.list.selecte" text="Selected"/>
								</a>
								<span class="p-title-text" id="ptitle" style="padding-left:30px;"></span>
							</div>

							<div id="pagination" class="pull-right " ></div>

						</div>
					</div>
				</div>
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
					
					 <div class="modal fade" id="markcfmModel" data-backdrop= 'static' tabindex="-1" role="dialog"  aria-hidden="true">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close">
										<span aria-hidden="true">×</span>
									</button>
									<h4 class="modal-title">
										<img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>
										&nbsp;<strong ><s:message code="label.generic.mark" text="Mark"/><s:message code="label.generic.confirm" text="Confirm"/>!</strong>
									</h4>
								</div>
								<div class="modal-body">
									<p><s:message code="label.entity.mark.confirm" text="Do you really want to mark the product as diamond product?"/></p>
								</div>
								<div class="modal-footer">
									<input type="hidden" id="url"/>
									<button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>
									<a href="javascript:void(0);"   onclick="" class="btn btn-success" data-dismiss="modal" id="markConfirmHref"><s:message code="button.label.ok" text="Confirm"/></a>
								</div>
							</div><!-- /.modal-content -->
						</div><!-- /.modal-dialog -->
					</div>

					 <div class="modal fade" id="unmarkcfmModel" data-backdrop= 'static' tabindex="-1" role="dialog"  aria-hidden="true">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal" aria-label="Close">
										<span aria-hidden="true">×</span>
									</button>
									<h4 class="modal-title">
										<img src="<c:url value="/resources/img/important-icon.png"/>" width="40"/>
										&nbsp;<strong ><s:message code="label.generic.unmark" text="Unmark"/><s:message code="label.generic.confirm" text="Confirm"/>!</strong>
									</h4>
								</div>
								<div class="modal-body">
									<p><s:message code="label.entity.mark.confirm" text="Do you really want to UN-MARK the product as diamond product?"/></p>
								</div>
								<div class="modal-footer">
									<input type="hidden" id="umarkurl"/>
									<button type="button" class="btn btn-default" data-dismiss="modal"><s:message code="button.label.cancel" text="Cancel"/></button>
									<a href="javascript:void(0);"   onclick="" class="btn btn-success" data-dismiss="modal" id="unmarkConfirmHref"><s:message code="button.label.ok" text="Confirm"/></a>
								</div>
							</div><!-- /.modal-content -->
						</div><!-- /.modal-dialog -->
					</div>
					