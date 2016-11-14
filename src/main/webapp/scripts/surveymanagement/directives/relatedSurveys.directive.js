/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedSurveys',
    function(SurveySearchService, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/surveymanagement/directives/' +
          'relatedSurveys.html.tmpl',
        scope: {},
        link: function(scope, element, attrs) {
          scope.ctrl.style = {};
          if (attrs.count > 3) {
            scope.ctrl.style.height = '400';
          } else {
            scope.ctrl.style.height = attrs.count * 138;
          }
        },
        controllerAs: 'ctrl',
        controller: function() {
          var ctrl = this;
          ctrl.count = Number(ctrl.count);
          var blockArea = blockUI.instances.get('blockRelatedSurveyContainer');
          ctrl.surveys = {
            pageToLoad: 0,
            items: [],
            totalHits: ctrl.count,
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
                  if (_.isArray(ctrl.methodParams)) {
                    var searchTerms = _.chunk(ctrl
                      .methodParams, 5);
                    SurveySearchService[ctrl.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(surveys) {
                        _.pullAllBy(surveys.docs, [{'found': false}], 'found');
                        this.items = _.concat(this.items, surveys.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    SurveySearchService[ctrl.methodName](
                    ctrl.methodParams,
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
          count: '@',
          surveyId: '@'
        }
      };
    });
