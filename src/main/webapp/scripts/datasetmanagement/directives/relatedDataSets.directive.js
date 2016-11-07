/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedDataSets',
    function(DataSetSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/datasetmanagement/directives/' +
          'relatedDataSets.html.tmpl',
        scope: {},
        link: function(scope, element, attrs) {
          scope.RelatedDataSetController.style = {};
          if (attrs.count > 3) {
            scope.RelatedDataSetController.style.height = '400';
          } else {
            scope.RelatedDataSetController.style.height = attrs.count * 165;
          }
        },
        controllerAs: 'RelatedDataSetController',
        controller: function() {
          var RelatedDataSetController = this;
          RelatedDataSetController.count =
          Number(RelatedDataSetController.count);
          var blockArea = blockUI.instances.get('blockRelatedDataSetContainer');
          RelatedDataSetController.dataSets = {
            pageToLoad: 0,
            items: [],
            totalHits: RelatedDataSetController.count,
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
                  if (_.isArray(RelatedDataSetController.methodParams)) {
                    var searchTerms = _.chunk(RelatedDataSetController
                      .methodParams, 5);
                    DataSetSearchResource[RelatedDataSetController.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(dataSets) {
                        _.pullAllBy(dataSets.docs, [{'found': false}], 'found');
                        this.items = _.concat(this.items, dataSets.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    DataSetSearchResource[RelatedDataSetController.methodName](
                    RelatedDataSetController.methodParams,
                    this.pageToLoad * 5, 5)
                    .then(angular.bind(this, function(dataSets) {
                      this.items = _.concat(this.items, dataSets.hits.hits);
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
