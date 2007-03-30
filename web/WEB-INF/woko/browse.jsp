<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:layout-render name="${layout}" title="Browsing Object">
    <stripes:layout-component name="contents">      	
    
    	<woko:view obj="${targetObject}"/>
    
    </stripes:layout-component>    
</stripes:layout-render>
    