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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;

/**
 * Abstract implementation of an auto-completion popup window for text fields.
 * @author Laurent Cohen
 */
public abstract class AutoCompleter {
  /**
   * Underlying list containing the elements to select.
   */
  protected JList list = new JList();
  /**
   * Container for the JList component, receiver of keyboard, mouse and focus events.
   */
  protected JPopupMenu popup = new JPopupMenu();
  /**
   * The field for which to perform auto-completion.
   */
  protected JTextField textComp;
  /**
   * Name of the auto-completer property set on the text component.
   */
  private static final String AUTOCOMPLETER = "AUTOCOMPLETER";

  /**
   * Initialize this autocompleter with the specified text field.
   * @param comp the field for which to perform auto-completion.
   */
  public AutoCompleter(final JTextField comp) {
    list.setModel(new DefaultListModel());
    textComp = comp;
    textComp.putClientProperty(AUTOCOMPLETER, this);
    JScrollPane scroll = new JScrollPane(list);
    scroll.setBorder(null);

    list.setFocusable(false);
    scroll.getVerticalScrollBar().setFocusable(false);
    scroll.getHorizontalScrollBar().setFocusable(false);

    popup.setBorder(BorderFactory.createLineBorder(Color.black));
    popup.add(scroll);
    list.addMouseListener(popupMouseListener);

    textComp.getDocument().addDocumentListener(documentListener);
    textComp.addFocusListener(focusListener);
    textComp.registerKeyboardAction(downAction, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_FOCUSED);
    textComp.registerKeyboardAction(upAction, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_FOCUSED);
    textComp.registerKeyboardAction(hidePopupAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
    textComp.registerKeyboardAction(downAction, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
    popup.addPopupMenuListener(new PopupMenuListener() {
      @Override
      public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
      }

      @Override
      public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
        textComp.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
      }

      @Override
      public void popupMenuCanceled(final PopupMenuEvent e) {
      }
    });
    list.setRequestFocusEnabled(false);
  }

  /**
   * Action invoked when [Enter] is pressed or a double-click occurs in the auto-completion popup.
   */
  protected Action acceptAction = new AbstractAction() {
    @Override
    public void actionPerformed(final ActionEvent e) {
      JComponent tf = (JComponent) e.getSource();
      AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
      completer.popup.setVisible(false);
      if (completer.list.getSelectedIndex() >= 0) {
        try {
          textComp.getDocument().removeDocumentListener(documentListener);
          completer.acceptedListItem((String) completer.list.getSelectedValue());
        } finally {
          textComp.getDocument().addDocumentListener(documentListener);
        }
      }
    }
  };

  /**
   * Listener on the text field's document, used to popup the auto-completion window when the text is changed.
   */
  protected DocumentListener documentListener = new DocumentListener() {
    @Override
    public void insertUpdate(final DocumentEvent e) {
      showPopup();
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
      showPopup();
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
    }
  };

  /**
   * Focus listener for the text field.
   * Used to hide the autocompletion window when the user exits the text field.
   * Also adds the new field value to the list in the autocompletion popup.
   */
  protected FocusListener focusListener = new FocusListener() {
    @Override
    public void focusGained(final FocusEvent e) {
    }

    @Override
    public void focusLost(final FocusEvent e) {
      Component comp = e.getOppositeComponent();
      if (comp == null) {
        return;
      }
      if (!comp.equals(list) && !comp.equals(popup) && !(comp instanceof JRootPane)) {
        updateListData();
        popup.setVisible(false);
      }
    }
  };

  /**
   * Listens for double-click selection events on the auto-completion popup.
   */
  protected MouseListener popupMouseListener = new MouseAdapter() {
    @Override
    public void mouseClicked(final MouseEvent event) {
      if ((event.getButton() == MouseEvent.BUTTON1) && (event.getClickCount() == 2))
      {
        int index = list.locationToIndex(new Point(event.getX(), event.getY()));
        if (index < 0) return;
        DefaultListModel model = (DefaultListModel) list.getModel();
        String text = (String) model.getElementAt(index);
        try {
          textComp.getDocument().removeDocumentListener(documentListener);
          AutoCompleter.this.acceptedListItem(text);
        } finally {
          textComp.getDocument().addDocumentListener(documentListener);
        }
        popup.setVisible(false);
      }
    }
  };

  /**
   * Display the auto-completion popup, according to the value of the text field and previously entered values.
   */
  private void showPopup() {
    popup.setVisible(false);
    java.util.List<String> items = getItems();
    if (textComp.isEnabled() && (items != null) && (items.size() != 0)) {
      DefaultListModel model = (DefaultListModel) list.getModel();
      model.removeAllElements();
      for (String s: items) model.addElement(s);
      textComp.registerKeyboardAction(acceptAction, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
      int size = list.getModel().getSize();
      list.setVisibleRowCount(size < 10 ? size : 10);

      int x = 0;
      try {
        int pos = Math.min(textComp.getCaret().getDot(), textComp.getCaret().getMark());
        x = textComp.getUI().modelToView(textComp, pos).x;
      } catch (BadLocationException e) {
        // this should never happen!!!
        e.printStackTrace();
      }
      popup.show(textComp, x, textComp.getHeight());
      popup.setVisible(true);
    }
    else popup.setVisible(false);
    textComp.requestFocus();
  }

  /**
   * Action to navigate one elemnt down in the list.
   */
  static Action downAction = new AbstractAction() {
    @Override
    public void actionPerformed(final ActionEvent e) {
      JComponent tf = (JComponent) e.getSource();
      AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
      if (tf.isEnabled()) {
        if (completer.popup.isVisible()) completer.selectNextPossibleValue();
        else completer.showPopup();
      }
    }
  };

  /**
   * Action to navigate one elemnt up in the list.
   */
  static Action upAction = new AbstractAction() {
    @Override
    public void actionPerformed(final ActionEvent e) {
      JComponent tf = (JComponent) e.getSource();
      AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
      if (tf.isEnabled()) {
        if (completer.popup.isVisible()) completer.selectPreviousPossibleValue();
      }
    }
  };

  /**
   * Hide the autocompletion popup window.
   */
  static Action hidePopupAction = new AbstractAction() {
    @Override
    public void actionPerformed(final ActionEvent e) {
      JComponent tf = (JComponent) e.getSource();
      AutoCompleter completer = (AutoCompleter) tf.getClientProperty(AUTOCOMPLETER);
      if (tf.isEnabled()) completer.popup.setVisible(false);
    }
  };

  /**
   * Selects the next item in the list. It won't change the selection if the currently selected item
   * is already the last item.
   */
  protected void selectNextPossibleValue() {
    int si = list.getSelectedIndex();
    if (si < list.getModel().getSize() - 1) {
      list.setSelectedIndex(si + 1);
      list.ensureIndexIsVisible(si + 1);
    }
  }

  /**
   * Selects the previous item in the list. It won't change the selection if the currently selected item
   * is already the first item.
   */
  protected void selectPreviousPossibleValue() {
    int si = list.getSelectedIndex();
    if (si > 0) {
      list.setSelectedIndex(si - 1);
      list.ensureIndexIsVisible(si - 1);
    }
  }

  /**
   * Update list model depending on the data in textfield.
   * @return true if the model was updated, false otherwise.
   */
  protected abstract boolean updateListData();

  /**
   * Update the text field according to the item selected in the list.
   * @param selected the item selected in the list.
   */
  protected abstract void acceptedListItem(String selected);

  /**
   * Get the list of items to display, based on the text field's content.
   * @return a list of strings.
   */
  public abstract java.util.List<String> getItems();
}
