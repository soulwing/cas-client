package org.soulwing.cas.client;

import java.util.ArrayList;
import java.util.List;


/**
 * A value class that represents the response to the CAS 
 * <code>/proxyValidate</code> function.
 *
 * @author Carl Harris
 * 
 */
public class ProxyValidationResponse extends ServiceValidationResponse {

  private List proxies = new ArrayList();

  /**
   * Gets the list of proxies from the CAS response.
   * @return list of String elements containing proxy names.
   */
  public List getProxies() {
    return proxies;
  }

  /**
   * Sets the list of proxies for the CAS response.
   * @param proxies list of String elements containing proxy names.
   */
  public void setProxies(List proxies) {
    this.proxies = proxies;
  }
  
  /**
   * Adds a proxy to the list of proxies for the CAS response.
   * @param proxy proxy name string to add.
   */
  public void addProxy(String proxy) {
    this.proxies.add(proxy);
  }
  
}
