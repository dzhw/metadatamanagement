package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import ch.qos.logback.classic.Logger;

/**
 * Dto representation of the logger.
 */
public class LoggerDto {

  private String name;

  private String level;

  public LoggerDto(Logger logger) {
    this.name = logger.getName();
    this.level = logger.getEffectiveLevel().toString();
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
