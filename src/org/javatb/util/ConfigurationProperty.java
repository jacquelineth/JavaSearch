/*
 * JavaTB.
 * Copyright (C) 2008 JavaTB Team.
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

/**
 * Handles an eumeration of the supported configuration properties
 * and their default values.
 * @author Laurent Cohen
 */
public enum ConfigurationProperty {
  /**
   * Known file extensions for zip-compatible archives.
   */
  ZIP_EXTENSIONS("org.javatb.search.zip.extensions", "zip jar war ear rar pak ods odp odg odt"),
  /**
   * Max number of items in the auto-completion popups.
   */
  HISTORY_SIZE("org.javatb.history.size", "10"),
  /**
   * Class name of the renderer for the search results view.
   */
  RESULT_RENDERER("org.javatb.renderer.class", "org.javatb.search.ui.results.TableResultRenderer");

  /**
   * The name of the property.
   */
  private final String propName;
  /**
   * The default value for the property.
   */
  private final String def;

  /**
   * Initialize this configuration property.
   * @param name the name of the property.
   * @param def the default value for the property.
   */
  private ConfigurationProperty(final String name, final String def) {
    this.propName = name;
    this.def = def;
  }

  public String getPropertyName() {
    return propName;
  }

  public String getDefault() {
    return def;
  }

  public int getDefaultAsInt() {
    return StringUtils.toInt(def, 0);
  }
}
