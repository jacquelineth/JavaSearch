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
import java.io.File;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

import org.javatb.search.*;
import org.javatb.search.ui.results.*;
import org.javatb.util.*;

/**
 * Main class for the JavaSearch UI.
 * @author Laurent Cohen
 */
public class UILauncher
{
  public static Theme theme = Theme.DEFAULT;
  //private static Theme theme = Theme.TEST;
  private String rootPath = "";
  private String filePattern = "";
  private String searchTerm = "";
  /**
   * The number of matches in the current/last search, if any.
   */
  private int nbMatches = 0;
  /**
   * The text field holding the root of the search path.
   */
  private JTextField pathField = null;
  /**
   * The file pattern text field.
   */
  private JTextField fileField = null;
  /**
   * The auto completion decorator for the file pattern.
   */
  private SortedPersistedAutoCompleter fileFieldDecorator = null;
  /**
   * The search term text field.
   */
  private JTextField termField = null;
  /**
   * The auto completion decorator for the search term.
   */
  private SortedPersistedAutoCompleter termFieldDecorator = null;
  /**
   * The number of matches displayed in the status bar.
   */
  private JTextField nbMatchesField = null;
  /**
   * The current path being explored during a search, displayed in the status bar.
   */
  private JTextField exploringField = null;
  /**
   * The button that launches the search.
   */
  private JButton goButton = null;
  /**
   * The button that interrupts an ongoing search.
   */
  private JButton stopButton = null;
  /**
   * 
   */
  private ResultRenderer resultRenderer;
  /**
   * Main frame of this UI.
   */
  private static JFrame frame = null;
  /**
   * The engine which performs the search according to the user-specified criteria.
   */
  private SearchEngine searchEngine = null;
  /**
   * Action performed when clicking on the 'go' or 'stop' button.
   */
  private AbstractAction searchAction = new AbstractAction() {
    @Override
    public void actionPerformed(final ActionEvent e) {
      if (goButton.isEnabled()) search();
      else searchEngine.setStopped(true);
    }
  };
  /**
   * A boolean flag indicating whether a search is currently ongoing.
   */
  private final AtomicBoolean searching = new AtomicBoolean(false);

