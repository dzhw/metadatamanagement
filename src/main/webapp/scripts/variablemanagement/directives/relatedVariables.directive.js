/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedVariables',
  function(VariableSearchResource, blockUI) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/' +
        'relatedVariables.html.tmpl',
      scope: {},
      link: function(scope, element, attrs) {
        scope.RelatedVariableController.style = {};
        if (attrs.count > 3) {
          scope.RelatedVariableController.style.height = '400';
        } else {
          scope.RelatedVariableController.style.height = attrs.count * 142;
        }
      },
      controllerAs: 'RelatedVariableController',
      controller: function() {
        var RelatedVariableController = this;
        RelatedVariableController.count =
        Number(RelatedVariableController.count);
        var blockArea = blockUI.instances.get('blockRelatedVariableContainer');
        RelatedVariableController.variables = {
          pageToLoad: 0,
          items: [],
          totalHits: RelatedVariableController.count,
          currentlyLoadingPage: -1,
          getItemAtIndex: function(index) {
            if (index >= this.items.length && index < this.totalHits) {
              this.fetchMoreItems_(index);
              return null;
            }
            return this.items[index];
          },
          getLength: function() {
            if (this.items.length === this.totalHits) {
              return this.items.length;
            }
            return this.items.length + 1;
          },
          fetchMoreItems_: function() {
              if (this.currentlyLoadingPage !== this.pageToLoad) {
                this.currentlyLoadingPage = this.pageToLoad;
                blockArea.start();
                if (_.isArray(RelatedVariableController.methodParams)) {
                  var searchTerms = _.chunk(RelatedVariableController
                    .methodParams, 5);
                  VariableSearchResource[RelatedVariableController.methodName]
                  (searchTerms[this.pageToLoad])
                    .then(angular.bind(this, function(variables) {
                      _.pullAllBy(variables.docs, [{'found': false}], 'found');
                      this.items = _.concat(this.items, variables.docs);
                      this.pageToLoad += 1;
                    })).finally(function() {
                      blockArea.stop();
                    });
                } else {
                  VariableSearchResource[RelatedVariableController.methodName](
                  RelatedVariableController.methodParams,
                  this.pageToLoad * 5, 5)
                  .then(angular.bind(this, function(variables) {
                    this.items = _.concat(this.items, variables.hits.hits);
                    this.pageToLoad += 1;
                  })).finally(function() {
                      blockArea.stop();
                    });
                }
              }
            }
        };

      },
      bindToController: {
        methodName: '@',
        methodParams: '=',
        count: '@'
      }
    };
  });
