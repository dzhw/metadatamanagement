package eu.dzhw.fdz.metadatamanagement.web.rest.mapper;

import eu.dzhw.fdz.metadatamanagement.domain.*;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.VariableDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Variable and its DTO VariableDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VariableMapper {

    @Mapping(source = "survey.id", target = "surveyId")
    @Mapping(source = "survey.title", target = "surveyTitle")
    VariableDTO variableToVariableDTO(Variable variable);

    @Mapping(source = "surveyId", target = "survey")
    Variable variableDTOToVariable(VariableDTO variableDTO);

    default Survey surveyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Survey survey = new Survey();
        survey.setId(id);
        return survey;
    }
}
