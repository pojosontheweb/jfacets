<%@ page import="java.util.HashMap" %>
<%@ page import="net.sf.woko.search.ISearchResults" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<c:set var="results" value="${view.targetObject}"/>

<script type="text/javascript">
    function toggleClassResults(id) {
        var elem = document.getElementById(id);
        if (elem.style.display == "none") {
            elem.style.display = 'inline';
        } else {
            elem.style.display = "none"
        }
    }
</script> 

<div class="browsed-object">
    <h2><stripes:label for="woko.search.results.title"/></h2>
    <p>
        <fmt:message key="woko.search.results.msg">
            <fmt:param value="${results.count}" />
            <fmt:param value="${results.query}" />
        </fmt:message>
    </p>
    <%
        // prepare the data to be displayed...
        HashMap<Class, List<Object>> resultsByClass = new HashMap<Class, List<Object>>();
        ISearchResults results = (ISearchResults) pageContext.getAttribute("results");
        for (Object o : results.getResults()) {
            Class clazz = o.getClass();
            List<Object> lst = resultsByClass.get(clazz);
            if (lst == null) {
                lst = new ArrayList<Object>();
                resultsByClass.put(clazz, lst);
            }
            lst.add(o);
        }
        // and show it
        for(Class clazz : resultsByClass.keySet()) {
            List<Object> hitsForClass = resultsByClass.get(clazz);
            int nbHits = hitsForClass.size();
            String simpleName = clazz.getSimpleName();
    %>
            <div class="results-for-class" id="results-<%=simpleName%>">
                <h1 onclick="toggleClassResults('resultsTable-<%=simpleName%>')"><%=clazz.getSimpleName()%> (<%=nbHits%>)</h1>
                <div id="resultsTable-<%=simpleName%>">
                    <table class="search-results" cellspacing="0" cellpadding="2">
                        <tbody>
    <%
                            int i = 1;
                            for(Object o : hitsForClass) {
                                String trClass = "odd";
                                if (i % 2 == 0) {
                                    trClass = "even";
                                }
                                i++;
                                Float score = results.getScore(o);
                                String scoreStr = "N.D.";
                                if (score!=null) {
                                    scoreStr = score.toString();
                                }

    %>
                            <tr class="<%=trClass%>">
                                <td><woko:linkToEntity entity="<%=o%>"/></td>
                                <td>&nbsp;</td>
                                <td><i><%=scoreStr%></i></td>
                            </tr>
    <%
                            }
    %>
                        </tbody>
                    </table>
                </div>
            </div>
    <%
        }
    %>

</div>