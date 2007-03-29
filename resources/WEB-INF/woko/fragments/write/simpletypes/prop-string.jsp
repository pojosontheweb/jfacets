<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<c:choose>
	<c:when test="${editPropertyValue.maxLen <= 50}">
		<stripes:text 
			name="object.${propertyDescriptor.name}"
			id="object.${propertyDescriptor.name}" 
			disabled="${editPropertyValue.generatedValue}" 
			size="${editPropertyValue.maxLen}"/>				
	</c:when>
	<c:otherwise>
		<stripes:textarea 
			name="object.${propertyDescriptor.name}"
			id="object.${propertyDescriptor.name}" 
			disabled="${editPropertyValue.generatedValue}" 
			cols="50" 
			rows="5"/>
	</c:otherwise>
</c:choose>

