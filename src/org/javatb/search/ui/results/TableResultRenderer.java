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
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.javatb.search.SearchElement;

/**
 * This implementation renders the results in a {@link JTable}.
 * @author Laurent Cohen
 */
public class TableResultRenderer implements ResultRenderer
{
  /**
   * The table where the results are displayed.
   */
  private JTable table;
  /**
   * The table model.
   */
  private ResultTableModel model;
  /**
   * The scrollpane containing the table.
   */
  private JScrollPane scrollPane = null;

  public TableResultRenderer()
  {
    model = new ResultTableModel();
    table = new JTable(model);
    TableRowSorter sorter = new TableRowSorter(model);
    SizeComparator scomp = new SizeComparator();
    sorter.setComparator(ResultTableModel.SIZE, scomp);
    sorter.setComparator(ResultTableModel.COMPRESSED_SIZE, scomp);
    table.setRowSorter(sorter);
    scrollPane = new JScrollPane(table);
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.setColumnSelectionAllowed(false);
    table.setRowSelectionAllowed(true);
    table.setShowGrid(false);
    table.setDefaultRenderer(Object.class, new ResultTableCellRenderer());
    table.addMouseListener(new ResultRendererMouseListener(this));
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    final TableColumnModel tcm = table.getColumnModel();
    tcm.getColumn(0).setPreferredWidth(500);
    tcm.getColumn(1).setPreferredWidth(60);
    tcm.getColumn(2).setPreferredWidth(60);
    tcm.getColumn(3).setPreferredWidth(120);
    new TablePersistenceHandler("result.table", table);
    table.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(final ComponentEvent e) {
        int sum = 0;
        for (int i=1; i<model.getColumnCount(); i++) sum += tcm.getColumn(i).getPreferredWidth();
        tcm.getColumn(0).setPreferredWidth(table.getWidth() - sum);
      }
    });
  }

  @Override
  public JComponent getUIComponent()
  {
    return scrollPane;
  }

  @Override
  public void addElement(final SearchElement element)
  {
    model.addElement(element);
  }

  @Override
  public SearchElement elementAt(final int row)
  {
    int modelRow = table.getRowSorter().convertRowIndexToModel(row);
    return model.elementAt(modelRow);
  }

  @Override
  public List<SearchElement> selectedElements()
  {
    int[] rows = table.getSelectedRows();
    List<SearchElement> list = new ArrayList<SearchElement>(rows.length);
    for (int row: rows) list.add(elementAt(row));
    return list;
  }

  @Override
  public void clear()
  {
    model.clear();
  }

  @Override
  public int rowFromLocation(final int x, final int y)
  {
    return table.rowAtPoint(new Point(x, y));
  }

  @Override
  public void showMenu(final JPopupMenu menu, final int x, final int y)
  {
    menu.show(table, x, y);
  }

  private int toInt(final String s, final int def) {
    int res = 0;
    try {
      res = Integer.valueOf(s);
    } catch(Exception e) {
      res = def;
    }
    return res;
  }

  /**
   * 
   */
  private static class SizeComparator implements Comparator<String>
  {
    /**
     * 
     */
    private static final char[] UNITS = {'b', 'k', 'm', 'g'};

    @Override
    public int compare(final String o1, final String o2)
    {
      if (o1 == o2) return 0;
      if (o1 == null) return -1;
      if (o2 == null) return 1;
      if (o1.isEmpty() && o2.isEmpty()) return 0;
      if (o1.length() < 2) return -1;
      if (o2.length() < 2 ) return 1;
      int i1 = indexOf(o1.charAt(o1.length() - 1));
      int i2 = indexOf(o2.charAt(o2.length() - 1));
      if (i1 < i2) return -1;
      if (i1 > i2) return 1;
      if (i1 < 0) return 0;
      Double d1 = null;
      try {
        d1 = Double.valueOf(o1.substring(0, o1.length() - 2));
      } catch(Exception e) {
        return -1;
      }
      Double d2 = null;
      try {
        d2 = Double.valueOf(o2.substring(0, o2.length() - 2));
      } catch(Exception e) {
        return 1;
      }
      return d1.compareTo(d2);
    }

    /**
     * Get the index of the specified character in the <code>UNITS</code> array.
     * @param c the character for which to find to index.
     * @return the index of <code>c</code> in the array, or -1 if c cannot be found inthe array.
     */
    private int indexOf(final char c)
    {
      for (int i=0; i<UNITS.length; i++) if (c == UNITS[i]) return i;
      return -1;
    }
  }
}
