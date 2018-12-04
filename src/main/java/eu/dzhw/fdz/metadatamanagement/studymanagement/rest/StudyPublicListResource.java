package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.io.IOException;

import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Ednpoint to deliver released studies.
 *
 * @author tgehrke
 *
 */
@Slf4j
@Controller
@RequestMapping("/api")
@Validated
@Api(value = "Study List Resource",
    description = "Endpoint for retrieving released studies.")
public class StudyPublicListResource {

  @Autowired
  private StudyListService studylistService;

  /**
   * Request a pageble list of released studies.
   *
   * @param page the page. default 0
   * @param size the size of a page. default 5
   * @return the page object. containing the list of studies as content and metadata regarding the
   *         paging.
   */
  @GetMapping(value = "/studies")
  @ApiOperation("Get the paged list of currently released studies.")
  public ResponseEntity<Page<Study>> listStudies(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") @Max(20) int size) {
    try {
      Page<Study> loadStudies = studylistService.loadStudies(page, size);
      return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(loadStudies);
    } catch (IOException e) {
      log.warn("reqzesting the list of studies failed", e);
      return ResponseEntity.badRequest().body(null);
    }
  }
}
