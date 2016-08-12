package eu.dzhw.fdz.metadatamanagement.citationmanagement.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
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
  @NotEmpty(message = "{error.citation.id.notEmpty}")
  private String id;

  private String author;

  private String bookTitle;

  private String chapter;

  private String edition;

  private String editor;

  private String institution;

  private String journal;

  private Integer publicationYear;

  private String title;

  private String series;

  private String volume;

  private String type;

  private String pages;

  private String publisher;

  private String organization;

  private String number;

  private String school;

  private String note;

  private String howPublished;
  
  private String citationString;
  
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
