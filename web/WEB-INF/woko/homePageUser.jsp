<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:useActionBean
        beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean"
        var="hbTools"
        />

<stripes:layout-render name="${layout}" title="Home">
    <stripes:layout-component name="contents">

        <h1><stripes:label for="woko.user.home.h1"/></h1>

        <p>
			<stripes:label for="woko.user.home.msg"/>
		</p>

        <p>
           <stripes:form beanclass="net.sf.woko.actions.SearchActionBean">
               <stripes:label for="woko.user.home.search.full.text"/>
               <stripes:text name="query"/>
               <stripes:submit name="searchCompass"/>
           </stripes:form>
        </p>

        <p>
           <stripes:form beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean">
               <stripes:label for="woko.user.home.search.by.class"/>
               <stripes:select name="classNameForList">
                   <stripes:option value=""></stripes:option>
                   <stripes:options-collection collection="${hbTools.hibernatedClasses}"
                           label="simpleName" value="name"/>
               </stripes:select>
               <stripes:submit name="list"/>
           </stripes:form>
        </p>


    </stripes:layout-component>
</stripes:layout-render>