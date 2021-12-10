package eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * A wrapper class that will be used when sending a PATCH request to the User API to update a
 * specific user's "field_welcome_dialog_deactivated" field.
 */
@Getter
public class UserWelcomeDialogDeactivatedPatchRequest {

  private final Data data;

  /**
   * Build the body of a welcome dialog deactivated patch request.
   *
   * @param id the id of the user being patched
   * @param welcomeDialogDeactivated the value that the user's welcome dialog
   *                                 deactivated will be set to.
   */
  public UserWelcomeDialogDeactivatedPatchRequest(
      final String id,
      final boolean welcomeDialogDeactivated
  ) {
    this.data = new Data(id, welcomeDialogDeactivated);
  }

  /**
   * A wrapper for the "data" field of a patch request to the User API server.
   */
  @Getter
  public static class Data {
    private final String type;
    private final String id;
    private final Attributes attributes;

    /**
     * Build the "data" JSON object of a patch request.
     *
     * @param id the id of the user being patched
     * @param welcomeDialogDeactivated the value that the user's welcome dialog
     *                                 deactivated will be set to.
     */
    public Data(
        final String id,
        final boolean welcomeDialogDeactivated
    ) {
      this.id = id;
      this.type = "user--user";
      this.attributes = new Attributes(welcomeDialogDeactivated);
    }

    /**
     * A wrapper for the "attribute" field of a patch request to the User API server.
     */
    @Getter
    public static class Attributes {
      @JsonProperty("field_welcome_dialog_deactivated")
      private final boolean welcomeDialogDeactivated;

      /**
       * Build the "attribute" JSON object of a patch request.
       *
       * @param welcomeDialogDeactivated the value that the user's welcome dialog
       *                                 deactivated will be set to.
       */
      public Attributes(final boolean welcomeDialogDeactivated) {
        this.welcomeDialogDeactivated = welcomeDialogDeactivated;
      }
    }
  }
}
