package net.sourceforge.jfacets.impl;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IFacetFactory;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;
import net.sourceforge.jfacets.simpleprofiles.SimpleProfileRepository;

public class FacetRepositoryImplTest extends TestCase {

	private IFacetFactory facetFactory;
	private IProfileRepository profileRepo; 
	private IFacetContextFactory facetCtxFactory;
	private IFacetDescriptorManager facetDescriptorManager;
	
	private FacetRepositoryImpl facetRepo;
	
	@Override
	public void setUp() {
		profileRepo = new SimpleProfileRepository();
		facetFactory = new DefaultFacetFactory();
		facetCtxFactory = new DefaultFacetContextFactory();
		try {
			facetDescriptorManager = new FacetDescriptorManager("test-facets.xml");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception caught : " + e);
		}
		facetRepo = new FacetRepositoryImpl(profileRepo, facetFactory, facetCtxFactory, facetDescriptorManager);
		assertNotNull(facetRepo);
	}
		
	public void testGetDirectSuperTypes() {
		Class[] classes = FacetRepositoryImpl.getDirectSuperTypes(ArrayList.class);	
		assertNotNull(classes);
		assertEquals(classes.length, 5);
		classes = FacetRepositoryImpl.getDirectSuperTypes(DefaultFacet.class);
		assertNotNull(classes);
		assertEquals(classes.length, 2);
		classes = FacetRepositoryImpl.getDirectSuperTypes(Object.class);
		assertNotNull(classes);
		assertEquals(classes.length, 0);
	}

	public void testGetProfileRepository() {
		assertNotNull(facetRepo.getProfileRepository());
		assertEquals(facetRepo.getProfileRepository(), profileRepo);
	}

	public void testGetFacetFactory() {
		assertNotNull(facetRepo.getFacetFactory());
	}

	public void testGetFacetContextFactory() {
		assertNotNull(facetRepo.getFacetContextFactory());
	}

	public void testGetFacetDescriptorManager() {
		assertNotNull(facetRepo.getFacetDescriptorManager());
	}

	public void testGetFacetStringIProfileObject() {		
		// (test, ivar, "blah")
		String name = "test";
		IProfile profile = profileRepo.getProfileById("ivar");
		Object target = new String("blah");
		Object facet = facetRepo.getFacet(name, profile, target);
		assertNotNull(facet);
		assertEquals(facet.getClass(), DefaultFacet.class);
		
		// (test, john, "blah")
		profile = profileRepo.getProfileById("john");
		target = new String("blah");
		facet = facetRepo.getFacet(name, profile, target);
		assertNotNull(facet);
		assertEquals(facet.getClass(), DefaultFacet.class);
	}

	public void testGetFacetStringIProfileObjectClass() {
		// (test, ivar, s, String.class)
		String name = "test";
		IProfile profile = profileRepo.getProfileById("ivar");
		Object target = "blah";
		Class targetClass = target.getClass();
		Object facet = facetRepo.getFacet(name, profile, target, targetClass);
		assertNotNull(facet);
		assertEquals(facet.getClass(), DefaultFacet.class);
		
		// (test, john, "blah", String.class)
		profile = profileRepo.getProfileById("john");
		target = "blah";
		targetClass = target.getClass();
		facet = facetRepo.getFacet(name, profile, target, targetClass);
		assertNotNull(facet);
		assertEquals(facet.getClass(), DefaultFacet.class);
		
		// test, john, null, String.class)
		target = null;
		targetClass = String.class;
		facet = facetRepo.getFacet(name, profile, target, targetClass);
		assertNotNull(facet);
		assertEquals(facet.getClass(), DefaultFacet.class);
		
	}

}
