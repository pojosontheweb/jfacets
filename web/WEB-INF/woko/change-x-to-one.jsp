<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<%@page import="net.sf.woko.util.Util"%>
<%@page import="net.sf.woko.actions.ChangeManyToOneActionBean"%>
<%@page import="java.util.List"%>

<%
ChangeManyToOneActionBean actionBean = (ChangeManyToOneActionBean)request.getAttribute("actionBean");
List results = actionBean.getResults();
String title = Util.getTitle(actionBean.getTargetObject());
String type = Util.getType(actionBean.getTargetObject());
String id = Util.getId(actionBean.getTargetObject());
String className = actionBean.getTargetObject().getClass().getName();
String propertyName = actionBean.getPropertyName();
%>

<wf:execFacet name="getPageLayout" var="layout"/>

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

            <h3>
                <stripes:label for="woko.edit.association.x-to-one.tooltip"/>
            </h3>

            <stripes:form beanclass="net.sf.woko.actions.ChangeManyToOneActionBean">
                <p>
                    <stripes:select name="selectedObjectId">
                        <stripes:option value=""></stripes:option>
                        <%
                        for(Object o : results) {
                        %>
                            <stripes:option value="<%=Util.getId(o)%>">
                                <wf:useFacet name="viewTitle" targetObject="<%=o%>" var="titleFacet"/>
                                ${titleFacet.title}
                            </stripes:option>
                        <%
                        }
                        %>
                    </stripes:select>
                </p>
                <stripes:submit name="ok" value="Ok"/> &nbsp;
                <stripes:submit name="cancel" value="Cancel"/>

                <stripes:hidden name="id" value="<%=actionBean.getId()%>"/>
                <stripes:hidden name="className" value="<%=actionBean.getClassName()%>"/>
                <stripes:hidden name="propertyName" value="<%=actionBean.getPropertyName()%>"/>
                <stripes:hidden name="propertyClass" value="<%=actionBean.getPropertyClass()%>"/>
            </stripes:form>

            <%@ include file="/WEB-INF/woko/fragments/write/associations/association-create-form.jsp"%>            

        </div>


    </stripes:layout-component>
</stripes:layout-render>