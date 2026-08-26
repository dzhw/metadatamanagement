package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Catgry;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Citation;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.CodeBook;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.DataDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.FileDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.FileTxt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.IdNo;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.LanguageEnum;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Location;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.StdyDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.TextElement;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.TitlStmt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Var;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Distribution;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for construction DDI codebook metadata for all variables of a
 * {@link DataPackage}.
 *
 * @author <a href="mailto:tmoeller@codematix.de">Theresa Möller</a>
 * @since Sep 2024
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class DataPackageDdiService {

  private final ElasticsearchClient client;

  @Autowired
  private final DataAcquisitionProjectRepository acquisitionProjectRepository;

  @Autowired
  private final DataPackageRepository dataPackageRepository;

  @Autowired
  private final DataSetRepository dataSetRepository;

  @Autowired
  private final QuestionRepository questionRepository;

  @Autowired
  private final VariableRepository variableRepository;

  @Autowired
  private final DoiBuilder doiBuilder;

  @Autowired
  private final DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  public Map<String, byte[]> buildXMLForAllDataPackages() {
    List<DataPackage> dataPackages = new LinkedList<>();

    List<DataAcquisitionProject> acqProjects = acquisitionProjectRepository
        .findByShadowFalseAndConfigurationRequirementsIsDataPackagesRequiredTrue();
    for (DataAcquisitionProject dataAcquisitionProject : acqProjects) {
      dataPackages.addAll(dataPackageRepository.findByDataAcquisitionProjectId(dataAcquisitionProject.getMasterId()));
    }

    Map<String, byte[]> xmlForDataPackage = new HashMap<>();

    for (DataPackage dataPackage : dataPackages) {
      try {
        byte[] xml = buildDdiXml(dataPackage.getId(), true);
        xmlForDataPackage.put(dataPackage.getId(), xml);
      } catch (JAXBException e) {
        log.error("Can't generate DDI XML for data package " + dataPackage.getId());
        log.debug(e.getMessage());
        continue;
      } catch (RuntimeException e) {
        log.error("Can't generate DDI XML for data package " + dataPackage.getId());
        log.debug(e.getMessage());
        continue;
      }
    }

    return xmlForDataPackage;
  } 

  /**
   * Builds the DDI Codebook XML for all variables of the given data package.
   *
   * @param dataPackageId the ID of the data package
   * @return XML bytes
   * @throws JAXBException if marshalling fails
   */
  public byte[] buildDdiXml(String dataPackageId, boolean useMongoDB) throws JAXBException {
    CodeBook variableMetadata;
    if (useMongoDB) {
      variableMetadata = this.getDdiVariablesMetadatafromMongo(dataPackageId);
    } else {
      variableMetadata = this.getDdiVariablesMetadata(dataPackageId);  
    }
    
    JAXBContext context = JAXBContext.newInstance(CodeBook.class);
    Marshaller mar = context.createMarshaller();
    mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    mar.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
        "http://www.ddialliance.org/Specification/DDI-Codebook/2.5/XMLSchema/codebook.xsd");
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    mar.marshal(variableMetadata, res);
    return res.toByteArray();
  }

  // Elastic extraction

  /**
   * Collects variables metadata according to DDI Codebook standard.
   *
   * @return the metadata
   */
  @SuppressWarnings("unused")
  public CodeBook getDdiVariablesMetadata(String dataPackageId) {

    SearchRequest request = SearchRequest.of(r -> r
        .index(ElasticsearchType.data_packages.name())
        .query(q -> q
            .term(t -> t
                .field("id")
                .value(dataPackageId))));

    SearchResponse<DataPackageSearchDocument> response;
    try {
      response = this.client.search(request, DataPackageSearchDocument.class);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }

    final var hits = response.hits().hits();
    if (hits.size() != 1) {
      throw new RuntimeException(String.format(
          "Expected one data package for id '%s', but found %d", dataPackageId, hits.size()));
    }
    var dataPackageDoc = Optional.ofNullable(hits.get(0).source())
        .orElseThrow(() -> new RuntimeException("Missing search document for data package with id " + dataPackageId));
    Optional.of(dataPackageDoc)
        .map(DataPackageSearchDocument::getRelease)
        .map(release -> release.getIsPreRelease() ? null : release)
        .orElseThrow(() -> new RuntimeException("Missing release infos or latest release is " +
            "a pre-release for data package with id " + dataPackageId));

    StdyDscr stdyDscr = this.getDdiStdyDscr(dataPackageDoc);
    List<FileDscr> fileDscrList = new ArrayList<>();
    for (DataSetSubDocument dataset : dataPackageDoc.getDataSets()) {
      fileDscrList.add(this.getDdiFileDsrc(dataset.getId(), dataset.getDescription()));
    }

    List<Var> varList = new ArrayList<>();
    for (VariableSubDocument variable : dataPackageDoc.getVariables()) {
      varList.add(this.getDdiVar(variable));
    }
    DataDscr dataDscr = new DataDscr(varList);
    return new CodeBook(stdyDscr, fileDscrList, dataDscr);
  }

  /**
   * Create the DDI var Element with data from the variable, and its related
   * questions.
   *
   * @return the var element
   */
  private Var getDdiVar(VariableSubDocument variableDoc) {
    List<TextElement> varLablList = new ArrayList<>();
    varLablList.add(new TextElement(LanguageEnum.de, variableDoc.getLabel().getDe()));
    varLablList.add(new TextElement(LanguageEnum.en, variableDoc.getLabel().getEn()));
    List<TextElement> qstnList = new ArrayList<>();
    if (variableDoc.getRelatedQuestions() != null && variableDoc.getRelatedQuestions().size() > 0) {
      for (var relQuest : variableDoc.getRelatedQuestions()) {
        final var request = SearchRequest.of(r -> r
            .index(ElasticsearchType.questions.name())
            .query(q -> q
                .term(t -> t
                    .field("id")
                    .value(relQuest.getQuestionId()))));
        SearchResponse<QuestionSearchDocument> response;
        try {
          response = this.client.search(request, QuestionSearchDocument.class);
        } catch (IOException e) {
          throw new ElasticsearchIoException(e);
        }
        final var hits = response.hits().hits();
        if (hits.isEmpty()) {
          throw new RuntimeException(String.format("Could not find question for id '%s'", relQuest.getQuestionId()));
        }
        for (var hit : hits) {
          var relatedQuestion = Optional.ofNullable(hit.source())
              .orElseThrow(() -> new RuntimeException(
                  "Missing search document for question with id " + relQuest.getQuestionId()));
          if (relatedQuestion.getQuestionText() != null && relatedQuestion.getQuestionText().getDe() != null) {
            qstnList.add(new TextElement(LanguageEnum.de, relatedQuestion.getQuestionText().getDe()));
          }
          if (relatedQuestion.getQuestionText() != null && relatedQuestion.getQuestionText().getEn() != null) {
            qstnList.add(new TextElement(LanguageEnum.en, relatedQuestion.getQuestionText().getEn()));
          }
        }
      }
    }

    final var request = SearchRequest.of(r -> r
        .index(ElasticsearchType.variables.name())
        .query(q -> q
            .term(t -> t
                .field("id")
                .value(variableDoc.getId()))));
    List<Catgry> catgryList = new ArrayList<>();
    List<TextElement> txtList = new ArrayList<>();
    SearchResponse<VariableSearchDocument> response;
    try {
      response = this.client.search(request, VariableSearchDocument.class);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }

    final var hits = response.hits().hits();
    if (hits.isEmpty()) {
      throw new RuntimeException(String.format("Could not find variable for id '%s'", variableDoc.getId()));
    }
    for (var hit : hits) {
      final var varDoc = hit.source();
      Optional.ofNullable(varDoc)
          .orElseThrow(
              () -> new RuntimeException("Missing search document for variable with id " + variableDoc.getId()));
      if (varDoc.getAnnotations() != null && varDoc.getAnnotations().getDe() != null) {
        txtList.add(new TextElement(LanguageEnum.de, varDoc.getAnnotations().getDe()));
      }
      if (varDoc.getAnnotations() != null && varDoc.getAnnotations().getEn() != null) {
        txtList.add(new TextElement(LanguageEnum.en, varDoc.getAnnotations().getEn()));
      }
      if ((varDoc.getScaleLevel().equals(ScaleLevels.NOMINAL) || varDoc.getScaleLevel().equals(ScaleLevels.ORDINAL))
          && varDoc.getDistribution() != null
          && varDoc.getDistribution().getValidResponses() != null) {

        extractCategoryList(catgryList, varDoc.getDistribution());
      }
    }
    final var name = variableDoc.getName();
    final var location = new Location(variableDoc.getDataSetId().split("\\$")[0]);
    return new Var(name, location, varLablList,
        qstnList.size() > 0 ? qstnList : null,
        txtList.size() > 0 ? txtList : null,
        catgryList);
  }

  /**
   * Creates the DDI element stdyDscr with data from the data package.
   *
   * @return the stdyDscr element
   */
  private StdyDscr getDdiStdyDscr(DataPackageSearchDocument doc) {
    TextElement titl = new TextElement(LanguageEnum.de, doc.getTitle().getDe());
    TextElement parTitl = new TextElement(LanguageEnum.en, doc.getTitle().getEn());
    IdNo idNo = null;
    if (doc.getDoi() != null && !doc.getDoi().isBlank()) {
      idNo = new IdNo("DOI", doc.getRelease().getVersion(), doc.getDoi());
    }
    Citation citation = new Citation(new TitlStmt(titl, parTitl, idNo));
    return new StdyDscr(citation);
  }

  // MongoDB related functions

  private CodeBook getDdiVariablesMetadatafromMongo(String dataPackageId) {
    DataPackage dataPackage = dataPackageRepository.findById(dataPackageId)
        .orElseThrow(() -> new RuntimeException(
            "Missing data package with id " + dataPackageId));

    Release release = getRelease(dataPackage);
    if (release == null || Boolean.TRUE.equals(release.getIsPreRelease())) {
      throw new RuntimeException("Missing release infos or latest release is a pre-release for "
          + "data package with id " + dataPackageId);
    }

    StdyDscr stdyDscr = this.getDdiStdyDscr(dataPackage);

    List<FileDscr> fileDscrList = new ArrayList<>();
    for (DataSet dataset : dataSetRepository.findByDataPackageId(dataPackageId)) {
      fileDscrList.add(this.getDdiFileDsrc(dataset.getId(), dataset.getDescription()));
    }

    List<Var> varList = new ArrayList<>();
    for (Variable variable : variableRepository.findByDataPackageId(dataPackageId)) {
      varList.add(this.getDdiVar(variable));
    }

    DataDscr dataDscr = new DataDscr(varList);
    return new CodeBook(stdyDscr, fileDscrList, dataDscr);
  }

  private Var getDdiVar(Variable variableDoc) {
    List<TextElement> varLablList = new ArrayList<>();
    varLablList.add(new TextElement(LanguageEnum.de, variableDoc.getLabel().getDe()));
    varLablList.add(new TextElement(LanguageEnum.en, variableDoc.getLabel().getEn()));

    List<TextElement> qstnList = new ArrayList<>();
    if (variableDoc.getRelatedQuestions() != null && !variableDoc.getRelatedQuestions().isEmpty()) {
      for (var relQuest : variableDoc.getRelatedQuestions()) {
        Question relatedQuestion = questionRepository.findById(relQuest.getQuestionId())
            .orElseThrow(() -> new RuntimeException(
                "Could not find question for id '" + relQuest.getQuestionId() + "'"));
        if (relatedQuestion.getQuestionText() != null && relatedQuestion.getQuestionText().getDe() != null) {
          qstnList.add(new TextElement(LanguageEnum.de, relatedQuestion.getQuestionText().getDe()));
        }
        if (relatedQuestion.getQuestionText() != null && relatedQuestion.getQuestionText().getEn() != null) {
          qstnList.add(new TextElement(LanguageEnum.en, relatedQuestion.getQuestionText().getEn()));
        }
      }
    }

    List<Catgry> catgryList = new ArrayList<>();
    List<TextElement> txtList = new ArrayList<>();

    if (variableDoc.getAnnotations() != null && variableDoc.getAnnotations().getDe() != null) {
      txtList.add(new TextElement(LanguageEnum.de, variableDoc.getAnnotations().getDe()));
    }
    if (variableDoc.getAnnotations() != null && variableDoc.getAnnotations().getEn() != null) {
      txtList.add(new TextElement(LanguageEnum.en, variableDoc.getAnnotations().getEn()));
    }

    if ((variableDoc.getScaleLevel().equals(ScaleLevels.NOMINAL)
        || variableDoc.getScaleLevel().equals(ScaleLevels.ORDINAL))
        && variableDoc.getDistribution() != null
        && variableDoc.getDistribution().getValidResponses() != null) {

        extractCategoryList(catgryList, variableDoc.getDistribution());
    }

    final var name = variableDoc.getName();
    final var location = new Location(variableDoc.getDataSetId().split("\\$")[0]);

    return new Var(name, location, varLablList,
        qstnList.size() > 0 ? qstnList : null,
        txtList.size() > 0 ? txtList : null,
        catgryList);
  }

  private StdyDscr getDdiStdyDscr(DataPackage doc) {
    TextElement titl = new TextElement(LanguageEnum.de, doc.getTitle().getDe());
    TextElement parTitl = new TextElement(LanguageEnum.en, doc.getTitle().getEn());
    IdNo idNo = null;

    String doi = getDoi(doc);
    Release release = getRelease(doc);
    if (doi != null && !doi.isBlank()) {
      idNo = new IdNo("DOI", release.getVersion(), doi);
    }
    Citation citation = new Citation(new TitlStmt(titl, parTitl, idNo));
    return new StdyDscr(citation);
  }

  private Release getRelease(DataPackage dataPackage) {
    DataAcquisitionProject project = getDataAcquisitionProject(dataPackage);
    Release release = project.getRelease();
    if (release == null) {
      release = dataAcquisitionProjectVersionsService.findLastRelease(project.getId());
    }
    return release;
  }

  private String getDoi(DataPackage dataPackage) {
    DataAcquisitionProject project = getDataAcquisitionProject(dataPackage);
    return doiBuilder.buildDataOrAnalysisPackageDoiForDataCite(project.getId(),
        getRelease(dataPackage));
  }

  private DataAcquisitionProject getDataAcquisitionProject(DataPackage dataPackage) {
    return acquisitionProjectRepository.findById(dataPackage.getDataAcquisitionProjectId())
        .orElseThrow(() -> new RuntimeException("Missing data acquisition project with id "
            + dataPackage.getDataAcquisitionProjectId()));
  }

  // shared functions

  private void extractCategoryList(List<Catgry> catgryList, final Distribution distribution) {
    for (ValidResponse validResponse : distribution.getValidResponses()) {
      String catValu = validResponse.getValue();
      List<TextElement> catLablList = new ArrayList<>();
      if (validResponse.getLabel() != null && validResponse.getLabel().getDe() != null) {
        catLablList.add(new TextElement(LanguageEnum.de, validResponse.getLabel().getDe()));
      }
      if (validResponse.getLabel() != null && validResponse.getLabel().getEn() != null) {
        catLablList.add(new TextElement(LanguageEnum.en, validResponse.getLabel().getEn()));
      }
      catgryList.add(new Catgry(catValu, catLablList));
    }
    // missing values
    if (distribution != null && distribution.getMissings() != null) {
      for (Missing missing : distribution.getMissings()) {
        String catValu = missing.getCode();
        List<TextElement> catLablList = new ArrayList<>();
        if (missing.getLabel() != null && missing.getLabel().getDe() != null) {
          catLablList.add(new TextElement(LanguageEnum.de, missing.getLabel().getDe()));
        }
        if (missing.getLabel() != null && missing.getLabel().getEn() != null) {
          catLablList.add(new TextElement(LanguageEnum.en, missing.getLabel().getEn()));
        }
        catgryList.add(new Catgry(catValu, catLablList));
      }
    }
  }

  /**
   * Create the DDI element fileDscr with data from the datasets of the data
   * package.
   *
   * @return the fileDscr element
   */
  private FileDscr getDdiFileDsrc(String id, I18nString description) {
      var strippedId = id.split("\\$")[0];
      final var fileTxt = new FileTxt(
          List.of(
              new TextElement(LanguageEnum.de, strippedId),
              new TextElement(LanguageEnum.en, strippedId)),
          List.of(
              new TextElement(LanguageEnum.de, description.getDe()),
              new TextElement(LanguageEnum.en, description.getEn())));
      return new FileDscr(strippedId, fileTxt);
  }
}
