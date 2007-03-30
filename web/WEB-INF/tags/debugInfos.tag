<%@ tag import="net.sf.woko.actions.devel.DebugActionBean" %>
<%@ tag import="org.acegisecurity.Authentication" %>
<%@ tag import="org.acegisecurity.context.SecurityContextHolder" %>
<%@ tag import="java.util.List" %>
<%@ tag import="org.acegisecurity.userdetails.UserDetails" %>

<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<%
    boolean debug = false;
    int count = 0;
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
        List<String> debuggedProfiles = DebugActionBean.getDebuggedUsers(application);
        if (debuggedProfiles != null) {
            // is the current user being debugged ?
            Object principal = auth.getPrincipal();
            String userName = null;
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
                for (String curProfile : debuggedProfiles) {
                    if (curProfile.equals(userName)) {
                        debug = true;
                        Integer i = (Integer) request.getAttribute("facetCount");
                        if (i != null)
                            count = i;
                        count++;
                        request.setAttribute("facetCount", count);
                        break;
                    }
                }
            }
        }
    }
    if (debug) {
%>
    <div class="facetDebug" id="facetDebugDiv<%=count%>">
        <a href="javascript:showDebugPopup(<%=count%>);">
            <img src="${pageContext.request.contextPath}/woko/images/debug.gif"
                 border="0"
                 alt="show facet info"
                 id="facetDebugLink<%=count%>"
                 class="facetDebugLink"/>
        </a>
        <div class="popup" id="popup<%=count%>">
            <h3>
                <img src="${pageContext.request.contextPath}/woko/images/message.png"/> &nbsp; 
                Facet information
            </h3>
            <table class="facetInfo" cellpadding="2" cellspacing="0">
                <tbody>
                    <tr>
                        <th class="facetInfo">Name</th>
                        <td class="facetInfo">${facet.context.facetName}</td>
                    </tr>
                    <tr>
                        <th class="facetInfo">Assigned to Profile</th>
                        <td class="facetInfo">${facet.context.facetDescriptor.profileId}</td>
                    </tr>
                    <tr>
                        <th class="facetInfo">Assigned to Type</th>
                        <td class="facetInfo">${facet.context.facetDescriptor.targetObjectType.name}</td>
                    </tr>
                     <tr>
                        <td colspan="2"></td>
                    </tr>
                    <tr>
                        <th class="facetInfo">Requesting Profile</th>
                        <td class="facetInfo">${facet.context.profile.id}</td>
                    </tr>
                    <tr>
                        <th class="facetInfo">Target Object</th>
                        <td class="facetInfo">${facet.context.targetObject}</td>
                    </tr>

                    <tr>
                        <th class="facetInfo">Facet class</th>
                        <td class="facetInfo">
                            <%--
                            <c:set var="convertedClassName">
                                ${fn:replace(facet.class.name, ".", "/")}
                            </c:set>
                            <a href="http://jfacets.rvkb.com/pub/woko-javadocs/${convertedClassName}.html"
                               target="_new">
                                ${facet.class.name}
                            </a>
                            --%>
                            ${facet.class.name}
                        </td>
                    </tr>
                </tbody>
            </table>
            <br/>
            <a href="javascript:hide('popup<%=count%>');">
                [close]
            </a>
            &nbsp;
            <%-- compute target type : nothing or the real target object's type --%>
            <c:choose>
                <c:when test="${not empty facet.context.targetObject}">
                    <c:set var="overrideType" value="${facet.context.targetObject.class.name}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="overrideType" value="CHOOSE TYPE"/>
                </c:otherwise>
            </c:choose>
            <stripes:link beanclass="net.sf.woko.actions.devel.OverrideFacetActionBean">
                <stripes:link-param name="facetName" value="${facet.context.facetName}"/>
                <stripes:link-param name="profileId" value="${facet.context.profile.id}"/>
                <stripes:link-param name="targetType" value="${overrideType}"/>
                [override facet]
            </stripes:link>
            (may require re-authentication as developer)
        </div>

        <jsp:doBody/>

    </div>
<% } else { %>
    <jsp:doBody/>
<% } %>
