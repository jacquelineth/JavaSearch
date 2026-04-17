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
import java.util.zip.ZipEntry;

/**
 * Descriptor class for a file, folder or archive entry.
 * Objects of this class are immutable.
 * @author Laurent Cohen
 */
public class SearchElement implements Serializable
{
  /**
   * Explicit serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  /**
   * Type of element is a file.
   */
  public static final int FILE = 1;
  /**
   * Type of element is a folder.
   */
  public static final int FOLDER = 2;
  /**
   * Type of element is an archive file.
   */
  public static final int ARCHIVE = 4;
  /**
   * Type of element is an archive entry.
   */
  public static final int ENTRY = 8;
  /**
   * The type of this element.
   */
  private int type = 0;
  /**
   * The path to this element's parent, including folders, files or nested archives in its ancestors' chain.
   */
  private List<SearchElement> path = null;
  /**
   * The name of this element, can be either the path on the file system or the name of an archive entry.
   */
  private String name = null;
  /**
   * The length of the file.
   */
  private long size = -1L;
  /**
   * The compressed length of the file, if applicable.
   */
  private long compressedSize = -1L;
  /**
   * Last modified date.
   */
  private Date lastModified = null;
  /**
   * The attributes of the file, if applicable.
   * Format is "RWXH" - readable, writeable, executable, hidden, replaced with a '.' if the attribute doesn't apply.
   * For instance: "R..H" means readable and hidden, "RWX." means read/write and execute permissions.
   */
  private String attributes = "";

  /**
   * Initialize this element with the specified name, type and path.
   * @param name the name of this element.
   * @param type the type of this element.
   * @param path the path to this element's parent.
   */
  public SearchElement(final String name, final int type, final List<SearchElement> path)
  {
    this.name = name;
    this.type = type;
    this.path = (path != null) ? path : new ArrayList<SearchElement>();
  }

  /**
   * Initialize this element with the specified name, type and path.
   * @param name the name of this element.
   * @param type the type of this element.
   * @param path the path to this element's parent.
   */
  public SearchElement(final File file, final int type, final List<SearchElement> path)
  {
    this.name = file.getPath();
    this.size = file.length();
    this.compressedSize = -1L;
    this.lastModified = file.lastModified() > 0L ? new Date(file.lastModified()) : null;
    StringBuilder sb = new StringBuilder();
    sb.append(file.canRead() ? 'R' : '.');
    sb.append(file.canWrite() ? 'W' : '.');
    sb.append(file.canExecute() ? 'X' : '.');
    sb.append(file.isHidden() ? 'H' : '.');
    this.attributes = sb.toString();
    this.type = type;
    this.path = (path != null) ? path : new ArrayList<SearchElement>();
  }

  /**
   * Initialize this element with the specified name, type and path.
   * @param name the name of this element.
   * @param type the type of this element.
   * @param path the path to this element's parent.
   */
  public SearchElement(final ZipEntry entry, final int type, final List<SearchElement> path)
  {
    this.name = entry.getName();
    this.size = entry.getSize();
    this.compressedSize = entry.getCompressedSize();
    this.lastModified = entry.getTime() > 0L ? new Date(entry.getTime()) : null;
    this.attributes = "R...";
    this.type = type;
    this.path = (path != null) ? path : new ArrayList<SearchElement>();
  }

  /**
   * Determine whether this element represents a file.
   * @return true if the element is a file, false otherwise.
   */
  public boolean isFile()
  {
    return (type & FILE) != 0;
  }

  /**
   * Determine whether this element represents a folder.
   * @return true if the element is a folder, false otherwise.
   */
  public boolean isFolder()
  {
    return (type & FOLDER) != 0;
  }

  /**
   * Determine whether this element represents an archive.
   * @return true if the element is an archive, false otherwise.
   */
  public boolean isArchive()
  {
    return (type & ARCHIVE) != 0;
  }

  /**
   * Determine whether this element represents an archive entry.
   * @return true if the element is archive entry, false otherwise.
   */
  public boolean isArchiveEntry()
  {
    return (type & ENTRY) != 0;
  }

  /**
   * Get the path to this element's parent.
   * @return a list of <code>SearchElement</code> instances.
   */
  public synchronized List<SearchElement> getPath()
  {
    return path;
  }

  /**
   * Get this element's name.
   * @return the name of this element as a string.
   */
  public synchronized String getName()
  {
    return name;
  }

  /**
   * Get a string representation of this object.
   * @return a string representation of this object.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (SearchElement elt: path) sb.append(elt.getName()).append(File.separatorChar);
    sb.append(name);
    return sb.toString();
  }

  /**
   * The length of the file.
   * @return the length as a long value, pr -1 if the length cannot be obtained.
   */
  public long getSize()
  {
    return size;
  }

  /**
   * Get the last modified date.
   * @return a {@link Date} object, or null if the last modified date cannot be obtained.
   */
  public Date getLastModified()
  {
    return lastModified;
  }

  /**
   * Get the compressed length of the file, if applicable.
   * @return the compressed length, or -1 if the file is not compressed or its compressed size cannot be obtained.
   */
  public long getCompressedSize()
  {
    return compressedSize;
  }

  /**
   * 
   * @return
   */
  public String getAttributes()
  {
    return attributes;
  }
}
