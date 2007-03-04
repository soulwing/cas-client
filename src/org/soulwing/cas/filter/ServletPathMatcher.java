/*
 * ServletPathMatcher.java
 *
 * Created on Mar 4, 2007
 *
 * Copyright (C) 2006, 2007 Carl E Harris, Jr.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 */
package org.soulwing.cas.filter;

import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A collection of servlet path glob expressions that can be matched against
 * an arbitrary servlet path.
 *
 * @author Carl Harris
 */
public class ServletPathMatcher {

  private Pattern[] pathsToMatch;

  public ServletPathMatcher(String[] paths) {
    pathsToMatch = new Pattern[paths.length];
    if (paths.length > 0) {
      GlobCompiler compiler = new GlobCompiler();
      for (int i = 0; i < paths.length; i++) {
        try {
          pathsToMatch[i] = compiler.compile(paths[i]);
        }
        catch (MalformedPatternException ex) {
          throw new IllegalArgumentException("compile error for pattern "
              + paths[i] + ": ", ex);
        }
      }
    }
  }
  
  public boolean matches(String servletPath) {
    Perl5Matcher matcher = new Perl5Matcher();
    for (int i = 0; i < pathsToMatch.length; i++) {
      if (matcher.matches(servletPath, pathsToMatch[i])) {
        return true;
      }
    }
    return false;
  }

}
