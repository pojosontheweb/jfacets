<%@ page import="net.sf.woko.facets.WokoDbGroovyFacetDescriptor" %>
<%@ page import="de.java2html.Java2Html" %>
<%@ page import="net.sf.woko.util.facettemplates.FacetTemplate" %>
<%@ include file="/WEB-INF/woko/taglibs.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<wf:execFacet name="getPageLayout" var="layout"/>

<%-- use the layout returned from the facet --%>
<stripes:layout-render name="${layout}" title="Facet Studio">

    <%-- we change the CSS for this page : we want a larger screen ! --%>
    <stripes:layout-component name="html-head-css">
        <link href="${pageContext.request.contextPath}/woko/woko-large.css" type="text/css" rel="stylesheet"/>
    </stripes:layout-component>

    <stripes:layout-component name="html-head">
        <script type="text/javascript">
            function showCode(templateName) {
                var div = document.getElementById('code-' + templateName);
                if (div.style.display == 'none') {
                    div.style.display = "";
                } else {
                    div.style.display = "none";
                }
            }
        </script>
    </stripes:layout-component>

    <stripes:layout-component name="contents">
        <div class="facet-wizzard">

            <stripes:form beanclass="net.sf.woko.actions.devel.NewFacetWizzardActionBean">
                <h1>Dynamic Facet Wizzard</h1>
                <p>
                    Please select the type of facet you want to create :
                    <br/><br/>
                    <display:table name="${actionBean.facetTemplates}"
                           id="facetTemplate"
                           class="facetDescriptors"
                           cellpadding="2"
                           cellspacing="0">

                        <display:column headerClass="facetDescriptorsHeader" title="&nbsp;">
                            <stripes:radio value="${facetTemplate.name}" name="templateName"/>
                        </display:column>
                        <display:column title="facet type" headerClass="facetDescriptorsHeader">
                            <strong>${facetTemplate.name}</strong>
                        </display:column>
                        <display:column title="comments" headerClass="facetDescriptorsHeader" property="comment"/>
                        <display:column title="code example" headerClass="facetDescriptorsHeader">
                            <a href="javascript:showCode('${facetTemplate.name}');" class="codeLink">Show code</a>
                            <div id="code-${facetTemplate.name}" style="display: none;">
                                <%
                                    FacetTemplate ft = (FacetTemplate)pageContext.getAttribute("facetTemplate");
                                    String formattedCode = Java2Html.convertToHtml(ft.getCode());
                                %>
                                <%=formattedCode%>
                            </div>
                        </display:column>
                    </display:table>

                    <stripes:submit name="create"/>
            </stripes:form>

        </div>
    </stripes:layout-component>
</stripes:layout-render>