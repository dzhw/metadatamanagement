/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedVariables',
  function(VariableSearchResource, blockUI) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/' +
        'relatedVariables.html.tmpl',
      controllerAs: 'relatedVariableController',
      controller: function() {
        var relatedVariableController = this;
        var blockArea = blockUI.instances.get('blockRelatedVariableCard');
        relatedVariableController.page = {
          currentPageNumber: 1,
          totalHits: 0,
          size: 5
        };
        relatedVariableController.search = function() {
          blockArea.start();
          if (_.isArray(relatedVariableController.methodParams)) {
            var searchTerms = _.chunk(relatedVariableController
              .methodParams, relatedVariableController.page.size);
            relatedVariableController.page.totalHits =
            relatedVariableController.methodParams.length;
            VariableSearchResource[relatedVariableController.methodName]
            (searchTerms[relatedVariableController.page.currentPageNumber - 1])
              .then(function(variables) {
                relatedVariableController.variables = variables.docs;
              }).finally(function() {
                blockArea.stop();
              });
          } else {
            var from = (relatedVariableController.page.currentPageNumber - 1) *
              relatedVariableController.page.size;
            VariableSearchResource[relatedVariableController.methodName]
            (relatedVariableController.methodParams, from,
              relatedVariableController.page.size)
              .then(function(variables) {
                relatedVariableController.page.totalHits = variables.hits.total;
                relatedVariableController.variables = variables.hits.hits;
              }).finally(function() {
                blockArea.stop();
              });
          }
        };
        relatedVariableController.search();
      },
      bindToController: {
        methodName: '@',
        methodParams: '='
      }
    };
  });
