package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.Catgry;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.Citation;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.CodeBook;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.DataDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.FileDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.FileTxt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.LanguageEnum;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.StdyDscr;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.TextElement;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.TitlStmt;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddiCodebook.Var;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service for construction DDI codebook metadata for all veriables of a {@link DataPackage}.
 * @since Sep 2024
 * @author <a href="mailto:tmoeller@codematix.de">Theresa Möller</a>
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
@Slf4j
public class DataPackageDdiService {

  private final RestHighLevelClient client;

  private final Gson gson;

  /**
   * Exports all variables belonging to a given datapackage according to the DDI Codebook standard.
   * @param dataPackageId the ID of the study
   * @return DDI metadata as XML
   */
  public ResponseEntity<?> exportDdiVariablesAsXML(String dataPackageId) {
    try {
      CodeBook variableMetadata = this.getDdiVariablesMetadata(dataPackageId);
      XmlMapper mapper = new XmlMapper();
      ByteArrayResource resource = new ByteArrayResource(mapper.writeValueAsBytes(variableMetadata));
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=Variables_PID_MDM_Export.xml");
      return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
    } catch (IOException ex) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Collects variables metadata according to DDI Codebook standard.
   * @return the metadata
   */
  private CodeBook getDdiVariablesMetadata(String dataPackageId) throws JsonProcessingException {
    StdyDscr stdyDscr = this.getDdiStdyDscr();

    List<FileDscr> fileDscrList = new ArrayList<>();
    // for each dataset of the data package
    for (int i = 0; i < 3; i++) {
      //todo make a list of all added datasets and do add there variables later on
      fileDscrList.add(this.getDdiFileDsrc());
    }

    List<Var> varList = new ArrayList<>();
    // for each variable of the data package
    for (int j = 0; j < 10; j++) {
      varList.add(this.getDdiVar());
    }
    DataDscr dataDscr = new DataDscr(varList);
    CodeBook codeBook = new CodeBook(stdyDscr, fileDscrList, dataDscr);

    return codeBook;


//    SearchRequest surveyRequest = new SearchRequest();
//    SearchSourceBuilder builderSurveys = new SearchSourceBuilder();
//    builderSurveys.query(QueryBuilders.termsQuery("id", surveyId));
//    surveyRequest.source(builderSurveys);
//    surveyRequest.indices("surveys");
//
//    SearchRequest variablesRequest = new SearchRequest();
//    SearchSourceBuilder builderVariables = new SearchSourceBuilder();
//    builderVariables.query(QueryBuilders.termsQuery("surveyIds", surveyId))
//      .size(10000);
//    variablesRequest.source(builderVariables);
//    variablesRequest.indices("variables");
//    try {
//      SearchResponse surveyResponse =  client.search(surveyRequest, RequestOptions.DEFAULT);
//      List<SearchHit> surveyHits = Arrays.asList(surveyResponse.getHits().getHits());
//      if (surveyHits.size() == 0) {
//        throw new ElasticsearchException(String.format("Could not find survey with id '%s'", surveyId));
//      }
//      SurveySearchDocument surveyDoc = gson.fromJson(
//        surveyHits.get(0).getSourceAsString(), SurveySearchDocument.class);
//      // toDo: Decide on language usage
//      CodeBook.StdyDscr.Citation.TitlStmt titlStmt = new CodeBook.StdyDscr.Citation.TitlStmt(surveyDoc.getTitle().getEn());
//      CodeBook.StdyDscr.Citation citation = new CodeBook.StdyDscr.Citation(titlStmt);
//      CodeBook.StdyDscr study = new CodeBook.StdyDscr(citation);
//      SearchResponse variableResponse =  client.search(variablesRequest, RequestOptions.DEFAULT);
//      List<SearchHit> hits = Arrays.asList(variableResponse.getHits().getHits());
//      if (hits.size() > 0) {
//        log.info(String.format("Found %d variables for survey '%s'", hits.size(),surveyId));
//        List<CodeBook.DataDscr.Var> variableList = new ArrayList<>();
//        for (var variable : hits) {
//          VariableSearchDocument variableDoc = gson.fromJson(
//            variable.getSourceAsString(), VariableSearchDocument.class);
//          CodeBook.DataDscr.Var varMetadata = new CodeBook.DataDscr.Var(variableDoc.getName());
//          variableList.add(varMetadata);
//        }
//        CodeBook.DataDscr dataDscr = new CodeBook.DataDscr(variableList);
//        return new CodeBook(study, dataDscr);
//      } else {
//        log.info(String.format("No variables found for studyId '%s'", surveyId));
//      }
//    } catch (IOException e) {
//      throw new ElasticsearchIoException(e);
//    }
    //return null;
  }

  /**
   *
   * @return
   */
  private Var getDdiVar() {
    String name = "Variable ID";
    String files = "Dataset ID";
    List<TextElement> varLablList = new ArrayList<>();
    varLablList.add(new TextElement(LanguageEnum.de, "Variable Label in German"));
    varLablList.add(new TextElement(LanguageEnum.en, "Variable Label in English"));
    List<TextElement> qstnList = new ArrayList<>();
    // possibly empty
    qstnList.add(new TextElement(LanguageEnum.de, "Related Question label in German"));
    qstnList.add(new TextElement(LanguageEnum.en, "Related Question label in English"));
    List<Catgry> catgryList = new ArrayList<>();
    for (int k = 0; k < 5; k++) {
      // only for nominal/ordinal
      String catValu = "Value";
      List<TextElement> catLablList = new ArrayList<>();
      catLablList.add(new TextElement(LanguageEnum.de, "Value Label in German"));
      catLablList.add(new TextElement(LanguageEnum.en, "Value Label in English"));
      catgryList.add(new Catgry(catValu, catLablList));
    }
    return new Var(name, files, varLablList, qstnList, catgryList);
  }

  /**
   *
   * @return
   */
  private FileDscr getDdiFileDsrc() {
    String id = "Dataset ID";
    String fileName = "Dataset ID";
    TextElement fileCont = new TextElement(LanguageEnum.de, "Dataset Title in German");
    FileTxt fileTxt = new FileTxt(fileName, fileCont);
    return new FileDscr(id, fileTxt);
  }

  /**
   *
   * @return
   */
  private StdyDscr getDdiStdyDscr() {
    TextElement titl = new TextElement(LanguageEnum.de, "Data Package Title in German");
    TextElement parTitl = new TextElement(LanguageEnum.en, "Data Package Title in English");
    Citation citation = new Citation(new TitlStmt(titl, parTitl));
    return new StdyDscr(citation);
  }

}
