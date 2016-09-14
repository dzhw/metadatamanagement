/* Author: Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').service('StudyBuilderService',
  function(StudyResource, CleanJSObjectService, CurrentProjectService) {
    var parseErrors = [];
    var getStudies = function(study) {
      var studiesObjArray = [];

      //Create releases
      var releases = [];
      for (var j = 0; j < study.releases.length; j++) {
        var releaseObj = {
          version: study.releases[j].version,
          doi: study.releases[j].doi,
          date: study.releases[j].date,
          notes: {
            en: study.releases[j]['notes.en'],
            de: study.releases[j]['notes.de'],
          }
        };
        releases.push(releaseObj);
      }
      parseErrors.length = 0;
      for (var i = 0; i < study.length; i++) {
        var studyObj = {
          id: CurrentProjectService.getCurrentProject().id,
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
          releases: releases,
          surveyIds: CleanJSObjectService.
          removeWhiteSpace(study[i].surveyIds),
          dataSetIds: CleanJSObjectService.
          removeWhiteSpace(study[i].dataSetIds),
          instrumentIds: CleanJSObjectService.
          removeWhiteSpace(study[i].instrumentIds),
          relatedPublicationIds: CleanJSObjectService.
          removeWhiteSpace(study[i].relatedPublicationIds)
        };
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
