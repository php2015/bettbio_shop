<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 

<%@ page session="false" %>
var more = '<s:message code="label.product.moreitems" text="More" />';
var addCart='<s:message code="button.label.addToCart" text="Add to cart" />';
/**
* Builds the product container div from the product list
**/
function buildProductsList(productList) {
   	products = new Array();
	var list = '';
	if(productList != null && productList.length>0){
		//$("#productsList").html("");
		$.each(productList, function(i, p) {
			products[i]=p.id;
			var _li = '<li class="list-group-item" style="border-radius: 0px;border-right:0px;border-left:0px;">';
			var _li_end = '</li>';
			var _a = '<a href="<c:url value="/shop/product/" />' +p.id + '.html">';
			var _a_end = '</a>';
			var _div = '<div style="padding-top:3px;">';
			var _div_end = '</div>';
			var _span = '<span style="color:#aaa">';
			var _span_end = "</span>";
			list += _li;
			list += '<div class="row">';
			list += '<div class="col-xs-3 col-md-4" style="padding-left:1%;padding-right:1%">' + _a;
			/* product image */
			if(p.imageUrl!=null) {
				list += '<div><img src="${pageContext.request.contextPath }' + p.imageUrl +'" style="border:1px solid #eee;padding:1px;width:100%;"></div>';
			}else{
				list += '<div><img src="<c:url value="/resources/img/pimg.jpg" />" style="border:1px solid #eee;width:100%;"></div>';
			}
			list += _a_end + _div_end;
			
			list += '<div class="col-xs-9 col-md-8">';
			/* product name */
			list += '<h4 style="color:#000;">' + _a + p.productName + _a_end + '</h4>';
			/* product price*/
			list += '<div id="price_' + p.id + '"></div>';
			/* product quality */
			if(p.quality >0){
				var modNum = p.quality%20;
				var intNum = parseInt(p.quality/20);
				list += _div;
				for(var index=0;index<intNum;index++){
					list += '<img src="<c:url value="/resources/img/stars/star-on.png" />">'
				}
				if(modNum>0){
					list +='<img src="<c:url value="/resources/img/stars/star-half.png" />">'
				}
				list += _div_end;
			}
			/* product brand */
			if(p.manufacturer!=null&&p.manufacturer!='') {
				list += _div + _span + p.manufacturer + _span_end + _div_end; 
			}
			/* product merchant */
			list += _div + _span + p.storeName + _span_end + _div_end;
			list += _div_end + _div_end;
			list += _li_end;
			
		});
		getPrice();
		}
	//$("#prodcutTbody").append(tbBody);
	$("#productsList").append(list);
}