  /**
   * Launches the explorer UI.
   * @param args if an argument is specified, it is used as the search start location,
   * otherwise the current directory will be used.
   */
  public static void main(String...args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      frame = new JFrame("Find4j");
      Image img = Toolkit.getDefaultToolkit().getImage("images/JavaTB-icon.gif");
      frame.setIconImage(img);
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(final WindowEvent e) {
          onClose();
        }
      });
      if ((args == null) || (args.length < 1)) args = new String[] { System.getProperty("user.dir") };
      final UILauncher ui = new UILauncher(args[0]);
      JComponent comp = ui.createUI();
      JRootPane root = frame.getRootPane();
      InputMap inputMap = root.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
      ActionMap actionMap = root.getActionMap();
      actionMap.put("escape.action", new AbstractAction() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          if (ui.isSearching()) ui.stopSearch();
          else onClose();
        }
      });
      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape.action");
      frame.getContentPane().add(comp);
      new FramePersistenceHandler("main.frame", frame);
      //frame.setSize(800, 600);
      frame.setVisible(true);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Save the search fields hisory and exit.
   */
  private static void onClose() {
    PreferencesManager.saveAll();
    System.exit(0);
  }

  /**
   * Initialize this UI.
   * @param rootPath the root search path, passed on from the command line.
   */
  public UILauncher(final String rootPath) {
    this.rootPath = rootPath;
    try {
      Properties props = Configuration.getProperties();
      String s = Configuration.getString(ConfigurationProperty.RESULT_RENDERER);
      if ((s != null) && !"".equals(s.trim())) {
        Class<?> c = Class.forName(s);
        this.resultRenderer = (ResultRenderer) c.newInstance();
      }
    } catch(Exception e) {
      //this.resultRenderer = new ListResultRenderer();
      this.resultRenderer = new TableResultRenderer();
    }
  }

  /**
   * Create the UI components.
   * @return the root container of the UI, as a <code>JComponent</code> instance.
   */
  public JComponent createUI() {
    JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createTopPanel(), resultRenderer.getUIComponent());
    sp.setDividerSize(0);
    JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, createStatusBar());
    sp2.setDividerSize(0);
    sp2.setResizeWeight(1);
    return sp2;
  }

  /**
   * Create the panel holding the buttons and information used in the search.
   * @return initialized panel
   */
  public JComponent createTopPanel() {
    return createBoxLayoutPanel(BoxLayout.Y_AXIS, createRootPathPanel(), createSearchCriteriaPanel());
  }

  /**
   * Create the single line panel containing the search start folder or archive.
   * @return a <code>JPanel</code> instance.
   */
  private JPanel createRootPathPanel() {
    String tooltip = formatTooltip("Select a folder or compressed file<br>as the new search root");
    pathField = new JTextField(rootPath);
    pathField.setToolTipText(tooltip);
    theme.setColors(pathField);
    JButton btn = createButton("...", tooltip, new Dimension(30, 20));
    btn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent event) {
        chooseFolder();
      }
    });
    JLabel label = new JLabel("Search in ");
    theme.setColors(label);
    label.setToolTipText(tooltip);
    return createBoxLayoutPanel(BoxLayout.X_AXIS, label, pathField, btn);
  }

  /**
   * Create the single line panel containing the file pattern and search term fields,
   * along with the buttons.
   * @return a <code>JPanel</code> instance.
   */
  private JPanel createSearchCriteriaPanel() {
    String tooltip = formatTooltip("Enter the pattern for the files to search<br>'?' and '*' wildcards are accepted");
    fileField = new JTextField(filePattern);
    fileFieldDecorator = new SortedPersistedAutoCompleter(fileField, "filePattern");
    fileField.setToolTipText(tooltip);
    theme.setColors(fileField);
    JLabel fileLabel = new JLabel("File pattern");
    fileLabel.setToolTipText(tooltip);
    theme.setColors(fileLabel);

    tooltip = formatTooltip("Enter the text to find");
    termField = new JTextField(searchTerm);
    termFieldDecorator = new SortedPersistedAutoCompleter(termField, "searchTerm");
    termField.setToolTipText(tooltip);
    theme.setColors(termField);
    JLabel termLabel = new JLabel(" Search term");
    termLabel.setToolTipText(tooltip);
    theme.setColors(termLabel);

    goButton = createButton("Go", "Launch the search with the specified criteria", new Dimension(60, 20));
    goButton.addActionListener(searchAction);
    frame.getRootPane().setDefaultButton(goButton);
    stopButton = createButton("Stop", "Interrupt the current search", new Dimension(70, 20));
    stopButton.setEnabled(false);
    stopButton.addActionListener(searchAction);

    return createBoxLayoutPanel(BoxLayout.X_AXIS, fileLabel, fileField, termLabel, termField, goButton, stopButton);
  }

  /**
   * Create a status bar at the bottom of the UI, displaying the folder
   * or archive being searched, and the latest number of search matches.
   * @return
   */
  private JPanel createStatusBar() {
    nbMatchesField = new JTextField("0");
    theme.setColors(nbMatchesField);
    JLabel matchesLabel = new JLabel(" Matches");
    theme.setColors(matchesLabel);
    nbMatchesField.setHorizontalAlignment(SwingConstants.RIGHT);
    nbMatchesField.setEditable(false);
    nbMatchesField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    Dimension d = new Dimension(50, 20);
    nbMatchesField.setMinimumSize(d);
    nbMatchesField.setMaximumSize(d);
    nbMatchesField.setPreferredSize(d);
    JLabel exploringLabel = new JLabel(" Exploring");
    theme.setColors(exploringLabel);
    exploringField = new JTextField("");
    theme.setColors(exploringField);
    exploringField.setEditable(false);
    exploringField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    JPanel line3 = createBoxLayoutPanel(BoxLayout.X_AXIS, exploringLabel, exploringField, matchesLabel, nbMatchesField);
    Color c = line3.getBackground();
    exploringField.setBackground(c);
    nbMatchesField.setBackground(c);

    JPanel panel = new JPanel();
    theme.setColors(panel);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(Box.createVerticalStrut(2));
    panel.add(line3);
    panel.add(Box.createVerticalStrut(3));
    return panel;
  }

  /**
   * Layout the specified components on a single line.
   * @param comps the components to layout.
   * @return a <code>JPanel</code> enclosing a laying out the components.
   */
  private JPanel createBoxLayoutPanel(int axis, final Component...comps) {
    JPanel panel = new JPanel();
    theme.setColors(panel);
    panel.setLayout(new BoxLayout(panel, axis));
    panel.add(axis == BoxLayout.X_AXIS ? Box.createHorizontalStrut(5) : Box.createVerticalStrut(5));
    for (Component c: comps) {
      panel.add(c);
      panel.add(axis == BoxLayout.X_AXIS ? Box.createHorizontalStrut(5) : Box.createVerticalStrut(5));
    }
    return panel;
  }

  /**
   * Create a button with the specified label and fixed dimension.
   * @param label the button's text.
   * @param tooltip the button's tooltip text.
   * @param dim the button's dimension.
   * @return a <code>JButton</code> instance.
   */
  private JButton createButton(final String label, final String tooltip, final Dimension dim) {
    JButton btn = new JButton(label);
    btn.setToolTipText(tooltip);
    btn.setMinimumSize(dim);
    btn.setMaximumSize(dim);
    btn.setPreferredSize(dim);
    return btn;
  }

  /**
   * Choose the folder of archive to start the next search from.
   */
  public void chooseFolder() {
    JFileChooser chooser = new JFileChooser();
    File file = new File(rootPath);
    if (!file.exists())  file = new File(System.getProperty("user.dir"));
    else if (!file.isDirectory()) file = file.getParentFile();
    chooser.setCurrentDirectory(file);
    chooser.setMultiSelectionEnabled(false);
    FileFilter filter = new FileFilter() {
      @Override
      public boolean accept(final File file) {
        if (!file.exists()) return false;
        if (file.isDirectory()) return true;
        String ext = FileUtils.getFileExtension(file);
        return FileUtils.ZIP_EXT.contains(ext);
      }
      @Override
      public String getDescription() {
        StringBuilder sb = new StringBuilder("<html><p align='center'>File folders and archives<br><i>(as specified in the configuration)</i></html>");
        return sb.toString();
      }
    };
    chooser.addChoosableFileFilter(filter);
    chooser.setFileFilter(filter);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    chooser.setAcceptAllFileFilterUsed(false);
    int response = chooser.showDialog(null, "Select");
    if (JFileChooser.APPROVE_OPTION == response) {
      rootPath = chooser.getSelectedFile().getPath();
      pathField.setText(rootPath);
    }
  }

  /**
   * Perform the search using the information entered in the text fields.
   */
  public synchronized void search() {
    try {
      goButton.setEnabled(false);
      stopButton.setEnabled(true);

      fileFieldDecorator.updateListData();
      termFieldDecorator.updateListData();
      resultRenderer.clear();
      rootPath = pathField.getText();
      filePattern = fileField.getText();
      searchTerm = termField.getText();
      nbMatches = 0;
      nbMatchesField.setText("0");

      searchEngine = new SearchEngine(new String[] { rootPath, filePattern, searchTerm});
      new Thread(new SearchTask()).start();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Determine whether a search is currently ongoing.
   * @return <code>true</code> if a search is running, <code>false</code> otherwise.
   */
  public boolean isSearching() {
    return searching.get();
  }

  /**
   * Update the 'Exploring' text field in the status bar with the specified message.
   * @param text the text of the message.
   * @param color the color in which to display the message.
   */
  protected void updateExploringFieldText(final String text, final Color color) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        exploringField.setForeground(color);
        exploringField.setText(text);
      }
    });
  }

  /**
   * Update the 'Matches' text field in the status bar with the specified message.
   * @param elt
   */
  protected void updateMatches(final SearchElement elt) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        resultRenderer.addElement(elt);
        nbMatchesField.setText(Integer.toString(nbMatches));
      }
    });
  }

  /**
   * Get the number of matches in the current/last search.
   * @return the number of matches an int.
   */
  public int getNbMatches() {
    return nbMatches;
  }

  /**
   * Set the number of matches in the current/last search.
   * @param nbMatches the number of matches an int.
   */
  public void setNbMatches(final int nbMatches) {
    this.nbMatches = nbMatches;
  }

  /**
   * Increment the number of matches.
   */
  public void incNbMatches() {
    nbMatches++;
  }
 
  /**
   * Request from the engine that it stops its search.
   */
  public void stopSearch() {
    if (searchEngine != null) searchEngine.setStopped(true);
  }

  /**
   * Surround a piece of text with &lt;html&gt; and &lt;/html&gt; tags.
   * @param text the text to format.
   * @return a formatted text that can be displayed as html-formatted.
   */
  private String formatTooltip(final String text) {
    return new StringBuilder("<html>").append(text).append("</html>").toString();
  }

  /**
   * This task is to be run in a thread separate from the AWT thread,
   * to ensure the UI is still active and responsive while a search is running.
   */
  public class SearchTask implements Runnable {
    @Override
    public void run() {
      SearchEngineListener listener = null;
      try {
        searching.set(true);
        listener = new SearchListener(UILauncher.this);
        searchEngine.addSearchEngineListener(listener);
        searchEngine.search();
      } catch(Exception e) {
        e.printStackTrace();
      } finally {
        searching.set(false);
        goButton.setEnabled(true);
        stopButton.setEnabled(false);
        if ((listener != null) && (searchEngine != null)) searchEngine.removeSearchEngineListener(listener);
        searchEngine = null;
      }
    }
  }
}
