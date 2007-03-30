<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
    String engine = request.getParameter("engine");
%>
<html>
<head>
	<title>CodePress - Real Time Syntax Highlighting Editor written in JavaScript</title>
	<meta name="description" content="CodePress source code editor window" />
	<link type="text/css" href="../themes/default/codepress.css?timestamp=<%=System.currentTimeMillis()%>" rel="stylesheet" />
	<link type="text/css" href="../languages/java.css?timestamp=<%=System.currentTimeMillis()%>" rel="stylesheet" id="cp-lang-style" />
	<script type="text/javascript" src="../engines/<%=engine%>.js?timestamp=<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript" src="../languages/java.js?timestamp=<%=System.currentTimeMillis()%>"></script>
</head>
<%
    if (engine.equals("gecko")) {
%>
        <body id='code'></body>
<%
    } else if (engine.equals("msie")) {
%>
        <body><pre id='code'></pre></body>
<%
    }
%>
</html>