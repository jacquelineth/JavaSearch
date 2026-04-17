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

import java.util.*;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.javatb.util.*;

/**
 * This class handles the persistence, in the user preferences, of the
 * widths of the columns of a {@link JTable}.
 * @author Laurent Cohen
 */
public class TablePersistenceHandler implements Persistable {
  /**
   * The table whose attributes are persisted.
   */
  private final JTable table;

  /**
   * Initialize this persistence handler.
   * @param name the name assigned to this persitence handler, used by the {@link PreferencesManager}.
   * @param table the table whose attributes are persisted.
   */
  public TablePersistenceHandler(final String name, final JTable table) {
    this.table = table;
    PreferencesManager.register(name, this);
  }

  @Override
  public Map<String, String> getValues() {
    Map<String, String> map = new HashMap<String, String>();
    final TableColumnModel tcm = table.getColumnModel();
    for (int i=0; i<tcm.getColumnCount(); i++) map.put("col."+i, String.valueOf(tcm.getColumn(i).getPreferredWidth()));
    return map;
  }

  @Override
  public void setValues(final Map<String, String> values) {
    final TableColumnModel tcm = table.getColumnModel();
    for (int i=0; i<tcm.getColumnCount(); i++) tcm.getColumn(i).setPreferredWidth(StringUtils.toInt(values.get("col."+i), 100));
  }
}
