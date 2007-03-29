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

public class MaxValueHandler implements ValidationHandler<MaxValue, Number>
{
    public boolean isValid(MaxValue maxValue, Number value)
    {
        return (value == null ? 0 : value.doubleValue()) <= maxValue.value();
    }
	
	public String getErrorMessage(MaxValue maxValue, Number value)
	{
		double doubleValue = maxValue.value();
		long longValue = (long) doubleValue;
		
		return "Value for %s must be less than or equal to " +
				(longValue == doubleValue ? "" + longValue : "" + doubleValue);
	}
}
