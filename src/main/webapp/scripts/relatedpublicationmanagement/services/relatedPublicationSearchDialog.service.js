/* global document*/

'use strict';
angular.module('metadatamanagementApp')
  .service('RelatedPublicationSearchDialogService',
    function($mdDialog, blockUI, RelatedPublicationSearchResource,
      CleanJSObjectService) {
      var relatedPublications = [];
      var showDialog = function() {
        var dialogParent = angular.element(document.body);
        $mdDialog.show({
          controller: 'RelatedPublicationSearchDialogController',
          controllerAs: 'ctrl',
          parent: dialogParent,
          clickOutsideToClose: true,
          locals: {
            relatedPublications: relatedPublications
          },
          templateUrl: 'scripts/relatedpublicationmanagement/views/' +
            'relatedPublicationSearchDialog.html.tmpl',
        });
      };
      var findBySurveyId = function(surveyId) {
        blockUI.start();
        RelatedPublicationSearchResource.findBySurveyId(surveyId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              relatedPublications = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      var findByQuestionId = function(questionId) {
        blockUI.start();
        RelatedPublicationSearchResource.findByQuestionId(questionId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              relatedPublications = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      var findByVariableId = function(variableId) {
        blockUI.start();
        RelatedPublicationSearchResource.findByVariableId(variableId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              relatedPublications = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      var findByDataSetId = function(dataSetId) {
        blockUI.start();
        RelatedPublicationSearchResource.findByDataSetId(dataSetId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              relatedPublications = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      var findByStudyId = function(studyId) {
        blockUI.start();
        RelatedPublicationSearchResource.findByStudyId(studyId)
          .then(function(items) {
            if (!CleanJSObjectService.isNullOrEmpty(items)) {
              relatedPublications = items.hits.hits;
              blockUI.stop();
              showDialog();
            } else {
              blockUI.stop();
            }
          });
      };
      return {
        findBySurveyId: findBySurveyId,
        findByQuestionId: findByQuestionId,
        findByVariableId: findByVariableId,
        findByDataSetId: findByDataSetId,
        findByStudyId: findByStudyId
      };
    });
