package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto;

/**
 * This Message DTO is for transfering the id of a error message with all depending parameter 
 * to the client. 
 * 
 * @author dkatzberg
 */
public class PostValidationMessageDto {
  
  private String messageId;
  
  private String[] messageParameter;
  
  public PostValidationMessageDto(String messageId, String[] messageParameter) { 
    this.messageId = messageId;
    this.messageParameter = messageParameter.clone();
  }

  /* GETTER / SETTER */
  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String[] getMessageParameter() {
    return this.messageParameter.clone();
  }

  public void setMessageParameter(String[] messageParameter) {
    this.messageParameter = messageParameter.clone();
  }
}
