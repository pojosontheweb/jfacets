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

import java.lang.annotation.*;

/**
 * This interface defines the way that {@link BeanValidator}
 * will validate and retrieve validation error messages.
 * 
 * @param <T>	Annotation type that this handler validates
 * @param <V>	Type of value that will be passed in to be validated
 * @author Aaron Porter
 * @see Validation
 */
public interface ValidationHandler<T extends Annotation, V>
{
	/**
	 * @param annotation
	 * @param value
	 * @return true if successfully validated
	 */
	public boolean isValid(T annotation, V value);

	/** Creates an error message that will be used in a {@link ValidationException} to
	 * indicate a validation failure. This message will be used in a call to
	 * <code>String.format()</code> with the following varargs:
	 * <ol>
	 *  <li>nicely formatted property name</li>
	 *  <li>setter method name</li>
	 *  <li>value that failed validation</li>
	 *  <li>bean's canonical class name</li>
	 *  <li>bean's simple class name</li>
	 *  <li>validation's canonical class name</li>
	 *  <li>validation's simple class name</li>
	 *  <li>validation handler's canonical class name</li>
	 *  <li>validation handler's simple class name</li>
	 * </ol>
	 *
	 * @param annotation the validation annotation that the value failed
	 * @param value the value that failed validation
	 * @return formatting string that will be passed to <code>String.format()</code>
	 */
	public String getErrorMessage(T annotation, V value);
}
