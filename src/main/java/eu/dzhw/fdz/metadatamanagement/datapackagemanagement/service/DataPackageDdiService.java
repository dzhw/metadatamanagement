package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Catgry;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Citation;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.CodeBook;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.DataDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.FileDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.FileTxt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.LanguageEnum;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.StdyDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.TextElement;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.TitlStmt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook.Var;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSubDocument;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for construction DDI codebook metadata for all veriables of a {@link DataPackage}.
 *
 * @author <a href="mailto:tmoeller@codematix.de">Theresa Möller</a>
 * @since Sep 2024
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class DataPackageDdiService {

  private final RestHighLevelClient client;

  private final Gson gson;

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
    } catch (JAXBException | JsonProcessingException ex) {
      log.error("Error generating XML: " + ex);
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Collects variables metadata according to DDI Codebook standard.
   *
   * @return the metadata
   */
  private CodeBook getDdiVariablesMetadata(String dataPackageId) throws JsonProcessingException {
    // get Data
    SearchRequest dataPackageRequest = new SearchRequest();
    SearchSourceBuilder builderSurveys = new SearchSourceBuilder();
    builderSurveys.query(QueryBuilders.termsQuery("id", dataPackageId));
    dataPackageRequest.source(builderSurveys);
    dataPackageRequest.indices("data_packages");

    try {
      SearchResponse surveyResponse = client.search(dataPackageRequest, RequestOptions.DEFAULT);
      List<SearchHit> hits = Arrays.asList(surveyResponse.getHits().getHits());
      if (hits.size() != 1) {
        throw new ElasticsearchException(
          String.format("Expected one data package for id '%s', but found %d", dataPackageId, hits.size()));
      }
      DataPackageSearchDocument dataPackageDoc = gson.fromJson(
          hits.get(0).getSourceAsString(), DataPackageSearchDocument.class);
      if (dataPackageDoc.getRelease() == null
          || (dataPackageDoc.getRelease() != null && dataPackageDoc.getRelease().getIsPreRelease())) {
        throw new ElasticsearchException(
          String.format("DDI codebook export failed. Can only export released data packages. "
            + "Data package with id '%s' is not released.", dataPackageDoc.getId())
        );
      }

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
      CodeBook codeBook = new CodeBook(stdyDscr, fileDscrList, dataDscr);

      return codeBook;

    } catch (IOException e) {
      log.error("An error occurred while querying the ES. ", e);
    }
    return null;
  }

  /**
   * Create the DDI var Element with data from the variable, and its related questions.
   *
   * @return the var element
   */
  private Var getDdiVar(VariableSubDocument variableDoc) {
    String name = variableDoc.getId();
    String files = variableDoc.getDataSetId();
    List<TextElement> varLablList = new ArrayList();
    varLablList.add(new TextElement(LanguageEnum.de, variableDoc.getLabel().getDe()));
    varLablList.add(new TextElement(LanguageEnum.en, variableDoc.getLabel().getEn()));
    List<TextElement> qstnList = new ArrayList();
    if (variableDoc.getRelatedQuestions() != null && variableDoc.getRelatedQuestions().size() > 0) {
      for (RelatedQuestionSubDocumentProjection relQuest : variableDoc.getRelatedQuestions()) {
        SearchRequest questionRequest = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("id", relQuest.getQuestionId()));
        questionRequest.source(builder);
        questionRequest.indices("questions");
        try {
          SearchResponse questionResponse = client.search(questionRequest, RequestOptions.DEFAULT);
          List<SearchHit> hits = Arrays.asList(questionResponse.getHits().getHits());
          if (hits.size() == 0) {
            throw new ElasticsearchException(
              String.format("Could not find question for id '%s'", relQuest.getQuestionId()));
          }
          for (SearchHit hit : hits) {
            QuestionSearchDocument relatedQuestion = gson.fromJson(
              hit.getSourceAsString(), QuestionSearchDocument.class);
            if (relatedQuestion.getQuestionText() != null && relatedQuestion.getQuestionText().getDe() != null) {
              qstnList.add(new TextElement(LanguageEnum.de, relatedQuestion.getQuestionText().getDe()));
            }
            if (relatedQuestion.getQuestionText() != null && relatedQuestion.getQuestionText().getEn() != null) {
              qstnList.add(new TextElement(LanguageEnum.en, relatedQuestion.getQuestionText().getEn()));
            }
          }
        } catch (IOException e) {
          log.error("An exception occurred querying the questions index. ", e);
        }
      }
    }

    SearchRequest variableRequest = new SearchRequest();
    SearchSourceBuilder builderVar = new SearchSourceBuilder();
    builderVar.query(QueryBuilders.termsQuery("id", variableDoc.getId()));
    variableRequest.source(builderVar);
    variableRequest.indices("variables");
    List<Catgry> catgryList = new ArrayList<>();
    List<TextElement> txtList = new ArrayList<>();
    try {
      SearchResponse variableResponse = client.search(variableRequest, RequestOptions.DEFAULT);
      List<SearchHit> hits = Arrays.asList(variableResponse.getHits().getHits());
      if (hits.size() == 0) {
        throw new ElasticsearchException(
          String.format("Could not find variable for id '%s'", variableDoc.getId()));
      }
      for (SearchHit hit : hits) {
        VariableSearchDocument varDoc = gson.fromJson(
          hit.getSourceAsString(), VariableSearchDocument.class);
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
          if (varDoc.getDistribution() != null && varDoc.getDistribution().getMissings() != null)
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
    } catch (IOException e) {
      log.error("An exception occurred querying the variables index. ", e);
    }
    return new Var(name, files, varLablList,
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
    String id = dataset.getId();
    String fileName = dataset.getId();
    // todo: Clarify why this is just in German and if it is the correct field
    TextElement fileCont = new TextElement(LanguageEnum.de, dataset.getDescription().getDe());
    FileTxt fileTxt = new FileTxt(fileName, fileCont);
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
    Citation citation = new Citation(new TitlStmt(titl, parTitl));
    return new StdyDscr(citation);
  }
}
