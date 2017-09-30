<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!--Set google map API key -->
<!-- c:if test="${requestScope.CONFIGS['displayStoreAddress'] == true}"-->
<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=0e40978d6a9b14cd57e622b001126352"></script> 
<!--script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?sensor=true">
</script-->
<!-- /c:if-->
<script src="<c:url value="/resources/js/check-functions.js" />"></script>
<script type="text/javascript">

var RecaptchaOptions = {
	    theme : 'clean'
};


$(document).ready(function() {
	
	isEzybioFormValid();
	$("input[type='text']").on("change keyup paste", function(){
		isEzybioFormValid();
	});
	$("#comment").on("change keyup paste", function(){
		isEzybioFormValid();
	});
	
    $("#submitContact").click(function() {
    	sendContact();
    });

});


function sendContact(){
	$('#pageContainer').showLoading();
	$(".alert-error").hide();
	$(".alert-success").hide();
	var data = $('#ezybioForm').serialize();
	//console.log(data);
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/shop/store/${requestScope.MERCHANT_STORE.code}/contact"/>',
	  data: data,
	  cache: false,
	  dataType: 'json',
	  success: function(response){
		  
		    $('#pageContainer').hideLoading();
		  	if(response.response.status==-1) {
		  		$(".alert-error").html('<s:message code="validaion.recaptcha.not.matched" text="An error message occured while trying to send"/>');
		  		$(".alert-error").show();
				$(".alert-success").hide();
		  	}else if(response.response.status==-2){
		  		$(".alert-error").html('<s:message code="NotEmpty.contact.captchaResponseField" text="An error message occured while trying to send"/>');
		  		$(".alert-error").show();
				$(".alert-success").hide();
		  	}else{
		  		$(".alert-error").hide();
				$(".alert-success").show();
		  	}
		  	changeValidateImg();
	  },
	  error: function(xhr, textStatus, errorThrown) {
	    	$('#pageContainer').hideLoading();
	  		//alert('error ' + errorThrown);
	  }

	});
	
}

</script>
<br>
<br>
<br>
<div class="head-navbar-left head-navbar-right">
	<br>

	<div class=" row" >
	
		<div class="col-sm-8">
            <form:form action="#" method="POST" id="ezybioForm" class="form-horizontal" name="ezybioForm" commandName="contact">
                   <div id="store.success" class="alert-success" style="display:none;"><s:message code="message.email.success" text="Your message has been sent"/></div>   
           				<div id="store.error" class="alert-danger" style="display:none;"><s:message code="message.email.success" text="An error occurred while sending your message, pleas try again later"/></div>
                              <form:errors id="contactForm" path="*" cssClass="alert-danger" element="div" />
                              <fieldset>
                              	  <div class="form-group">
				                        <label class="col-sm-2 control-label" for="inputName"><s:message code="label.entity.name" text="Name"/>:</label>
				                        <div class="col-sm-4">
				                        		<s:message code="NotEmpty.customer.userName" text="Name is required" var="msgName"/>
				  								 <form:input path="name" cssClass="required form-control " id="name" title="${msgName}"/>
				  								<span> <form:errors path="name" cssClass="error" /></span>
				                        </div>
				                        <label class="col-sm-2 control-label" for="inputEmail"><s:message code="label.generic.email" text="Email address"/>:</label>
				                        <div class="col-sm-4">			              		
				              				 <s:message code="NotEmpty.store.email" text="Email is required" var="msgEmail"/>
		                                     <form:input path="email" cssClass="required form-control email" id="email" title="${msgEmail}"/>
		                                     <span><form:errors path="email" cssClass="error" /></span>
					            		</div>
				           			</div>
				           			<div class="form-group">
				                        <label class="col-sm-2 control-label" for="inputSubject"><s:message code="label.generic.subject" text="Subject"/>:</label>
				                        <div class="col-sm-10">
				                        		 <form:input path="subject" cssClass="form-control " id="subject"/>
		                                   		 <span> <form:errors path="subject" cssClass="error" /></span>
				                        </div>
				           			</div>
                        	      <div class="form-group">
				                        <label class="col-sm-2 control-label" for="textarea"><s:message code="label.generic.comments" text="Comments"/>:</label>
				                        <div class="col-sm-10">			              		
				              				  <s:message code="NotEmpty.store.comments" text="Comments is required" var="msgComments"/>
		                                   	 <form:textarea path="comment" cssClass=" required form-control " rows="10" id="comment" title="${msgComments}"/>
					            		</div>
				           			</div>
		                           <div class="form-group">
				                         <label class="col-sm-2 control-label" ><s:message code="label.generic.validatecode" text="Validatecode"/>:</label>
				                        <div class="col-sm-4">			              		
				              				  <s:message code="NotEmpty.store.validatecode" text="validatecode is required" var="validatecodeName"/>
										  	 <form:input path="captchaResponseField" cssClass="required form-control " id="captchaResponseField" title="${validatecodeName}"/>
										  	  <span><form:errors path="captchaResponseField" cssClass="error" /></span>
					            		</div>
				                        <label class="col-sm-2 control-label" ><a href="#" onclick="changeValidateImg()"><s:message code="label.generic.change.validatecode" text="Change"/></a>:</label>
				                        <div class="col-sm-4">			              		
				              				 <a href="#" onclick="changeValidateImg()"><img id="imgObj" src="<c:url value="/shop/store/code.html"/>" /></a>
					            		</div>
				           			</div>
									<div class="form-group">
  										  <div class="col-sm-10 col-sm-offset-2">
     											<input id="Ezybiosubmit" type="button" value="<s:message code="label.generic.send" text="Send"/>" name="register" class="btn btn-large">
  							 			 </div>
  									</div>	
				</fieldset>
			</form:form>
			

			
            </div>
