<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<stripes:select name="object.${propertyDescriptor.name}">
	<stripes:option value=""></stripes:option>
	<stripes:option value="true">true</stripes:option>
	<stripes:option value="false">false</stripes:option>
</stripes:select>