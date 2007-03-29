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

import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Vector;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * <code>BeanValidator</code> provides an easy way to perform validation
 * on beans using annotations. The validation annotations may be from
 * the com.mongus.beans.validation package, Hibernate, or have a custom
 * translator.
 * <p />
 * Call <code><a href="#validate(T)">validate(T)</a></code> before storing the value passed in to
 * prevent your bean from containing invalid data. A
 * {@link ValidationException} is thrown if the value passed in fails
 * any of the validations. The <code>ValidationException</code> will
 * contain the <code>String</code> in the {@link ErrorMessage}
 * annotation if it exists or a default error message if there is no
 * <code>ErrorMessage</code>.
 * <p />
 * Validation annotations will be recognized on the getter, setter, or field.
 * <p />
 * <b><u>Example Usage:</u></b><pre>
 * import com.mongus.beans.validation.MaxLength;
 * import static com.mongus.beans.validation.BeanValidator.validate;
 * 
 * class MyBean
 * {
 *     {@literal @MaxLength(20)}
 *     public void setName(final String name)
 *     {
 *         validate(name);
 *         this.name = name;
 *     }
 * }
 * </pre>
 * 
 * @author Aaron Porter
 * @see com.mongus.beans.validation.translator.Translator
 * @version 0.9
 */
public class BeanValidator
{
    private BeanValidator() { }
    
    private static Boolean hibernateAvailable;
    
    /**
     * Call this method from your bean's setter method before storing
     * the value to prevent your bean from containing invalid data.
     * 
     * @param <T> the type of the value to be validated
     * @param value the value to be validated
     * @throws ValidationException if validation fails
     */
    @SuppressWarnings("unchecked")
    public static <T> void validate(T value) throws ValidationException
	{
		Class callingClass = Util.getCallingClass();
		Method callingMethod = Util.getCallingMethod();
		
		Vector<Annotation> annotations = new Vector<Annotation>();
		
		annotations.addAll(Arrays.asList(callingMethod.getAnnotations()));
		ErrorMessage errorMessage = callingMethod.getAnnotation(ErrorMessage.class);
		
		String callingMethodName = callingMethod.getName();
		
		String propertyName = Character.toLowerCase(callingMethodName.charAt(3)) + callingMethodName.substring(4);
		
		if (callingMethodName.startsWith("set"))
		{
			// They could have annotated the get method
            try
            {
                Method method = callingClass.getDeclaredMethod('g' + callingMethodName.substring(1));
				annotations.addAll(Arrays.asList(method.getAnnotations()));
				if (errorMessage == null) errorMessage = method.getAnnotation(ErrorMessage.class);
			}
            catch (SecurityException e) { }
            catch (NoSuchMethodException e) { }
            
            // Or the field itself
            try
            {
            	Field field = callingClass.getDeclaredField(propertyName);
				annotations.addAll(Arrays.asList(field.getAnnotations()));
				if (errorMessage == null) errorMessage = field.getAnnotation(ErrorMessage.class);
            }
            catch (SecurityException e) { }
            catch (NoSuchFieldException e) { }
		}
		
		if (hibernateAvailable == null)
		{
			try
			{
				Thread.currentThread().getContextClassLoader().loadClass("org.hibernate.validator.ClassValidator");
				hibernateAvailable = new Boolean(true);
			}
			catch (Exception e)
			{
				hibernateAvailable = new Boolean(false);
				System.out.println("Hibernate appears to be unavailable.");
			}
		}
		
		if (hibernateAvailable.booleanValue())
		{
			InvalidValue[] invalidValues = new ClassValidator(callingClass).getPotentialInvalidValues(propertyName, value);
			
			if (invalidValues.length > 0)
				throw new ValidationException(Util.getDescriptiveName(callingMethod) + " " + invalidValues[0].getMessage());
		}
		
		for (Annotation annotation : AnnotationTranslator.translateAnnotations(annotations))
		{
			try
			{
				Validation validation = annotation.annotationType().getAnnotation(Validation.class);
			
				if (validation != null)
				{
					ValidationHandler validator = validation.value().newInstance();
					
					if (!validator.isValid(annotation, value))
					{
						String errorFormat = errorMessage != null ? errorMessage.value() :
																	validator.getErrorMessage(annotation, value);
	
						throw new ValidationException(String.format(errorFormat,
																	Util.getDescriptiveName(callingMethod),
																	callingMethod.getName(),
																	value,
																	callingClass.getCanonicalName(),
																	callingClass.getSimpleName(),
																	annotation.annotationType().getClass().getCanonicalName(),
																	annotation.annotationType().getClass().getSimpleName(),
																	validator.getClass().getCanonicalName(),
																	validator.getClass().getSimpleName()
																	));
					}
				}
			}
			catch (IllegalAccessException e) { e.printStackTrace(); }
			catch (InstantiationException e) { e.printStackTrace(); }
		}		
	}
}
