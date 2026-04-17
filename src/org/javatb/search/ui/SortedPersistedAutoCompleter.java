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

import java.util.*;

import javax.swing.*;

import org.javatb.util.*;

/**
 * Implementation of an AutoCompleter that persists the {@link org.javatb.search.ui.SortedPersistedAutoCompleter#MAX_ITEMS MAX_ITEMS}
 * most recently used items.<br>
 * Items are persisted using the Java preferences APIs.
 * @author Laurent Cohen
 */
public class SortedPersistedAutoCompleter extends AutoCompleter implements Persistable
{
	/**
	 * Maximum number of items kept in the list.
	 */
	private static final int MAX_ITEMS = Configuration.getInt(ConfigurationProperty.HISTORY_SIZE);
	/**
	 * The list of items.
	 */
	private List<String> items = new ArrayList<String>();
	/**
	 * The sorted list of items.
	 */
	private List<String> sortedItems = new ArrayList<String>();

	/**
	 * Initial this auto completer with the speciufied tet component.
	 * @param comp the text component to decorate.
	 */
	public SortedPersistedAutoCompleter(JTextField comp, String name)
	{
		super(comp);
		PreferencesManager.register(name, this);
	}

	/**
	 * Update the text field according to the item selected in the list.
	 * @param selected the item selected in the list.
	 * @see org.javatb.search.ui.AutoCompleter#acceptedListItem(java.lang.String)
	 */
	protected void acceptedListItem(String selected)
	{
		textComp.setText(selected);
	}

	/**
	 * Update list model depending on the data in textfield.
	 * @return true if the model was updated, false otherwise.
	 * @see org.javatb.search.ui.AutoCompleter#updateListData()
	 */
	protected boolean updateListData()
	{
		return updateListData(textComp.getText());
	}

	/**
	 * Update list model with the specified value.
	 * @param text the value to update the model with.
	 * @return true if the model was updated, false otherwise.
	 */
	private boolean updateListData(String text)
	{
		if ((text == null) || "".equals(text.trim())) return false;
		int idx = Collections.binarySearch(sortedItems, text);
		if (idx >= 0) return false;
		idx = -1 - idx;
		items.add(text);
		sortedItems.add(idx, text);
		if (items.size() > MAX_ITEMS)
		{
			String toRemove = items.remove(0);
			idx = Collections.binarySearch(sortedItems, toRemove);
			sortedItems.remove(idx);
		}
		return true;
	}

	/**
	 * Get the list of items to display, based on the text field's content.
	 * @return a list of strings.
	 * @see org.javatb.search.ui.AutoCompleter#getItems()
	 */
	public List<String> getItems()
	{
		String text = textComp.getText();
		List<String> tmp = new ArrayList<String>();
		for (String s: sortedItems)
		{
			if (s.startsWith(text)) tmp.add(s);
		}
		return tmp;
	}

	/**
	 * Get the list of items to display, based on the text field's content.
	 * @return a list of strings.
	 */
	public Map<String, String> getValues()
	{
		Map<String, String> map = new TreeMap<String, String>();
		int count = 1;
		for (String s: sortedItems) map.put("value" + count++, s);
		return map;
	}

	/**
	 * Set a list of values as the list of items.
	 * @param values the values to set as items.
	 */
	public void setValues(Map<String, String> values)
	{
		if (values == null) return;
		for (String s: values.values()) updateListData(s);
	}
}
