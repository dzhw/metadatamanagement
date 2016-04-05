package eu.dzhw.fdz.metadatamanagement.common.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import ch.qos.logback.classic.Logger;

/**
 * DTO for setting log levels dynamically.
 */
public class LoggerDto {

  private String name;

  private String level;

  /**
   * Create the DTO.
   */
  public LoggerDto(Logger logger) {
    this.name = logger.getName();
    this.level = logger.getEffectiveLevel()
      .toString();
  }

  @JsonCreator
  public LoggerDto() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  @Override
  public String toString() {
    return "LoggerDTO{" + "name='" + name + '\'' + ", level='" + level + '\'' + '}';
  }
}
