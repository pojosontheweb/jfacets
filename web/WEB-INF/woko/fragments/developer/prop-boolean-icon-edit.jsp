<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty editPropertyValue.propVal and editPropertyValue.propVal}">
		<img src="${pageContext.request.contextPath}/woko/images/light-green.png"/>
	</c:when>
	<c:otherwise>
		<img src="${pageContext.request.contextPath}/woko/images/light-red.png"/>	
	</c:otherwise>
</c:choose>
&nbsp;
<stripes:select name="object.${propertyDescriptor.name}">
	<stripes:option value=""></stripes:option>
	<stripes:option value="true">true</stripes:option>
	<stripes:option value="false">false</stripes:option>
</stripes:select>