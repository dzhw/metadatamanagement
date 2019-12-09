package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentService;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyAttachmentService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyAttachmentService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for getting all attachments of a project.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataAcquisitionProjectAttachmentsResourceController {

  private final StudyAttachmentService studyAttachmentService;

  private final SurveyAttachmentService surveyAttachmentService;

  private final DataSetAttachmentService dataSetAttachmentService;

  private final InstrumentAttachmentService instrumentAttachmentService;


  /**
   * Load all attachment metadata objects for the given project id.
   * 
   * @param dataAcquisitionProjectId The id of an project.
   * @return A map of lists of metadata objects.
   */
  @RequestMapping(path = "/data-acquisition-projects/{id:.+}/attachments",
      method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByStudyId(@PathVariable("id") String dataAcquisitionProjectId) {
    Map<String, List<?>> result = new HashMap<>();
    if (!StringUtils.isEmpty(dataAcquisitionProjectId)) {
      result.put("study",
          deduplicate(studyAttachmentService.findAllByProject(dataAcquisitionProjectId),
              StudyAttachmentMetadata::getFileName));
      result.put("surveys", sort(
          deduplicate(surveyAttachmentService.findAllByProject(dataAcquisitionProjectId),
              SurveyAttachmentMetadata::getFileName),
          SurveyAttachmentMetadata::getIndexInSurvey, SurveyAttachmentMetadata::getSurveyNumber));
      result.put("dataSets", sort(
          deduplicate(dataSetAttachmentService.findAllByProject(dataAcquisitionProjectId),
              DataSetAttachmentMetadata::getFileName),
          DataSetAttachmentMetadata::getIndexInDataSet,
          DataSetAttachmentMetadata::getDataSetNumber));
      result.put("instruments",
          sort(
              deduplicate(instrumentAttachmentService.findAllByProject(dataAcquisitionProjectId),
                  InstrumentAttachmentMetadata::getFileName),
              InstrumentAttachmentMetadata::getIndexInInstrument,
              InstrumentAttachmentMetadata::getInstrumentNumber));
      return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(result);
    } else {
      return ResponseEntity.badRequest().body(null);
    }
  }

  private <T> List<T> deduplicate(List<T> attachments, Function<T, String> deduplicateBy) {
    return attachments.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(deduplicateBy))),
            ArrayList::new));
  }

  private <T> List<T> sort(List<T> attachments, Function<T, Integer> firstOrderCriteria,
      Function<T, Integer> secondOrderCriteria) {
    return attachments.stream()
        .sorted(Comparator.comparing(firstOrderCriteria).thenComparing(secondOrderCriteria))
        .collect(Collectors.toList());
  }
}
