<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<stripes:text 
	name="object.${propertyDescriptor.name}" 
	id="object.${propertyDescriptor.name}" 
	disabled="${editPropertyValue.generatedValue}"/>