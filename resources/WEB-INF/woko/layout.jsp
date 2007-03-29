<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<%@ taglib prefix="authz" uri="http://acegisecurity.org/authz" %>

<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<stripes:layout-definition>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Woko :: ${title}</title>
        <stripes:layout-component name="html-head-css">
            <link href="${pageContext.request.contextPath}/woko/woko.css" type="text/css" rel="stylesheet"/>
            <link href="${pageContext.request.contextPath}/woko/woko-debug.css" type="text/css" rel="stylesheet"/>
        </stripes:layout-component>
        <script language="JavaScript" src="${pageContext.request.contextPath}/woko/js/woko-popups.js"></script>
        <jsp:include page="/WEB-INF/woko/fragments/feed-html-head.jsp"/>
        <link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/woko/jscalendar-1.0/skins/aqua/theme.css" title="Aqua" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/woko/jscalendar-1.0/calendar.js"></script>
        <%
            if (request.getLocale()!=null &&
                    request.getLocale().getLanguage().equalsIgnoreCase("fr")) {
        %>
            <script type="text/javascript" src="${pageContext.request.contextPath}/woko/jscalendar-1.0/lang/calendar-fr.js"></script>
        <%
            } else {
        %>
            <script type="text/javascript" src="${pageContext.request.contextPath}/woko/jscalendar-1.0/lang/calendar-en.js"></script>
        <%
            }
        %>
        <script type="text/javascript" src="${pageContext.request.contextPath}/woko/jscalendar-1.0/calendar-setup.js"></script>
        <stripes:layout-component name="html-head"/>
    </head>

	<c:set var="username">
		<authz:authentication operation="username"/>
	</c:set>    

    <body id="altbody">

    <div id="wrapper-header">
		<div id="header">
			<c:if test="${username ne 'anonymousUser'}">
				<div id="searchBox">
					<stripes:form action="/search">
						<stripes:text name="query"/>
						<stripes:submit name="searchCompass" value="search"/>
					</stripes:form>
				</div>
				<div id="logout">
					<stripes:label for="woko.login.label.loggedAs">
						Logged in as
					</stripes:label>
						<b><i>${username}</i></b>  
						|							
					<stripes:link beanclass="net.sf.woko.actions.LoginActionBean" event="logout">
						<stripes:label for="woko.login.label.logout">
							log-out
						</stripes:label>
					</stripes:link>
				</div>
			</c:if>
			
		</div>
	</div>

	<div id="wrapper-menu">
		<div id="menu">
			<ul>				
				<woko:execAndInclude facetName="viewMenu"
					objClass="<%=Object.class%>"
				/>			
			</ul>
		</div>
	</div>
	    
    <div id="content">
        <stripes:messages/>
       	<stripes:errors/>               	
        <stripes:layout-component name="contents"/>
    </div>
    <div id="footer">
    	<%-- obtain the footer using a facet --%>
    	<woko:execAndInclude facetName="getAppFooter"
			objClass="<%=Object.class%>"
			/>		
    </div>

    <%-- include the codepress scripts if needed
    <script src="${pageContext.request.contextPath}/codepress/codepress.js"
        type="text/javascript"
        id="cp-script"
        lang="en-us">
    </script>
    --%>

    </body>
    </html>
</stripes:layout-definition>