<?xml version="1.0" encoding="utf-8"?>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<%
    String appPath = request.getRequestURL().toString();
    int i = appPath.indexOf("../");
    appPath = appPath.substring(0, i);
%>

<fmt:setLocale value="en_EN"/>
<c:set var="pattern">EE, dd MMMM yyyy HH:mm:ss 'GMT'</c:set>
<fmt:timeZone value="GMT">
	<rss version="2.0">
		<channel>
			<title>${viewFeed.channelTitle}</title>
			<link><![CDATA[<%=appPath%>/browser/Browse.action?id=${actionBean.id}&className=${actionBean.className}]]></link>
			<description>
				The feed allows you to get stay in touch with events related 
				to the object (updates for the moment !!!).
			</description>
			<lastBuildDate><fmt:formatDate 
					value="${actionBean.feed.lastBuildDate}" 
					type="BOTH" 
					dateStyle="FULL" 
					timeStyle="FULL"
					pattern="${pattern}"
					/></lastBuildDate>
			<language>en-us</language>
			<c:forEach items="${actionBean.items}" var="item">
				<item>
					<title>Object ${item.type} on <fmt:formatDate 
					value="${item.pubDate}" 
					type="BOTH" 
					dateStyle="FULL" 
					timeStyle="FULL"
					pattern="${pattern}"
					/></title>
	<%--
					<guid>[CDATA[ ${pageContext.request.contextPath}/browser/Browse.action?id=${actionBean.objectId}&className=${actionBean.object.class.name} ]]</guid>
	--%>
					<pubDate><fmt:formatDate 
							value="${item.pubDate}" 
							type="BOTH" 
							dateStyle="FULL" 
							timeStyle="FULL"
							pattern="${pattern}"
							/></pubDate>
					<description>
						'${viewFeed.title}' has been ${item.type} on 
						<fmt:formatDate 
							value="${item.pubDate}" 
							type="BOTH" 
							dateStyle="FULL" 
							timeStyle="FULL"
							pattern="${pattern}"
							/> by user '${item.username}'. Click the link to see this object.</description>
				</item>	
			</c:forEach>
		</channel>
	</rss>
</fmt:timeZone>