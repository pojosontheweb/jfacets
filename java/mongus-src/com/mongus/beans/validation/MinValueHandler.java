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

public class MinValueHandler implements ValidationHandler<MinValue, Number>
{
    public boolean isValid(MinValue minValue, Number value)
    {
        return (value == null ? 0 : value.doubleValue()) >= minValue.value();
    }
	
	public String getErrorMessage(MinValue minValue, Number value)
	{
		double doubleValue = minValue.value();
		long longValue = (long) doubleValue;
		
		return "Value for %s must be greater than or equal to " +
				(longValue == doubleValue ? "" + longValue : "" + doubleValue);
	}
}