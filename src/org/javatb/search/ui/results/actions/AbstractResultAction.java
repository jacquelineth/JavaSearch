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
package org.javatb.search.ui.results.actions;

import java.util.List;

import javax.swing.AbstractAction;

import org.javatb.search.SearchElement;
import org.javatb.search.ui.results.ResultRenderer;

/**
 * Abstract superclass for all actions related to the list of search results.
 * The intent of this class is to abstract all its subclasses (actions) from the component to which they apply.
 * This allows, for instance, writing the same code whether the component is a JList or a JTable.
 * @author Laurent Cohen
 */
public abstract class AbstractResultAction extends AbstractAction
{
  /**
   * The component on which this action is performed.
   */
  protected ResultRenderer renderer = null;

  /**
   * Initialize this action with the specified component.
   * @param renderer the renderer on which this action is performed.
   */
  public AbstractResultAction(final ResultRenderer renderer)
  {
    this.renderer = renderer;
  }

  /**
   * Get the elements currently selected in the component.
   * @return an array of <code>SearchElement</code> instances.
   */
  public List<SearchElement> getSelection()
  {
    List<SearchElement> list = renderer.selectedElements();
    return list;
  }

  /**
   * 
   * @return
   */
  @Override
  public boolean isEnabled()
  {
    List<SearchElement> sel = getSelection();
    return (sel != null) && (sel.size() > 0);
  }
}
