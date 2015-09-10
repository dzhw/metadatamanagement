package eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups;

/**
 * The ModifyValidationGroup collect all possible validation group for modify a object: edit and
 * create. The Modify group is a group sequence of edit and create.
 * 
 * @author Daniel Katzberg
 *
 */
public class ModifyValidationGroup {

  /**
   * The edit validation group will be validate annotation, if the user is at edit mode.
   */
  public interface Edit {
  }

  /**
   * The create validation group will be validate annotation, if the user is at create mode.
   */
  public interface Create {
  }

}
