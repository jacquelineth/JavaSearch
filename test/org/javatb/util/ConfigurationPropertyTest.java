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
import org.junit.Test;

/**
 * Unit tests for ConfigurationProperty enum.
 */
public class ConfigurationPropertyTest {

  @Test
  public void testZipExtensionsProperty() {
    assertNotNull(ConfigurationProperty.ZIP_EXTENSIONS);
    assertEquals("org.javatb.search.zip.extensions", ConfigurationProperty.ZIP_EXTENSIONS.getPropertyName());
    assertNotNull(ConfigurationProperty.ZIP_EXTENSIONS.getDefault());
    assertTrue(ConfigurationProperty.ZIP_EXTENSIONS.getDefault().contains("zip"));
    assertTrue(ConfigurationProperty.ZIP_EXTENSIONS.getDefault().contains("jar"));
  }

  @Test
  public void testHistorySizeProperty() {
    assertNotNull(ConfigurationProperty.HISTORY_SIZE);
    assertEquals("org.javatb.history.size", ConfigurationProperty.HISTORY_SIZE.getPropertyName());
    assertEquals("10", ConfigurationProperty.HISTORY_SIZE.getDefault());
    assertEquals(10, ConfigurationProperty.HISTORY_SIZE.getDefaultAsInt());
  }

  @Test
  public void testResultRendererProperty() {
    assertNotNull(ConfigurationProperty.RESULT_RENDERER);
    assertEquals("org.javatb.renderer.class", ConfigurationProperty.RESULT_RENDERER.getPropertyName());
    assertTrue(ConfigurationProperty.RESULT_RENDERER.getDefault().contains("TableResultRenderer"));
  }

  @Test
  public void testEnumValues() {
    ConfigurationProperty[] values = ConfigurationProperty.values();
    assertEquals(3, values.length);
    assertTrue(contains(values, ConfigurationProperty.ZIP_EXTENSIONS));
    assertTrue(contains(values, ConfigurationProperty.HISTORY_SIZE));
    assertTrue(contains(values, ConfigurationProperty.RESULT_RENDERER));
  }

  private boolean contains(ConfigurationProperty[] array, ConfigurationProperty property) {
    for (ConfigurationProperty cp : array) {
      if (cp == property) return true;
    }
    return false;
  }
}
