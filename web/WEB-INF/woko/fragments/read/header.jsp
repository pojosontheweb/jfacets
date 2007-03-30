<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<div class="objectLinks">
    <c:if test="${view.editable}">
        <a href="${pageContext.request.contextPath}/edit/${view.shortClassName}/${view.id}" class="editLink">
            <stripes:label for="woko.edit.link.text">
                edit object
            </stripes:label>
        </a>
    </c:if>
    <c:if test="${view.deletable}">
        <a href="${pageContext.request.contextPath}/delete/${view.shortClassName}/${view.id}" class="deleteLink">
            <stripes:label for="woko.delete.link.text">
                delete object
            </stripes:label>
        </a>
    </c:if>
    <c:if test="${not empty viewFeed and viewFeed.showFeed}">
        <a href="${pageContext.request.contextPath}/feed/${view.shortClassName}/${view.id}" class="feedLink">
            <stripes:label for="woko.feed.link.text">
                RSS feeds
            </stripes:label>
        </a>
    </c:if>
</div>
	