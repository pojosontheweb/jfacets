/* Copyright (C) 2005-2006 Aaron Porter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.mongus.beans.validation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import com.mongus.beans.validation.translator.Translator;

/**
 * The AnnotationTranslator allows anyone to add support for
 * existing validation annotations from third-party packages such as
 * <a href="http://annotations.hibernate.org">Hibernate</a>.
 * 
 * @author Aaron Porter
 * @version 0.9
 */
public class AnnotationTranslator
{
	private static HashMap<String, Class<? extends Translator>> translators = new HashMap<String, Class<? extends Translator>>();
	
	/**
     * This method registers a translator to be used by
     * {@link #translateAnnotations(List)}.
	 * @param className the fully qualified class name of the
     * annotation used to annotate the validation annotations
	 * @param translatorClass the class used to perform the
     * translation
	 */
	public static void registerTranslator(String className, Class<? extends Translator> translatorClass)
	{
		translators.put(className, translatorClass);
	}
	
	static
	{
		// Default translator would go here
	}
	
	/**
     * This method looks for annotations have annotations
     * that match fully qualified class names registered
     * through {@link #registerTranslator(String, Class)}.
     * If it finds a match it creates an instance of the
     * associated {@link com.mongus.beans.validation.translator.Translator},
     * calls {@link com.mongus.beans.validation.translator.Translator#init(Annotation)}
     * to provide the original annotation, and replaces the
     * original annotation with the Translator.
     * 
	 * @param originalAnnotations annotations to be translated
	 * @return <code>List</code> of translated annotations
	 */
	public static List<Annotation> translateAnnotations(final List<Annotation> originalAnnotations)
	{
		Vector<Annotation> annotations = new Vector<Annotation>();
		
		for (Annotation annotation : originalAnnotations)
		{
			boolean translated = false;
			
			for (Annotation meta : annotation.annotationType().getAnnotations())
			{
				Class translatorClass = translators.get(meta.annotationType().getName());
				
                try
                {
					if (translatorClass != null)
					{
						Translator translator;
	                    translator = (Translator) translatorClass.newInstance();
	                    translator.init(annotation);
						annotations.add(translator);
						translated = true;
					}
                }
                catch (InstantiationException e) { e.printStackTrace(); }
                catch (IllegalAccessException e) { e.printStackTrace(); }
			}
			
			if (!translated)
				annotations.add(annotation);
		}
		
		return annotations;
	}
}
