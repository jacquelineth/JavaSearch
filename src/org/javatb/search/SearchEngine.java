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
package org.javatb.search;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;

import org.javatb.util.FileUtils;
import org.slf4j.*;

/**
 * Utility class to search specific files in a file folder, a zip-compatible archive or a set of archives.<br>
 * Obtaining the search results is done as an event-driven operation.<br>
 * You have to register a listener to be notified of all search events.
 * @author Laurent Cohen
 */
public class SearchEngine {
  /**
   * Logger for this class.
   */
  static Logger log = LoggerFactory.getLogger(SearchEngine.class);
  /**
   * Fast way to determine whether debug level is enabled.
   */
  static boolean debugEnabled = log.isDebugEnabled();
  /**
   * The path from which to search recursively.
   */
  private String rootPath = null;
  /**
   * The file name search pattern in Java Regex format.
   */
  private Pattern filePattern = null;
  /**
   * The file content search pattern in Java Regex format.
   */
  private Pattern contentPattern = null;
  /**
   * Path of the archive or folder currently being explored.
   */
  private LinkedList<SearchElement> currentPath = new LinkedList<SearchElement>();
  /**
   * The listeners that receive notifications from this archive explorer.
   */
  private List<SearchEngineListener> listeners = new ArrayList<SearchEngineListener>();
  /**
   * Determine whether the search is to stopped.
   */
  private boolean stopped = false;

