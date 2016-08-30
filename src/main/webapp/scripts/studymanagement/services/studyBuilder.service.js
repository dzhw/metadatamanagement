/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService) {
    var getStudies = function(studies, projectId) {
      var studiesObjArray = [];
      for (var i = 0; i < studies.length; i++) {
        var data = studies[i];
        var studyObj = {
          id: data.id,
          title: {
            en: data['title.en'],
            de: data['title.de']
          },
          description: {
            en: data['description.en'],
            de: data['description.de']
          },
          institution: {
            en: data['institution.en'],
            de: data['institution.de']
          },
          surveySeries: {
            en: data['surveySeries.en'],
            de: data['surveySeries.de']
          },
          sponsor: {
            en: data['sponsor.en'],
            de: data['sponsor.de']
          },
          citationHint: {
            en: data['citationHint.en'],
            de: data['citationHint.de']
          },
          authors: data.authors,
          accessWays: CleanJSObjectService.
          removeWhiteSpace(data.accessWays),
          dataAcquisitionProjectId: projectId,
          releases: CleanJSObjectService.
          removeWhiteSpace(data.releases),
          studyIds: CleanJSObjectService.
          removeWhiteSpace(data.studyIds),
          instrumentIds: CleanJSObjectService.
          removeWhiteSpace(data.instrumentIds),
          relatedPublicationIds: CleanJSObjectService.
          removeWhiteSpace(data.relatedPublicationIds)
        };
        var cleanedStudyObject = CleanJSObjectService
          .removeEmptyJsonObjects(studyObj);
        studiesObjArray[i] = new StudyResource(cleanedStudyObject);
      }
      return studiesObjArray;
    };
    return {
      getStudies: getStudies
    };
  });
