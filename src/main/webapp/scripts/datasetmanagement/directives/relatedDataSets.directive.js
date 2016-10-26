/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedDataSets',
    function(DataSetSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/datasetmanagement/directives/' +
          'relatedDataSets.html.tmpl',
        scope: {},
        controllerAs: 'relatedDataSetController',
        controller: function() {
          var relatedDataSetController = this;
          var blockArea = blockUI.instances.get('blockRelatedDataSetCard');
          relatedDataSetController.page = {
            currentPageNumber: 1,
            totalHits: 0,
            size: 5
          };
          relatedDataSetController.search = function() {
            blockArea.start();
            if (_.isArray(relatedDataSetController.methodParams)) {
              var searchTerms = _.chunk(relatedDataSetController
                .methodParams, relatedDataSetController.page.size);
              relatedDataSetController.page.totalHits = relatedDataSetController
              .methodParams.length;
              DataSetSearchResource[relatedDataSetController.methodName]
              (searchTerms[relatedDataSetController.page.currentPageNumber - 1])
                .then(function(dataSets) {
                  relatedDataSetController.dataSets = dataSets.docs;
                }).finally(function() {
                  blockArea.stop();
                });
            } else {
              var from = (relatedDataSetController.page.currentPageNumber - 1) *
                relatedDataSetController.page.size;
              DataSetSearchResource[relatedDataSetController.methodName](
                relatedDataSetController.methodParams, from,
                relatedDataSetController.page.size)
                .then(function(dataSets) {
                  relatedDataSetController.page.totalHits = dataSets.hits.total;
                  relatedDataSetController.dataSets = dataSets.hits.hits;
                }).finally(function() {
                  blockArea.stop();
                });
            }
          };
          relatedDataSetController.search();
        },
        bindToController: {
          methodName: '@',
          methodParams: '='
        }
      };
    });
