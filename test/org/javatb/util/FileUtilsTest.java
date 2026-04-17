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
 * Unit tests for FileUtils class.
 */
public class FileUtilsTest {

  @Test
  public void testGetFileExtensionJava() {
    assertEquals("java", FileUtils.getFileExtension("Test.java"));
  }

  @Test
  public void testGetFileExtensionTxt() {
    assertEquals("txt", FileUtils.getFileExtension("readme.txt"));
  }

  @Test
  public void testGetFileExtensionJar() {
    assertEquals("jar", FileUtils.getFileExtension("archive.jar"));
  }

  @Test
  public void testGetFileExtensionNoExtension() {
    assertNull(FileUtils.getFileExtension("Makefile"));
    assertNull(FileUtils.getFileExtension("README"));
  }

  @Test
  public void testGetFileExtensionHiddenGz() {
    assertEquals("gz", FileUtils.getFileExtension(".bashrc.gz"));
  }

  @Test
  public void testGetFileShortNameUnix() {
    assertEquals("file.txt", FileUtils.getFileShortName("/path/to/file.txt"));
  }

  @Test
  public void testGetFileShortNameWindows() {
    assertEquals("file.txt", FileUtils.getFileShortName("C:\\Users\\Documents\\file.txt"));
  }

  @Test
  public void testGetFileShortNameNoPath() {
    assertEquals("archive.jar", FileUtils.getFileShortName("archive.jar"));
  }

  @Test
  public void testGetFileShortNameMixed() {
    assertEquals("Test.java", FileUtils.getFileShortName("C:\\src\\Test.java"));
  }
}
