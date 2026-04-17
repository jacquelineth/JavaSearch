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
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Unit tests for PreferencesManager class.
 */
public class PreferencesManagerTest {

  @Test
  public void testRegisterPersistable() {
    TestPersistable persistable = new TestPersistable();
    // Should not throw
    PreferencesManager.register("testComponent", persistable);
  }

  @Test
  public void testSaveAll() {
    TestPersistable persistable = new TestPersistable();
    PreferencesManager.register("testSaveAll", persistable);
    // Should not throw
    PreferencesManager.saveAll();
  }

  @Test
  public void testSaveNamed() {
    TestPersistable persistable = new TestPersistable();
    PreferencesManager.register("testSaveNamed", persistable);
    // Should not throw
    PreferencesManager.save("testSaveNamed");
  }

  @Test
  public void testRegisterCallsSetValues() {
    TestPersistable persistable = new TestPersistable();
    PreferencesManager.register("testSetValues", persistable);
    // setValues should have been called on registration to restore saved prefs
    assertTrue(persistable.setValuesCalled);
  }

  /**
   * Test implementation of Persistable.
   */
  private static class TestPersistable implements Persistable {
    boolean setValuesCalled = false;

    @Override
    public Map<String, String> getValues() {
      Map<String, String> map = new HashMap<String, String>();
      map.put("key", "value");
      return map;
    }

    @Override
    public void setValues(Map<String, String> values) {
      setValuesCalled = true;
    }
  }
}
