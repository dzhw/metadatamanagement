/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util;

/**
 * Helper Class for JUnits to produce valid ids!
 * 
 * @author Daniel Katzberg
 *
 */
public class UnitTestCreateValidIds {

  public static String buildDataPackageId(String dataAcquisitionProjectId) {
    return "stu-" + dataAcquisitionProjectId + "$";
  }
  
  public static String buildAnalysisPackageId(String dataAcquisitionProjectId) {
    return "ana-" + dataAcquisitionProjectId + "$";
  }
  
  public static String buildDataSetId(String dataAcquisitionProjectId, int dataSetNumber) {
    return "dat-" + dataAcquisitionProjectId + "-ds" + dataSetNumber + "$";
  }
  
  public static String buildQuestionId(String dataAcquisitionProjectId, int instrumentNumber, String questionNumber) {
    return "que-" + dataAcquisitionProjectId + "-ins" + instrumentNumber + "-" + questionNumber + "$";
  }
  
  public static String buildInstrumentId(String dataAcquisitionProjectId, int instrumentNumber) {
    return "ins-" + dataAcquisitionProjectId + "-ins" + instrumentNumber + "$";
  }
  
  public static String buildSurveyId(String dataAcquisitionProjectId, int surveyNumber) {
    return "sur-" + dataAcquisitionProjectId + "-sy" + surveyNumber + "$";
  }
  
  public static String buildVariableId(String dataAcquisitionProjectId, int dataSetNumber, String variableName) {
    return "var-" + dataAcquisitionProjectId + "-ds" + dataSetNumber + "-" + variableName + "$";
  }
  
  public static String buildRelatedPublicationId(String id) {
    return "pub-" + id + "$";
  }
}
