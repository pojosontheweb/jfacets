<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<stripes:select name="object.${propertyDescriptor.name}">
	<c:if test="${not editPropertyValue.requiredField}">
		<stripes:option value=""></stripes:option>
	</c:if>
	<stripes:options-enumeration enum="${propertyDescriptor.propertyType.name}"/> 
</stripes:select>