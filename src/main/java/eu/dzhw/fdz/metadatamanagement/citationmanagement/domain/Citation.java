package eu.dzhw.fdz.metadatamanagement.citationmanagement.domain;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Citation. (For Variable Documentation, Latex. Imported from CITAVI)
 *
 * @author Daniel Katzberg
 *
 */
@Document(collection = "citations")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement"
    + ".citationmanagement.domain.builders")
public class Citation extends AbstractRdcDomainObject {

  @Id
  @NotEmpty(message = "{citation-management.error.citation.id.not-empty}")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "citation-management.error.citation.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.id.size")
  private String id;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.author.size")
  private String author;

  @Size(max = StringLengths.LARGE, message = "citation-management.error.citation.book-title.size")
  private String bookTitle;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.chapter.size")
  private String chapter;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.edition.size")
  private String edition;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.editor.size")
  private String editor;

  @Size(max = StringLengths.MEDIUM,
      message = "citation-management.error.citation.institution.size")
  private String institution;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.journal.size")
  private String journal;

  private Integer publicationYear;

  @Size(max = StringLengths.LARGE, message = "citation-management.error.citation.title.size")
  private String title;

  @Size(max = StringLengths.LARGE, message = "citation-management.error.citation.series.size")
  private String series;

  @Size(max = StringLengths.SMALL, message = "citation-management.error.citation.volume.size")
  private String volume;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.type.size")
  private String type;

  @Size(max = StringLengths.SMALL, message = "citation-management.error.citation.pages.size")
  private String pages;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.publisher.size")
  private String publisher;

  @Size(max = StringLengths.MEDIUM,
      message = "citation-management.error.citation.organization.size")
  private String organization;

  @Size(max = StringLengths.SMALL, message = "citation-management.error.citation.number.size")
  private String number;

  @Size(max = StringLengths.MEDIUM, message = "citation-management.error.citation.school.size")
  private String school;

  @Size(max = StringLengths.LARGE, message = "citation-management.error.citation.note.size")
  private String note;

  @Size(max = StringLengths.MEDIUM,
      message = "citation-management.error.citation.how-published.size")
  private String howPublished;

  @NotEmpty(message = "{citation-management.error.citation.citation-string.not-empty}")
  @Size(max = StringLengths.MEDIUM,
      message = "citation-management.error.citation.citation-string.size")
  private String citationString;

  @NotEmpty(message = "{citation-management.error.citation.source-reference.not-empty}")
  @Size(max = StringLengths.LARGE,
      message = "citation-management.error.citation.source-reference.size")
  private String sourceReference;

  /*
   * (non-Javadoc)
   *
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return this.id;
  }

  /*
   * (non-Javadoc)
   *
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("author", author)
      .add("bookTitle", bookTitle)
      .add("chapter", chapter)
      .add("edition", edition)
      .add("editor", editor)
      .add("institution", institution)
      .add("journal", journal)
      .add("publicationYear", publicationYear)
      .add("title", title)
      .add("series", series)
      .add("volume", volume)
      .add("type", type)
      .add("pages", pages)
      .add("publisher", publisher)
      .add("organization", organization)
      .add("number", number)
      .add("school", school)
      .add("note", note)
      .add("howPublished", howPublished)
      .add("citationString", citationString)
      .add("sourceReference", sourceReference)
      .toString();
  }

  /* GETTER / SETTER */
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getChapter() {
    return chapter;
  }

  public void setChapter(String chapter) {
    this.chapter = chapter;
  }

  public String getEdition() {
    return edition;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  public String getEditor() {
    return editor;
  }

  public void setEditor(String editor) {
    this.editor = editor;
  }

  public String getInstitution() {
    return institution;
  }

  public void setInstitution(String institution) {
    this.institution = institution;
  }

  public String getJournal() {
    return journal;
  }

  public void setJournal(String journal) {
    this.journal = journal;
  }

  public Integer getPublicationYear() {
    return publicationYear;
  }

  public void setPublicationYear(Integer publicationYear) {
    this.publicationYear = publicationYear;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPages() {
    return pages;
  }

  public void setPages(String pages) {
    this.pages = pages;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getHowPublished() {
    return howPublished;
  }

  public void setHowPublished(String howPublished) {
    this.howPublished = howPublished;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCitationString() {
    return citationString;
  }

  public void setCitationString(String citationString) {
    this.citationString = citationString;
  }

  public String getSourceReference() {
    return sourceReference;
  }

  public void setSourceReference(String sourceReference) {
    this.sourceReference = sourceReference;
  }
}
