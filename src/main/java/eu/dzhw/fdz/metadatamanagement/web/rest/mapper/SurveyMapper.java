package eu.dzhw.fdz.metadatamanagement.web.rest.mapper;

import eu.dzhw.fdz.metadatamanagement.domain.*;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.SurveyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Survey and its DTO SurveyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SurveyMapper {

    SurveyDTO surveyToSurveyDTO(Survey survey);

    @Mapping(target = "variabless", ignore = true)
    Survey surveyDTOToSurvey(SurveyDTO surveyDTO);
}
