package org.soulwing.cas.apps.undertow;

public class CasConfigurationBean implements CasConfiguration {

  @Override
  public String getServerUrl() {
    return "SERVER URL";
  }

  @Override
  public String getApplicationUrl() {
    return "APPLICATION URL";
  }

}
