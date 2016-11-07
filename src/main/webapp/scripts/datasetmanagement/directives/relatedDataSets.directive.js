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
          relatedDataSetController.count =
          Number(relatedDataSetController.count);
          var blockArea = blockUI.instances.get('blockRelatedDataSetContainer');
          relatedDataSetController.dataSets = {
            pageToLoad: 0,
            items: [],
            totalHits: relatedDataSetController.count,
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
                  if (_.isArray(relatedDataSetController.methodParams)) {
                    var searchTerms = _.chunk(relatedDataSetController
                      .methodParams, 5);
                    DataSetSearchResource[relatedDataSetController.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(dataSets) {
                        _.pullAllBy(dataSets.docs, [{'found': false}], 'found');
                        this.items = _.concat(this.items, dataSets.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    DataSetSearchResource[relatedDataSetController.methodName](
                    relatedDataSetController.methodParams,
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