<!-- END LEFT-SIDE CONTACT FORM AREA -->


<!-- BEGIN RIGHT-SIDE CONTACT FORM AREA -->
              <div class="col-sm-4" >
									<!-- COMPANY ADDRESS -->   
					                              
	                              
					 	<!-- div itemscope itemtype="http://schema.org/Organization"> 
						 	<h2 itemprop="name"><c:out value="${requestScope.MERCHANT_STORE.storename}"/></h2><br/>  
						 	<div itemprop="address" itemscope itemtype="http://schema.org/PostalAddress"> 
							 	<span itemprop="streetAddress"><c:out value="${requestScope.MERCHANT_STORE.storeaddress}"/> <c:out value="${requestScope.MERCHANT_STORE.storecity}"/></span><br/>
							 	<span itemprop="addressLocality"><c:choose><c:when test="${not empty requestScope.MERCHANT_STORE.storestateprovince}"><c:out value="${requestScope.MERCHANT_STORE.storestateprovince}"/></c:when><c:otherwise><script>$.ajax({url: "<c:url value="/shop/reference/zoneName"/>",type: "GET",data: "zoneCode=${requestScope.MERCHANT_STORE.zone.code}",success: function(data){$('#storeZoneName').html(data)}})</script><span id="storeZoneName"><c:out value="${requestScope.MERCHANT_STORE.zone.code}"/></span></c:otherwise></c:choose>,
							 	<span id="storeCountryName"><script>$.ajax({url: "<c:url value="/shop/reference/countryName"/>",type: "GET",data: "countryCode=${requestScope.MERCHANT_STORE.country.isoCode}",success: function(data){$('#storeCountryName').html(data)}})</script></span></span><br/>
							 	<span itemprop="postalCode"><c:out value="${requestScope.MERCHANT_STORE.storepostalcode}"/></span><br/>
							 	<abbr title="Phone"><s:message code="label.generic.phone" text="Phone" /></abbr>: <span itemprop="telephone"><c:out value="${requestScope.MERCHANT_STORE.storephone}"/></span>
							 </div>
					 	</div-->
					
					
                   <!-- div class="contactMapCanvas " id="map_canvas" ></div-->                
					<h2 itemprop="name">联系我们</h2><br/>  
					<div itemprop="address" itemscope itemtype="http://schema.org/PostalAddress"> 
						<span >如需了解BETTBIO更多专业和高端信息服务，您还可以通过以下方式BettBio联系，我们期待与您商谈并满足您的个性化需求。</span><br><br>
						
						<span >QQ:800065440</span><br><br>
						
						<span >E-mail:order@bettbio.com</span><br><br>
						
						<span >Tel:021-61552750,021-61552739</span><br><br>
						
						<span >我们需要倾听您的声音，您可将您的意见和建议、平台错误内容、信息服务咨询等通过留言板功能与我们联系，我们将尽快给您回复。</span>
						</div>
            </div>
<!-- END RIGHT-SIDE CONTACT FORM AREA -->
<!-- CUSTOM CONTENT --> 
			
                                  
								

			</div>
				

<!-- GOOGLE MAP -->  
<!-- c:if test="${requestScope.CONFIGS['displayStoreAddress'] == true}"-->
<!-- script type="text/javascript">
var map = new AMap.Map("map_canvas", {
    resizeEnable: true,
    zoom:12
});
AMap.service(["AMap.PlaceSearch"], function() {
    var placeSearch = new AMap.PlaceSearch({ 
        pageSize:5,
        pageIndex:1,
        city:"${requestScope.MERCHANT_STORE.storecity}",
        map: map,
        panel: "result"
    });
    
    placeSearch.search('${requestScope.MERCHANT_STORE.storeaddress}');
});
</script-->

<!--  script>

var address = '<c:out value="${requestScope.MERCHANT_STORE.storeaddress}"/> <c:out value="${requestScope.MERCHANT_STORE.storecity}"/> <c:out value="${requestScope.MERCHANT_STORE.zone.code}"/> <c:out value="${requestScope.MERCHANT_STORE.storepostalcode}"/>';

if(address!=null) {
	geocoder = new google.maps.Geocoder();
		var mapOptions = {
  			zoom: 8,
  			mapTypeId: google.maps.MapTypeId.ROADMAP
		}
		map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);

		geocoder.geocode( { 'address': address}, function(results, status) {
  			if (status == google.maps.GeocoderStatus.OK) {
    			map.setCenter(results[0].geometry.location);
    			var marker = new google.maps.Marker({
        				map: map,
        				position: results[0].geometry.location
    			});
 			} else {
    			alert("Geocode was not successful for the following reason: " + status);
  			}
		});
}

</script-->

<!-- /c:if-->
 </div>
 <br>

