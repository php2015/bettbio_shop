<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<%-- 因为搜狗推广而注销
 <c:if test="${sessionScope.IS_FIRST_ACCESS}">
        <div id="modal-overlay" style="position: absolute; left: 0px; top: 0px; width:100%; height:100%; text-align:center; z-index: 1000; background-color: rgba(128,128,128,0.5); ">
                                        <button onclick="document.getElementById('modal-overlay').style.visibility='hidden'" style="position:absolute; top:0px; right:0px; margin-right:40px;margin-top:40px; font-size: 32px;">
                                                        <s:message code="button.label.close" text="Close" />X
                                        </button>
                                        <a href="<c:url value="/shop/activity.html" />" target="_blank">
                                                        <img src="<c:url value="/resources/img/diamond_pop.png" />" height="100%" />
                                        </a>
        </div>
</c:if>
--%>
<%-- 取消百度商桥
<script type="text/javascript">
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?8331a3baf0372580bab3780bb020b999";
  var s = document.getElementsByTagName("script")[0];
  s.parentNode.insertBefore(hm, s);
})();
</script>
--%>

<%-- 腾讯企点 --%>
<%-- 暂时屏蔽
<!-- WPA start -->
<script src="//wp.qiye.qq.com/qidian/2852132394/870ef0e1cfd76ace1039a88b9b19b74e" charset="utf-8"></script>
<!-- WPA end -->
--%>
