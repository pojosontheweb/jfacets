package com.mongus.stripes;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.mapping.Property;
import org.hibernate.property.Getter;
import org.hibernate.proxy.HibernateProxy;

import com.mongus.util.Log;
import net.sf.woko.util.Util;

public class PrimaryKeyUtil
{
	private static Log log = Log.getInstance(PrimaryKeyUtil.class);
	
	private final static Map<Class, Class> cache = new HashMap<Class, Class>();
	
	public static Class getPrimaryKeyType(Class targetType)
	{
		Class pkType = cache.get(targetType);
		
		if (pkType != null)
			return pkType;

		if (pkType == null)
		{
			pkType = getPrimaryKeyProperty(targetType).getType().getReturnedClass();
			
			log.debug("Primary key for ", targetType.getName(), " is a ", pkType.getName());
		}
		
		cache.put(targetType, pkType);
		
		return pkType;
	}
	
	public static Property getPrimaryKeyProperty(Class targetType)
	{
        targetType = Util.deproxifyCglibClass(targetType);
        return HibernateProvider.getInstance().getConfiguration(targetType).getClassMapping(targetType.getName()).getIdentifierProperty();
	}
	
	public static Object getPrimaryKeyValue(Object entity)
	{
        Object res = null;

        if (entity instanceof HibernateProxy) {

            // the entity is proxified, let's use the hibernate API to get its ID !
            res = ((HibernateProxy)entity).getHibernateLazyInitializer().getIdentifier();

        } else {

            // the entity isn't proxified, let's get the ID through reflection...
            Class entityClass = entity.getClass();
            Property property = PrimaryKeyUtil.getPrimaryKeyProperty(entityClass);
            Getter getter = property.getGetter(entityClass);
            res = getter.get(entity);
        }
        return res;        
    }
}
