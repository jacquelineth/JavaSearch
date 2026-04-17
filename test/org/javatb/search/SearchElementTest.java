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

package org.javatb.search;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for SearchElement class.
 */
public class SearchElementTest {

  @Test
  public void testFileConstructor() {
    SearchElement element = new SearchElement("test.txt", SearchElement.FILE, new ArrayList<SearchElement>());
    assertNotNull(element);
    assertEquals("test.txt", element.getName());
    assertTrue(element.isFile());
    assertFalse(element.isFolder());
    assertFalse(element.isArchive());
    assertFalse(element.isArchiveEntry());
  }

  @Test
  public void testFolderConstructor() {
    SearchElement element = new SearchElement("testfolder", SearchElement.FOLDER, new ArrayList<SearchElement>());
    assertNotNull(element);
    assertEquals("testfolder", element.getName());
    assertFalse(element.isFile());
    assertTrue(element.isFolder());
  }

  @Test
  public void testArchiveConstructor() {
    SearchElement element = new SearchElement("test.jar", SearchElement.ARCHIVE, new ArrayList<SearchElement>());
    assertTrue(element.isArchive());
    assertFalse(element.isFile());
  }

  @Test
  public void testArchiveEntryConstructor() {
    SearchElement element = new SearchElement("entry.class", SearchElement.ENTRY, new ArrayList<SearchElement>());
    assertTrue(element.isArchiveEntry());
    assertFalse(element.isFile());
  }

  @Test
  public void testPathHandling() {
    SearchElement parent = new SearchElement("parent", SearchElement.FOLDER, new ArrayList<SearchElement>());
    List<SearchElement> path = new ArrayList<SearchElement>();
    path.add(parent);
    
    SearchElement child = new SearchElement("child", SearchElement.FILE, path);
    assertNotNull(child.getPath());
    assertEquals(1, child.getPath().size());
    assertEquals("parent", child.getPath().get(0).getName());
  }

  @Test
  public void testToStringPath() {
    List<SearchElement> path = new ArrayList<SearchElement>();
    path.add(new SearchElement("root", SearchElement.FOLDER, new ArrayList<SearchElement>()));
    path.add(new SearchElement("sub", SearchElement.FOLDER, new ArrayList<SearchElement>()));
    
    SearchElement element = new SearchElement("file.txt", SearchElement.FILE, path);
    String result = element.toString();
    assertNotNull(result);
    assertTrue(result.contains("file.txt"));
  }

  @Test
  public void testSize() {
    SearchElement element = new SearchElement("test.txt", SearchElement.FILE, new ArrayList<SearchElement>());
    assertEquals(-1, element.getSize());
  }

  @Test
  public void testCompressedSize() {
    SearchElement element = new SearchElement("test.txt", SearchElement.FILE, new ArrayList<SearchElement>());
    assertEquals(-1, element.getCompressedSize());
  }
}
