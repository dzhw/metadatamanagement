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
  
}
