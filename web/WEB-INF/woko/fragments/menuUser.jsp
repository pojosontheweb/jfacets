<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<li>
    <a href="${pageContext.request.contextPath}">
        <stripes:label for="woko.menu.home"/>
    </a>
</li>
<li>
    <stripes:link beanclass="net.sf.woko.actions.CreateObjectActionBean">
        <stripes:label for="woko.menu.create.object"/>
    </stripes:link>
</li>