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

import java.util.*;
import java.util.prefs.Preferences;

import org.slf4j.*;

/**
 * Utility class to manage persistence of application properties or values through the preferences API.
 * @author Laurent Cohen
 */
public class PreferencesManager
{
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(PreferencesManager.class);
  /**
   * The root of the preferences subtree in which the search parameters values are saved.
   */
  private static Preferences PREFERENCES = Preferences.userRoot().node("JavaTB/Find4j");
  /**
   * List of components registered with this preferences manager.
   */
  private static Map<String, Persistable> components = new HashMap<String, Persistable>();

  /**
   * Register a persistable component.
   * @param name the name under which to register the component.
   * @param persistable the component to register.
   */
  public static synchronized void register(final String name, final Persistable persistable)
  {
    try
    {
      components.put(name, persistable);
      Preferences prefs = PREFERENCES.node(name);
      String[] keys = prefs.keys();
      Map<String, String> values = new HashMap<String, String>();
      for (String key: keys)
      {
        String value = prefs.get(key, null);
        if ((value != null) && !"".equals(value.trim())) values.put(key, value);
      }
      persistable.setValues(values);
    }
    catch(Exception e)
    {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Register a persistable component.
   * @param name the name under which to register the component.
   * @param persistable the component to register.
   */
  public static synchronized void save(final String name)
  {
    try
    {
      Persistable comp = components.get(name);
      if (comp == null) return;
      Preferences prefs = PREFERENCES.node(name);
      String[] keys = prefs.keys();
      Map<String, String> values = comp.getValues();
      for (Map.Entry<String, String> entry: values.entrySet())
      {
        prefs.put(entry.getKey(), entry.getValue());
      }
    }
    catch(Exception e)
    {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Persist all components to the underlying storage.
   */
  public synchronized static void saveAll()
  {
    for (String name: components.keySet()) save(name);
  }
}
