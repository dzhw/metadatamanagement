package eu.dzhw.fdz.metadatamanagement.usermanagement.websocket.dto;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * A message which can be send to all connected users via websockets.
 * 
 * @author René Reitmann
 */
public class MessageDto {
  private String sender;
  private I18nString text;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public I18nString getText() {
    return text;
  }

  public void setText(I18nString text) {
    this.text = text;
  }
}
