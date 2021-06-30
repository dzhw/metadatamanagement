package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * An access way of a {@link Variable} or a {@link DataSet} indicates how the data user will be able
 * to work with the data.
 */
public class AccessWays {

  public static final String DOWNLOAD_CUF = "download-cuf";
  public static final String DOWNLOAD_SUF = "download-suf";
  public static final String REMOTE_DESKTOP = "remote-desktop-suf";
  public static final String ONSITE_SUF = "onsite-suf";
  public static final String NOT_ACCESSIBLE = "not-accessible";

  public static final List<String> ALL = Collections.unmodifiableList(
      Arrays.asList(DOWNLOAD_CUF, DOWNLOAD_SUF, REMOTE_DESKTOP, ONSITE_SUF, NOT_ACCESSIBLE));

  /**
   * Get a string which can be used for presenting access ways to users.
   * @param accessWay an access way
   * @return the more human readable string
   */
  public static String displayAccessWay(String accessWay) {
    if (!ALL.contains(accessWay)) {
      throw new NotImplementedException(
          "AccessWay '" + accessWay + "' has not yet been implemented!");
    }
    switch (accessWay) {
      case NOT_ACCESSIBLE:
        return "Not accessible";
      case DOWNLOAD_CUF:
        return "CUF: Download";
      case DOWNLOAD_SUF:
        return "SUF: Download";
      case REMOTE_DESKTOP:
        return "SUF: Remote-Desktop";
      case ONSITE_SUF:
        return "SUF: On-Site";
      default:
        throw new NotImplementedException(
            "AccessWay '" + accessWay + "' has not yet been implemented!");
    }
  }

  /**
   * Create a freemarker method to be used in freemarker templates to display access ways.
   * 
   * @return The {@link TemplateMethodModelEx} which is a freemarker method.
   */
  public static TemplateMethodModelEx createDisplayAccessWayMethod() {
    return new TemplateMethodModelEx() {
      @SuppressWarnings("rawtypes")
      @Override
      public TemplateModel exec(List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
          throw new TemplateModelException("Wrong arguments");
        }
        return new SimpleScalar(displayAccessWay(((SimpleScalar) arguments.get(0)).getAsString()));
      }
    };
  }
}
