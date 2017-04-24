package eu.dzhw.fdz.metadatamanagement.projectmanagement.client;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * The client for the dara registration and unregistration.
 * @author Daniel Katzberg
 *
 */
@Component
public class DaraClient {
  
  @Autowired
  private DataAcquisitionProjectRepository projectRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Value(value = "classpath:templates/dara/register.xml")
  private Resource registerXml;
  
  //Resource Type
  private static final int RESOURCE_TYPE_DATASET = 2;
  
  //Availability Controlled
  private static final int AVAILABILITY_CONTROLLED_DELIVERY = 2;
  //private static final int AVAILABILITY_CONTROLLED_NOT_AVAILABLE = 4;
  
  /**
   * Registers a dataset with a given doi at dara.
   * @param projectId The id of the Project.
   * @param studyId The id of the study.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public void registerDoi(String projectId, String studyId) throws IOException, TemplateException {
    
    //Load Project
    DataAcquisitionProject project = this.projectRepository.findOne(projectId);
        
    //Read register xml
    String registerXmlStr = IOUtils.toString(this.registerXml.getInputStream(), Charsets.UTF_8);
    
    //Fill template
    String filledTemplate = this.fillTemplate(registerXmlStr, 
            this.getTemplateConfiguration(), 
            this.getDataForTemplate(studyId, AVAILABILITY_CONTROLLED_DELIVERY));
    
    System.out.println(filledTemplate);
    //TODO DKatzberg Send filled Template
    
    project.setHasBeenReleasedBefore(false);//TODO Change later to true, on success!!
  }
  
  public void unregisterDoi(String studyId) {
    //TODO DKatzberg
  }
  
  private Map<String, Object> getDataForTemplate(String studyId, int availabilityControlled) {
    
    Map<String, Object> dataForTemplate = new HashMap<>();
    
    //Get Study Information
    Study study = this.studyRepository.findOne(studyId);    
    dataForTemplate.put("study", study);
    
    //Add Date
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    dataForTemplate.put("releaseDate", formatter.format(LocalDate.now()));
    
    //Add Availability Controlled
    dataForTemplate.put("availabilityControlled", availabilityControlled);
    
    //Add Resource Type
    dataForTemplate.put("resourceType", RESOURCE_TYPE_DATASET);
    
    return dataForTemplate;
  }
  
  /**
   * @return a configratution object for the registration.
   */
  private Configuration getTemplateConfiguration() {
    // Configuration, based on Freemarker Version 2.3.23
    Configuration templateConfiguration = new Configuration(Configuration.VERSION_2_3_23);
    templateConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.toString());
    templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    templateConfiguration.setNumberFormat("0.######");
    
    return templateConfiguration;
  }
  
  /**
   * This method fills the xml templates.
   *
   * @param templateContent The content of a xml template.
   * @param templateConfiguration The configuration for freemarker.
   * @param dataForTemplateThe data for a xml template. 
   * @return The filled xml templates as byte array.
   * @throws IOException Handles IO Exception.
   * @throws TemplateException Handles template Exceptions.
   */
  private String fillTemplate(String templateContent,
      Configuration templateConfiguration, Map<String, Object> dataForTemplate) 
          throws IOException, TemplateException {
    // Read Template and escape elements
    Template texTemplate = new Template("xmlTemplate", templateContent, templateConfiguration);
    try (Writer stringWriter = new StringWriter()) {
      texTemplate.process(dataForTemplate, stringWriter);
      
      stringWriter.flush();
      return stringWriter.toString();      
    }
  }
}
