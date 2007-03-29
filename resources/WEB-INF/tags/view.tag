<%--
	JSP tag file that invokes the "view" facet for passed object 
	and shows the result
--%>

<%@ attribute name="obj" required="false" type="java.lang.Object" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jfacets.sourceforge.net/jfacets.tld" prefix="wf" %>
<%@ taglib prefix="woko" tagdir="/WEB-INF/tags" %>

<c:choose>
	<c:when test="${not empty obj}">
		<woko:execAndInclude 
			facetName="view"
			obj="${obj}" 
			objClass="${obj.class}"
		/>
	</c:when>
	<c:otherwise>
		Target object is null !
	</c:otherwise>		
</c:choose>