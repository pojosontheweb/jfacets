<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<h2>
	<c:if test="${viewTitle.showType}">
		${viewTitle.type} ::
	</c:if>
	${viewTitle.title}
</h2>