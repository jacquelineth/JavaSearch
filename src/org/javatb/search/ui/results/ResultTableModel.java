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

import java.text.*;
import java.util.*;

import javax.swing.table.AbstractTableModel;

import org.javatb.search.SearchElement;

/**
 * 
 * @author Laurent Cohen
 */
public class ResultTableModel extends AbstractTableModel {
  /**
   * Index of the file path column.
   */
  static final int PATH = 0;
  /**
   * Index of the file size column.
   */
  static final int SIZE = 1;
  /**
   * Index of the compressed file size column.
   */
  static final int COMPRESSED_SIZE = 2;
  /**
   * Index of the last modified date column.
   */
  static final int DATE = 3;
  /**
   * Index of the file attributes column.
   */
  static final int ATTRIBUTES = 4;

  private static final long KB = 1024L;
  private static final long MB = KB * KB;
  private static final long GB = KB * MB;

  private final NumberFormat nf = NumberFormat.getInstance();
  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  /**
   * The actual results.
   */
  private final List<SearchElement> results = new ArrayList<SearchElement>();

  public ResultTableModel() {
    nf.setMaximumFractionDigits(1);
  }

  @Override
  public int getRowCount() {
    return results.size();
  }

  @Override
  public int getColumnCount() {
    return ATTRIBUTES + 1;
  }

  @Override
  public Object getValueAt(final int row, final int column) {
    if ((row < 0) || (row >= results.size())) return null;
    SearchElement elt = results.get(row);
    switch(column) {
      case PATH: return elt.toString();
      case SIZE:
        /*
        if ((elt.getSize() < 0L) && (elt.getCompressedSize() < 0L)) return "";
        StringBuilder sb = new StringBuilder();
        if (elt.getSize() >= 0L) sb.append(formatSize(elt.getSize()));
        if (elt.getCompressedSize() >= 0L) {
          if (sb.length() > 0) sb.append(' ');
          sb.append('(').append(formatSize(elt.getCompressedSize())).append(')');
        }
        return sb.toString();
         */
        return (elt.getSize() < 0L) ? "" : formatSize(elt.getSize());
      case COMPRESSED_SIZE: return (elt.getCompressedSize() < 0L) ? "" : formatSize(elt.getCompressedSize());
      case DATE: return elt.getLastModified() == null ? "" : sdf.format(elt.getLastModified());
      case ATTRIBUTES: return elt.getAttributes();
    }
    return "";
  }

  @Override
  public String getColumnName(final int column) {
    switch(column)
    {
      case PATH: return "Path";
      case SIZE: return "Size";
      case COMPRESSED_SIZE: return "Compressed size";
      case DATE: return "Modified";
      case ATTRIBUTES: return "Attributes";
    }
    return "";
  }

  public void addElement(final SearchElement element)
  {
    int size = results.size();
    results.add(element);
    fireTableRowsInserted(size, size);
  }

  public SearchElement elementAt(final int row)
  {
    return results.get(row);
  }

  public void clear()
  {
    if (results.isEmpty()) return;
    int size = results.size();
    results.clear();
    fireTableRowsDeleted(0, size-1);
  }

  private String formatSize(final long size) {
    if (size < 0L) return "";
    else if (size < KB) return "" + size + " b";
    else if (size < MB) return nf.format((double) size / (double) KB) + " k";
    else if (size < GB) return nf.format((double) size / (double) MB) + " m";
    return nf.format((double) size / (double) GB) + " g";
  }
}
