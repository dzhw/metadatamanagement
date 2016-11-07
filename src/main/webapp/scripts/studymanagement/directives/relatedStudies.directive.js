/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedStudies',
    function(StudySearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/studymanagement/directives/' +
          'relatedStudies.html.tmpl',
        scope: {},
        controllerAs: 'RelatedStudyController',
        controller: function() {
          var RelatedStudyController = this;
          RelatedStudyController.count =
          Number(RelatedStudyController.count);
          var blockArea = blockUI.instances.get('blockRelatedSurveyContainer');
          RelatedStudyController.studies = {
            pageToLoad: 0,
            items: [],
            totalHits: RelatedStudyController.count,
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
                  if (_.isArray(RelatedStudyController.methodParams)) {
                    var searchTerms = _.chunk(RelatedStudyController
                      .methodParams, 5);
                    StudySearchResource
                    [RelatedStudyController.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(studies) {
                        _.pullAllBy(studies.docs, [{'found': false}], 'found');
                        this.items = _.concat(this.items, studies.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    StudySearchResource
                    [RelatedStudyController.methodName](
                    RelatedStudyController.methodParams,
                    this.pageToLoad * 5, 5)
                    .then(angular.bind(this, function(studies) {
                      this.items = _.concat(this.items, studies.hits.hits);
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
