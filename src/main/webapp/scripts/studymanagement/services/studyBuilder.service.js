/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService) {
    var parseErrors = [];
    var getStudies = function(study) {
      var studiesObjArray = [];
      parseErrors.length = 0;
      console.log(study);
      for (var i = 0; i < study.length; i++) {
        var studyObj = {
          id: study[i].id,
          title: {
            en: study[i]['title.en'],
            de: study[i]['title.de']
          },
          description: {
            en: study[i]['description.en'],
            de: study[i]['description.de']
          },
          institution: {
            en: study[i]['institution.en'],
            de: study[i]['institution.de']
          },
          surveySeries: {
            en: study[i]['surveySeries.en'],
            de: study[i]['surveySeries.de']
          },
          sponsor: {
            en: study[i]['sponsor.en'],
            de: study[i]['sponsor.de']
          },
          citationHint: {
            en: study[i]['citationHint.en'],
            de: study[i]['citationHint.de']
          },
          authors: study[i].authors,
          accessWays: CleanJSObjectService.
          removeWhiteSpace(study[i].accessWays),
          dataAcquisitionProjectId: study.dataAcquisitionProjectId,
          releases: CleanJSObjectService.
          removeWhiteSpace(study[i].releases),
          surveyIds: CleanJSObjectService.
          removeWhiteSpace(study[i].surveyIds),
          dataSetIds: CleanJSObjectService.
          removeWhiteSpace(study[i].dataSetIds),
          instrumentIds: CleanJSObjectService.
          removeWhiteSpace(study[i].instrumentIds),
          relatedPublicationIds: CleanJSObjectService.
          removeWhiteSpace(study[i].relatedPublicationIds)
        };
        console.log(studyObj);
        CleanJSObjectService.removeEmptyJsonObjects(studyObj);
        studiesObjArray.push(new StudyResource(studyObj));
      }
      return studiesObjArray;
    };
    return {
      getStudies: getStudies,
      getParseErrors: parseErrors
    };
  });
