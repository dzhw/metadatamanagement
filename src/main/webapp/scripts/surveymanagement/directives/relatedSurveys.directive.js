/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedSurveys',
    function(SurveySearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/surveymanagement/directives/' +
          'relatedSurveys.html.tmpl',
        controllerAs: 'relatedSurveyController',
        controller: function() {
          var relatedSurveyController = this;
          var blockArea = blockUI.instances.get('blockRelatedSurveyCard');
          relatedSurveyController.page = {
            currentPageNumber: 1,
            totalHits: 0,
            size: 5
          };
          relatedSurveyController.search = function() {
            blockArea.start();
            if (_.isArray(relatedSurveyController.methodParams)) {
              var searchTerms = _.chunk(relatedSurveyController
                .methodParams, relatedSurveyController.page.size);
              relatedSurveyController.page.totalHits = relatedSurveyController
              .methodParams.length;
              SurveySearchResource[relatedSurveyController.methodName]
              (searchTerms[relatedSurveyController.page.currentPageNumber - 1])
                .then(function(Surveys) {
                  relatedSurveyController.Surveys = Surveys.docs;
                }).finally(function() {
                  blockArea.stop();
                });
            } else {
              var from = (relatedSurveyController.page.currentPageNumber - 1) *
                relatedSurveyController.page.size;
              SurveySearchResource[relatedSurveyController.methodName](
                relatedSurveyController.methodParams, from,
                relatedSurveyController.page.size)
                .then(function(Surveys) {
                  relatedSurveyController.page.totalHits = Surveys.hits.total;
                  relatedSurveyController.Surveys = Surveys.hits.hits;
                }).finally(function() {
                  blockArea.stop();
                });
            }
          };
          relatedSurveyController.search();
        },
        bindToController: {
          methodName: '@',
          methodParams: '='
        }
      };
    });
