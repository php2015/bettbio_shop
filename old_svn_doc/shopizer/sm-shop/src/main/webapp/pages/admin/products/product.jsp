<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
 request.setCharacterEncoding("UTF-8");
%>
<%@ page session="false" %>			

<link href="<c:url value="/resources/css/bootstrap/css/bootstrap-datepicker.min.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/bootstrap/bootstrap-datepicker.min.js" />"></script>
<script src="<c:url value="/resources/js/ckeditor/ckeditor.js" />"></script>
<script src="<c:url value="/resources/js/jquery.formatCurrency-1.4.0.js" />"></script>
<script src="<c:url value="/resources/js/jquery.alphanumeric.pack.js" />"></script>
<script src="<c:url value="/resources/js/adminFunctions.js" />"></script>
<script src="<c:url value="/resources/js/tree/bootstrap-treeview.js" />"></script> 
<link href="<c:url value="/resources/css/image/dropify.css" />" rel="stylesheet"></link>
<script src="<c:url value="/resources/js/tools/dropify.js" />"></script>
<style>
.row {
	margin: 15px 0px;
}
.nodisplay {
	display:none;
}
</style>	
<script SRC="<c:url value="/resources/js/selectList.js" />"></script>	
	
<script type="text/javascript">
	var self='<s:message code="label.bettbio.selfdifine" text="Edit product" />';
	$(function(){
		$('#sku').alphanumeric();
		$('#quantity').numeric();
		$('#ordermin').numeric();
		$('#ordermax').numeric();
		$('#order').numeric();
		$('#weight').numeric({allow:"."});
		$('#width').numeric({allow:"."});
		$('#length').numeric({allow:"."});
		$('#hight').numeric({allow:"."});
		$("#cateName").bind('trigger click', function(){
			getCateTree();
		});
		//load category tree
		$.ajax({
			type: 'POST',
			dataType: "json",
			url: '<c:url value="/admin/products/pcategorys.html" />',
			success: function(productList) {
				var nodeData = new Array();
				for (var i = 0; i < productList.categorys.length; i++) {
					nodeData[i]= getCateNode(productList.categorys[i]);
				}
				if($("#cateName").val()==null || $("#cateName").val()==''){
					 $("#cateId").val(nodeData[0].id);
					 $("#cateName").val(nodeData[0].text);
				}
				
				 $('#tree').treeview({
					color: "#000",
					backColor: "#eee",
			        showBorder: true,
			        data: nodeData,
			        levels: 2,
			        enableLinks:true
				});
				 $('#tree').on("nodeSelected",function(event, data){
					 $("#cateId").val(data.id);
					 $("#cateName").val(data.text);
					 $("#tree").toggle();
				 });
				 
			},
			error: function(jqXHR,textStatus,errorThrown) { 
				//alert('Error ' + jqXHR + "-" + textStatus + "-" + errorThrown);
			}
		});
		  if(${product.product.productIsFree }==1||${product.product.productIsFree }){
			  $("#productIsFree option:first").attr("selected","true");
		  } else {
			  $("#productIsFree option:last").attr("selected","true");
		  }
		 	  
		  if('${operator}'=="new") {
			  $("#new_pid", window.parent.document).val('${product.product.id}'); //ç±äº§åä¿¡æ¯ç±æ°å¢é¡µé¢å°ç¼è¾é¡µé¢ï¼åå§åäº§åid
			  $("#producttitle",window.parent.document).val('${product.descriptions[0].name}');
			  window.parent.window.loadProductTab(1);
			 
		  }
	  getStores(null,self);
	  
	  var drEvent = $('.dropify-event').dropify();
	  
      drEvent.on('dropify.beforeClear', function(event, element){
    	  var rdel=confirm("你真的要删除 \"" + element.filename + "\"文件吗?");
    	  if(rdel == true){
    		  //if(${product.productImage} != null){
    			  removeImage($('#sp_imageId').html());
    		  //}
    	  }
          return rdel;
      });
	});
		
	function getCateTree() {
		$("#tree").toggle();
	}
	function getCateNode(nodes){
		var item = new Object();
			item.text=nodes.categoryName;
			item.id=nodes.categoryID;
			item.onclick="";
		if(nodes.categorys !=null && nodes.categorys.length>0 ){
			var sonNode = new Array();
			for(var i=0; i<nodes.categorys.length;i++){
				sonNode[i]=getCateNode(nodes.categorys[i]);
			}		
			item.nodes=sonNode;
		}
		return item;
	}	
	
	function digitalRemove(digitalID){
		if(digitalID==null || digitalID=='' || typeof(digitalID) == "undefined"){
			return ;
		}
		$("#store.error").show();
		if(confirm("该附件确认要删除吗?")){
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/products/product/removeFile.html"/>',
			  data: 'fileId=' + digitalID,
			  dataType: 'json',
			  success: function(response){
		
					var status = response.response.status;
					if(status==0 || status ==9999) {
						
						$(".alert-success").show();
						$("#digital_"+digitalID).html('<input class="input-file btn btn-primary" id="file" name="file" type="file"/>');
						
					} else {
						
						//display message
						$(".alert-error").show();
					}
		
			  
			  },
			  error: function(xhr, textStatus, errorThrown) {
			  	//alert('error ' + errorThrown);
			  }
			  
			});
		}
	}
	//群集 删除 产品 图片
	function removeImage(imageId){
		if(imageId==null || imageId=='' || typeof(imageId) == "undefined"){
			return ;
		}
			$("#store.error").show();
			$.ajax({
			  type: 'POST',
			  url: '<c:url value="/admin/products/product/removeImage.html"/>',
			  data: 'imageId=' + imageId,
			  dataType: 'json',
			  success: function(response){
		
					var status = response.response.status;
					if(status==0 || status ==9999) {
						
						$(".alert-success").show();						
						
					} else {
						
						//display message
						$(".alert-error").show();
					}
		
			  
			  },
			  error: function(xhr, textStatus, errorThrown) {
			  	//alert('error ' + errorThrown);
			  }
			  
			});
	}
	
	function countChar(textareaName,spanName,count) 
	{ 
		if (count==null) count = 200;
		if(document.getElementById(textareaName).value.length>count){
			document.getElementById(textareaName).value = document.getElementById(textareaName).value.substr(0,count);
		}
		document.getElementById(spanName).innerHTML = count - document.getElementById(textareaName).value.length; 
	}
	
