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
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.base.Charsets;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Access component for getting health information or registration or updates for dara and the doi.
 * 
 * @author Daniel Katzberg
 */
@Service
@Slf4j
public class DaraService {
  public static final String IS_ALiVE_ENDPOINT = "api/isAlive";
  public static final String REGISTRATION_ENDPOINT = "study/importXML";
      
  @Autowired
  private MetadataManagementProperties metadataManagementProperties;
  
  @Autowired
  private DataAcquisitionProjectRepository projectRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Value(value = "classpath:templates/dara/register.xml.tmpl")
  private Resource registerXml;
  
  @Autowired
  private Environment env;
  
  private RestTemplate restTemplate;
  
  //Resource Type
  private static final int RESOURCE_TYPE_DATASET = 2;
  
  //Availability Controlled
  private static final int AVAILABILITY_CONTROLLED_DELIVERY = 2;
  private static final int AVAILABILITY_CONTROLLED_NOT_AVAILABLE = 4;
  
  /**
   * Constructor for Dara Services. Set the Rest Template.
   */
  public DaraService() {
    this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    this.restTemplate.getMessageConverters()
      .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
  }
  
  /**
   * Check the dara health endpoint. 
   * @return Returns the status of the dara server.
   */
  public boolean isDaraHealthy() {
    
    final String daraHealthEndpoint = this.getApiEndpoint() + IS_ALiVE_ENDPOINT;
      
    ResponseEntity<String> result = 
        this.restTemplate.getForEntity(daraHealthEndpoint, String.class);
    
    return result.getStatusCode().equals(HttpStatus.OK);
  }
  
  /**
   * Registers or updates a dataset with a given doi to dara.
   * @param projectId The id of the Project.
   * @return The HttpStatus from Dara 
   *        Returns a false, if something gone wrong.
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public HttpStatus registerOrUpdateProjectToDara(String projectId) 
      throws IOException, TemplateException {
    
    //Load Project
    DataAcquisitionProject project = this.projectRepository.findOne(projectId);
        
    //Read register xml
    String registerXmlStr = IOUtils.toString(this.registerXml.getInputStream(), Charsets.UTF_8);
    
    //Fill template
    String filledTemplate = this.fillTemplate(registerXmlStr, 
            this.getTemplateConfiguration(), 
            this.getDataForTemplate(projectId, AVAILABILITY_CONTROLLED_DELIVERY));
    
    //Send Rest Call for Registration
    HttpStatus httpStatusFromDara = 
        this.postToDaraImportXml(filledTemplate, project.getHasBeenReleasedBefore());
    return httpStatusFromDara; 
  }
  
  /**
   * This is the kernel method for registration, update and unregister of a doi element. 
   * @param filledTemplate The filled and used template.
   * @param hasBeenReleasedBefore The parameter for the project, which is released before or not.
   * @return the HttpStatus from Dara.
   */
  private HttpStatus postToDaraImportXml(String filledTemplate, boolean hasBeenReleasedBefore) {
    
    log.debug("XML Element to Dara: " + filledTemplate);
    
    //Load Dara Information
    final String daraEndpoint = 
        this.metadataManagementProperties.getDara().getEndpoint() + REGISTRATION_ENDPOINT;
    final String daraUsername = this.metadataManagementProperties.getDara().getUsername();
    final String daraPassword = this.metadataManagementProperties.getDara().getPassword();
        
    //Build Header
    HttpHeaders headers = new HttpHeaders();    
    headers.add("Content-Type", "application/xml;charset=UTF-8");
    String auth = daraUsername + ":" + daraPassword;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName(Charsets.UTF_8.name())));
    headers.add("Authorization", "Basic " + new String(encodedAuth, Charsets.UTF_8));
    
    //Build
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(daraEndpoint)
        .queryParam("registration", Boolean.valueOf(!hasBeenReleasedBefore).toString());
        
    //Build Request
    HttpEntity<String> request = new HttpEntity<>(filledTemplate, headers);
    
    //Send Post
    //Info: result.getBody() has the registered DOI
    try {
      ResponseEntity<String> result = 
            this.restTemplate.postForEntity(builder.build().toUri(), request, String.class);      
      return result.getStatusCode();
    } catch (HttpClientErrorException httpClientError) {
      log.debug("HTTP Error durind Dara call", httpClientError);
      log.debug("Dara Response Body:\n" + httpClientError.getResponseBodyAsString());
      //Has been released is false? Something went wrong at the local save?
      //Catch the second try for registring 
      //Idempotent Method!
      if (httpClientError.getStatusCode().is4xxClientError()
          && httpClientError.getResponseBodyAsString()
            .equals("A resource with the given doiProposal exists in the system.")) {
        return HttpStatus.CREATED;
      } else {
        return httpClientError.getStatusCode();
      }
    }
  }

  /**
   * This method set a registered doi at dara to not available. 
   * 
   * @param projectId The id of the project.
   * @return The HttpStatus from Dara 
   *        Returns a false, if something gone wrong. 
   * @throws IOException the io exception for non readable xml file.
   * @throws TemplateException Exception for filling the template.
   */
  public HttpStatus unregisterProjectToDara(String projectId) 
      throws IOException, TemplateException {
    //Load Project
    DataAcquisitionProject project = this.projectRepository.findOne(projectId);
        
    //Read register xml
    String registerXmlStr = IOUtils.toString(this.registerXml.getInputStream(), Charsets.UTF_8);
    
    //Fill template
    String filledTemplate = this.fillTemplate(registerXmlStr, 
            this.getTemplateConfiguration(), 
            this.getDataForTemplate(projectId, AVAILABILITY_CONTROLLED_NOT_AVAILABLE));
    
    //Send Rest Call for Registration
    return this.postToDaraImportXml(filledTemplate, project.getHasBeenReleasedBefore()); 
  }
  
  /**
   * Load all needed Data for the XML Templates. The data is callable in freemarker by:
   *    study 
   *    releaseDate
   *    availabilityControlled
   *    resourceType
   * @param projectId The id of the project to find the study.
   * @param availabilityControlled The availability of the data.
   * @return Returns a Map of names and the depending objects. 
   *     If the key is 'study' so the study object is the value. 
   *     Study is the name for the object use in freemarker.
   */
  private Map<String, Object> getDataForTemplate(String projectId, 
      int availabilityControlled) {
    
    Map<String, Object> dataForTemplate = new HashMap<>();
    
    //Get Study Information
    Study study = this.studyRepository.findOneByDataAcquisitionProjectId(projectId);    
    dataForTemplate.put("study", study);
    
    //Add Date
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    dataForTemplate.put("releaseDate", formatter.format(LocalDate.now()));
    
    //Add Availability Controlled
    dataForTemplate.put("availabilityControlled", availabilityControlled);
    
    //Add Resource Type
    dataForTemplate.put("resourceType", RESOURCE_TYPE_DATASET);
    
    dataForTemplate.put("isDaraTest", !env.acceptsProfiles(Constants.SPRING_PROFILE_PROD));
    
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
  
  public RestTemplate getRestTemplate() {
    return this.restTemplate;
  }
}
