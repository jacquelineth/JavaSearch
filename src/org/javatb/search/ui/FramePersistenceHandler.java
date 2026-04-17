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

package org.javatb.search.ui;

import java.awt.*;
import java.util.*;

import javax.swing.JFrame;

import org.javatb.util.*;

/**
 * This class handles the persistence, in the user preferences, of the
 * on-screen location and size of a frame.
 * @author Laurent Cohen
 */
public class FramePersistenceHandler implements Persistable {
  /**
   * The frame whose attributes to persist.
   */
  private final JFrame frame;

  /**
   * Initialize this persistence handler.
   * @param name the name assigned to this persitence handler, used by the {@link PreferencesManager}.
   * @param frame the frame whose attributes to persist.
   */
  public FramePersistenceHandler(final String name, final JFrame frame) {
    this.frame = frame;
    PreferencesManager.register(name, this);
  }

  @Override
  public Map<String, String> getValues() {
    int state = frame.getExtendedState();
    boolean maximized = (state & Frame.MAXIMIZED_BOTH) > 0;
    if (maximized) frame.setExtendedState(Frame.NORMAL);
    Map<String, String> map = new HashMap<String, String>();
    Point p = frame.getLocationOnScreen();
    map.put("ui.x", String.valueOf(p.x));
    map.put("ui.y", String.valueOf(p.y));
    Dimension d = frame.getSize();
    map.put("ui.width", String.valueOf(d.width));
    map.put("ui.height", String.valueOf(d.height));
    map.put("ui.maximized", String.valueOf(maximized));
    return map;
  }

  @Override
  public void setValues(final Map<String, String> values) {
    Point p = new Point(StringUtils.toInt(values.get("ui.x"), 0), StringUtils.toInt(values.get("ui.y"), 0));
    frame.setLocation(p);
    Dimension d = new Dimension(StringUtils.toInt(values.get("ui.width"), 800), StringUtils.toInt(values.get("ui.height"), 600));
    frame.setSize(d);
    boolean maximized = StringUtils.toBoolean(values.get("ui.maximized"), false);
    if (maximized) frame.setExtendedState(Frame.MAXIMIZED_BOTH);
  }
}
