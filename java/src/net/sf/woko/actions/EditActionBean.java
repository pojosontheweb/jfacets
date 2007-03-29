package net.sf.woko.actions;

import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.facets.write.EditObject;
import net.sf.woko.facets.write.SaveObject;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.action.*;

import org.apache.log4j.Logger;

/**
 * Action bean used for editing objects. 
 */
@UrlBinding("/@edit/:className/:id/")
public class EditActionBean extends BaseActionBean {

	public static final String JSP_EDIT = "/WEB-INF/woko/edit.jsp";
	
	public static final String KEY_SESS_FRESHLY_CREATED = "freshlyCreated";
	
	private static final Logger logger = Logger.getLogger(EditActionBean.class);
	
	@Validate(required=true,on="create")
	private String className;
	
	private String id;
	
	private Object object;
	
	private boolean isNew = false;
	
	@Before(LifecycleStage.BindingAndValidation)
	public void loadObject() {
		String s = getRequest().getParameter("isFresh");
		if (s==null) {		
			id = getRequest().getParameter("id");
			className = getRequest().getParameter("className");
			Object[] objectData = Util.getObjectData(id, className, getContext());
			object = objectData[0];
			isNew = false;			
		} else {
            object = getRequest().getSession().getAttribute(KEY_SESS_FRESHLY_CREATED);
			if (object!=null) {
				isNew = true;		
				id = Util.getId(object);
				className = object.getClass().getName();
			}
		}
	}
	
	private boolean saveObject() {
		// use a facet to save the object & perform validation if needed...
		SaveObject saveObjectFacet = 
			(SaveObject)WebFacets.get(getRequest()).
				getFacet("saveObject", object, getRequest());
		
		if (saveObjectFacet==null)
			return false;
		saveObjectFacet.save(isNew, getContext().getValidationErrors());		
		
		getRequest().getSession().removeAttribute(KEY_SESS_FRESHLY_CREATED);
		logger.info("New object " + object + " created and persisted successfuly !");
		if (isNew)
			getContext().getMessages().add(new LocalizableMessage("woko.create.info.created"));
		else 
			getContext().getMessages().add(new LocalizableMessage("woko.edit.info.saved"));
		return true;
	}	
	
	@DefaultHandler
    public Resolution edit() {
		getContext().getRequest().setAttribute(EditObject.KEY_REQ_TARGET_OBJECT, object);
		return new ForwardResolution(JSP_EDIT);
	}
	
	@HandlesEvent("save")
	public Resolution save() {
		boolean saved = saveObject();
		if (!saved)
			return error("You don't seem to be allowed to save this object !", new Object[0]);

		RedirectResolution rr = new RedirectResolution(EditActionBean.class);
		String id = Util.getId(object);
		String daFullClass = object.getClass().getName();
		rr.addParameter("id", new Object[]{ id });
		rr.addParameter("className", new Object[]{ daFullClass });
		return rr;
	}
			
	@HandlesEvent("create")
	public Resolution create() {
		Class clazz;
		try {
			clazz = Class.forName(className);			
		}catch(ClassNotFoundException e) {
			String msg = "Unable to find specified class : " + className;
			logger.error(msg, e);
			return error(msg, new Object[0]);
		}
		
		try {
			object = clazz.newInstance();
			getRequest().getSession().setAttribute(KEY_SESS_FRESHLY_CREATED, object);
		} catch (Exception e) {
			String msg = "Unable to create instance of the specified class using default constructor : " + className;
			logger.error(msg, e);
			return error(msg, new Object[0]);
		}
		getContext().getMessages().add(new LocalizableMessage("woko.create.info.transient", new Object[0]));
		return new ForwardResolution(JSP_EDIT);		
	}
	
	@HandlesEvent("closeEditing")
	@DontValidate
	public Resolution closeEditing() {
		getRequest().getSession().removeAttribute(KEY_SESS_FRESHLY_CREATED);
		if (isNew) {
			getContext().getMessages().add(
					new LocalizableMessage("woko.create.info.canceled"));
			return new RedirectResolution(IndexActionBean.class);
		} else {
			getContext().getMessages().add(
					new LocalizableMessage("woko.edit.info.closed"));
			RedirectResolution r = new RedirectResolution(ViewActionBean.class);
			id = Util.getId(object);
			className = object.getClass().getName();
			r.addParameter("id", new Object[]{ id });
			r.addParameter("className", new Object[]{ className });
			return r;
		}		
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Object getObject() {
		return object;
	}
}
