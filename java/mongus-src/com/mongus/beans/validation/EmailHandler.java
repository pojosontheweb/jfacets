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

public class EmailHandler implements ValidationHandler<Email, Object>
{

	public String getErrorMessage(Email annotation, Object value)
	{
		return "%s is not a valid email address";
	}

	public boolean isValid(Email annotation, Object value)
	{
		return value.toString().matches("^[-a-zA-Z0-9_.]+@([-a-zA-Z0-9]+[.])+([a-zA-Z0-9]{2,4})$");
	}
}
