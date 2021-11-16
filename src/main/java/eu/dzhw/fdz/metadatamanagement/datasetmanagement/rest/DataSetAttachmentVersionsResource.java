package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetAttachmentVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of the {@link DataSetAttachmentMetadata} domain
 * object.
 * 
 * @author René Reitmann
 */
@RestController
@RequiredArgsConstructor
public class DataSetAttachmentVersionsResource {
  
  private final DataSetAttachmentVersionsService dataSetAttachmentVersionsService;
    
  /**
   * Get the previous 10 versions of the dataSet attachment metadata.
   * 
   * @param dataSetId The id of the dataSet
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous dataSet versions
   */
  @GetMapping("/api/data-sets/{dataSetId}/attachments/{filename:.+}/versions")
  public ResponseEntity<?> findPreviousdataSetAttachmentVersions(
      @PathVariable String dataSetId,
      @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<DataSetAttachmentMetadata> dataSetAttachmentVersions =
        dataSetAttachmentVersionsService.findPreviousDataSetAttachmentVersions(dataSetId,
            filename, limit, skip);
    
    if (dataSetAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(dataSetAttachmentVersions);
  }
}
