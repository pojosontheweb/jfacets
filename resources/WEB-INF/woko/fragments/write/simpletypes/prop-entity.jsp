<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty editPropertyValue.propVal}">
		<wf:execFacet name="viewPropertyValue" targetObject="${editPropertyValue.propVal}" var="page"/>
		<jsp:include page="${page}"/>	
	</c:when>
	<c:otherwise>
		&nbsp;
	</c:otherwise>
</c:choose>
&nbsp;
<span style="font-size:8px;">
<stripes:link beanclass="net.sf.woko.actions.ChangeManyToOneActionBean">
	<stripes:link-param name="id" value="${targetObjectId}"/>
	<stripes:link-param name="className" value="${targetObjectClass.name}"/>
	<stripes:link-param name="propertyName" value="${propertyDescriptor.name}"/>
	<stripes:link-param name="propertyClass" value="${propertyDescriptor.propertyType.name}"/>
    <stripes:label for="woko.edit.association.change.link"/>
</stripes:link>
</span>
