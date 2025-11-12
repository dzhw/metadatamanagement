package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
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
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for construction DDI codebook metadata for all variables of a {@link DataPackage}.
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

  /**
   * Exports all variables metadata belonging to a given data package according to the DDI Codebook standard.
   *
   * @param dataPackageId the ID of the study
   * @return DDI metadata as XML
   */
  public ResponseEntity<?> exportDdiVariablesAsXml(String dataPackageId) {
    try {
      CodeBook variableMetadata = this.getDdiVariablesMetadata(dataPackageId);
      JAXBContext context = JAXBContext.newInstance(CodeBook.class);
      Marshaller mar = context.createMarshaller();
      mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      mar.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
          "http://www.ddialliance.org/Specification/DDI-Codebook/2.5/XMLSchema/codebook.xsd");
      ByteArrayOutputStream res = new ByteArrayOutputStream();
      mar.marshal(variableMetadata, res);
      ByteArrayResource resource = new ByteArrayResource(res.toByteArray());
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=Variables_DDI_MDM_Export.xml");
      return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
    } catch (JAXBException ex) {
      log.error("Error generating XML: " + ex);
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Collects variables metadata according to DDI Codebook standard.
   *
   * @return the metadata
   */
  private CodeBook getDdiVariablesMetadata(String dataPackageId) {

    var request = SearchRequest.of(r -> r
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
      fileDscrList.add(this.getDdiFileDsrc(dataset));
    }

    List<Var> varList = new ArrayList<>();
    for (VariableSubDocument variable : dataPackageDoc.getVariables()) {
      varList.add(this.getDdiVar(variable));
    }
    DataDscr dataDscr = new DataDscr(varList);
    return new CodeBook(stdyDscr, fileDscrList, dataDscr);
  }

  /**
   * Create the DDI var Element with data from the variable, and its related questions.
   *
   * @return the var element
   */
  private Var getDdiVar(VariableSubDocument variableDoc) {
    List<TextElement> varLablList = new ArrayList();
    varLablList.add(new TextElement(LanguageEnum.de, variableDoc.getLabel().getDe()));
    varLablList.add(new TextElement(LanguageEnum.en, variableDoc.getLabel().getEn()));
    List<TextElement> qstnList = new ArrayList();
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
            .orElseThrow(() -> new RuntimeException("Missing search document for question with id " + relQuest.getQuestionId()));
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
        .orElseThrow(() -> new RuntimeException("Missing search document for variable with id " + variableDoc.getId()));
      if (varDoc.getAnnotations() != null && varDoc.getAnnotations().getDe() != null) {
        txtList.add(new TextElement(LanguageEnum.de, varDoc.getAnnotations().getDe()));
      }
      if (varDoc.getAnnotations() != null && varDoc.getAnnotations().getEn() != null) {
        txtList.add(new TextElement(LanguageEnum.en, varDoc.getAnnotations().getEn()));
      }
      if ((varDoc.getScaleLevel().equals(ScaleLevels.NOMINAL) || varDoc.getScaleLevel().equals(ScaleLevels.ORDINAL))
          && varDoc.getDistribution() != null
          && varDoc.getDistribution().getValidResponses() != null) {
        for (ValidResponse validResponse : varDoc.getDistribution().getValidResponses()) {
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
        if (varDoc.getDistribution() != null && varDoc.getDistribution().getMissings() != null) {
          for (Missing missing : varDoc.getDistribution().getMissings()) {
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
    }
    final var name = variableDoc.getName();
    final var location = new Location(variableDoc.getDataSetId().split("\\$")[0]);
    return new Var(name, location, varLablList,
        qstnList.size() > 0 ? qstnList : null,
        txtList.size() > 0 ? txtList : null,
        catgryList);
  }

  /**
   * Create the DDI element fileDscr with data from the datasets of the data package.
   *
   * @return the fileDscr element
   */
  private FileDscr getDdiFileDsrc(DataSetSubDocument dataset) {
    var id = dataset.getId().split("\\$")[0];
    final var fileTxt = new FileTxt(
        List.of(
          new TextElement(LanguageEnum.de, id),
          new TextElement(LanguageEnum.en, id)
        ),
        List.of(
          new TextElement(LanguageEnum.de, dataset.getDescription().getDe()),
          new TextElement(LanguageEnum.en, dataset.getDescription().getEn())
        )
    );
    return new FileDscr(id, fileTxt);
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
}
