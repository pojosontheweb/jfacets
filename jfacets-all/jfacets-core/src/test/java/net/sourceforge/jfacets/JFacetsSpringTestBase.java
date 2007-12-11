package net.sourceforge.jfacets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

/**
 * Base test case that provides access to 
 * the JFacets bean via Spring.
 */
public abstract class JFacetsSpringTestBase extends TestCase {

	protected JFacets jFacets;
	protected ApplicationContext applicationContext;
	
	protected String appContextPath;
	
	public JFacetsSpringTestBase(String appContextPath) {
		this.appContextPath = appContextPath;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		applicationContext = new ClassPathXmlApplicationContext(new String[]{ appContextPath });
		jFacets = (JFacets)applicationContext.getBean("jFacets");
		assertNotNull(jFacets);
	}
	
}
