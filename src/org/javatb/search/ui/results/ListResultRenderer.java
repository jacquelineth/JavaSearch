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

import java.awt.Point;
import java.util.*;

import javax.swing.*;

import org.javatb.search.SearchElement;
import org.javatb.search.ui.UILauncher;

/**
 * This implementation renders the results in a {@link JList}.
 * @author Laurent Cohen
 */
public class ListResultRenderer implements ResultRenderer
{
  /**
   * A JList displaying the searchr esults.
   */
  private JList resultList = null;
  /**
   * The scrollpane containing the list.
   */
  private JScrollPane scrollPane = null;

  public ListResultRenderer()
  {
    resultList = new JList();
    resultList.setName("Search Results List");
    UILauncher.theme.setColors(resultList);
    resultList.setBorder(BorderFactory.createEmptyBorder());
    resultList.setModel(new DefaultListModel());
    resultList.addMouseListener(new ResultRendererMouseListener(this));
    scrollPane = new JScrollPane(resultList);
  }

  @Override
  public JComponent getUIComponent()
  {
    return scrollPane;
  }

  @Override
  public void addElement(final SearchElement element)
  {
    ((DefaultListModel) resultList.getModel()).addElement(element);
  }

  @Override
  public SearchElement elementAt(final int row)
  {
    return (SearchElement) ((DefaultListModel) resultList.getModel()).elementAt(row);
  }

  @Override
  @SuppressWarnings("deprecation")
  public List<SearchElement> selectedElements()
  {
    Object[] sel = resultList.getSelectedValues();
    List<SearchElement> list = new ArrayList<SearchElement>(sel.length);
    for (Object o: sel) list.add((SearchElement) o);
    return list;
  }

  @Override
  public void clear()
  {
    ((DefaultListModel) resultList.getModel()).removeAllElements();
  }

  @Override
  public int rowFromLocation(final int x, final int y)
  {
    return resultList.locationToIndex(new Point(x, y));
  }

  @Override
  public void showMenu(final JPopupMenu menu, final int x, final int y)
  {
    menu.show(resultList, x, y);
  }
}
