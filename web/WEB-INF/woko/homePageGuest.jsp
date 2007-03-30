<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:layout-render name="${layout}" title="Home">
    <stripes:layout-component name="contents">
    
		<h1><stripes:label for="woko.guest.home.h1"/></h1>
		
		<p>
			<stripes:label for="woko.guest.home.msg"/>
		</p>		 
				
    </stripes:layout-component>
</stripes:layout-render>