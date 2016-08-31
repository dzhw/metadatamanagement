/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService) {
    var parseErrors = [];
    var getStudies = function(study) {
      var studiesObjArray = [];
      parseErrors.length = 0;
      var studyObj = {
        id: study.id,
        title: {
          en: study['title.en'],
          de: study['title.de']
        },
        description: {
          en: study['description.en'],
          de: study['description.de']
        },
        institution: {
          en: study['institution.en'],
          de: study['institution.de']
        },
        surveySeries: {
          en: study['surveySeries.en'],
          de: study['surveySeries.de']
        },
        sponsor: {
          en: study['sponsor.en'],
          de: study['sponsor.de']
        },
        citationHint: {
          en: study['citationHint.en'],
          de: study['citationHint.de']
        },
        authors: study.authors,
        accessWays: CleanJSObjectService.
        removeWhiteSpace(study.accessWays),
        studyAcquisitionProjectId: study.studyAcquisitionProjectId,
        releases: CleanJSObjectService.
        removeWhiteSpace(study.releases),
        studyIds: CleanJSObjectService.
        removeWhiteSpace(study.studyIds),
        instrumentIds: CleanJSObjectService.
        removeWhiteSpace(study.instrumentIds),
        relatedPublicationIds: CleanJSObjectService.
        removeWhiteSpace(study.relatedPublicationIds)
      };
      CleanJSObjectService.removeEmptyJsonObjects(studyObj);
      studiesObjArray.push(new StudyResource(studyObj));
      return studiesObjArray;
    };
    return {
      getStudies: getStudies,
      getParseErrors: parseErrors
    };
  });
