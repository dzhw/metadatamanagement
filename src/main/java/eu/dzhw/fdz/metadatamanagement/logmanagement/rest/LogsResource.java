package eu.dzhw.fdz.metadatamanagement.logmanagement.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import eu.dzhw.fdz.metadatamanagement.common.rest.dto.LoggerDto;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api")
public class LogsResource {

  /**
   * Get the current log levels.
   */
  @RequestMapping(value = "/logs", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<LoggerDto> getList() {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    return context.getLoggerList()
      .stream()
      .map(LoggerDto::new)
      .collect(Collectors.toList());
  }

  /**
   * Set a new log level.
   */
  @RequestMapping(value = "/logs", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Timed
  public void changeLevel(@RequestBody LoggerDto jsonLogger) {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    context.getLogger(jsonLogger.getName())
      .setLevel(Level.valueOf(jsonLogger.getLevel()));
  }
}
