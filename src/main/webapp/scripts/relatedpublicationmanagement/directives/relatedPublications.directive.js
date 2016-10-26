/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedPublications',
    function(RelatedPublicationSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/relatedpublicationmanagement/directives/' +
          'relatedPublications.html.tmpl',
        controllerAs: 'relatedPublicationController',
        controller: function() {
          var relatedPublicationController = this;
          var blockArea = blockUI.instances.get('blockrelatedPublicationCard');
          relatedPublicationController.page = {
            currentPageNumber: 1,
            totalHits: 0,
            size: 5
          };
          relatedPublicationController.search = function() {
            if (relatedPublicationController.methodParams) {
              blockArea.start();
              if (_.isArray(relatedPublicationController.methodParams)) {
                var searchTerms = _.chunk(relatedPublicationController
                  .methodParams, relatedPublicationController.page.size);
                relatedPublicationController.page.totalHits =
                relatedPublicationController.methodParams.length;
                RelatedPublicationSearchResource[relatedPublicationController
                  .methodName](searchTerms[relatedPublicationController.page
                    .currentPageNumber - 1])
                    .then(function(relatedPublications) {
                      relatedPublicationController.relatedPublications =
                      relatedPublications.docs;
                    }).finally(function() {
                      blockArea.stop();
                    });
              } else {
                var from = (relatedPublicationController
                  .page.currentPageNumber - 1) * relatedPublicationController
                  .page.size;
                RelatedPublicationSearchResource[relatedPublicationController
                  .methodName](relatedPublicationController.methodParams, from,
                  relatedPublicationController.page.size)
                  .then(function(relatedPublications) {
                          relatedPublicationController.page.totalHits =
                          relatedPublications.hits.total;
                          relatedPublicationController.relatedPublications =
                          relatedPublications.hits.hits;
                        }).finally(function() {
                          blockArea.stop();
                        });
              }
            }
          };
          relatedPublicationController.search();
        },
        bindToController: {
          methodName: '@',
          methodParams: '='
        }
      };
    });
