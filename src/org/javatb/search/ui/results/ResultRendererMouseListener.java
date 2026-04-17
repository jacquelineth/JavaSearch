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
package org.javatb.search.ui.results;

import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;

import org.javatb.search.SearchElement;
import org.javatb.search.ui.results.actions.*;
import org.javatb.util.FileUtils;
import org.slf4j.*;

/**
 * Mouse listener for search results list.
 * Processes mouse events to perform default actions or display popup menus.
 * @author Laurent Cohen
 */
public class ResultRendererMouseListener extends MouseAdapter
{
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(ResultRendererMouseListener.class);
  /**
   * Determines whether debug log statements are enabled.
   */
  private static boolean debugEnabled = log.isDebugEnabled();
  /**
   * 
   */
  private final ResultRenderer renderer;

  public ResultRendererMouseListener(final ResultRenderer renderer)
  {
    this.renderer = renderer;
  }

  @Override
  public void mousePressed(final MouseEvent event)
  {
    int x = event.getX();
    int y = event.getY();
    int button = event.getButton();
    int count = event.getClickCount();

    if ((button == MouseEvent.BUTTON1) && (count == 2))
    {
      int index = renderer.rowFromLocation(x, y);
      if (index < 0) return;
      SearchElement elt = renderer.elementAt(index);
      if (elt.isFile() || elt.isArchiveEntry())
      {
        FileUtils.openSearchElement(elt);
      }
    }
    else if ((button == MouseEvent.BUTTON3) && (count == 1))
    {
      JPopupMenu menu = new JPopupMenu();
      JMenuItem open = menu.add(new OpenAction(renderer));
      Font font = open.getFont();
      open.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
      menu.add(new EditAction(renderer));
      menu.add(new CopyToClipBoardAction(renderer));
      menu.add(new DeleteAction(renderer));
      //TJ Added
      menu.add(new ExploreFileLocationAction(renderer));
      renderer.showMenu(menu, x, y);
    }
  }
}
