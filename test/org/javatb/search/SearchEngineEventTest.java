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
import org.junit.Test;

/**
 * Unit tests for SearchEngineEvent class.
 */
public class SearchEngineEventTest {

  @Test
  public void testEventConstruction() {
    SearchElement element = new SearchElement("test.txt", SearchElement.FILE, new ArrayList<SearchElement>());
    SearchEngineEvent event = new SearchEngineEvent(SearchEngineEvent.Type.MATCH, element);
    
    assertNotNull(event);
    assertEquals(SearchEngineEvent.Type.MATCH, event.type());
    assertEquals(element, event.searchElement());
  }

  @Test
  public void testEventTypes() {
    SearchElement element = new SearchElement("test", SearchElement.FOLDER, new ArrayList<SearchElement>());
    
    SearchEngineEvent startEvent = new SearchEngineEvent(SearchEngineEvent.Type.START, element);
    assertEquals(SearchEngineEvent.Type.START, startEvent.type());
    
    SearchEngineEvent exploreEvent = new SearchEngineEvent(SearchEngineEvent.Type.EXPLORING, element);
    assertEquals(SearchEngineEvent.Type.EXPLORING, exploreEvent.type());
    
    SearchEngineEvent endEvent = new SearchEngineEvent(SearchEngineEvent.Type.END, element);
    assertEquals(SearchEngineEvent.Type.END, endEvent.type());
  }

  @Test
  public void testToString() {
    SearchElement element = new SearchElement("test", SearchElement.FILE, new ArrayList<SearchElement>());
    SearchEngineEvent event = new SearchEngineEvent(SearchEngineEvent.Type.MATCH, element);
    
    String result = event.toString();
    assertNotNull(result);
    assertTrue(result.contains("SearchEngineEvent"));
    assertTrue(result.contains("MATCH"));
  }
}
