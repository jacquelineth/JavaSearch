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

import static org.junit.Assert.*;
import java.util.Properties;
import org.junit.Test;

/**
 * Unit tests for Configuration class.
 */
public class ConfigurationTest {

  @Test
  public void testGetProperties() {
    Properties props = Configuration.getProperties();
    assertNotNull(props);
  }

  @Test
  public void testGetPropertiesNotEmpty() {
    Properties props = Configuration.getProperties();
    assertFalse(props.isEmpty());
  }

  @Test
  public void testGetZipExtensions() {
    String extensions = Configuration.getString(ConfigurationProperty.ZIP_EXTENSIONS);
    assertNotNull(extensions);
    assertFalse(extensions.isEmpty());
  }

  @Test
  public void testGetHistorySize() {
    int size = Configuration.getInt(ConfigurationProperty.HISTORY_SIZE);
    assertTrue(size > 0);
  }

  @Test
  public void testGetResultRenderer() {
    String renderer = Configuration.getString(ConfigurationProperty.RESULT_RENDERER);
    assertNotNull(renderer);
    assertTrue(renderer.contains("ResultRenderer"));
  }

  @Test
  public void testReset() {
    // reset should not throw and properties should still be accessible after
    Configuration.reset();
    assertNotNull(Configuration.getProperties());
  }

  @Test
  public void testGetArrayProperty() {
    String[] extensions = Configuration.getArrayProperty(ConfigurationProperty.ZIP_EXTENSIONS);
    assertNotNull(extensions);
    assertTrue(extensions.length > 0);
  }
}
