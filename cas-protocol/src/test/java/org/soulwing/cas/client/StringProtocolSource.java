/*
 * StringProtocolSource.java
 *
 * Created on Sep 8, 2006
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
package org.soulwing.cas.client;

import java.io.StringReader;

import org.xml.sax.InputSource;


/**
 * A mock up for ProtocolSource.
 *
 * @author Carl Harris
 * 
 */
public class StringProtocolSource implements ProtocolSource {

  private String text;

  public StringProtocolSource() {  
  }
  
  public StringProtocolSource(String text) {
    this();
    setText(text);
  }
  
  public String getText() {
    return this.text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  public InputSource getSource(String url) {
    return new InputSource(new StringReader(text));
  }

}
