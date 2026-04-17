/*
 * JavaTB.
 * Copyright (C) 2008-2012 JavaTB Team.
 * http://javatb.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javatb.util;

//import static org.javatb.util.ConfigurationProperties.*;

import java.io.*;
import java.util.Properties;

import org.slf4j.*;

/**
 * This class handles loading and retrieving the properties defined in the
 * "config/config.properties" configuration file.
 * @author Laurent Cohen
 */
public class Configuration {
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(Configuration.class);
  /**
   * Determines whether debug log statements are enabled.
   */
  private static boolean debugEnabled = log.isDebugEnabled();
  /**
   * Default location of the configuration file.
   */
  public static final String DEFAULT_FILE = "config.properties";
  /**
   * Holds the configuration properties.
   */
  private static Properties props = null;

  /**
   * Get the configuration properties.
   * @return a TypedProperties instance.
   */
  public static Properties getProperties() {
    if (props == null) loadProperties();
    return props;
  }

  /**
   * Reset and reload the configuration.
   * This allows reloading the configuration from a different source or file
   * (after changing the values of the related system properties for instance).
   */
  public static void reset() {
    loadProperties();
  }

  /**
   * Load the configuration properties from a file.
   */
  private static void loadProperties() {
    props = new Properties();
    InputStream is = null;
    try {
      is = getStream();
      if (is != null) props.load(is);
    } catch(Exception e) {
      log.error("error reading the configuration", e);
    } finally {
      try {
        is.close();
      } catch (Exception e2) {
        log.error(e2.getMessage(), e2);
      }
    }
  }

  /**
   * Get an input stream from which to read the configuration properties.
   * @return an {@link InputStream} instance.
   * @throws Exception if any error occurs while trying to obtain the stream.
   */
  private static InputStream getStream() throws Exception {
    String filename = DEFAULT_FILE;
    if (log.isDebugEnabled()) log.debug("reading configuration file: " + filename);
    InputStream is = null;
    File file = new File(filename);
    if (file.exists()) is = new BufferedInputStream(new FileInputStream(file));
    if (is == null) is = Configuration.class.getClassLoader().getResourceAsStream(filename);
    return is;
  }

  /**
   * Convert a property expressed as a list of space-separated strings into a string array.
   * @param name the property to convert.
   * @return an array of strings, possibly empty.
   */
  public static String[] getArrayProperty(final ConfigurationProperty property) {
    String s = getProperties().getProperty(property.getPropertyName(), property.getDefault());
    if (s == null) return new String[0];
    return s.split("\\s");
  }

  public static String getString(final ConfigurationProperty property) {
    return getProperties().getProperty(property.getPropertyName(), property.getDefault());
  }

  /**
   * Get the value of the specified property as an int.
   * @param property the property to find.
   * @return the value as an int.
   */
  public static int getInt(final ConfigurationProperty property) {
    return property == null ? null : StringUtils.toInt(property.getPropertyName(), property.getDefaultAsInt());
  }
}
