package net.sf.woko.actions.devel;

import net.sf.woko.actions.BaseActionBean;
import net.sf.woko.actions.EditActionBean;
import net.sf.woko.util.facettemplates.FacetTemplate;
import net.sf.woko.util.facettemplates.FacetTemplateManager;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.jfacets.web.WebFacets;
import net.sourceforge.jfacets.IProfileRepository;
import net.sourceforge.jfacets.IProfile;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.Authentication;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 19 mars 2007
 * Time: 19:54:16
 * To change this template use File | Settings | File Templates.
 */
@UrlBinding("/tools/override-facet.action")
public class OverrideFacetActionBean extends BaseActionBean {

    @Validate(required = true)
    private String facetName;

    @Validate(required = true)
    private String profileId;

    @Validate(required = true)
    private String targetType;

    public String getFacetName() {
        return facetName;
    }

    public void setFacetName(String facetName) {
        this.facetName = facetName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @DefaultHandler
    public Resolution display() {
        // is the current user of role DEVELOPER ???
        WebFacets wf = WebFacets.get(getRequest());
        Object overrideFacet = wf.getFacet("overrideFacet", getRequest());
        if (overrideFacet==null) {
            // log current user out
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
            getRequest().getSession().invalidate();
//            SecurityContextHolder.getContext().
        }
        // issue a redirect...
        RedirectResolution rr = new RedirectResolution(EditActionBean.class, "create");
        rr.addParameter("isFresh", true);
        rr.addParameter("className", "net.sf.woko.facets.WokoDbGroovyFacetDescriptor");
        rr.addParameter("object.name", facetName);
        rr.addParameter("object.profileId", profileId);
        rr.addParameter("object.targetObjectClassName", targetType);
        // obtain template and add code example if any...
        FacetTemplateManager ftm = new FacetTemplateManager();
        for(FacetTemplate ft : ftm.getFacetTemplates(getContext().getServletContext())) {
            if (ft.getName().equals(facetName)) {
                rr.addParameter("object.sourceCode", ft.getCode());
                break;
            }
        }
        return rr;
    }
}
