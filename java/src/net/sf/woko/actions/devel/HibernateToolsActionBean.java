package net.sf.woko.actions.devel;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Modifier;

import net.sf.woko.util.Util;
import net.sf.woko.util.IWokoInternal;
import net.sf.woko.search.HQLSearchResults;
import net.sf.woko.actions.BaseActionBean;
import net.sf.woko.actions.ViewActionBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.validation.Validate;
import org.hibernate.Session;
import org.hibernate.Query;

/**
 * Used for the hibernate tools page (triggered from navbar)
 *
 */
@UrlBinding("/tools/devel/hibernate-tools.action")
public class HibernateToolsActionBean extends BaseActionBean {

	private List<Class> hibernatedClasses;

    @Validate(required=true, on="list")
	private String classNameForList;

	@Validate(required=true,on="listHql")
	private String hql;

    public List<Class> getHibernatedClasses() {
		if (hibernatedClasses==null) {
			hibernatedClasses = Util.getHibernatedClasses();
            ArrayList<Class> visible = new ArrayList<Class>();
            for(Class c : hibernatedClasses) {
                if (!IWokoInternal.class.isAssignableFrom(c))
                    visible.add(c);
            }
            hibernatedClasses = visible;
        }
        return hibernatedClasses;
    }

    public List<Class> getHibernatedConcreteClasses() {
        List<Class> all = getHibernatedClasses();
        ArrayList<Class> res = new ArrayList<Class>();
        for(Class c : all) {
            if (!Modifier.isAbstract(c.getModifiers()))
                res.add(c);
        }
        return res;
    }


    @DefaultHandler
    public Resolution display() {
		return new ForwardResolution("/WEB-INF/woko/hibernateTools.jsp");
	}

    @SuppressWarnings("unchecked")
	public Resolution list() {
		return findByHql("select o from " + classNameForList + " as o");
	}

	public Resolution listHql() {
		return findByHql(hql);
	}

	@SuppressWarnings("unchecked")
	private Resolution findByHql(String hql) {
		// list all instances for the given type
		Session s = Util.getSession();
		Query q = s.createQuery(hql);
		List res = q.list();
		HQLSearchResults rs = new HQLSearchResults();
		rs.setHqlResults(res);
		rs.setHql(hql);
		// bind the target object & class to the request
		getRequest().setAttribute(ViewActionBean.KEY_TARGET, rs);
		getRequest().setAttribute(ViewActionBean.KEY_TARGET_CLASS, rs.getClass());
		// forward to JSP
		return new ForwardResolution(ViewActionBean.JSP_BROWSE);
	}

    public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public String getClassNameForList() {
		return classNameForList;
	}

	public void setClassNameForList(String classNameForList) {
		this.classNameForList = classNameForList;
	}
	
}
