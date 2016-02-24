package eu.dzhw.fdz.metadatamanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.BibliographicalReference;

/**
 * The 'complete' Projection of a bibliographical reference domain object. 'complete' means all
 * attributes will be displayed.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = BibliographicalReference.class)
public interface CompleteBibliographicalReferenceProjection
    extends AbstractRdcDomainObjectProjection {

  String getAuthor();

  String getBookTitle();

  String getChapter();

  String getEdition();

  String getEditor();

  String getInstitution();

  String getJournal();

  Integer getPublicationYear();

  String getTitle();

  String getSeries();

  String getVolume();

  String getType();

  String getPages();

  String getPublisher();

  String getOrganization();

  String getNumber();

  String getSchool();

  String getNote();

  String getHowPublished();

}
