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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.Action;

import org.javatb.search.SearchElement;
import org.javatb.search.ui.results.ResultRenderer;
import org.javatb.util.FileUtils;

/**
 * Action for copying the selected results to the system clipboard.
 * @author Laurent Cohen
 */
public class DeleteAction extends AbstractResultAction
{
  /**
   * Initialize this action.
   */
  public DeleteAction(final ResultRenderer renderer)
  {
    super(renderer);
    putValue(Action.NAME, "Delete");
  }

  /**
   * 
   * @param event
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  @Override
  public void actionPerformed(final ActionEvent event)
  {
    List<SearchElement> sel = getSelection();
    for (SearchElement elt: sel)
    {
      File file = new File(elt.getName());
      if (file.exists()) FileUtils.deleteRecursive(file);
    }
  }

  /**
   * 
   * @return
   * @see javax.swing.AbstractAction#isEnabled()
   */
  @Override
  public boolean isEnabled()
  {
    List<SearchElement> sel = getSelection();
    if ((sel == null) || (sel.size() <= 0)) return false;
    for (SearchElement elt: sel)
    {
      if (elt.isArchiveEntry()) return false;
    }
    return true;
  }
}
