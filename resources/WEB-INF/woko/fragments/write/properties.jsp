<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<div class="reflected-properties">
	<table class="properties" cellpadding="2" cellspacing="0">
		<tbody>
			<c:set var="descriptors" value="${editProperties.propertyDescriptors}"/>	
			<c:forEach varStatus="propNames" items="${editProperties.propertyNames}" var="name"> 		
				<c:set var="propertyDescriptor" value="${editProperties.descriptorsByName[name]}" scope="request"/>
				<c:set var="val" value="${descriptors[propertyDescriptor]}"/>				
				<tr>
					<th>
						<%-- label --%>
						<c:set var="label">${editProperties.shortClassName}.${name}</c:set>						
						<stripes:label for="${label}">
							${name}
						</stripes:label>
					</th>
					<td>
						<%-- 
							is the property writable ?  
							if yes, then invoke edit facet, 
							if not, then invode view
						--%>
						<c:choose>
							<c:when test="${editProperties.writeAllowed[propNames.index]}">							
								<woko:editPropertyValue targetObject="${targetObject}"
                                    targetObjectClass="${targetObject.class}"
                                    propertyValue="${val}"
									propertyClass="${propertyDescriptor.propertyType}"
								/>
							</c:when>
							<c:otherwise>							
								<woko:viewPropertyValue targetObject="${targetObject}"
                                    targetObjectClass="${targetObject.class}"
                                    propertyValue="${val}"
									propertyClass="${propertyDescriptor.propertyType}"
								/>
							</c:otherwise>
						</c:choose>										
					</td>					
				</tr>
			</c:forEach>
            <c:if test="${not empty edit}">
                <tr>
                    <td colspan="2">
                        <stripes:submit name="save"/>
                        <stripes:submit name="closeEditing"/>
                    </td>
                </tr>                
            </c:if>
		</tbody>
	</table>
	<stripes:hidden name="id" value="${editProperties.id}"/>
	<stripes:hidden name="className" value="${editProperties.className}"/>
	<c:if test="${not empty param.isFresh}">		
		<stripes:hidden name="isFresh" value="true"/>
	</c:if>
</div>
