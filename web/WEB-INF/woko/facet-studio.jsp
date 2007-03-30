<%@ page import="net.sf.woko.facets.WokoDbGroovyFacetDescriptor" %>
<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<wf:execFacet name="getPageLayout" var="layout"/>

<%-- use the layout returned from the facet --%>
<stripes:layout-render name="${layout}" title="Facet Studio">

    <%-- we change the CSS for this page : we want a larger screen ! --%>
    <stripes:layout-component name="html-head-css">
        <link href="${pageContext.request.contextPath}/woko/woko-large.css" type="text/css" rel="stylesheet"/>
    </stripes:layout-component>

    <stripes:layout-component name="contents">

    <%-- build the url of the facet studio action for display tag --%>
    <stripes:url beanclass="net.sf.woko.actions.devel.FacetStudioActionBean" var="beanUrl"/>

    <div class="facetDescriptors">

        <h1>Facet Studio</h1>

        <p>
             <c:if test="${actionBean.useDbFacets}">
                <stripes:form beanclass="net.sf.woko.actions.devel.FacetStudioActionBean">
                    <%-- input and button for filtering --%>
                    <input type="text"/><input type="button" value="filter"/>
                    &nbsp;
                    &nbsp;
                    <stripes:submit name="createDynamicFacet" value="Create new dynamic facet"/>
                    <stripes:submit name="reload" value="Reload dynamic facets"/>
                    <stripes:submit name="dumpAsXml" value="Dump all facets as XML"/>
                </stripes:form>


            </c:if>

            <%-- we use display tag to show existing facets... --%>
            <display:table name="${actionBean.allDescriptors}"
                           id="curD"
                           requestURI="${beanUrl}"
                           class="facetDescriptors"
                           cellpadding="2"
                           cellspacing="0">

                <%-- link for editing dynamic facets --%>
                <display:column title="edit" sortable="true" headerClass="facetDescriptorsHeader">
                    <% if (pageContext.getAttribute("curD") instanceof WokoDbGroovyFacetDescriptor) { %>
                        <woko:linkToEntity entity="${curD}" text="[edit]"/>
                    <% } else { %>
                        &nbsp;
                    <% } %>
                </display:column>

                <%-- status (active/inactive/static) --%>
                <display:column title="status" sortable="true" headerClass="facetDescriptorsHeader">
                    <%
                        if (pageContext.getAttribute("curD") instanceof WokoDbGroovyFacetDescriptor) {
                            WokoDbGroovyFacetDescriptor dbd = (WokoDbGroovyFacetDescriptor)pageContext.getAttribute("curD");
                            if (dbd.getActive()) {  %>
                                dyn. (active)
                    <%
                            } else {
                    %>
                                dyn. (inactive)
                    <%
                            }
                        } else {
                    %>
                                static
                    <%
                        }
                    %>
                </display:column>

                <%-- facet name --%>
                <display:column property="name" sortable="true" headerClass="facetDescriptorsHeader"/>

                <%-- profile id --%>
                <display:column property="profileId" sortable="true" headerClass="facetDescriptorsHeader"/>

                <%-- target type --%>
                <display:column title="TargetObjectType" sortable="true" headerClass="facetDescriptorsHeader">
                    ${curD.targetObjectType.name}
                </display:column>

                <%-- facet class --%>
                <display:column title="FacetClass" sortable="true" headerClass="facetDescriptorsHeader">
                    <c:set var="facetClass" value="${curD.facetClass}"/>
                        ${curD.facetClass.name}
                </display:column>

                <display:footer>
                    ${fn:length(actionBean.allDescriptors)} registered facet descriptors                
                </display:footer>

            </display:table>

        </p>
    </div>

    </stripes:layout-component>
</stripes:layout-render>