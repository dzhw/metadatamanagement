/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService, StudyIdBuilderService) {
    var buildStudy = function(studyFromExcel,
      dataAcquisitionProjectId) {
      var study = {
        id: StudyIdBuilderService.buildStudyId(
          dataAcquisitionProjectId),
        title: {
          en: studyFromExcel['title.en'],
          de: studyFromExcel['title.de']
        },
        description: {
          en: studyFromExcel['description.en'],
          de: studyFromExcel['description.de']
        },
        institution: {
          en: studyFromExcel['institution.en'],
          de: studyFromExcel['institution.de']
        },
        surveySeries: {
          en: studyFromExcel['surveySeries.en'],
          de: studyFromExcel['surveySeries.de']
        },
        sponsor: {
          en: studyFromExcel['sponsor.en'],
          de: studyFromExcel['sponsor.de']
        },
        citationHint: {
          en: studyFromExcel['citationHint.en'],
          de: studyFromExcel['citationHint.de']
        },
        dataAvailability: {
          en: studyFromExcel['dataAvailability.en'],
          de: studyFromExcel['dataAvailability.de']
        },
        surveyDesign: {
          en: studyFromExcel['surveyDesign.en'],
          de: studyFromExcel['surveyDesign.de']
        },
        authors: studyFromExcel.authors,
        accessWays: CleanJSObjectService.
        removeWhiteSpace(studyFromExcel.accessWays),
        dataAcquisitionProjectId: dataAcquisitionProjectId
      };
      var cleanedStudyObject = CleanJSObjectService
        .removeEmptyJsonObjects(study);
      return new StudyResource(cleanedStudyObject);
    };
    return {
      buildStudy: buildStudy
    };
  });
