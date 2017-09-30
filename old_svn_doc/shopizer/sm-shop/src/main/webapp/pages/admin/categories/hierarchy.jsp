<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<script src="<c:url value="/resources/js/tree/hierarchy.js" />"></script> 
<%@ page session="false" %>				
				

 <style type="text/css">
        .dragingNode {
            z-index: 10000;
            background: #CCC;
            border: 1px #00B83F dotted; 
            color: #333;
            font-size: 12px;
            text-align:center;
            font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
            position: absolute;
        }

       
    </style>
    <script type="text/javascript">
        
           
           
    </script>

<div class="tabbable" id="hierachy">

						<jsp:include page="/common/adminTabs.jsp" />
  					
  					 	
								<br/>
								<div class="well">
									<s:message code="label.category.hierarchy.text" text="Drag categories to re-organize the hierarchy" />
								</div>
								<br/>
      		<form id="form1">
        <div >
            <ul class="list-group" id="treeDemo" ></ul>
        </div>  
        <label id="Label1"></label>
    </form>
			      			     
			      			     
      					

				</div>	      			     
