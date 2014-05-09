package net.sourceforge.jfacets.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.FacetDescriptorManagerBase;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.log.JFacetsLogger;

import org.xml.sax.SAXException;

/**
 * The FacetDescriptorManager : handles loading of descriptors from an 
 * XML resource, and provides access to them.
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 * 
 */
public class FacetDescriptorManager extends FacetDescriptorManagerBase {

	private static final JFacetsLogger logger = JFacetsLogger.getLogger(FacetDescriptorManager.class);

	/** location of the facet descriptors file */
	private String facetsFilePath = null;

	/**
	 * Creates the manager and loads descriptors.
	 * @param facetsFilePath The path of the facets XML descriptor to be used (CLASSPATH resource)
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws FactoryConfigurationError
	 */
	public FacetDescriptorManager(String facetsFilePath) 
		throws IOException, ParserConfigurationException, SAXException, FactoryConfigurationError {
		
		this.facetsFilePath = facetsFilePath;
		if (!this.facetsFilePath.startsWith("/"))
			this.facetsFilePath = "/" + this.facetsFilePath;
		int nbDesc = loadDescriptors();
		if (logger.isInfoEnabled()) logger.info("FacetDescriptorManager created, " + nbDesc + " descriptor(s) loaded");
	}
	
	protected int loadDescriptors() throws IOException, ParserConfigurationException, SAXException, FactoryConfigurationError {
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : getting facets XML file from " + facetsFilePath);
		InputStream is = getClass().getResourceAsStream(facetsFilePath);
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : parsing...");
		FacetsSaxParser parser = new FacetsSaxParser(is);
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : ... parsed");
		List<FacetDescriptor> descriptors = parser.getDescriptors();
        setDescriptors(descriptors);
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : " + descriptors.size() + " descriptors loaded OK");
		return descriptors.size();
	}

}
