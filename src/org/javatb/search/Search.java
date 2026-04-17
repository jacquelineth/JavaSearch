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

import java.util.*;

import org.slf4j.*;

/**
 * Utility class to search specific files in a file folder, a zip-compatible archive or a set of archives.<br>
 * Obtaining the search results is done as an event-driven operation.<br>
 * You have to register a listener to be notified of all search events.
 * @author Laurent Cohen
 */
public class Search {
  /**
   * Logger for this class.
   */
  static Logger log = LoggerFactory.getLogger(Search.class);
  /**
   * Fast way to determine whether debug level is enabled.
   */
  static boolean debugEnabled = log.isDebugEnabled();

  /**
   * Search a file matching the specified pattern in an archive file or set of archive files.
   * <p>usage: org.javatb.search.SearchEngine <i>rootPath</i> <i>filePattern</i> [<i>searchString</i>]</code><br>
   * where:
   * <ul>
   * <li><i>rootPath</i> is the path to a folder or an archive<br>
   * compatible archive formats include: .zip, .jar, .ear, .war and .rar</li>
   * <li><i>filePattern</i> specifies a pattern for the files to visit within nested folders or archives<br>
   * if empty, all files will be visited. The wildcards '*' and '?' can be used</li>
   * <li><i>searchString</i> is an optional string to search for in the visited files</li>
   * <ul>
   * @param args specifies the root path, file pattern and search term.<br>
   */
  public static void main(final String...args) {
    try {
      String[] params = new String[3];
      Arrays.fill(params, "");
      if ((args == null) || (args.length < 1)) printUsage("There should be at least 1 argument");
      else if (args.length > 3) printUsage("There should be at most 3 arguments");
      System.arraycopy(args, 0, params, 0, args.length);
      final List<SearchElement> res = new ArrayList<SearchElement>();
      SearchEngine aex = new SearchEngine(params);
      aex.addSearchEngineListener(new SearchEngineListener() {
        @Override
        public void newMessage(final SearchEngineEvent event) {
          if (SearchEngineEvent.Type.MATCH.equals(event.type())) {
            res.add(event.searchElement());
            System.out.println(event.searchElement());
          }
        }
      });
      aex.search();
      if (res.isEmpty()) System.out.println("No matches found");
      else System.out.println("Found " + res.size() + " match" + (res.size() > 1 ? "es" : ""));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Print usage instructions to the console and exit.
   */
  private static void printUsage(final String message) {
    System.out.println("Usage: " + Search.class.getName() + " <rootPath> <filePattern> [<searchString>]");
    System.out.println("");
    System.out.println("where:");
    System.out.println("");
    System.out.println("  - <rootPath> is the path to a folder or an archive");
    System.out.println("    compatible archive formats include: .zip, .jar, .ear, .war and .rar");
    System.out.println("");
    System.out.println("  - <filePattern> is a pattern to search for in each archived file name");
    System.out.println("    the wildcards '*' and '?' can be used");
    System.out.println("");
    System.out.println("  - <searchString> is an optional string to search for in the visited files");
    System.out.println("");
    System.out.println("  - example: " + Search.class.getName() + " ../lib *.class listener");
    System.out.println("    searches for all .class files in ../lib and subfolders that contain the string 'listener'");
    System.exit(0);
  }
}
