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
 * Unit tests for StringUtils class.
 */
public class StringUtilsTest {

  @Test
  public void testToIntValidString() {
    assertEquals(42, StringUtils.toInt("42", 0));
    assertEquals(0, StringUtils.toInt("0", -1));
    assertEquals(-100, StringUtils.toInt("-100", 0));
  }

  @Test
  public void testToIntInvalidString() {
    assertEquals(10, StringUtils.toInt("abc", 10));
    assertEquals(-1, StringUtils.toInt("not a number", -1));
    assertEquals(0, StringUtils.toInt("12.34", 0));
  }

  @Test
  public void testToIntNullString() {
    assertEquals(99, StringUtils.toInt(null, 99));
  }

  @Test
  public void testToBooleanValidTrue() {
    assertTrue(StringUtils.toBoolean("true", false));
    assertTrue(StringUtils.toBoolean("True", false));
    assertTrue(StringUtils.toBoolean("TRUE", false));
  }

  @Test
  public void testToBooleanValidFalse() {
    assertFalse(StringUtils.toBoolean("false", true));
    assertFalse(StringUtils.toBoolean("False", true));
    assertFalse(StringUtils.toBoolean("FALSE", true));
  }

  @Test
  public void testToBooleanInvalidString() {
    // Boolean.valueOf() returns false for any non-"true" string, default is not used
    assertFalse(StringUtils.toBoolean("maybe", true));
    assertFalse(StringUtils.toBoolean("not boolean", false));
  }

  @Test
  public void testToBooleanNullString() {
    // Boolean.valueOf(null) returns false, default is not used
    assertFalse(StringUtils.toBoolean(null, true));
    assertFalse(StringUtils.toBoolean(null, false));
  }

  @Test
  public void testEmptyString() {
    assertEquals(-1, StringUtils.toInt("", -1));
    assertFalse(StringUtils.toBoolean("", false));
  }
}
