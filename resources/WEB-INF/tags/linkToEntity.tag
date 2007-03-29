<%@ tag import="net.sf.woko.facets.read.ViewObjectTitle" %>
<%@ tag import="net.sourceforge.jfacets.web.WebFacets" %>
<%@ tag import="net.sf.woko.util.Util" %>
<%--
    JSP tag file that outputs a link for browsing an entity
--%>

<%@ attribute name="entity" required="true" type="java.lang.Object" %>
<%@ attribute name="text" required="false" %>

<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="tmp4Link" scope="request" value="${entity}"/>
<%
    Object o = request.getAttribute("tmp4Link");
    String id = Util.getId(o);
    String className = Util.stripPackageName(
            Util.deproxifyCglibClass(o.getClass()));
    if (text == null) {
        ViewObjectTitle titleFacet = (ViewObjectTitle) WebFacets.get(request).getFacet("viewTitle", o, request);
        text = titleFacet.getTitle();
    }
%>
<a href="${pageContext.request.contextPath}/view/<%=className%>/<%=id%>">
	<%=text%>
    <jsp:doBody/>
</a>