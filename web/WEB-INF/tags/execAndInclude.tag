<%--
	JSP tag file that simply execs a facet and 
	performs a JSP include on the result of 
	execution.
--%>

<%@ attribute name="facetName" required="true" %>
<%@ attribute name="obj" required="false" type="java.lang.Object" %>
<%@ attribute name="objClass" required="false" type="java.lang.Class" %>

<%@ include file="/WEB-INF/woko/taglibs.jsp" %>
<wf:execFacet name="${facetName}"
	targetObject="${obj}" 
	targetObjectClass="${objClass}"
	var="res"/>
<woko:debugInfos>
<jsp:include flush="true" page="${res}"/>
</woko:debugInfos>

     
    
