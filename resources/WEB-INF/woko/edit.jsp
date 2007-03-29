<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<stripes:useActionBean beanclass="net.sf.woko.actions.EditActionBean" var="actionBean"/>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:layout-render name="${layout}" title="Edit object">
    <stripes:layout-component name="contents">

		<woko:edit obj="${actionBean.object}"/>
	
    </stripes:layout-component>
</stripes:layout-render>