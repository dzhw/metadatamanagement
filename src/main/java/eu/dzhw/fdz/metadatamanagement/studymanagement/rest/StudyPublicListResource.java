package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyListService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api")
public class StudyPublicListResource {
  @Autowired
  private StudyListService studylistService;

  @GetMapping(value = "/studies")
  public ResponseEntity<List<StudySearchDocument>> listStudies(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size) {
    try {
      List<StudySearchDocument> loadStudies = studylistService.loadStudies(page, size);
      return ResponseEntity.ok().body(loadStudies);
    } catch (IOException e) {
      log.warn("reqzesting the list of studies failed", e);
      return ResponseEntity.badRequest().body(null);
    }
  }
}