  /**
   * Initialize this archive explorer with the specified root path and search pattern.
   * @param args the parameters of the archive explorer.
   */
  public SearchEngine(final String...args) {
    rootPath = args[0];
    String s = "".equals(args[1].trim()) ? "*" : args[1];
    filePattern = stringToPattern(s);
    if ((args.length > 2) && (args[2] != null) && !"".equals(args[2].trim())) {
      contentPattern = Pattern.compile(args[2], Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }
  }

  /**
   * Convert the search pattern into a java.util.regex.Pattern.
   * @param patternString the stringPattern to convert.
   * @return a <code>java.util.regex.Pattern</code> instance.
   */
  private Pattern stringToPattern(final String patternString) {
    StringBuffer sb = new StringBuffer();
    for (int i=0; i<patternString.length(); i++) {
      char c = patternString.charAt(i);
      switch(c) {
        case '*':
        case '?':
          sb.append(".").append(c);
          break;
        case '.':
          sb.append("\\.");
          break;
        default:
          sb.append(Character.toLowerCase(c));
          break;
      }
    }
    return Pattern.compile(sb.toString());
  }

  /**
   * Start the search.
   * @throws Exception if an error is raised while doing the search.
   */
  public void search() throws Exception {
    String[] paths = rootPath.split(File.pathSeparator);
    SearchElement elt = new SearchElement(rootPath, 0, null);
    fireEvent(SearchEngineEvent.Type.START, elt);
    try {
      for (String path: paths) search(new File(path));
    } finally {
      fireEvent(SearchEngineEvent.Type.END, elt);
    }
  }

  /**
   * Search from an abstract file path.
   * @param file the root of the search tree.
   * @throws Exception if an error is raised while doing the search.
   */
  public void search(final File file) throws Exception {
    if (isStopped()) return;
    if (!file.exists()) return;
    String name = file.getName();
    Matcher matcher = filePattern.matcher(name.toLowerCase());
    // file name matches search pattern?
    String path = file.getPath();
    SearchElement elt = null;
    boolean match = false;
    if (!file.isDirectory()) {
      match = matcher.matches();
      String ext = FileUtils.getFileExtension(file.getName());
      // check if file is an archive
      if (FileUtils.ZIP_EXT.contains(ext)) {
        if (contentPattern != null) match = false;
        //elt = new SearchElement(path, SearchElement.ARCHIVE|SearchElement.FILE, null);
        elt = new SearchElement(file, SearchElement.ARCHIVE|SearchElement.FILE, null);
        fireEvent(SearchEngineEvent.Type.EXPLORING, elt);
        if (debugEnabled) log.debug("looking into " + file);
        addElementToPath(elt);
        try {
          search(new BufferedInputStream(new FileInputStream(file)));
        } finally {
          removeLastPathElement();
        }
      } else {
        //elt = new SearchElement(path, SearchElement.FILE, null);
        elt = new SearchElement(file, SearchElement.FILE, null);
        if ((match) && (contentPattern != null) && checkEnoughMemory(file.length())) {
          try {
            String content = FileUtils.readTextFile(file);
            matcher = contentPattern.matcher(content);
            match = match && matcher.find();
            if (debugEnabled) log.debug("file " + (match ? "match!!! " : "") + path);
          } catch(Exception e) {
            match = false;
          }
        }
        else if (!match && debugEnabled) log.debug("file ignored " + path);
      }
    } else {
      if (contentPattern != null) match = false;
      // process contained files and sub-directories
      if (debugEnabled) log.debug("looking into " + file);
      elt = new SearchElement(path, SearchElement.FOLDER, null);
      fireEvent(SearchEngineEvent.Type.EXPLORING, elt);
      File[] children = file.listFiles();
      for (File child: children) {
        if (isStopped()) return;
        search(child);
      }
    }
    if (match) fireEvent(SearchEngineEvent.Type.MATCH, elt);
  }

  /**
   * Search from an input stream representing an archive file.
   * @param is the root of the search subtree.
   * @throws Exception if an error is raised while doing the search.
   */
  public void search(final InputStream is) throws Exception {
    if (isStopped()) return;
    ZipEntry entry = null;
    ZipInputStream zis = null;
    try {
      try {
        zis = new ZipInputStream(is);
        entry = zis.getNextEntry();
      } catch(IOException e) {
        if (debugEnabled) log.debug(e.getMessage(), e);
        // file not in zip format
        if (zis != null) zis.close();
        return;
      }
      while (entry != null) {
        if (isStopped()) return;
        SearchElement elt = null;
        String name = entry.getName();
        List<SearchElement> cpath = getCurrentPath();
        Matcher matcher = filePattern.matcher(FileUtils.getFileShortName(name).toLowerCase());
        // file name matches search pattern?
        boolean match = false;
        if (!entry.isDirectory()) {
          match = matcher.matches();
          String ext = FileUtils.getFileExtension(name);
          // check if entry is a nested archive
          byte[] byteContent = null;
          try {
            byteContent = FileUtils.zipEntryAsBytes(zis);
          } catch(Exception e) {
            if (debugEnabled) log.debug(e.getMessage(), e);
            continue;
          }
          if (FileUtils.ZIP_EXT.contains(ext)) {
            //elt = new SearchElement(name, SearchElement.ARCHIVE|SearchElement.ENTRY, cpath);
            elt = new SearchElement(entry, SearchElement.ARCHIVE|SearchElement.ENTRY, cpath);
            if (contentPattern != null) match = false;
            addElementToPath(elt);
            InputStream newIs = null;
            try {
              newIs = new ByteArrayInputStream(byteContent);
              if (debugEnabled) log.debug("looking into " + elt);
              fireEvent(SearchEngineEvent.Type.EXPLORING, elt);
              search(newIs);
            } finally {
              if (newIs != null) newIs.close();
              removeLastPathElement();
            }
          } else {
            //elt = new SearchElement(name, SearchElement.ENTRY, cpath);
            elt = new SearchElement(entry, SearchElement.ENTRY, cpath);
            if ((match) && (contentPattern != null) && checkEnoughMemory(entry.getSize())) {
              String content = FileUtils.readTextFile(new InputStreamReader(new ByteArrayInputStream(byteContent)));
              matcher = contentPattern.matcher(content);
              match = match && matcher.find();
              if (debugEnabled) log.debug("entry " + (match ? "match!!! " : "") + name);
            }
            else if (!match && debugEnabled) log.debug("entry ignored " + name);
          }
        } else {
          if (contentPattern != null) match = false;
          elt = new SearchElement(name, SearchElement.ENTRY|SearchElement.FOLDER, cpath);
        }
        if (match) fireEvent(SearchEngineEvent.Type.MATCH, elt);
        entry = zis.getNextEntry();
      }
    } finally {
      if (zis != null) zis.close();
    }
  }

  public synchronized void addElementToPath(final SearchElement elt) {
    currentPath.add(elt);
  }

  public synchronized void removeLastPathElement() {
    currentPath.removeLast();
  }

  /**
   * Get the current search path as string.
   * @return a string representation of the current path.
   */
  public synchronized List<SearchElement> getCurrentPath() {
    return new ArrayList<SearchElement>(currentPath);
  }

  /**
   * Generate a set from an array.
   * @param <U> the type of the elements in the array.
   * @param array the array to convert into a set.
   * @return a set containing all the elements of the input array.
   */
  public static <U> Set<U> asSet(final U[] array) {
    Set<U> set = new HashSet<U>();
    for (U u: array) set.add(u);
    return set;
  }

  /**
   * Add a listener to the list of listeners of this archive explorer.
   * @param listener the listener to add.
   */
  public void addSearchEngineListener(final SearchEngineListener listener) {
    listeners.add(listener);
  }

  /**
   * Remove a listener from the list of listeners of this archive explorer.
   * @param listener the listener to remove.
   */
  public void removeSearchEngineListener(final SearchEngineListener listener) {
    listeners.remove(listener);
  }

  /**
   * Notify all listeners of the specified message.
   * @param message
   */
  protected void fireEvent(final SearchEngineEvent.Type type, final SearchElement message) {
    for (SearchEngineListener listener: listeners) listener.newMessage(new SearchEngineEvent(type, message));
  }

  /**
   * Determine whether the current search was cancelled.
   * @return true if the search was cancelled, false otherwise.
   */
  public synchronized boolean isStopped() {
    return stopped;
  }

  /**
   * Specify whether to cancel the current search
   * @param stopped
   */
  public synchronized void setStopped(final boolean stopped) {
    this.stopped = stopped;
  }

  /**
   * Determine if there is enough memory to read he current file or zip entry content.
   * @param size the size of the current file or zip entry.
   * @return true if the available memory is greater than the file size, false otherwise.
   */
  private boolean checkEnoughMemory(final long size) {
    Runtime rt = Runtime.getRuntime();
    long used = rt.totalMemory() - rt.freeMemory();
    long max = rt.maxMemory();
    long available = max - used;
    return available >= 3*size;
  }
}
