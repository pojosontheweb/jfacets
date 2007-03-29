package net.sf.woko.facets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.impl.MetaFacetDescriptorManager;
import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.util.Util;

/**
 * Woko FacetDescriptorManager : enables DB and other "regular" facets.
 */
public class WokoDbFacetsDescriptorManager implements IFacetDescriptorManager {

	private static final Logger logger = Logger.getLogger(WokoDbFacetsDescriptorManager.class);
		
	/** cache of loaded DB facet descriptors */
	private ArrayList<WokoDbGroovyFacetDescriptor> dbDescriptors = new ArrayList<WokoDbGroovyFacetDescriptor>();	
	/** cache of loaded inactive DB facet descriptors */
	private ArrayList<WokoDbGroovyFacetDescriptor> inactiveDbDescriptors = new ArrayList<WokoDbGroovyFacetDescriptor>();
		
	public WokoDbFacetsDescriptorManager() {
		logger.info("manager created (db facets not yet loaded)");
	}
	
	public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
		logger.debug("Trying to find descriptor " + name + "," + profileId + "," + targetObjectType);
		// try to find in cached db facets descriptors
		for (WokoDbGroovyFacetDescriptor fd : dbDescriptors) {
			logger.debug("Trying decriptor " + fd);
            if (fd.getName().equals(name) &&
					fd.getProfileId().equals(profileId) && 
					fd.getTargetObjectType().equals(targetObjectType)) {
				// active descriptor found
				logger.debug("Found " + fd);
				return fd;
			}				
		}
		logger.debug("not found, returning null");
		return null;
	}

	public FacetDescriptor[] getDescriptors() {
		FacetDescriptor[] res = new FacetDescriptor[dbDescriptors.size()];
		res = dbDescriptors.toArray(res);
		logger.debug("getDescriptors() - returning " + res.length + " DB facet descriptors");
		return res;
	}

	/**
	 * Reloads DB facet descriptors from the database. 
	 */
	public synchronized void reloadDbDescriptors() {
		Session s = Util.getSession();
		int activeCount = 0;
		int inactiveCount = 0;
		Query q = s.createQuery("select f from WokoDbGroovyFacetDescriptor as f");
		dbDescriptors = new ArrayList<WokoDbGroovyFacetDescriptor>();
		inactiveDbDescriptors = new ArrayList<WokoDbGroovyFacetDescriptor>();
		for (Iterator iter = q.list().iterator(); iter.hasNext();) {
			WokoDbGroovyFacetDescriptor fd = (WokoDbGroovyFacetDescriptor) iter.next();
			if (fd.getActive()) {
				dbDescriptors.add(fd);
				activeCount++;
			} else {
				inactiveDbDescriptors.add(fd);
				inactiveCount++;
			}
		}
		logger.info("DB facets reloaded : " + activeCount + " active, " + inactiveCount + " inactive");
	}

	public List<WokoDbGroovyFacetDescriptor> getInactiveDbDescriptors() {
		return new ArrayList<WokoDbGroovyFacetDescriptor>(inactiveDbDescriptors);
	}
	
	public static WokoDbFacetsDescriptorManager get(HttpServletRequest request) {		
		IFacetDescriptorManager fdm = (IFacetDescriptorManager)
			WebFacets.get(request).getFacetRepository().getFacetDescriptorManager();
		if (fdm instanceof WokoDbFacetsDescriptorManager)
			return (WokoDbFacetsDescriptorManager)fdm;
		else if (fdm instanceof MetaFacetDescriptorManager) {
			MetaFacetDescriptorManager mfdm = (MetaFacetDescriptorManager)fdm;
			for (IFacetDescriptorManager m : mfdm.getManagers()) {
				if (m instanceof WokoDbFacetsDescriptorManager)
					return (WokoDbFacetsDescriptorManager)m;
			}	
		}		
		return null;
	}

}
