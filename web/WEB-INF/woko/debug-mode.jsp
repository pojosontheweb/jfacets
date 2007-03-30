<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>
<stripes:layout-render name="${layout}" title="Debug mode">
    <stripes:layout-component name="contents">

        <h1>Facet debugging</h1>
        <p>
            Facet debugging allows to generate additional informations in the page
            as popups that show which facet is used to render what, and give you necessary
            informations for the overriding.
        </p>
        <c:choose>
            <c:when test="${actionBean.debug}">
                <h2>Currently debugged user(s)</h2>
                <p>
                    Here is the list of currently debugged users :
                    <c:choose>
                        <c:when test="${fn:length(actionBean.debuggedUsers)>0}">
                            <ul>
                                <c:forEach items="${actionBean.debuggedUsers}" var="userName">
                                    <li>
                                        ${userName}
                                        <stripes:link beanclass="net.sf.woko.actions.devel.DebugActionBean" event="removeUserForDebug">
                                            <stripes:link-param name="userName" value="${userName}"/>
                                            [remove]
                                        </stripes:link>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <ul>
                                <li><i>No users debugged for the moment.</i></li>
                            </ul>
                        </c:otherwise>
                    </c:choose>

                </p>
                <h3>Add a user for debug</h3>
                <p>
                    <stripes:form
                            beanclass="net.sf.woko.actions.devel.DebugActionBean">
                        <stripes:text name="userName"/>
                        <stripes:submit name="addUserForDebug"/>
                    </stripes:form>
                </p>
            </c:when>
            <c:otherwise>
                <div class="noteMacro">
                    Debugging is disabled ! Please check your web.xml settings in order to
                    enable debug infos.
                </div>
            </c:otherwise>
        </c:choose>
    </stripes:layout-component>
</stripes:layout-render>