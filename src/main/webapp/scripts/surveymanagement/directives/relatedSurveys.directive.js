/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedSurveys',
    function(SurveySearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/surveymanagement/directives/' +
          'relatedSurveys.html.tmpl',
        scope: {},
        controllerAs: 'relatedSurveyController',
        controller: function() {
          var relatedSurveyController = this;
          relatedSurveyController.count = Number(relatedSurveyController.count);
          var blockArea = blockUI.instances.get('blockRelatedSurveyContainer');
          relatedSurveyController.infiniteItems = {
            pageToLoad: 0,
            items: [],
            totalHits: relatedSurveyController.count,
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
                  if (_.isArray(relatedSurveyController.methodParams)) {
                    var searchTerms = _.chunk(relatedSurveyController
                      .methodParams, 5);
                    SurveySearchResource[relatedSurveyController.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(surveys) {
                        _.pullAllBy(surveys.docs, [{'found': false}], 'found');
                        this.items = _.concat(this.items, surveys.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    SurveySearchResource[relatedSurveyController.methodName](
                    relatedSurveyController.methodParams,
                    this.pageToLoad * 5, 5)
                    .then(angular.bind(this, function(surveys) {
                      this.items = _.concat(this.items, surveys.hits.hits);
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
