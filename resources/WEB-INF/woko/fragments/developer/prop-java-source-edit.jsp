<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<%--
<code id="codepress"
      class="cp hideLanguage"
      title="Yoooooo !"
      onmouseout="document.getElementById('object.${propertyDescriptor.name}').value = CodePress.getCode()">
${editPropertyValue.propVal}
</code>
<div class="sourceCodeEdit">
	<stripes:hidden
		name="object.${propertyDescriptor.name}"
		id="object.${propertyDescriptor.name}"/>
</div>
--%>
<stripes:textarea name="object.${propertyDescriptor.name}" cols="80" rows="20"/>

