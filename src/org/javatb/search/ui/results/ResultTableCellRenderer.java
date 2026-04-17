/*
 * JavaTB.
 * Copyright (C) 2008-2014 JavaTB Team.
 * http://javatb.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.javatb.search.ui.results;

import java.awt.Component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer used to render the alignment of cell values in the table.
 * @author Laurent Cohen
 */
public class ResultTableCellRenderer extends DefaultTableCellRenderer
{
  /**
   * The insets for this renderer.
   */
  private Border border = BorderFactory.createEmptyBorder(0, 2, 0, 2);

  /**
   * Returns the default table cell renderer.
   * @param table the JTable to which this renderer applies.
   * @param value the value of the rendered cell.
   * @param isSelected determines whether the cell is selected.
   * @param hasFocus determines whether the cell has the focus.
   * @param row the row of the rendered cell.
   * @param column the column of the rendered cell.
   * @return the default table cell renderer.
   */
  @Override
  public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    int alignment = SwingConstants.LEFT;
    if ((column == 1) || (column == 2)) alignment = SwingConstants.RIGHT;
    renderer.setHorizontalAlignment(alignment);
    return renderer;
  }
}
