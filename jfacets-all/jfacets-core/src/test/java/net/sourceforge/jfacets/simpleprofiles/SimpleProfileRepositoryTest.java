package net.sourceforge.jfacets.simpleprofiles;

import net.sourceforge.jfacets.IProfile;
import junit.framework.TestCase;

public class SimpleProfileRepositoryTest extends TestCase {

	private SimpleProfileRepository s;
	
	protected void setUp() throws Exception {
		super.setUp();
		s = new SimpleProfileRepository();
	}

	public void testCreateProfile() {
		IProfile p = s.createProfile("test1");
		assertNotNull(p);
		assertEquals(p.getId(), "test1");
	}

	public void testGetProfileById() {
		IProfile p = s.getProfileById("ivar");
		assertNotNull(p);
		assertEquals(p.getId(), "ivar");
	}

	public void testGetSuperProfiles() {
		IProfile p = s.getProfileById("ivar");
		IProfile[] superPfls = s.getSuperProfiles(p);
		assertNotNull(superPfls);
		assertEquals(1, superPfls.length);
	}

}
