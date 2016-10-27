/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedQuestions',
    function(QuestionSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/questionmanagement/directives/' +
          'relatedQuestions.html.tmpl',
        scope: {},
        controllerAs: 'relatedQuestionController',
        controller: function() {
          var relatedQuestionController = this;
          var blockArea = blockUI.instances.get('blockRelatedQuestionCard');
          relatedQuestionController.page = {
            currentPageNumber: 1,
            totalHits: 0,
            size: 5
          };
          relatedQuestionController.search = function() {
            if (relatedQuestionController.methodParams) {
              blockArea.start();
              if (_.isArray(relatedQuestionController.methodParams)) {
                var searchTerms = _.chunk(relatedQuestionController
                  .methodParams, relatedQuestionController.page.size);
                QuestionSearchResource[relatedQuestionController.methodName]
                  (searchTerms[relatedQuestionController.page
                    .currentPageNumber - 1]).then(function(questions) {
                      _.pullAllBy(questions.docs, [{'found': false}], 'found');
                      relatedQuestionController.page.totalHits = questions.docs
                      .length;
                      relatedQuestionController.questions = questions.docs;
                    }).finally(function() {
                      blockArea.stop();
                    });
              } else {
                var from = (relatedQuestionController
                  .page.currentPageNumber - 1) * relatedQuestionController
                  .page.size;
                QuestionSearchResource[relatedQuestionController.methodName](
                  relatedQuestionController.methodParams, from,
                  relatedQuestionController.page.size)
                  .then(function(questions) {
                          relatedQuestionController.page.totalHits =
                          questions.hits.total;
                          relatedQuestionController.questions =
                          questions.hits.hits;
                        }).finally(function() {
                          blockArea.stop();
                        });
              }
            }
          };
          relatedQuestionController.search();
        },
        bindToController: {
          methodName: '@',
          methodParams: '='
        }
      };
    });
