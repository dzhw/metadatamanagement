package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Charsets;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Access component for getting health information or registration or updates for dara and the doi.
 * 
 * @author Daniel Katzberg
 */
@Service
public class DaraService {
  
  private static final String IS_ALiVE_ENDPOINT = "api/isAlive";
  private static final String REGISTRATION_ENDPOINT = "study/importXML";
      
  @Autowired
  private MetadataManagementProperties metadataManagementProperties;
  
  @Autowired
  private DataAcquisitionProjectRepository projectRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Value(value = "classpath:templates/dara/register.xml.tmpl")
  private Resource registerXml;
  
  //Resource Type
  private static final int RESOURCE_TYPE_DATASET = 2;
  
  //Availability Controlled
  private static final int AVAILABILITY_CONTROLLED_DELIVERY = 2;
  //private static final int AVAILABILITY_CONTROLLED_NOT_AVAILABLE = 4;
  
  /**
   * Check the dara health endpoint. 
   * @return Returns the status of the dara server.
   */
  public boolean isDaraHealth() {
    
    final String uri = 
        this.metadataManagementProperties.getDara().getEndpoint() + IS_ALiVE_ENDPOINT;
    
    ResponseEntity<String> result = new RestTemplate().getForEntity(uri, String.class);
    
    return result.getStatusCode().equals(HttpStatus.OK);
  }
  
  /**
   * Registers a dataset with a given doi at dara.
   * @param projectId The id of the Project.
   * @param studyId The id of the study.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public boolean registerDoi(String projectId, String studyId) 
      throws IOException, TemplateException {
    
    //Load Project
    DataAcquisitionProject project = this.projectRepository.findOne(projectId);
        
    //Read register xml
    String registerXmlStr = IOUtils.toString(this.registerXml.getInputStream(), Charsets.UTF_8);
    
    //Fill template
    String filledTemplate = this.fillTemplate(registerXmlStr, 
            this.getTemplateConfiguration(), 
            this.getDataForTemplate(studyId, AVAILABILITY_CONTROLLED_DELIVERY));
    
    //Send Rest Call for Registration
    boolean isRegistered = this.sendRegistration(filledTemplate, project.isHasBeenReleasedBefore());
    project.setHasBeenReleasedBefore(isRegistered);
    
    return isRegistered; 
  }
  
  private boolean sendRegistration(String filledTemplate, boolean hasBeenReleasedBefore) {
    
    //Load Dara Information
    final String daraEndpoint = 
        this.metadataManagementProperties.getDara().getEndpoint() + REGISTRATION_ENDPOINT;
    final String daraUsername = this.metadataManagementProperties.getDara().getUsername();
    final String daraPassword = this.metadataManagementProperties.getDara().getPassword();
    
    //Build Parameter
    Map<String, String> uriVariables = new HashMap<>();
    uriVariables.put("registration", Boolean.toString(!hasBeenReleasedBefore));
    
    //Build Header
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/xml;charset=UTF-8");
    String auth = daraUsername + ":" + daraPassword;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(Charsets.UTF_8.name())));
    String authHeader = "Basic " + new String(encodedAuth, Charsets.UTF_8);
    headers.add("Authorization", authHeader);
    
    //Build Request
    HttpEntity<String> request = new HttpEntity<>(filledTemplate, headers);
    
    //Send Post
    ResponseEntity<String> result = 
        new RestTemplate().postForEntity(daraEndpoint, request, String.class, uriVariables);
    
    return result.getStatusCode().equals(HttpStatus.CREATED) 
        || result.getStatusCode().equals(HttpStatus.OK);
  }

  public void unregisterDoi(String studyId) {
    //TODO DKatzberg
  }
  
  /**
   * Load all needed Data for the XML Templates. The data is callable in freemarker by:
   *    study 
   *    releaseDate
   *    availabilityControlled
   *    resourceType
   * @param studyId The id of the study.
   * @param availabilityControlled The availability of the data.
   * @return Returns a Map of names and the depending objects. 
   *     If the key is 'study' so the study object is the value. 
   *     Study is the name for the object use in freemarker.
   */
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
  
  /**
   * Returns dara api endpont.
   * @return the api endpoint given by the configuration.
   */
  public String getApiEndpoint() {
    return this.metadataManagementProperties.getDara().getEndpoint();
  }
}
