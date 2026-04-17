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

import java.util.EventObject;

/**
 * Event class for events generated during a search.
 * @author Laurent Cohen
 */
public class SearchEngineEvent extends EventObject
{
  /**
   * 
   */
  private static final long serialVersionUID = -960604374657039487L;

  /**
   * The types of events that can be generated.
   */
  public enum Type { EXPLORING, MATCH, START, END };
  /**
   * The type of this event.
   */
  private Type type = null;

  /**
   * Initialize this event with the specified type and element.
   * @param type the type of event.
   * @param element the element (file, folder of archive entry) on which the event is occurring.
   */
  public SearchEngineEvent(final Type type, final SearchElement element)
  {
    super(element);
    this.type = type;
  }

  /**
   * Get the element for which this event occurred.
   * @return a <code>SearchElement</code> instance.
   */
  public SearchElement searchElement()
  {
    return (SearchElement) super.getSource();
  }

  /**
   * Get the type of this event.
   * @return the event type.
   */
  public Type type()
  {
    return type;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName()).append('[');
    sb.append("type=").append(type);
    sb.append(", elt=").append(searchElement());
    sb.append(']');
    return sb.toString();
  }
}
