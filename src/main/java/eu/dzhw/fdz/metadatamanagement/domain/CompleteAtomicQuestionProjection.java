package eu.dzhw.fdz.metadatamanagement.domain;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.AtomicQuestionType;
import eu.dzhw.fdz.metadatamanagement.domain.projections.AbstractRdcDomainObjectProjection;

/**
 * Projection used to expose all attributes (including ids and versions), including sub resources.
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author Daniel Katzberg
 *
 */
@Projection(name = "complete", types = AtomicQuestion.class)
public interface CompleteAtomicQuestionProjection extends AbstractRdcDomainObjectProjection {

  AtomicQuestionType getType();

  String getName();

  String getCompositeQuestionName();

  I18nString getFootnote();

  I18nString getQuestionText();

  I18nString getInstruction();

  I18nString getIntroduction();

  I18nString getSectionHeader();

  String getDataAcquisitionProjectId();

  String getQuestionnaireId();

  String getVariableId();
}
