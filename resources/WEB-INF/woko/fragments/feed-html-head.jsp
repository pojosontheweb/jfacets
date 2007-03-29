<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<c:if test="${not empty viewFeed and viewFeed.showFeed}">
	<link rel="alternate" 
		type="application/rss+xml" 
		title="Woko - RSS" 
		href="${pageContext.request.contextPath}/Feed.action?id=${viewFeed.feedObjectId}&className=${viewFeed.feedObjectClass}" />
</c:if>