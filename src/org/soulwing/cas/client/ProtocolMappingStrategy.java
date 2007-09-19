/*
 * ProtocolMappingStrategy.java
 *
 * Created on Jul 7, 2007 
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

import org.jdom.Element;

/**
 * A strategy for mapping the XML response from the CAS server to a 
 * <code>ValidationResponse</code> object.
 *
 * @author Carl Harris
 */
public interface ProtocolMappingStrategy {

  /**
   * Gets a <code>ValidationResponse</code> that corresponds to the content of a 
   * JDOM <code>Element</code> representing the the response
   * from the CAS server. 
   * @param element the subject CAS response <code>Element</code>
   * @return an appropriate subclass of <code>ValidationResponse</code>
   */
  ValidationResponse mapResponse(Element element);
  
}
