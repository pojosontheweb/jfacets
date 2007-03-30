<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<%@page import="de.java2html.Java2Html"%>
<%
ViewPropertyValue viewPV = (ViewPropertyValue)
	request.getAttribute("viewPropertyValue");
String propVal = (String)viewPV.getPropVal();
String converted = null;
if (propVal!=null)
	converted = Java2Html.convertToHtml(propVal);
if (converted!=null) {
%>
<%@page import="net.sf.woko.facets.read.ViewPropertyValue"%>
<div class="groovyCode">
	<%=converted%>
</div>
<% } else { %>
  <div style="color:red;">No source code</div>
<% } %>