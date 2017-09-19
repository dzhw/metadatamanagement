package eu.dzhw.fdz.metadatamanagement.common.websocket.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

/**
 * Mongo Document for keeping track of currently open websocket sessions.
 * 
 * @author René Reitmann
 */
@Document(collection = "active_websocket_sessions")
public class ActiveWebsocketSession {
  
  @Id
  private String id;
  
  private String ipAddress;
  
  private String browser;
  
  private String browserMajorVersion;
  
  private String clientOs;
  
  private String clientOsVersion;
  
  private Set<String> stompVersions;
  
  private LocalDateTime connectedAt;

  /**
   * Generate a new session.
   * @param id The websockets session id
   * @param ipAddress the remote ipaddress of the user
   * @param browser the clients browser
   * @param browserMajorVersion the clients browser version
   * @param clientOs the name of the clients operation system
   * @param clientOsVersion the version of the clients operationg system
   * @param connectedAt the date and time when the connection has been established
   */
  public ActiveWebsocketSession(String id, String ipAddress, String browser,
      String browserMajorVersion, String clientOs, String clientOsVersion,
      Set<String> stompVersions, LocalDateTime connectedAt) {
    super();
    this.id = id;
    this.ipAddress = ipAddress;
    this.browser = browser;
    this.browserMajorVersion = browserMajorVersion;
    this.clientOs = clientOs;
    this.clientOsVersion = clientOsVersion;
    this.stompVersions = stompVersions;
    this.connectedAt = connectedAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public Set<String> getStompVersions() {
    return stompVersions;
  }

  public void setStompVersions(Set<String> stompVersions) {
    this.stompVersions = stompVersions;
  }

  public LocalDateTime getConnectedAt() {
    return connectedAt;
  }

  public void setConnectedAt(LocalDateTime connectedAt) {
    this.connectedAt = connectedAt;
  }
   
  public String getBrowser() {
    return browser;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public String getBrowserMajorVersion() {
    return browserMajorVersion;
  }

  public void setBrowserMajorVersion(String browserMajorVersion) {
    this.browserMajorVersion = browserMajorVersion;
  }

  public String getClientOs() {
    return clientOs;
  }

  public void setClientOs(String clientOs) {
    this.clientOs = clientOs;
  }

  public String getClientOsVersion() {
    return clientOsVersion;
  }

  public void setClientOsVersion(String clientOsVersion) {
    this.clientOsVersion = clientOsVersion;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getId());
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ActiveWebsocketSession domainObject = (ActiveWebsocketSession) object;
    return Objects.equals(this.getId(), domainObject.getId());
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("id", id)
      .add("ipAddress", ipAddress)
      .add("browser", browser)
      .add("browserMajorVersion", browserMajorVersion)
      .add("clientOs", clientOs)
      .add("clientOsVersion", clientOsVersion)
      .add("stompVersions", stompVersions)
      .add("connectedAt", connectedAt)
      .toString();
  }
}
