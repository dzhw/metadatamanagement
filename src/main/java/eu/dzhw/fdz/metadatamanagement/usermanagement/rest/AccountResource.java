package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class AccountResource {

  /**
   * Check if the user is authenticated, and return its login.
   */
  @RequestMapping(value = "/authenticate", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> isAuthenticated(HttpServletRequest request) {
    log.debug("REST request to check if the current user is authenticated");
    return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(request.getRemoteUser());
  }
}
