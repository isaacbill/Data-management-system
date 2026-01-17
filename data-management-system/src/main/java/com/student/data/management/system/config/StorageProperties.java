package com.student.data.management.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
  private String baseDirWindows;
  private String baseDirLinux;

  public String getBaseDirWindows() { return baseDirWindows; }
  public void setBaseDirWindows(String baseDirWindows) { this.baseDirWindows = baseDirWindows; }

  public String getBaseDirLinux() { return baseDirLinux; }
  public void setBaseDirLinux(String baseDirLinux) { this.baseDirLinux = baseDirLinux; }
}