<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty viewPropertyValue.propVal and viewPropertyValue.propVal}">
		<img src="${pageContext.request.contextPath}/woko/images/light-green.png"/>
	</c:when>
	<c:otherwise>
		<img src="${pageContext.request.contextPath}/woko/images/light-red.png"/>	
	</c:otherwise>
</c:choose>