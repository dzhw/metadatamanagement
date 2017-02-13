package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import io.searchbox.annotations.JestId;

/**
 * Subset of publication attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class RelatedPublicationSubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "related-publication-management.error.related-publication.id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication.id.size")
  @Pattern(regexp = Patterns.NO_WHITESPACE,
      message = "related-publication-management.error.related-publication.id.pattern")
  private String id;
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication." + "doi.size")
  private String doi;
  @Size(max = StringLengths.MEDIUM,
      message = "related-publication-management.error.related-publication." + "title.size")
  @NotEmpty(
      message = "related-publication-management.error.related-publication." + "title.not-empty")
  private String title;
  @Size(max = StringLengths.LARGE,
      message = "related-publication-management.error.related-publication." + "authors.size")
  @NotEmpty(
      message = "related-publication-management.error.related-publication." + "authors.not-empty")
  private String authors;

  public RelatedPublicationSubDocument() {
    super();
  }

  @Override
  public String getId() {
    return this.id;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

}
