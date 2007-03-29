package com.mongus.stripes;

import java.util.Locale;

import net.sourceforge.stripes.format.DefaultFormatterFactory;
import net.sourceforge.stripes.format.Formatter;

public class HibernateFormatterFactory extends DefaultFormatterFactory
{
	@Override
    public Formatter getFormatter(Class clazz, Locale locale, String formatType, String formatPattern)
    {
		Formatter formatter = super.getFormatter(clazz, locale, formatType, formatPattern);
		
		HibernateProvider hibernateProvider = HibernateProvider.getInstance();
		
		if ((formatter == null) && (hibernateProvider != null) && (hibernateProvider.getSession(clazz) != null))
		{
			formatter = new HibernateFormatter();
            formatter.setFormatType(formatType);
            formatter.setFormatPattern(formatPattern);
            formatter.setLocale(locale);
            formatter.init();
		}
		
		return formatter;
    }
}
