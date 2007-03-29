package net.sf.woko.search;

import java.util.List;

import net.sf.woko.util.Util;

import org.compass.annotations.Searchable;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassConfigurationFactory;
import org.compass.gps.CompassGps;
import org.compass.gps.CompassGpsDevice;
import org.compass.gps.device.hibernate.Hibernate3GpsDevice;
import org.compass.gps.impl.SingleCompassGps;

/**
 * Compass utilities
 */
public class CompassUtil {
	
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(CompassUtil.class);
	
	/** the app-wide compass instance */
	private static Compass COMPASS_INSTANCE;
		
	/**
	 * Initializes compass
	 */
	@SuppressWarnings("unchecked")
	public static void initialize() {
		if (COMPASS_INSTANCE==null) {
			logger.info("Initializing Compass Util...");
			CompassConfiguration conf = CompassConfigurationFactory.newConfiguration(); 		
			conf.configure();
			// get a list of persistent classes and add them to compass...
			List<Class> hibernatedClasses = Util.getHibernatedClasses();
			for (Class clazz : hibernatedClasses) {
				// is that class searchable ???
				Searchable s = (Searchable)clazz.getAnnotation(Searchable.class);
				if (s!=null) {				
					conf.addClass(clazz);
					logger.info("Class " + clazz + " added to compass (has the @Searchable annotation)");
				} else {
					logger.info("Class " + clazz + " does not have the @Searchable annotation, not searchable");
				}
			}
			COMPASS_INSTANCE = conf.buildCompass();
            logger.info("Compass built : " + COMPASS_INSTANCE);
			CompassGps gps = new SingleCompassGps(COMPASS_INSTANCE);
			CompassGpsDevice hibernateDevice =
			     new Hibernate3GpsDevice("hibernate", Util.getSessionFactory());
			gps.addGpsDevice(hibernateDevice);
			gps.start();
			logger.info("Hibernate3 device started");
		} else {
			logger.warn("Trying to initialize compass but it's already there !");
		}
	}
	
	/**
	 * Shuts compass down
	 */
	public static void shutdown() {
        COMPASS_INSTANCE.close();
	}
	
	/**
	 * Performs a compass search
	 */
	public static CompassSearchResults search(String query) {
		CompassTemplate template = new CompassTemplate(COMPASS_INSTANCE);
		return (CompassSearchResults)template.execute(new SearchCallback(query));
	}
	 
}

/**
 * Simple compass callback that performs a search for a given query 
 */
class SearchCallback implements CompassCallback {
	private final String query;

	public SearchCallback(String query) {
		this.query = query;
	}

	public Object doInCompass(CompassSession session) throws CompassException {
		CompassHits hits = session.find(query);
        return new CompassSearchResults(query, hits);
	}
}
