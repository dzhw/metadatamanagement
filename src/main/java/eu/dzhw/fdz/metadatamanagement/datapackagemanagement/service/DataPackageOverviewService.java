package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.service.AbstractReportService;
import eu.dzhw.fdz.metadatamanagement.common.service.MarkdownHelper;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskManagementService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.filemanagement.service.FileService;

/**
 * This service generated the data package overview. It fill the tex template using freemarker.
 */
@Service
public class DataPackageOverviewService extends AbstractReportService {
  private final DataPackageRepository dataPackageRepository;

  /**
   * Instantiate and autowire the service.
   */
  public DataPackageOverviewService(FileService fileService, TaskManagementService taskService,
      MarkdownHelper markdownHelper, DataPackageRepository dataPackageRepository) {
    super(fileService, taskService, markdownHelper);
    this.dataPackageRepository = dataPackageRepository;
  }

  @Override
  protected Map<String, Object> loadDataForTemplateFilling(String dataPackageId, String version) {
    // Create Map for the template
    Map<String, Object> dataForTemplate = new HashMap<>();

    dataForTemplate.put("dataPackage",
        this.dataPackageRepository.findById(dataPackageId).orElse(null));
    dataForTemplate.put("version", version);

    return dataForTemplate;
  }

  @Override
  protected List<String> getSimpleTemplateFiles() {
    return List.of("Main.tex");
  }

  @Override
  protected List<String> getComplexTemplateFiles() {
    return new ArrayList<>();
  }
}
