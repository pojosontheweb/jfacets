package net.sf.woko.actions.devel;

import net.sf.woko.actions.BaseActionBean;
import net.sf.woko.actions.EditActionBean;
import net.sf.woko.util.facettemplates.FacetTemplate;
import net.sf.woko.util.facettemplates.FacetTemplateManager;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.SimpleError;

import java.util.List;

@UrlBinding("/tools/devel/facet-wizzard.action")
public class NewFacetWizzardActionBean extends BaseActionBean {

    String templateName;

    public List<FacetTemplate> getFacetTemplates() {
        return new FacetTemplateManager().getFacetTemplates(getContext().getServletContext());
    }

    @DefaultHandler
    @DontValidate
    public Resolution display() {
        return new ForwardResolution("/WEB-INF/woko/facet-wizzard.jsp");
    }

    public Resolution create() {
        FacetTemplate template = null;
        for(FacetTemplate ft : getFacetTemplates()) {
            if (ft.getName().equals(templateName)) {
                template = ft;
                break;
            }
        }
        return new RedirectResolution(EditActionBean.class, "create").
                addParameter("isFresh", true).
                addParameter("className", "net.sf.woko.facets.WokoDbGroovyFacetDescriptor").
                addParameter("object.name", template.getName()).
                addParameter("object.sourceCode", template.getCode());
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @ValidationMethod
    public void validate() {
        if (templateName==null) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError("You must choose the type of the facet to create !"));
        }
    }

}
