package net.sf.woko.actions.devel;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.actions.BaseActionBean;
import net.sf.woko.facets.WokoDbGroovyFacetDescriptor;
import net.sf.woko.facets.WokoDbFacetsDescriptorManager;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.LifecycleStage;

@UrlBinding("/tools/devel/facet-studio.action")
public class FacetStudioActionBean extends BaseActionBean {

    /**
     * A list containing all facet descriptors (both static and active/inactive
     * dynamic).
     * This list is created before binding and validation.
     */
    private List<FacetDescriptor> allDescriptors;

    /**
     * The dynamic facet descriptor manager.
     * Loaded before binding and validation.
     */
    private WokoDbFacetsDescriptorManager fdm;

    /**
     * Gets hold of required stuff, and populates the list of descriptors.
     */
    @Before(LifecycleStage.BindingAndValidation)
	public void before() {
		// get fdm
		fdm = WokoDbFacetsDescriptorManager.get(getRequest());
		// compute list
        allDescriptors = new ArrayList<FacetDescriptor>();
        FacetDescriptor[] allActive = WebFacets.get(getRequest()).getFacetRepository().getFacetDescriptorManager().getDescriptors();
        for(FacetDescriptor fd : allActive) {
            allDescriptors.add(fd);
        }
        allDescriptors.addAll(getInactiveDbDescriptors());
	}
	
	public boolean isUseDbFacets() {
		return fdm!=null;
	}

	private List<WokoDbGroovyFacetDescriptor> getInactiveDbDescriptors() {
		return WokoDbFacetsDescriptorManager.get(getRequest()).getInactiveDbDescriptors();
	}

	public List<FacetDescriptor> getAllDescriptors() {
		return allDescriptors;
	}

    /**
     * Default handler, returna Forward Resolution to the JSP.
     */
    @DefaultHandler
	public Resolution display() {
        if (!isUseDbFacets()) {
            getContext().getMessages().add(new SimpleMessage("Dynamic facets are not enabled !"));
        }
        return new ForwardResolution("/WEB-INF/woko/facet-studio.jsp");
	}

    /**
     * Reloads the dynamic facet descriptors and redirects to this action
     * @return
     */
    public Resolution reload() {
		fdm.reloadDbDescriptors();
		getContext().getMessages().add(new SimpleMessage("Facet descriptors reloaded"));
        return new RedirectResolution(FacetStudioActionBean.class);
	}

    /**
     * Return a streaming resolution with the XML descriptors for all facets.
     */
    public Resolution dumpAsXml() {
		WebFacets wf = WebFacets.get(getRequest());
		String data = wf.dumpFacetsAsXml();
		return new StreamingResolution("text/xml", data);
	}

    @DontValidate
    public Resolution createDynamicFacet() {
        return new RedirectResolution(NewFacetWizzardActionBean.class);
        //return new RedirectResolution(EditActionBean.class, "create").
          //      addParameter("isFresh", true).
            //    addParameter("className", "net.sf.woko.facets.WokoDbGroovyFacetDescriptor");
    }

}
