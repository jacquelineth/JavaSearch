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


import org.slf4j.*;

/**
 * This class provides a set of utility methods for manipulating Strings.
 * @author Laurent Cohen
 */
public final class StringUtils {
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(StringUtils.class);
  /**
   * Fast way to determine whether debug level is enabled.
   */
  private static boolean debugEnabled = log.isDebugEnabled();

  /**
   * Instantiation of this class is not permitted.
   */
  private StringUtils() {
  }

  /**
   * Convert the specified string into an <code>int</code>.
   * @param source the string to convert.
   * @param def a default value to return if the string cannot be parsed into an int value.
   * @return an int value.
   */
  public static int toInt(final String source, final int def) {
    int res = 0;
    try {
      res = Integer.valueOf(source);
    } catch(Exception e) {
      res = def;
    }
    return res;
  }

  /**
   * Convert the specified string into a <code>boolean</code>.
   * @param source the string to convert.
   * @param def a default value to return if the string cannot be parsed into a boolean value.
   * @return a boolean value.
   */
  public static boolean toBoolean(final String source, final boolean def) {
    boolean res = false;
    try {
      res = Boolean.valueOf(source);
    } catch(Exception e) {
      res = def;
    }
    return res;
  }
}
