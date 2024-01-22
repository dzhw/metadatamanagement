package eu.dzhw.fdz.metadatamanagement.common.rest;


import eu.dzhw.fdz.metadatamanagement.common.service.TweetService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tweet Controller.
 */
@RestController
@RequiredArgsConstructor
public class TweetController {

  private final TweetService tweetService;

  /**
   * Create tweet.
   *
   * @param tweetTextInput Tweet text.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @PostMapping(value = "/api/data-acquisition-projects/tweet")
  public ResponseEntity<?> createTweet(@RequestBody(required = false) String tweetTextInput) {
    return tweetService.createTweet(tweetTextInput);
  }
}

