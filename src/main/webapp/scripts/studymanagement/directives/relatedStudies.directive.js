/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedStudies',
    function(StudySearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/studymanagement/directives/' +
          'relatedStudies.html.tmpl',
        controllerAs: 'relatedStudyController',
        controller: function() {
          var relatedStudyController = this;
          var blockArea = blockUI.instances.get('blockRelatedStudyCard');
          relatedStudyController.page = {
            currentPageNumber: 1,
            totalHits: 0,
            size: 5
          };
          relatedStudyController.search = function() {
            if (relatedStudyController.methodParams) {
              blockArea.start();
              if (_.isArray(relatedStudyController.methodParams)) {
                var searchTerms = _.chunk(relatedStudyController
                  .methodParams, relatedStudyController.page.size);
                relatedStudyController.page.totalHits =
                relatedStudyController.methodParams.length;
                StudySearchResource[relatedStudyController.methodName]
                  (searchTerms[relatedStudyController.page
                    .currentPageNumber - 1]).then(function(studies) {
                      relatedStudyController.studies = studies.docs;
                    }).finally(function() {
                      blockArea.stop();
                    });
              } else {
                var from = (relatedStudyController
                  .page.currentPageNumber - 1) * relatedStudyController
                  .page.size;
                StudySearchResource[relatedStudyController.methodName](
                  relatedStudyController.methodParams, from,
                  relatedStudyController.page.size)
                  .then(function(studies) {
                          relatedStudyController.page.totalHits =
                          studies.hits.total;
                          relatedStudyController.studies =
                          studies.hits.hits;
                        }).finally(function() {
                          blockArea.stop();
                        });
              }
            }
          };
          relatedStudyController.search();
        },
        bindToController: {
          methodName: '@',
          methodParams: '='
        }
      };
    });
