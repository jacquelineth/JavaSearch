/*
 * JavaTB.
 * Copyright (C) 2008-2014 JavaTB Team.
 * http://javatb.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.javatb.util;

import java.util.Map;

/**
 * Interface for all objects who want their state persisted as preferences.
 * @author Laurent Cohen
 */
public interface Persistable
{
  /**
   * Get the list of values to persist.
   * @return a list of strings.
   */
  Map<String, String> getValues();

  /**
   * Set a list of values read from persisted preferences.
   * @param values the values to set.
   */
  void setValues(Map<String, String> values);
}
