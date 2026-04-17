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
package org.javatb.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * This log formatter provies a nicer formatting than the default simple formatter of the JDK.<br>
 * Records are formatted with this format:<br>
 * yyyy-MM-dd HH:mm:ss.SSS [&lt;log_level&gt;][&lt;class_fqn&gt;.&lt;method&gt;()] &lt;message&gt;
 * @author Laurent Cohen
 */
public class NiceLogFormatter extends Formatter
{
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  /**
   * Default constructor.
   */
  public NiceLogFormatter()
  {
  }

  /**
   * Format the given LogRecord.
   * @param record the log record to format.
   * @return a formatted log record.
   * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
   */
  @Override
  public String format(final LogRecord record)
  {
    //yyyy-MM-dd HH:mm:ss.SSS [<level>][<class_fqn>.<method>()] <message>
    StringBuffer sb = new StringBuffer();
    Date date = new Date(record.getMillis());
    String s = null;
    synchronized(DATE_FORMAT)
    {
      s = DATE_FORMAT.format(date);
    }
    sb.append(s).append(" ");
    StringBuffer sb2 = new StringBuffer(record.getLevel().toString());
    while (sb2.length() < 7) sb2.append(" ");
    sb.append("[").append(sb2).append("]");
    sb.append("[");
    s = record.getSourceClassName();
    if (s != null) sb.append(s);
    s = record.getSourceMethodName();
    if (s != null) sb.append(".").append(s).append("()");
    sb.append("] ");
    sb.append(record.getMessage());
    Throwable t = record.getThrown();
    if (t != null)
    {
      StringWriter sw = new StringWriter();
      t.printStackTrace(new PrintWriter(sw));
      sb.append("\n").append(sw.toString());
    }
    sb.append("\n");
    return sb.toString();
  }
}
