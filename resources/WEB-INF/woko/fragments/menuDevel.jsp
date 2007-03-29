<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<jsp:include page="/WEB-INF/woko/fragments/menuUser.jsp"/>
<li>
    <stripes:link beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean">
        <stripes:label for="woko.menu.hibernate.tools"/>
    </stripes:link>
</li>
<li>
    <stripes:link beanclass="net.sf.woko.actions.devel.FacetStudioActionBean">
        <stripes:label for="woko.menu.dynamic.facets"/>
    </stripes:link>
</li>
<li>
    <stripes:link beanclass="net.sf.woko.actions.devel.DebugActionBean">
        <stripes:label for="woko.menu.debug.mode"/>
    </stripes:link>    
</li>

