package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.io.IOException;

import javax.validation.constraints.Max;

import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageListService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Ednpoint to deliver released dataPackages.
 *
 * @author tgehrke
 *
 */
@Slf4j
@Controller
@RequestMapping("/api")
@Validated
@Tag(name = "DataPackage List Resource",
    description = "Endpoint for retrieving released dataPackages.")
@RequiredArgsConstructor
public class DataPackagePublicListResource {

  private final DataPackageListService dataPackageListService;

  /**
   * Request a pageble list of released dataPackages.
   *
   * @param page the page. default 0
   * @param size the size of a page. default 5
   * @return the page object. containing the list of dataPackages as content and metadata regarding
   *         the paging.
   */
  @GetMapping(value = "/data-packages")
  @Operation(summary = "Get the paged list of currently released dataPackages.")
  @ResponseBody
  public ResponseEntity<Page<DataPackageSearchDocument>> listDataPackages(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") @Max(20) int size) {
    try {
      Page<DataPackageSearchDocument> loadDataPackages =
          dataPackageListService.loadDataPackages(page, size);
      return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(loadDataPackages);
    } catch (IOException e) {
      log.warn("requesting the list of dataPackages failed", e);
      return ResponseEntity.badRequest().body(null);
    }
  }
}
