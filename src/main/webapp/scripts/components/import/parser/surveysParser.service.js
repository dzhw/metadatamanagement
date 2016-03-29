'use strict';

angular.module('metadatamanagementApp').service('SurveysParser',
function(Survey) {
  var getSurveys = function(surveys, projectId) {
      var surveysObjArray = [];
      for (var i = 0; i < surveys.length; i++) {
        var data = surveys[i];
        if (!data.id || data.id === '') {
          /*$scope.uploadStatus.pushError($translate.instant(
            'metadatamanagementApp.dataAcquisitionProject.' +
            'detail.logMessages.' +
            'missingId', {
              index: i + 1
            }));*/
        } else {
          var surveyObj = {
            id: data.id,
            dataAcquisitionProjectId: projectId,
            questionnaireId: data.questionnaireId,
            title: {
              en: data['title.en'],
              de: data['title.de']
            },
            fieldPeriod: {
              start: data['fieldPeriod.start'],
              end: data['fieldPeriod.end']
            }
          };
          surveysObjArray[i] = new Survey(surveyObj);
        }
      }
      return surveysObjArray;
    };
  return {
      getSurveys: getSurveys
    };
});