</script>
<div class="tabbable">
  <div class="tab-content">
    <div class="tab-pane active" id="catalogue-section">
    <div class="sm-ui-component">
	  <div style="width:100%">
	  <c:choose>
		<c:when test="${product.product.id!=null && product.product.id>0}">
		  <script>
		    $("#productlabel", window.parent.document).html('<s:message code="label.product.edit" text="Edit product" />');
		    $("#producttitle", window.parent.document).html('<c:out value="${product.descriptions[0].name}"/>&nbsp;&nbsp;<span style="font-size:12px;color:blue">(预览)</span>');
			$("#producttitle", window.parent.document).attr("href",'<c:url value='/shop/product/${product.product.id}.html'/>');
			//$("#sp_title",window.parent.document).html('(预览)');
			//$("#sp_title",window.parent.document).attr("href",'<c:url value='/shop/product/${product.product.id}.html'/>');
			</script>
		</c:when>
		<c:otherwise>
		  <script>
		    $("#new_productlabel", window.parent.document).html('<s:message code="menu.catalogue-reagent-create" text="Create product" />');
		  </script>
		</c:otherwise>
	  </c:choose>
 	  <c:url var="productSave" value="/admin/products/save.html"/>
      <form:form method="POST" enctype="multipart/form-data" commandName="product" action="${productSave}" class="form-horizontal">
		<form:hidden path="product.id" />
		
                       <form:errors path="*" cssClass="alert alert-danger" element="div" />
                       <script type="text/javascript">
                          $(function(){
                              $(window).load(function(){
                                var temp = $("#suc").val();
                                if(temp=="success"){
                                   alert("要求成功完成!");
                                }else if($("#store.error").attr("style")=="display:none;"){
                                   alert("要求未完成!");
                                }
                              });
                          });
                       </script>
                       <input type="hidden" id="suc" value="${success}"/>
                       <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"> <s:message code="message.success" text="Request successfull"/></div>   
                       <div id="store.error" class="alert alert-danger" style="display:none;"> <s:message code="message.error" text="An error occured"/> </div>
                   
                   	<div class="row">
                   	  <div class="col-sm-4">
                   	   <label>货号(必填且最多255位)</label>
                   		<form:input cssClass="highlight form-control" id="code" path="product.code" maxlength="255"/>
                           <span class="help-inline"><form:errors path="product.code" cssClass="error" /></span>
                   	  </div>
                   	  <div class="col-sm-4">
                   	    <label><s:message code="label.productedit.categoryname" text="Category"/>(必填)</label>
                   		<input type="text" class="highlight form-control" readonly id="cateName" value="${product.product.categoryName}"/>
                   		<form:hidden path="product.categoryId" cssClass="highlight form-control" id="cateId"/>
                   		<div id="tree" class="col-sm-12" style="position:absolute;top: 54px;left:5px;z-index: 99;display:none"></div>
                   	  </div>
                   	  <div class="col-sm-4">
                   	    <label><s:message code="label.productedit.batchnum" text="batchnum"/>(最多255位)</label>
                   	    <form:input cssClass="form-control" id="batchnum" path="product.batchnum" maxlength="255" />
                        <span class="help-inline"><form:errors path="product.batchnum" cssClass="error" /></span>
                   	  </div>
                   	</div>
                   	
                   	<div class="row">
                   	  <div  class="col-sm-1">
                   	    <div style="float:left;line-height:30px;">
                   	  	  <label><s:message code="label.product.available" text="Product available"/></label>
                   	  	  <form:checkbox path="product.available" />
                   	  	</div>
                   	  </div>
                   	  <div class="col-sm-3">
                   	  	  <label><s:message code="label.product.availabledate" text="Date available"/></label>
                   		  <input id="dateAvailable" name="dateAvailable" class="form-control datepicker" value="${product.dateAvailable}" 
                   		  type="text" data-provide="datepicker"> 
                       </div>
                   	  <div class="col-sm-4">
                   	    <label><s:message code="label.productedit.cas" text="cas"/>(最多255位)</label>
                   		<form:input cssClass="form-control" id="cas" path="product.cas" maxlength="255"/>
                           <span class="help-inline"><form:errors path="product.cas" cssClass="error" /></span>
                   	  </div>
                       <div class="col-sm-4">
                             <label><s:message code="label.product.manufacturer" text="Manufacturer"/></label>
                             <div class="input-group">
	                             <input type="text" class="form-control typeahead"	name="searchstorename" id="searchstorename" readonly value="<c:out value="${manName}"/>" />
	                             <span class="input-group-addon dropdown-toggle" id="sns" data-toggle="collapse" aria-haspopup="true" onclick="setreadonly()"	href="#collapseExample" aria-expanded="false"	aria-controls="collapseExample"> 
	                             <span	class="glyphicon glyphicon-th " aria-hidden="true"></span>	</span>
	                           </div>								
	                     		  <!-- form:select cssClass="form-control" items="${manufacturers}" itemValue="id" itemLabel="descriptions[0].name"  path="product.manufacturer.id"/-->
	                     		  <form:input type="hidden" path="product.manufacturer.id" name="storename" id="storenamehidden" /> 
                          <span class="help-inline"></span>
                      </div>
                      <div class="collapse" id="collapseExample">
									<div class="well" id="storeNameList"></div>
								</div>
                   	</div>
                   	
                   	
                   	<div class="row">
                   	  <div class="col-sm-4">
                   	    <label><s:message code="label.bettbio.product.pattern" text="Contact Pattern"/></label>
                   	    <c:choose>
                   	    	<c:when test="${product.product.auditSection.audit>0 }">
                   	    	<!-- å·²å®¡æ ¸æåæ¬¡å®¡æ ¸ï¼åä¸åè®¸ä¿®æ¹åä½æ¨¡å¼ -->
                   	    		<select class="form-control" id="productIsFree" disabled="disabled" name="product.productIsFree" value="${product.product.productIsFree }">
									<option value="1"><s:message code="label.bettbio.product.free" text="Product Free"/></option>
									<option value="0"><s:message code="label.bettbio.product.charge" text="Product Charge"/></option>
								</select>  
                   	    	</c:when>
                   	    	<c:otherwise>
                   	    	    <select class="form-control" id="productIsFree" name="product.productIsFree" value="${product.product.productIsFree }">
									<option value="1"><s:message code="label.bettbio.product.free" text="Product Free"/></option>
									<option value="0"><s:message code="label.bettbio.product.charge" text="Product Charge"/></option>
								</select>
                   	    	</c:otherwise>
                   	    </c:choose>
                   	      
                   	  </div>
                   	  <div class="col-sm-8" id="chargediv">
                   	    <label><s:message code="label.bettbio.product.charge.perior" text="Charge Period"/></label>
					    <div id="chargePeriod" class="input-daterange input-group">
					    	<c:choose>
	                   	    	<c:when test="${product.product.auditSection.audit>0 }">
		                   	    	<!-- å·²å®¡æ ¸æåæ¬¡å®¡æ ¸ï¼åä¸åè®¸ä¿®æ¹åä½æ¨¡å¼ -->
		                   	    	<input type="text" class="form-control datepicker" disabled="disabled"  name="product.dateChargeBegin" value='<fmt:formatDate value="${product.product.dateChargeBegin}" pattern="yyyy-MM-dd"/>' data-provide="datepicker"/>
								    <span class="input-group-addon">to</span>
								    <input type="text" class="form-control datepicker" disabled="disabled" name="product.dateChargeEnd" value='<fmt:formatDate value="${product.product.dateChargeEnd}" pattern="yyyy-MM-dd"/>' data-provide="datepicker"/>
	                   	    	</c:when>
	                   	    	<c:otherwise>
								    <input type="text" class="form-control datepicker"  name="product.dateChargeBegin" value='<fmt:formatDate value="${product.product.dateChargeBegin}" pattern="yyyy-MM-dd"/>' data-provide="datepicker"/>
								    <span class="input-group-addon">to</span>
								    <input type="text" class="form-control datepicker"  name="product.dateChargeEnd" value='<fmt:formatDate value="${product.product.dateChargeEnd}" pattern="yyyy-MM-dd"/>' data-provide="datepicker"/>
	                   	    	</c:otherwise>
                   	    	</c:choose>
					    </div>
					    <script type="text/javascript">
                            $('.datepicker').datepicker({
                            	startDate: 0,
                            	format: "yyyy-mm-dd",
                                language: "zh-CN",
                                autoclose: true
                            });
                          </script>
                   	  </div>
                   	</div>
                   	
                   	<c:forEach items="${product.descriptions}" var="description" varStatus="counter">
                   	<div class="row">
                   	  <div class="col-sm-6">
                   	    <label><s:message code="label.productedit.productname" text="Product name"/>(必填且最多512位)</label>
                   	    <form:input maxlength="512" cssClass="form-control highlight" id="name${counter.index}" path="descriptions[${counter.index}].name"/>
                           <span class="help-inline"><form:errors path="descriptions[${counter.index}].name" cssClass="error" /></span>
                   	  </div>
                   	  <div class="col-sm-6">
                   	    <label><s:message code="label.productedit.productenname" text="Product English name"/>(最多512位)</label>
                   	    <form:input maxlength="512" cssClass="form-control" id="enName${counter.index}" path="descriptions[${counter.index}].enName" onkeyup="value=value.replace( /[\\u4E00-\\u9FA5\\uFF00-\\uFFFF]/g,'')" onafterpaste="value=value.replace(/[\\u4E00-\\u9FA5\\uFF00-\\uFFFF]/g,'')"/>
                   	  </div>
                   	</div>
                   	
                   	<div class="row">
                   	  <div class="col-sm-6">
                   	   <label><s:message code="label.productedit.productsimpdesc" text="Product simpleDesc"/></label>
                   	     <span class="help-inline col-sm-offset-1"><span><s:message code="label.productedit.inputnum" text="You can input"/></span><span id="simpNum">200</span><span><s:message code="label.productedit.inputstr" text="character"/></span>
                           <span class="help-inline col-sm-offset-1"><form:errors path="descriptions[${counter.index}].simpleDescription" cssClass="error" /></span>
                   	    <form:textarea id="simpleD" rows="3" cssClass="form-control" path="descriptions[${counter.index}].simpleDescription" onkeydown='countChar("simpleD","simpNum");' onKeyUp='countChar("simpleD","simpNum");' onblur='countChar("simpleD","simpNum");'/>
                   	  </div>
                   	  <div class="col-sm-6">
                   	    <label><s:message code="label.productedit.productstoreconddesc" text="Product storecondDescription"/></label>
                   	    <span class="help-inline col-sm-offset-1"><span><s:message code="label.productedit.inputnum" text="You can input"/></span><span id="condNum">50</span><span><s:message code="label.productedit.inputstr" text="character"/></span>
                           <span class="help-inline col-sm-offset-1"><form:errors path="descriptions[${counter.index}].storecondDescription" cssClass="error" /></span>
                    	<form:textarea id="condDescription" rows="3" cssClass="form-control" path="descriptions[${counter.index}].storecondDescription" onkeydown='countChar("condDescription","condNum", 50);' onKeyUp='countChar("condDescription","condNum", 50);' onblur='countChar("condDescription","condNum", 500);'/>
                           
                   	  </div>
                   	</div>
                   	
                   	<div class="row">
                   	  <div class="col-sm-12">
                   	    <label><s:message code="label.productedit.productdesc" text="Product description"/></label>
                       	<textarea cols="30" id="descriptions${counter.index}.description" name="descriptions[${counter.index}].description">
                 			  <c:out value="${product.descriptions[counter.index].description}"/>
                 			</textarea>
                 			<script type="text/javascript">
				//<![CDATA[

					CKEDITOR.replace('descriptions[${counter.index}].description',
					{
						skin : 'office2003',
						toolbar : 
						[
							['Source','-','Save','NewPage','Preview'], 
							['Cut','Copy','Paste','PasteText','-','Print'], 
							['Undo','Redo','-','Find','-','SelectAll','RemoveFormat'], '/', 
							['Bold','Italic','Underline','Strike','-','Subscript','Superscript'], 
							['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'], 
							['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'], 
							['Link','Unlink','Anchor'], 
							['Image','Flash','Table','HorizontalRule','SpecialChar','PageBreak'], '/', 
							['Styles','Format','Font','FontSize'], ['TextColor','BGColor'], 
							['Maximize', 'ShowBlocks'] 
						],
						
						filebrowserWindowWidth : '720',
      						filebrowserWindowHeight : '740',
						filebrowserImageBrowseUrl :    '<c:url value="/admin/content/fileBrowser.html"/>'
					});

				//]]>
			</script>
                   	  </div>
                   	</div>
                   	<form:hidden path="descriptions[${counter.index}].language.id" />
                    <form:hidden path="descriptions[${counter.index}].language.code" />
					<form:hidden path="descriptions[${counter.index}].id" />
					<form:hidden path="descriptions[${counter.index}].metatagKeywords" />
					<form:hidden path="descriptions[${counter.index}].productExternalDl" />
                   	</c:forEach>

               <form:hidden path="availability.region" />
               <form:hidden path="availability.id" />
              
               <form:hidden path="product.sku" />
             <div class="row">
                   	  <div class="col-sm-12">
                   	    <label>
                   	      <s:message code="label.product.image" text="Image"/>(上传图片最大为2M且都为png、jpg、jpeg、gif、bmp、pdf格式)&nbsp;
                   	    </label>
                   	    <div class="controls" id="imageControl">
                              <c:forEach var="images_search" items="${product.imagesName}">
                             	<div  style="margin-left:-14px" class="col-sm-2 pull-left">
                             	  <span id="sp_imageId" hidden="hidden">${images_search.id}</span>
                             	  <input type="file" name="image" id="image"   
                             	     class="dropify-event" data-default-file="<sm:productImage imageName="${images_search.productImage}" 
                             	     product="${product.product}"/>"/>
                             	</div>
                              </c:forEach>
                               <c:if test="${product.imagesLength<5}">
                               		<c:forEach begin="1" end="${5-product.imagesLength}" step="1">
		                                <div class="col-sm-2 pull-left" style="margin-left:-14px">
			                               <input type="file" name="image" id="image"  class="dropify-event">
			                            </div>
			                           </c:forEach>
                               </c:if>                               
                   		</div>
                   		</div>
                    <div class="row">
                     <div class="col-sm-12" style=""></div>
                    </div>
                   		<span style="font-weight: bold;margin-left:14px">附件</span>
                   		<div class="controls" id="fileControl">
	                   		<c:forEach var="digitalProduct" items="${digitalProduct}">
	                   			 <div class="col-sm-3"  id="digital_${digitalProduct.id}"> <a id="fileLink" href="<sm:adminProductDownload digitalProduct="${digitalProduct}" />">${digitalProduct.productFileName}</a>
	                   			 <a href="javascript:void(0);"  onclick="digitalRemove(${digitalProduct.id})">删除附件</a></div>
	                   		</c:forEach>
	                   		<c:if test="${size<3}">
	                   			<c:forEach  begin="1" end="${3-size}" step="1">
	                   			  <div class="col-sm-3"><input class="input-file btn btn-primary" id="file" name="file" type="file"/></div>
	                   			</c:forEach>
	                   		</c:if>
							
					   </div>
					   <div class="row">
					     <div class="col-sm-6"></div>
					   </div>
					   <div class="row">
                   		 <div class="col-sm-10" style="font-style:italic;color:#aaa;margin-left:-5px;"><a href='<c:url value="/shop/software/list.html"/>' target="blank"><s:message code="label.common.software.tip" text="Common Software Download"/></a></div>
                   		</div>
                   	</div>

	    	<div class="row">
		  <div class="col-sm-12">
		  <div class="pull-right">
                 <button type="submit" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
              </div>
             </div>
		</div>
              
                   </form:form>
                   
              <c:if test="${product.product.id!=null && product.product.id>0}">      
               <c:url var="createSimilar" value="/admin/products/product/duplicate.html"/>
               <form:form method="POST" enctype="multipart/form-data" commandName="product" action="${createSimilar}">
					<input type="hidden" name="productId" value="${product.product.id}" />
                    <div class="form-actions">
                        <div class="pull-right" style="padding:0px 15px">
                          <button type="submit" class="btn btn-info"><s:message code="label.product.createsimilar" text="Create similar product"/></button>
                        </div>
               		</div>

                </form:form>
               </c:if>
               </div>
 			</div>
		  </div>
 	</div>
</div>