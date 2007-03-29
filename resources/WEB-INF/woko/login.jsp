<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<wf:execFacet name="getPageLayout" var="layout"/>

<stripes:layout-render name="${layout}" title="Authentication required">
    <stripes:layout-component name="contents">

      	<div class="login-form">
      	
  			<h2>
				<stripes:label for="woko.login.text">
					Please log-in
				</stripes:label>
  			</h2>
  						
			<p>			
							   
				<stripes:form beanclass="net.sf.woko.actions.LoginActionBean">
			      <table>
			        <tr>
			        	<td>
			        		<stripes:label for="woko.login.label.user">
			        			User:
			        		</stripes:label>	
			        	</td>
			        	<td><input type='text' name='j_username' value="${actionBean.usernameStr}"/></td>
			        </tr>
			        <tr>
			        	<td>
							<stripes:label for="woko.login.label.password">
								Password:
			        		</stripes:label>	
			        	</td>
			        	<td><input type='password' name='j_password'></td>
			        </tr>
			        <tr>
			        	<td>
			        		<input type="checkbox" name="_acegi_security_remember_me">
			        	</td>
			        	<td>
			        		<stripes:label for="woko.login.label.rememberMe">
								Remember me
			        		</stripes:label>	
			        	</td>
			        </tr>
			
			        <tr><td colspan='2'><stripes:submit name="submitLogin"/></td></tr>
			      </table>		
				
				</stripes:form>			    
		    </p>
		</div>
		
    </stripes:layout-component>
</stripes:layout-render>