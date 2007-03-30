<%@ page import="org.acegisecurity.context.SecurityContextHolder" %>
<%@ page import="org.acegisecurity.Authentication" %>
<%@ page import="org.acegisecurity.ui.AccessDeniedHandlerImpl" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Access Denied</title>
    </head>
    <body>
    <div id="container">
      	<div id="login-box">
      	
		<h1>Sorry, access is denied</h1>


		<p>
		<%= request.getAttribute(AccessDeniedHandlerImpl.ACEGI_SECURITY_ACCESS_DENIED_EXCEPTION_KEY)%>
		</p>
		
		<p>
		
		<%		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null) { %>
					Authentication object as a String: <%= auth.toString() %><BR><BR>
		<%      } %>
		</p>
		
		</div>
	</div>
	</body>
</html>

		