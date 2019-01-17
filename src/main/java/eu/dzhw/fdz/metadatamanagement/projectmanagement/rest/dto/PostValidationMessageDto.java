package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto;

import java.util.List;

/**
 * This Message DTO is for transferring the id of an error message with all depending parameters
 * to the client. 
 * 
 * @author Daniel Katzberg
 */
public class PostValidationMessageDto {
  
  private String messageId;
  
  private List<String> messageParameter;
  
  public PostValidationMessageDto(String messageId, List<String> messageParameter) { 
    this.messageId = messageId;
    this.messageParameter = messageParameter;
  }

  /* GETTER / SETTER */
  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public List<String> getMessageParameter() {
    return this.messageParameter;
  }
}
