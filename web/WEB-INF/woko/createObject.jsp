<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<stripes:useActionBean beanclass="net.sf.woko.actions.devel.HibernateToolsActionBean" var="actionBean"/>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:layout-render name="${layout}" title="Create object">
    <stripes:layout-component name="contents">
    
		<h1>
            <stripes:label for="woko.create.h1"/>
        </h1>

		<p>
            <stripes:label for="woko.create.msg"/>
		</p>
		
		<p>
		<stripes:form beanclass="net.sf.woko.actions.EditActionBean">
			<table class="properties" cellspacing="0" cellpadding="2">
				<stripes:hidden name="isFresh" value="true"/>
			<%--
				<tr>
					<th>Id (optional) : </th>
					<td><stripes:text name="id"/></td>
				</tr>				
				 --%>
				<tr>
					<th>
                        <stripes:label for="woko.create.class">
                            Class :
                        </stripes:label>
                    </th>
					<td>
						<stripes:select name="className">
							<stripes:option value=""></stripes:option>
							<stripes:options-collection collection="${actionBean.hibernatedConcreteClasses}"
									label="simpleName" value="name"/>
						</stripes:select>
						</td>
				</tr>				
				<tr>						
					<td colspan="2"><stripes:submit name="create"/></td>
				</tr>				
			</table>
		</stripes:form>
		</p>

    </stripes:layout-component>
</stripes:layout-render>