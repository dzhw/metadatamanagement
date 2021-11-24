package eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Mock wrapper class which will be used by the MockServer to generate response bodies.
 */
public class User {
  private static final String USER_TEMPLATE = """
        {
          "id": "%s",
          "name": "%s",
          "mail": "%s",
          "langcode": "%s",
          "field_welcome_dialog_deactivated": %s,
          "roles": [%s]
        }""";

  private static final String ROLE_TEMPLATE = """
          {
          "drupal_internal__id": "%s"
          }""";


  @Getter
  private final String id;
  @Getter
  private final String name;
  @Getter
  private final String mail;
  private final String langcode;
  private final boolean welcomeDialogDeactivated;
  @Getter
  private final List<String> roles;

  /**
   * Build the user with all the required fields.
   *
   * @param id the id of the user
   * @param name the name of the user
   * @param mail the email of the user
   * @param langcode the language code the user selected
   * @param welcomeDialogDeactivated whether the welcome dialog should be shown
   * @param roles the name of the roles which the user has
   */
  public User(
      final String id,
      final String name,
      final String mail,
      final String langcode,
      final boolean welcomeDialogDeactivated,
      final String... roles
  ) {
    this.id = id;
    this.name = name;
    this.mail = mail;
    this.langcode = langcode;
    this.welcomeDialogDeactivated = welcomeDialogDeactivated;

    this.roles = Arrays.asList(roles);
  }

  @Override
  public String toString() {
    return String.format(
        USER_TEMPLATE,
        this.id,
        this.name,
        this.mail,
        this.langcode,
        this.welcomeDialogDeactivated,
        roles.stream()
            .map(role -> String.format(ROLE_TEMPLATE, role))
            .collect(Collectors.joining(","))
    );
  }
}
