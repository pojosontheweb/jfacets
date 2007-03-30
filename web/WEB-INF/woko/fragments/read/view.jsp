<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<div class="browsed-object">
    <c:choose>
        <c:when test="${not empty targetObject}">

            <%-- header --%>
            <woko:execAndInclude facetName="viewHeader" obj="${targetObject}"/>
            <%-- title --%>
            <woko:execAndInclude facetName="viewTitle" obj="${targetObject}"/>
            <%-- properties --%>
            <woko:execAndInclude facetName="viewProperties" obj="${targetObject}"/>
            <%-- footer --%>
            <woko:execAndInclude facetName="viewFooter" obj="${targetObject}"/>
        </c:when>
        <c:otherwise>

            <p>The target object is null !</p>

        </c:otherwise>
    </c:choose>
</div>
