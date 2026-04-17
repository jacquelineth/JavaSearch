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

package org.javatb.search.archive;

import java.io.*;
import java.util.Date;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

/**
 * 
 * @author Laurent Cohen
 */
public class GZArchiveInputStream extends ArchiveInputStream
{
  /**
   * The name of this GZip file.
   */
  private final String name;
  /**
   * The unerlying gzip input stream.
   */
  private final GzipCompressorInputStream in;
  private GZipEntry entry = null;

  /**
   * 
   * @param name
   * @param in
   * @throws IOException
   */
  public GZArchiveInputStream(final String name, final InputStream in) throws IOException
  {
    this.name = name;
    this.in = new GzipCompressorInputStream(in);
  }


  @Override
  public ArchiveEntry getNextEntry() throws IOException
  {
    if (getBytesRead() > 0) return null;
    else
    {
      if (entry == null)
      {
      }
    }
    return entry;

  }

  public static class GZipEntry implements ArchiveEntry
  {
    @Override
    public Date getLastModifiedDate()
    {
      return null;
    }

    @Override
    public String getName()
    {
      return null;
    }

    @Override
    public long getSize()
    {
      return 0;
    }

    @Override
    public boolean isDirectory()
    {
      return false;
    }
  }
}
