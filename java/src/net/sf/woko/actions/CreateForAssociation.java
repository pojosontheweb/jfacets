package net.sf.woko.actions;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.facets.write.SaveObject;
import net.sf.woko.util.Util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;

@UrlBinding("/tools/create-for-association.action")
public class CreateForAssociation extends BaseActionBean implements ValidationErrorHandler {

    @Validate(required=true)
    private String id;
    @Validate(required=true)
    private String className;
    @Validate(required=true)
    private String propertyName;
    @Validate(required=true)
    private String newObjectClass;

    private Object object;

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

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getNewObjectClass() {
        return newObjectClass;
    }

    public void setNewObjectClass(String newObjectClass) {
        this.newObjectClass = newObjectClass;
    }

    public Object getObject() {
        return object;
    }

    @Before(LifecycleStage.BindingAndValidation)
    public void preBind() {
        try {
            object = Class.forName(getRequest().getParameter("newObjectClass")).newInstance();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Shows FORM for creating the new object
     */
    @DefaultHandler
    public Resolution displayFormAjax() {
        return new ForwardResolution("/WEB-INF/woko/fragments/write/associations/ajax-create.jsp");
    }

    public Resolution createAndAdd() {
        // persist the object...
        WebFacets wf = WebFacets.get(getRequest());
        SaveObject saveFacet =
                (SaveObject)wf.getFacet("saveObject", object, getRequest());
        saveFacet.save(true, getContext().getValidationErrors());

        // associate to owning object
        Object owningObject = Util.getObjectData(id, className, getContext())[0];
        PropertyDescriptor pd = Util.getPropertyDescriptor(owningObject, propertyName);
        if (Collection.class.isAssignableFrom(pd.getPropertyType())) {

            // prop is a collection, add to it
            Method readMethod = pd.getReadMethod();
            try {
                Collection c = (Collection)readMethod.invoke(owningObject);
                if (c==null) {
                    c = new ArrayList();
                }
                c.add(object);

            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                // TODO handle error !
                return null;
            }

        } else {

            // prop is a x-to-one, assign it !
            Method writeMethod = pd.getWriteMethod();
            try {
                writeMethod.invoke(owningObject, object);

            } catch (Exception e) {
                e.printStackTrace();
                // TODO handle error
                return null;
            }

        }

        saveFacet = (SaveObject)wf.getFacet("saveObject", owningObject, getRequest());
        saveFacet.save(false, getContext().getValidationErrors());

        // TODO message ?
        getContext().getMessages().add(
                new SimpleMessage(
                        "New <i>" +
                        Util.stripPackageName(newObjectClass) +
                        "</i> created and associated to <i>" +
                        Util.stripPackageName(className) +
                        "." +
                        propertyName +
                        "</i>"));

        StringBuilder message = new StringBuilder();
        String ctxPath = getRequest().getContextPath();
        String location = ctxPath + "/edit/" + Util.stripPackageName(className) + "/" + id;
        message.append("<script>window.location='");
        message.append(location);
        message.append("'</script>");

        return new StreamingResolution("text/html", new StringReader(message.toString()));
    }

     /** Converts errors to HTML and streams them back to the browser. */
    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        StringBuilder message = new StringBuilder();
        message.append("<div class=\"errors\"><ul class=\"errors\">");
        for (List<ValidationError> fieldErrors : errors.values()) {
            for (ValidationError error : fieldErrors) {
                message.append("<li>");
                message.append(error.getMessage(getContext().getLocale()));
                message.append("</li>");
            }
        }
        message.append("</ul></div>");

        return new StreamingResolution("text/html", new StringReader(message.toString()));
    }



}
