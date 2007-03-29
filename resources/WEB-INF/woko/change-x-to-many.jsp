<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<%@page import="net.sf.woko.actions.ChangeOneToManyActionBean"%>
<%@page import="java.util.List"%>

<%
ChangeOneToManyActionBean actionBean = (ChangeOneToManyActionBean)request.getAttribute("actionBean");
Collection alreadyThere = actionBean.getAlreadyThere();
List available = actionBean.getAvailable();
String title = Util.getTitle(actionBean.getTargetObject());
String type = Util.getType(actionBean.getTargetObject());
%>

<wf:execFacet name="getPageLayout" var="layout"/>

<%@page import="java.util.Collection"%>
<%@page import="net.sf.woko.util.Util"%>
<stripes:layout-render name="${layout}" title="Edit association">

    <stripes:layout-component name="html-head">
        <%@ include file="/WEB-INF/woko/fragments/association-ajax-script.jsp" %>
    </stripes:layout-component>

    <stripes:layout-component name="contents">
		
		<h1>
			<stripes:label for="woko.edit.association.title"/>
		</h1>
		
		<p>
            <fmt:message key="woko.edit.association.msg">
                <fmt:param value="<%=actionBean.getPropertyName()%>" />
                <fmt:param value="<%=title%>" />
                <fmt:param value="<%=type%>" />
            </fmt:message>		
		</p>

        <div id="daAjaxZone">

            <stripes:form beanclass="net.sf.woko.actions.ChangeOneToManyActionBean">

                <p>
                    <stripes:label for="woko.edit.association.x-to-many.tooltip"/>
                </p>

                <table border="0" cellspacing="0" cellpadding="5">
                    <tbody>
                        <tr>
                            <th>
                                <stripes:label for="woko.edit.association.x-to-many.add"/>
                            </th>
                            <th>
                                <stripes:label for="woko.edit.association.x-to-many.remove"/>
                            </th>
                        </tr>
                        <tr>
                            <td valign="top">
                                <stripes:select name="toAdd" multiple="true" size="10">
                                    <%
                                    for(Object o : available) {
                                    %>
                                        <stripes:option value="<%=Util.getId(o)%>">
                                            <wf:useFacet name="viewTitle" targetObject="<%=o%>" var="titleFacet"/>
                                            ${titleFacet.title}
                                        </stripes:option>
                                    <%
                                    }
                                    %>
                                </stripes:select>
                            </td>
                            <td valign="top">
                                <stripes:select name="toRemove" multiple="true" size="10">
                                    <%
                                    for(Object o : alreadyThere) {
                                    %>
                                        <stripes:option value="<%=Util.getId(o)%>">
                                            <wf:useFacet name="viewTitle" targetObject="<%=o%>" var="titleFacet"/>
                                            ${titleFacet.title}
                                        </stripes:option>
                                    <%
                                    }
                                    %>
                                </stripes:select>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <stripes:submit name="ok" value="Ok"/> &nbsp;
                                <stripes:submit name="cancel" value="Cancel"/>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <stripes:hidden name="id" value="<%=actionBean.getId()%>"/>
                <stripes:hidden name="className" value="<%=actionBean.getClassName()%>"/>
                <stripes:hidden name="propertyName" value="<%=actionBean.getPropertyName()%>"/>

            </stripes:form>

            <%@ include file="/WEB-INF/woko/fragments/write/associations/association-create-form.jsp"%>

        </div>
	
    </stripes:layout-component>
</stripes:layout-render>