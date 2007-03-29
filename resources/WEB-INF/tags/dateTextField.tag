<%@ tag import="java.text.SimpleDateFormat" %>

<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="fieldName" required="true" %>
<%@ attribute name="fieldId" required="true" %>
<%@ attribute name="disabled" required="false" type="java.lang.Boolean"%>

<%
    // simplistic conversion of the pattern used by the current locale
    // in order to pas that to the calendar...
    SimpleDateFormat sdf = new SimpleDateFormat();
    String pattern = sdf.toLocalizedPattern();
    // convert to jscalendar stuff...
    StringBuffer buf = new StringBuffer();
    boolean dayAdded = false;
    boolean monthAdded = false;
    boolean yearAdded = false;
    for(int i=0 ; i<pattern.length() ; i++) {
        char c = pattern.charAt(i);
        if ((c=='d' || c=='D') && !dayAdded) {
            buf.append("%d");
            dayAdded = true;
            if (!(dayAdded && monthAdded && yearAdded)) {
                buf.append("/");
            }
        }
        if ((c=='m' || c=='M') && !monthAdded) {
            buf.append("%m");
            monthAdded = true;
            if (!(dayAdded && monthAdded && yearAdded)) {
                buf.append("/");
            }
        }
        if ((c=='y' || c=='Y') && !yearAdded) {
            buf.append("%y");
            yearAdded = true;
            if (!(dayAdded && monthAdded && yearAdded)) {
                buf.append("/");
            }
        }
    }
    String jsPattern = buf.toString();
%>
<stripes:text
	name="${fieldName}"
	id="${fieldId}"
    disabled="${disabled}"/>
<c:if test="${not disabled}">
    <img src="${pageContext.request.contextPath}/woko/jscalendar-1.0/img.gif" id="imgDate_${fieldId}"
         style="cursor: pointer; border: none;"
         onmouseover="this.style.background='green';"
         onmouseout="this.style.background=''" />
    <script type="text/javascript">
      Calendar.setup(
        {
          inputField  : "${fieldName}",         // ID of the input field
          button      : "imgDate_${fieldId}",       // ID of the button
          ifFormat    : "<%=jsPattern%>",    // the format for the input
            date: new Date() // date if nothing is selected
        }
      );
    </script>
</c:if>