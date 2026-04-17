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
 * Unit tests for SearchEngineListener interface.
 */
public class SearchEngineListenerTest {

  @Test
  public void testListenerCreation() {
    TestSearchEngineListener listener = new TestSearchEngineListener();
    assertNotNull(listener);
    assertEquals(0, listener.getMessageCount());
  }

  @Test
  public void testNewMessageMatch() {
    TestSearchEngineListener listener = new TestSearchEngineListener();
    SearchElement element = new SearchElement("test.java", SearchElement.FILE, new ArrayList<SearchElement>());
    SearchEngineEvent event = new SearchEngineEvent(SearchEngineEvent.Type.MATCH, element);
    listener.newMessage(event);
    assertEquals(1, listener.getMessageCount());
    assertEquals(SearchEngineEvent.Type.MATCH, listener.getLastEvent().type());
  }

  @Test
  public void testNewMessageExploring() {
    TestSearchEngineListener listener = new TestSearchEngineListener();
    SearchElement element = new SearchElement("folder", SearchElement.FOLDER, new ArrayList<SearchElement>());
    SearchEngineEvent event = new SearchEngineEvent(SearchEngineEvent.Type.EXPLORING, element);
    listener.newMessage(event);
    assertEquals(1, listener.getMessageCount());
    assertEquals(SearchEngineEvent.Type.EXPLORING, listener.getLastEvent().type());
  }

  @Test
  public void testNewMessageStart() {
    TestSearchEngineListener listener = new TestSearchEngineListener();
    SearchElement element = new SearchElement("root", SearchElement.FOLDER, new ArrayList<SearchElement>());
    SearchEngineEvent event = new SearchEngineEvent(SearchEngineEvent.Type.START, element);
    listener.newMessage(event);
    assertEquals(1, listener.getMessageCount());
  }

  @Test
  public void testNewMessageEnd() {
    TestSearchEngineListener listener = new TestSearchEngineListener();
    SearchElement element = new SearchElement("root", SearchElement.FOLDER, new ArrayList<SearchElement>());
    SearchEngineEvent event = new SearchEngineEvent(SearchEngineEvent.Type.END, element);
    listener.newMessage(event);
    assertEquals(1, listener.getMessageCount());
  }

  @Test
  public void testMultipleMessages() {
    TestSearchEngineListener listener = new TestSearchEngineListener();
    SearchElement element = new SearchElement("test", SearchElement.FILE, new ArrayList<SearchElement>());
    for (SearchEngineEvent.Type type : SearchEngineEvent.Type.values()) {
      listener.newMessage(new SearchEngineEvent(type, element));
    }
    assertEquals(SearchEngineEvent.Type.values().length, listener.getMessageCount());
  }

  /**
   * Test implementation of SearchEngineListener.
   */
  private static class TestSearchEngineListener implements SearchEngineListener {
    private int messageCount = 0;
    private SearchEngineEvent lastEvent = null;

    @Override
    public void newMessage(SearchEngineEvent event) {
      messageCount++;
      lastEvent = event;
    }

    public int getMessageCount() {
      return messageCount;
    }

    public SearchEngineEvent getLastEvent() {
      return lastEvent;
    }
  }
}
