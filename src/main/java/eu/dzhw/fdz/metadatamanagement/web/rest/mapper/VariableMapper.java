package eu.dzhw.fdz.metadatamanagement.web.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.VariableDto;

/**
 * Mapper for the entity Variable and its DTO VariableDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VariableMapper {

  @Mapping(source = "survey.id", target = "surveyId")
  @Mapping(source = "survey.title", target = "surveyTitle")
  VariableDto variableToVariableDto(Variable variable);

  @Mapping(source = "surveyId", target = "survey")
  Variable variableDtoToVariable(VariableDto variableDto);

  /**
   * @param id The id of a survey.
   * @return returns a Survey by a given id.
   */
  default Survey surveyFromId(Long id) {
    if (id == null) {
      return null;
    }
    Survey survey = new Survey();
    survey.setId(id);
    return survey;
  }
}
