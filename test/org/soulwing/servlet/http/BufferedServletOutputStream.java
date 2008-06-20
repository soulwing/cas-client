/*
 * BufferedServletOutputStream.java
 *
 * Created on Jun 20, 2008
 *
 * Copyright (C) 2006, 2007, 2008 Carl E Harris, Jr.
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
package org.soulwing.servlet.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class BufferedServletOutputStream extends ServletOutputStream {

  private final ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
  
  /* (non-Javadoc)
   * @see java.io.OutputStream#write(int)
   */
  public void write(int b) throws IOException {
    bos.write(b);
  }

  /* (non-Javadoc)
   * @see java.io.OutputStream#close()
   */
  public void close() throws IOException {
    bos.close();
    super.close();
  }

  public byte[] toByteArray() {
    return bos.toByteArray();
  }
}