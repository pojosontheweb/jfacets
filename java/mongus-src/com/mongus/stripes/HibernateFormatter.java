package com.mongus.stripes;

import java.util.Locale;

import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.format.Formatter;

public class HibernateFormatter implements Formatter
{
	@SuppressWarnings("unchecked")
	public String format(Object object)
	{
		if (object == null)
			return "";
		
		Object primaryKeyValue = PrimaryKeyUtil.getPrimaryKeyValue(object);
		
		if (primaryKeyValue == null)
			return "";
		
		Formatter<Object> formatter = StripesFilter.getConfiguration().getFormatterFactory().getFormatter(primaryKeyValue.getClass(), locale, formatType, pattern);
		
		return formatter.format(primaryKeyValue);
	}

	private String pattern, formatType;
	private Locale locale;
	
	/** Not used */
	public void init() {}

	/** Save for use in format() */
	public void setFormatPattern(String pattern) { this.pattern = pattern; }

	/** Save for use in format() */
	public void setFormatType(String formatType) { this.formatType = formatType; }

	/** Save for use in format() */
	public void setLocale(Locale locale) { this.locale = locale; }
}
