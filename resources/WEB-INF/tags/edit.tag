<%--
	JSP tag file that invokes the "edit" facet for passed object 
	and shows the result
--%>

<%@ attribute name="obj" required="false" type="java.lang.Object" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jfacets.sourceforge.net/jfacets.tld" prefix="wf" %>
<%@ taglib prefix="woko" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>

<c:choose>
	<c:when test="${not empty obj}">
		<stripes:form beanclass="net.sf.woko.actions.EditActionBean">
			<woko:execAndInclude 
				facetName="edit" 
				obj="${obj}" 
				objClass="${obj.class}"/>
		</stripes:form>			
	</c:when>
	<c:otherwise>
		Target object is null !
	</c:otherwise>		
</c:choose>