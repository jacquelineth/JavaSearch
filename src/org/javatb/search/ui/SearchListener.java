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

import java.awt.Color;

import org.javatb.search.*;

/**
 * This listener updates the status bar at the bottom during the search.
 */
public class SearchListener implements SearchEngineListener {
  private UILauncher ui;

  public SearchListener(final UILauncher launcher) {
    this.ui = launcher;
  }

  @Override
  public void newMessage(final SearchEngineEvent event) {
    switch(event.type()) {
      case MATCH:
        ui.incNbMatches();
        ui.updateMatches(event.searchElement());
        break;

      case EXPLORING:
        ui.updateExploringFieldText(event.searchElement().toString(), Color.BLACK);
        break;

      case START:
        ui.setNbMatches(0);
        ui.updateExploringFieldText("", Color.BLACK);
        break;

      case END:
        if (ui.getNbMatches() > 0) ui.updateExploringFieldText("", Color.BLACK);
        else ui.updateExploringFieldText("No match found", Color.RED);
        break;

      default:
        break;
    }
    //System.out.println("processed " + event + ", nbMatches=" + ui.getNbMatches());
  }
}