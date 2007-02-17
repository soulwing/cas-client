/*
 * StringProtocolSource.java
 *
 * Created on Sep 8, 2006
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
