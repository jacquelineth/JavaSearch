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

import java.util.List;

import javax.swing.*;

import org.javatb.search.SearchElement;

/**
 * A generic interface encapsulating the UI component holding the search results.
 * @author Laurent Cohen
 */
public interface ResultRenderer
{
  /**
   * Get the UI component to dispaly in the UI.
   * @return a {@link JComponent} instance.
   */
  JComponent getUIComponent();

  /**
   * Add a new result.
   * @param element a {@link SearchElement} instance.
   */
  void addElement(SearchElement element);

  /**
   * Get the result at the specified row.
   * @param row the row to fetch the result from.
   * @return a {@link SearchElement} instance, or null if no element exists at the specified row.
   */
  SearchElement elementAt(int row);

  /**
   * Get the list of selected elements.
   * @return a list of {@link SearchElement} instances, which may be empty if none is selected.
   */
  List<SearchElement> selectedElements();

  /**
   * Remove all the elements in this result holder.
   */
  void clear();

  /**
   * Get a row index from the specified screen location.
   * @param x the location's x coordinate.
   * @param y the location's y coordinate.
   * @return the index of a row in the results component, or -1 if the location doesn't point to a row in the component.
   */
  int rowFromLocation(int x, int y);

  /**
   * Show the specified menu at the specfied location.
   * @param menu the menu to show.
   * @param x the location's x coordinate.
   * @param y the location's y coordinate.
   */
  void showMenu(JPopupMenu menu, int x, int y);
}
