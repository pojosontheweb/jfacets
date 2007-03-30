<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<div class="reflected-properties">
	<table class="properties" cellpadding="2" cellspacing="0">
		<tbody>
			<c:set var="descriptors" value="${viewProperties.propertyDescriptors}"/>	
			<c:forEach items="${viewProperties.propertyNames}" var="name"> 		
				<c:set var="propertyDescriptor" value="${viewProperties.descriptorsByName[name]}" scope="request"/>
				<c:set var="val" value="${descriptors[propertyDescriptor]}"/>				
				<tr>
					<th>
						<%-- label --%>
						<c:set var="label">${viewProperties.shortClassName}.${name}</c:set>						
						<stripes:label for="${label}">
							${name}
						</stripes:label>
					</th>
					<td>
						<woko:viewPropertyValue 
							targetObject="${targetObject}"
                            targetObjectClass="${view.targetObjectClass}" 
                            propertyValue="${val}"
							propertyClass="${propertyDescriptor.propertyType}"/>
					</td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
