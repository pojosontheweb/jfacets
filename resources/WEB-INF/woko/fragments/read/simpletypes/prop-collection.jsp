<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<ul class="reflected-collection">
	<c:if test="${not empty viewPropertyValue.propVal}">
		<c:forEach items="${viewPropertyValue.propVal}" var="item">
			<li class="reflected-collection">
				<wf:execFacet name="viewPropertyValue" targetObject="${item}" var="page"/>				
				<jsp:include page="${page}"/>
			</li>
		</c:forEach>
	</c:if>
</ul>