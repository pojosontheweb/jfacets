<%@ tag import="net.sf.woko.facets.write.EditPropertyValue" %>
<%@ tag import="net.sf.woko.util.Util" %>
<%@ tag import="net.sf.woko.facets.read.ViewPropertyValue" %>
<%--
    Shows a property in read-only mode using the "viewPropertyValue" facet.
    This tag also enables the use of property-specific facets, like :
    ("viewPropertyValue_xxx", profile, targetObject), where targetObject is
    the instance owning the property, and xxx is the name of the property
    to be displayed.
--%>

<%@ attribute name="targetObject" required="true" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="true" type="java.lang.Class" %>
<%@ attribute name="propertyValue" required="true" type="java.lang.Object" %>
<%@ attribute name="propertyClass" required="true" type="java.lang.Class" %>

<%@ include file="/WEB-INF/woko/taglibs.jsp" %>

<%
    ViewPropertyValue editPropValue = Util.getEditPropValueFacet(targetObject, targetObjectClass, propertyValue, propertyClass, request);
    Object res = editPropValue.execute();
    request.setAttribute("facetExecutionResult", res.toString());
%>
<woko:debugInfos>
    <jsp:include page="${facetExecutionResult}"/>
    <c:if test="${editPropertyValue.requiredField}">
        *
    </c:if>
</woko:debugInfos>