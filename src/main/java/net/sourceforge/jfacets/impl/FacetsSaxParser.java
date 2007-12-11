package net.sourceforge.jfacets.impl;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;


import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.log.JFacetsLogger;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser for facets XML descriptors.
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 */
public class FacetsSaxParser extends DefaultHandler  {
	
	/** a vector of loaded descriptors */
	private Vector descriptors = new Vector();
	/** the number of skipped descriptors */
	private int nbSkipped = 0;
	/** the logger to be used */
	private static final JFacetsLogger logger = JFacetsLogger.getLogger(FacetsSaxParser.class);
	
	/**
	 * Creates object, parses xml from supplied InputStream and creates descriptors
	 */
	public FacetsSaxParser (InputStream xml) throws IOException, ParserConfigurationException, SAXException, FactoryConfigurationError {
		//  create a Xerces SAX parser
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		//  parse the document
		parser.parse(new InputSource(xml), this);
		if (logger.isInfoEnabled()) logger.info("Stream parsed OK, " + descriptors.size() + " descriptors loaded");
	}
	
	/**
	 * Returns all loaded descriptors
	 */
	@SuppressWarnings("unchecked")
	public FacetDescriptor[] getDescriptors() {
		FacetDescriptor[] res = new FacetDescriptor[descriptors.size()];
		res = (FacetDescriptor[])descriptors.toArray(res);
		return res;
	}
	
	public void startDocument() {
		if (logger.isDebugEnabled()) logger.debug("Starting XML parsing...");
	}
	
	// call at element start
	@SuppressWarnings("unchecked")
	public void startElement (String uri, String local, String qName, Attributes atts) {
		if (qName.equals("facet")) {
			if (logger.isDebugEnabled()) logger.debug("startElement() : found a facet...");
			String facetName = null;
			String profile = null;
			Class objectType = null;
			Class facetClass = null;
			
			for (int i = 0; i < atts.getLength(); i++) {
				String attrName = atts.getQName(i);
				String attrVal = atts.getValue(attrName);
				
				if (attrName.equals("name")) {
					facetName = attrVal;
				} else if (attrName.equals("profile")) {
					profile = attrVal;
				} else if (attrName.equals("object_type")) {
					try {
						objectType = Class.forName(attrVal);
					}catch(ClassNotFoundException cnfex) {
						logger.warn("ClassNotFoundException caught ! Type could not be resolved (name=" + facetName + " profile=" + profile + " objectType=" + objectType + ") - DESCRIPTOR SKIPPED");
						nbSkipped++;
					}
				} else if (attrName.equals("class")) {
					try {
						if (logger.isDebugEnabled()) logger.debug("Trying to retrieve Class=" + attrVal + "...");
						facetClass = Class.forName(attrVal);
						if (logger.isDebugEnabled()) logger.debug("... OK, Class=" + facetClass);
					}catch(ClassNotFoundException cnfex) {
						logger.warn("ClassNotFoundException caught ! Class '" + attrVal + "' could not be resolved, Class.forName() failed ! - DESCRIPTOR SKIPPED");
					}
				}
				
				// creates descriptor and adds to vector if OK...
				if ((i==atts.getLength()-1) &&
						( (facetName!=null) && (profile!=null) && (objectType!=null) && (facetClass!=null))) {
					
					if (logger.isDebugEnabled()) logger.debug("Creating descriptor...");
					FacetDescriptor d = new FacetDescriptor();
					d.setName(facetName);
					d.setProfileId(profile);
					d.setTargetObjectType(objectType);
					d.setFacetClass(facetClass);
					descriptors.add(d);
					if (logger.isDebugEnabled()) logger.debug("facet descriptor created OK (" + d + ") and added to list");
				}
			}
		}
	}
	
	// call when cdata found
	public void characters(char[] text, int start, int length) {
	}
	
	// call at element end
	public void endElement (String uri, String local, String qName){
	}
	
	// call at document end
	public void endDocument() {
		if (logger.isInfoEnabled()) logger.info("Facets XML descriptor parsing over : found " + descriptors.size() + " descriptor(s)");
	}
	
}
