package eu.dzhw.fdz.metadatamanagement.web.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.SurveyDto;

/**
 * Mapper for the entity Survey and its DTO SurveyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SurveyMapper {

  SurveyDto surveyToSurveyDto(Survey survey);

  @Mapping(target = "variabless", ignore = true)
  Survey surveyDtoToSurvey(SurveyDto surveyDto);
}
