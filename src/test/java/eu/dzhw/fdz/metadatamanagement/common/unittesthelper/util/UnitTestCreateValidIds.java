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

  public static String buildStudyId(String dataAcquisitionProjectId) {
    return "stu-" + dataAcquisitionProjectId + "!";
  }
  
  public static String buildDataSetId(String dataAcquisitionProjectId, int dataSetNumber) {
    return "dat-" + dataAcquisitionProjectId + "-ds" + dataSetNumber + "!";
  }
  
  public static String buildQuestionId(String dataAcquisitionProjectId, int instrumentNumber, String questionNumber) {
    return "que-" + dataAcquisitionProjectId + "-ins" + instrumentNumber + "-" + questionNumber + "!";
  }
  
  public static String buildInstrumentId(String dataAcquisitionProjectId, int instrumentNumber) {
    return "ins-" + dataAcquisitionProjectId + "-ins" + instrumentNumber + "!";
  }
  
}
