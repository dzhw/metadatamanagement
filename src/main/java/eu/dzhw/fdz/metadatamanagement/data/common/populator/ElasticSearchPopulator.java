package eu.dzhw.fdz.metadatamanagement.data.common.populator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.data.repository.support.DefaultRepositoryInvokerFactory;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.repository.support.RepositoryInvoker;
import org.springframework.data.repository.support.RepositoryInvokerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.utils.PopulatorUtils;

/**
 * This is a json file to elasticsearch database populator. This populator reads the the supported
 * Locales {@code I18nConfiguration.SUPPORTED_LANGUAGES} and writes the depending json files into
 * the eleasticsearch database.
 * 
 * @author Daniel Katzberg
 */
public class ElasticSearchPopulator implements ApplicationListener<ContextRefreshedEvent> {

  /*
   * A slf4j logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchPopulator.class);

  /**
   * Reads the json resources files.
   */
  private Jackson2ResourceReader jackson2ResourceReader;

  /**
   * Singleton of the repository.
   */
  private Repositories repositories;

  /**
   * Util Class, which reads the resource data.
   */
  private PopulatorUtils populatorUtils;

  /**
   * The elasticsearch template for 'springified' access to elasticsearch.
   */
  private ElasticsearchTemplate elasticsearchTemplate;

  /**
   * A Map which remembers which mapping for which locale has already been created.
   */
  private HashMap<Locale, Set<Class<?>>> createdMappingsPerLocale = new HashMap<>();

  /**
   * This constructor needs all information for reading/loading the resource files from the
   * classpath: 1.) Application Context 2.) Populator Resources and 3.) A given template.
   * 
   * @param applicationContext The application context of the metadata management
   * @param populatorUtils For reading the resource files of the classpath.
   * @param elasticsearchTemplate a given elasticsearch template
   */
  @Autowired
  public ElasticSearchPopulator(ApplicationContext applicationContext,
      PopulatorUtils populatorUtils, ElasticsearchTemplate elasticsearchTemplate) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // add JSR 310 support to the json mapper
    objectMapper.findAndRegisterModules();
    this.jackson2ResourceReader = new Jackson2ResourceReader(objectMapper);
    this.repositories = new Repositories(applicationContext);
    this.populatorUtils = populatorUtils;
    this.elasticsearchTemplate = elasticsearchTemplate;
  }

  /**
   * This class populate objects from json files, depending on the Locale
   * {@code LocaleContextHolder.getLocale()}, into the elasticsearch database. The json files are
   * given by the resources .
   * 
   * @param locale the actual locale
   * @throws Exception The exception could thrown by the {@link Jackson2ResourceReader}
   */
  private void populateJsonFiles(Locale locale) throws Exception {
    // set temporary the local of the system
    LocaleContextHolder.setLocale(locale);
    // create the (currently just one) index per language
    elasticsearchTemplate.createIndex(AbstractDocument.class);

    // read the json data
    Resource[] resources = this.populatorUtils.loadJsonData(locale);
    for (Resource resource : resources) {
      Object object = this.jackson2ResourceReader.readFrom(resource, null);

      // check if the result object is a list
      boolean isList = Collection.class.isAssignableFrom(object.getClass());

      // list handling
      if (isList) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<Object> resultList = (List) object;
        for (Object result : resultList) {
          createMappingIfNotCreatedAlready(result.getClass());
        }
        this.persist(resultList);
        // single object handling
      } else {
        List<Object> resultList = new ArrayList<>();
        resultList.add(object);
        createMappingIfNotCreatedAlready(object.getClass());
        this.persist(resultList);
      }
    }
  }

  /**
   * Create a mapping in elasticsearch based on the annotations in the given class.
   * 
   * @param class1 A class annotated which spring data elasticsearch annotations.
   * @throws IOException IOException for problems of reading the resource files in the classpath.
   */
  private void createMappingIfNotCreatedAlready(Class<? extends Object> class1) throws IOException {
    Locale currentLocale = LocaleContextHolder.getLocale();
    Set<Class<?>> createdMappings = createdMappingsPerLocale.get(currentLocale);
    if (createdMappings == null) {
      createdMappings = new HashSet<>();
      createdMappingsPerLocale.put(currentLocale, createdMappings);
    }
    // create mapping if not created already
    if (!createdMappings.contains(class1)) {
      String type = AnnotationUtils.getAnnotation(class1, Document.class).type();
      String mapping = new String(FileCopyUtils.copyToByteArray(ResourceUtils.getFile(
          "classpath:data/" + currentLocale.getLanguage() + "/mappings/" + type + ".json")));
      elasticsearchTemplate.putMapping(class1, mapping);
      createdMappings.add(class1);
    }
  }

  /**
   * This method write the json objects into the elastic search database.
   * 
   * @param resultList A list of mapped Object (based on the json files).
   */
  private void persist(List<Object> resultList) {
    RepositoryInvokerFactory invokerFactory =
        new DefaultRepositoryInvokerFactory(this.repositories);

    for (Object result : resultList) {
      RepositoryInvoker invoker = invokerFactory.getInvokerFor(result.getClass());
      invoker.invokeSave(result);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.
   * springframework.context.ApplicationEvent)
   */
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {

    Locale defaultSystemLocale = LocaleContextHolder.getLocale();

    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      try {
        this.populateJsonFiles(locale);
      } catch (Exception e) {
        LOG.error("Unable to populate data for language " + locale.getLanguage(), e);
        throw new RuntimeException(e);
      }
    }

    LocaleContextHolder.setLocale(defaultSystemLocale);
  }
}
