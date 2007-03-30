<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:useActionBean beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean" var="hbTools"/>

<stripes:layout-render name="${layout}" title="Hibernate finders">
    <stripes:layout-component name="contents">

		<h1>Hibernate Tools</h1>

		<h2>Find by Type</h2>
		<p>Lookup for an object by type :
		<stripes:form beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean">
			<table class="properties" cellspacing="0" cellpadding="2">
				<tr>
					<th>Class : </th>
					<td>
						<stripes:select name="classNameForList">
							<stripes:option value=""></stripes:option>
							<stripes:options-collection collection="${hbTools.hibernatedClasses}"
									label="simpleName" value="name"/>
						</stripes:select>
					</td>
				</tr>				
				<tr>						
					<td colspan="2"><stripes:submit name="list"/></td>
				</tr>				
			</table>
		</stripes:form>
		</p>
				
		<h2>Query for objects</h2>
		<p>Type in your HQL query :
		<stripes:form beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean">
			<stripes:hidden name="className" value="dummy"/>
			<table class="properties" cellspacing="0" cellpadding="2">
				<tr>
					<th>HQL Query : </th>
					<td><stripes:textarea name="hql" cols="50" rows="5"/></td>
				</tr>				
				<tr>						
					<td colspan="2"><stripes:submit name="listHql"/></td>
				</tr>				
			</table>
		</stripes:form>
		</p>
		
    </stripes:layout-component>
</stripes:layout-render>