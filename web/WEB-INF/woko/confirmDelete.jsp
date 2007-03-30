<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:layout-render name="${layout}" title="Delete object">
    <stripes:layout-component name="contents">

        <c:choose>
            <c:when test="${actionBean.deleted}">

                <h1><stripes:label for="woko.delete.h1.deleted"/></h1>
                <p>
                    <i>${actionBean.objectTitle} (${actionBean.objectType})</i> <stripes:label for="woko.delete.msg.deleted"/>
                </p>               

            </c:when>
            <c:otherwise>

                <h1>
                    <stripes:label for="woko.delete.h1"/>
                </h1>
                <p>
                    <stripes:label for="woko.delete.msg"/>
                    <i>${actionBean.objectTitle}
                    (${actionBean.objectType})</i>
                </p>
                <p>
                    <stripes:form beanclass="net.sf.woko.actions.DeleteActionBean">
                        <stripes:hidden name="id" value="${actionBean.id}"/>
                        <stripes:hidden name="className" value="${actionBean.className}"/> 
                        <stripes:submit name="delete"/>
                    </stripes:form>
                </p>

            </c:otherwise>
        </c:choose>

    </stripes:layout-component>
</stripes:layout-render>