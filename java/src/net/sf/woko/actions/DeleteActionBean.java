package net.sf.woko.actions;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sf.woko.util.Util;
import net.sf.woko.facets.read.ViewObjectTitle;
import net.sf.woko.facets.write.DeleteObject;
import net.sourceforge.jfacets.web.WebFacets;

@UrlBinding("/@delete/:className/:id/:_eventName")
public class DeleteActionBean extends BaseActionBean {

    public static final String JSP_NAME = "/WEB-INF/woko/confirmDelete.jsp";  

    @Validate(required=true)
    private String id;

    @Validate(required=true)
    private String className;

    private Object target;

    private String objectTitle;

    private String objectType;

    private boolean deleted = false;

    private void computeObjectData() {
        target = Util.getObjectData(id, className, getContext())[0];
        WebFacets wf = WebFacets.get(getRequest());
        ViewObjectTitle vt = (ViewObjectTitle)wf.getFacet("viewTitle", target, getRequest());
        objectTitle = vt.getTitle();
        objectType = vt.getType();
    }

    @DefaultHandler
    public Resolution askConfirm() {
        computeObjectData();
        return new ForwardResolution(JSP_NAME);
    }

    public Resolution delete() {
        computeObjectData();
        // use a facet to delete the object & perform validation if needed...
        DeleteObject deleteObjectFacet =
            (DeleteObject)WebFacets.get(getRequest()).
                getFacet("deleteObject", target, getRequest());

        if (deleteObjectFacet==null) {
            // facet not found ??
            throw new RuntimeException("DeleteObject facet not found ! it should at least be assigned to Object.class " +
                    "so you should not have this exception ! check your facets assignation");
        }
        deleteObjectFacet.delete(getContext().getValidationErrors());
        
        return new RedirectResolution(getClass()).
                addParameter("_eventName", "confirmDeletion").
                addParameter("objectTitle", objectTitle).
                addParameter("objectType", objectType);
    }

    @DontValidate
    public Resolution confirmDeletion() {
        deleted = true;
        return new ForwardResolution(JSP_NAME);
    }

    public String getObjectType() {
        return objectType;
    }


    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectTitle() {
        return objectTitle;
    }


    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isDeleted() {
        return deleted;
    }

}
