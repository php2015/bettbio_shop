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
ul,li{float: left;list-style: none;}
</style>	
<script SRC="<c:url value="/resources/js/selectAll.js" />"></script>	
	
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
			url: '<c:url value="/admin/instrument/categorys.html" />',
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
			}
		});
	 
		 //åä½æ¨¡å¼selectæ§ä»¶çæ¾ç¤ºè½¬æ¢
		
		  //å¦ææ¯æ°å¢äº§åä¿å­æåï¼åæ¾ç¤ºå¶ä»çtabèåè¿è¡ç¼è¾
	  getStores(null,self);
	  var drEvent = $('.dropify-event').dropify();

      drEvent.on('dropify.beforeClear', function(event, element){
    	  var rdel=confirm("你真的要删除 \"" + element.filename + "\"文件吗?");
    	  if(rdel == true){
    		 removeImage($('#sp_imageId').html());
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
	 <%--  <c:choose>
		<c:when test="${product.product.id!=null && product.product.id>0}">
		  <script>
		    $("#productlabel", window.parent.document).html('<s:message code="label.others.edit" text="Edit product" />');
		   // $("#serviceTitle", window.parent.document).html('<c:out value="${product.descriptions[0].name}"/>');
		   // $("#new_pid", window.parent.document).val('${product.product.id}');
			//$("#serviceTitle", window.parent.document).attr("href",'<sm:productUrl productDescription="${product.descriptions[0]}" />');
			$("#producttitle", window.parent.document).html('<c:out value="${product.descriptions[0].name}"/>&nbsp;&nbsp;<span style="font-size:12px;color:blue">(预览)</span>');
			$("#producttitle", window.parent.document).attr("href",'<c:url value='/shop/product/${product.product.id}.html'/>');
			
			</script>
		</c:when>
		<c:otherwise>
		  <script>
		    $("#new_productlabel", window.parent.document).html('<s:message code="menu.catalogue-other-create" text="Create product" />');
		  </script>
		</c:otherwise>
	  </c:choose> --%>
 	<%--   <c:url var="authSave" value="/admin/store/addAuth.html"/> --%>
      <form:form method="POST" enctype="multipart/form-data" id="form" commandName="auth" class="form-horizontal" onsubmit="">
		<form:hidden path="id" />
		<form:hidden path="auth_type" value="2"/>
                       <form:errors path="*" cssClass="alert alert-danger" element="div" />
                       <script type="text/javascript">
                          $(function(){
                             /*  //品牌全不选
                              $("#all_chk").click(function(){
                                  if(this.checked){   
								      $("#chk_list :checkbox").attr("checked", false); 
								  }    
                              });
                              //添加授权
                              var idsval ="";
                              var idsid = "";
                              $("#chk_list :checkbox").click(function(){
                                  if(this.checked){
                                  
                                    idsid +=""+$(this).attr("tabindex")+"";
                                    idsid +=",";
                                    
                                    idsval+=""+$(this).val()+"";
                                    idsval+",";
                                  }
                              }); */
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
                        <div id="store.success" class="alert alert-success" style="<c:choose><c:when test="${success!=null}">display:block;</c:when><c:otherwise>display:none;</c:otherwise></c:choose>"><s:message code="message.success" text="Request successfull"/></div>   
                        <div id="store.error" class="alert alert-danger" style="display:none;"><s:message code="message.error" text="An error occured"/> </div>
                   	<div class="row">
                   	  <div class="col-sm-3">
                   	    <label>代理单位(必填最多255位)</label>
                   		<form:input cssClass="highlight form-control" id="company" path="company" maxlength="255"/>
                           <span class="help-inline"><form:errors path="company" cssClass="error" /></span>
                   	  </div>
                   	  <div class="col-sm-9" style="padding-top:5px">
                           <span style="font-weight:bold;"><s:message code="label.manufacturer.brand" text="Brands" />(必选,可多选)</span>
                         <%--  <li>
                           <input type="checkbox" id="all_chk"/>[全不选]&nbsp;
                          </li>
                          <c:forEach var="c" items="${manus.manus}"> 
            	            <li><input type="checkbox" value="${c.name}" tabindex="${c.id}" />&nbsp;<span>${c.name}</span>&nbsp;</li>&nbsp;&nbsp;&nbsp;
           	              </c:forEach>  --%>
           	             <table style="width:100%;height:100%;" border="0" cellpadding="0" cellspacing="0">
                   	     <tr>
				 		    <td  id="manCatePanel" style="width:900px;margin:30px 0px ;">
           	                  <div class="input-group">
				 		         <input type="hidden" name="storename" id="storenamehidden" />
	                             <input type="text" class="form-control typeahead"	name="searchstorename" id="searchstorename" readonly="readonly" />
	                             <span class="input-group-addon dropdown-toggle" id="sns" data-toggle="collapse" aria-haspopup="true" onclick="setreadonly()"	href="#collapseExample" aria-expanded="false"	aria-controls="collapseExample"> 
	                             <span	class="glyphicon glyphicon-th " aria-hidden="true"></span></span>
	                          </div>
                              <span class="help-inline"></span>
                              <div class="collapse" id="collapseExample">
								<div class="well" id="storeNameList"></div>
							  </div>
						  </td>
						  </tr>
						  </table>
                   	</div>
                   	</div>
                   	
                   <%-- <div class="row">
                   	  <form:select  items="${manus.manus}" itemValue="${manu.id}" itemLabel="${manu.name}"></form:select>
                   	  
                       <div class="col-sm-4">
                             <label><s:message code="label.product.manufacturer" text="Manufacturer"/></label>
                             <select class="form-control typeahead"> 
                   		<c:forEach var="c" items="${manus.manus}"> <option value="${c.id} }">${c.name}</option> </c:forEach> 
                   	</select>
                             <div class="input-group">
	                             <input type="text" class="form-control typeahead"	 path="brand" name="searchstorename" id="brand"  value="<c:out value="${manName}"/>" />
	                             <span class="input-group-addon dropdown-toggle" id="sns" data-toggle="collapse" aria-haspopup="true" onclick="setreadonly()"	href="#collapseExample" aria-expanded="false"	aria-controls="collapseExample"> 
	                             <span	class="glyphicon glyphicon-th " aria-hidden="true"></span>	</span>
	                           </div>								
	                     		  <!-- form:select cssClass="form-control" items="${manufacturers}" itemValue="id" itemLabel="descriptions[0].name"  path="product.manufacturer.id"/-->
	                     		  <form:input type="hidden" path="" name="storename" id="storenamehidden" /> 
                          <span class="help-inline"></span>
                      </div>
                      <div class="collapse" id="collapseExample">
									<div class="well" id="storeNameList"></div>
								</div>
                   	</div> --%>
                  
                   	<div class="row">
                   	  <div class="col-sm-8" id="chargediv">
                   	    <label>代理<s:message code="label.bettbio.product.charge.perior" text="Charge Period"/>(必填)</label>
					    <div id="chargePeriod" class="input-daterange input-group">
		                   	    	<input type="text" class="form-control datepicker" id="startTime" path="startTime" value='<fmt:formatDate value="${auth.startTime}" pattern="yyyy-MM-dd"/>' data-provide="datepicker"/>
								    <span class="input-group-addon">to</span>
								    <input type="text" class="form-control datepicker" id="endTime" path="endTime" value='<fmt:formatDate value="${auth.startTime}" pattern="yyyy-MM-dd"/>' data-provide="datepicker"/>
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
                   	
                   	<div class="row">
                   	  <div class="col-sm-12">
                   	    <label>代理书简介</label>
                   	     <span class="help-inline col-sm-offset-1">
                   	     <span><s:message code="label.productedit.inputnum" text="You can input"/></span>
                   	     <span id="simpNum">200</span><span><s:message code="label.productedit.inputstr" text="character"/></span>
                           <span class="help-inline col-sm-offset-1"><form:errors path="introduce" cssClass="error" /></span>
                   	    <form:textarea id="introduce" rows="3" cssClass="form-control" path="introduce" onkeydown='countChar("simpleD","simpNum");' onKeyUp='countChar("simpleD","simpNum");' onblur='countChar("simpleD","simpNum");'/>
                          
                   	  </div>
                   	</div>
                   	

              <div class="row">
            	  <div class="col-sm-12">
            	    <label>
            	      代理书快照(必上传)
            	    </label>
            	    <div class="controls" id="imageControl">
             	    <div id="localImag">
             	       <input type="file" name="myfiles" 
             	          data-default-file="" id="image" 
             	          class="dropify-event"  onchange="setImagePreview(this,localImag,this)"/>
             	    </div>
             	   <input type="text" value="" id="img_url" hidden="hidden"/>
             	   <script type="text/javascript" src="<c:url value="/resources/js/ajaxfileupload.js" />"></script>
		        <script type="text/javascript">
		        var ctx = "${pageContext.request.contextPath}";
		        //上传并预览图片
		        function setImagePreview(docObj, localImagId, imgObjPreview) {
				// 勾选图片，上传，并显示预览
			     var idStr = docObj.id;
			     var allowExtention = ".jpg,.bmp,.gif,.png"; 
			     var fileObj = document.getElementById(docObj.id).value;
			     var extention = fileObj.substring(fileObj.lastIndexOf(".") + 1).toLowerCase();
			     var browserVersion = window.navigator.userAgent.toUpperCase();
			     if (allowExtention.indexOf(extention) > -1) {
			    	//执行上传文件操作的函数
		    	    $.ajaxFileUpload({
		    	        //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
			    	        url:'<c:url value="/file/upsyload.html"/>',
			    	        secureuri:false,                       //是否启用安全提交,默认为false 
			    	        fileElementId:docObj.id,           //文件选择框的id属性
			    	        dataType:'text',                       //服务器返回的格式,可以是json或xml等
			    	        success:function(data, status){  
			    	        	//服务器响应成功时的处理函数
			    	            data = data.replace("<PRE>", '');  //ajaxFileUpload会对服务器响应回来的text内容加上<pre>text</pre>前后缀
			    	            data = data.replace("</PRE>", '');
			    	            data = data.replace('<pre style="word-wrap: break-word; white-space: pre-wrap;">', '');
			    	            data = data.replace("<pre>", '');
			    	            data = data.replace("</pre>", '');
			    	            //本例中设定上传文件完毕后,服务端会返回给前台[0`filepath]
			    	            if(data.substring(0, 1) == 0){   
			    	                //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
			    	            	$("#img_url").val(data.substring(2));
			    	            	$("#"+imgObjPreview).attr("data-default-file",""+data.substring(2)+"");
			    	            	//$("#"+imgObjPreview).attr("data-default-file", "${pageContext.request.contextPath}/file/showImage.html?path="+data.substring(2));
			    	            }else{
			    	                alert("图片上传失败，请重试2！！");
			    	            }
			    	        },
					    	error:function(data, status, e){
					    	         //服务器响应失败时的处理函数
					    	         alert("图片上传失败，请重试1！！");
					    	    }
					    	 });
						     }else{
						    	 alert("仅支持" + allowExtention + "为后缀名的文件!");
						    	 fileObj.value = ""; //清空选中文件
						    	 if (browserVersion.indexOf("MSIE") > -1) {
						             fileObj.select();
						             document.selection.clear();
						           }
						    	 fileObj.outerHTML = fileObj.outerHTML;
						     }
						}
					</script>
                		</div> 
                	</div>

		<div class="row">
		  <div class="col-sm-12">
			  <div class="pull-right">
	            <button id="bntSubmit2" class="btn btn-success"><s:message code="button.label.submit" text="Submit"/></button>
	          </div>
      	 </div>
		</div>
   </form:form>
                   
              <%-- <c:if test="${product.product.id!=null && product.product.id>0}">      
               <c:url var="createSimilar" value="/admin/products/product/duplicate.html"/>
               <form:form method="POST" enctype="multipart/form-data" commandName="product" action="${createSimilar}">
					<input type="hidden" name="productId" value="${product.product.id}" />
                    <div class="form-actions">
                        <div class="pull-right" style="padding:0px 15px">
                          <button type="submit" class="btn btn-info"><s:message code="label.product.createsimilar" text="Create similar product"/></button>
                        </div>
               		</div>

                </form:form>
               </c:if> --%>
               </div>
 			</div>
		  </div>
 	</div>
</div>