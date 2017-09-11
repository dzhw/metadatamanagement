package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

/**
 * A message which can be send to all connected users via websockets.
 * 
 * @author René Reitmann
 */
public class Message {
  private String sender;
  private String text;

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
