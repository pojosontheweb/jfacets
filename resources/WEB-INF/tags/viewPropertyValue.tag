<%@ tag import="net.sf.woko.facets.read.ViewPropertyValue" %>
<%@ tag import="net.sf.woko.util.Util" %>
<%@ tag import="net.sourceforge.jfacets.IExecutable" %>
<%--
    Shows a property in read-only mode using the "viewPropertyValue" facet.
    This tag also enables the use of property-specific facets, like :
    ("viewPropertyValue_xxx", profile, targetObject), where targetObject is
    the instance owning the property, and xxx is the name of the property
    to be displayed.
--%>

<%@ include file="/WEB-INF/woko/taglibs.jsp" %>

<%@ attribute name="targetObject" required="true" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="true" type="java.lang.Class" %>
<%@ attribute name="propertyValue" required="true" type="java.lang.Object" %>
<%@ attribute name="propertyClass" required="true" type="java.lang.Class" %>
    
<%
    ViewPropertyValue viewPropValue = Util.getViewPropValueFacet(targetObject, targetObjectClass, propertyValue, propertyClass, request);
    Object result = viewPropValue.execute();
    request.setAttribute("facetExecutionResult", result.toString());
%>
<woko:debugInfos>
    <jsp:include page="${facetExecutionResult}"/>
</woko:debugInfos>