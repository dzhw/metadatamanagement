/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedQuestions',
    function(QuestionSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/questionmanagement/directives/' +
          'relatedQuestions.html.tmpl',
        scope: {},
        controllerAs: 'RelatedQuestionController',
        controller: function() {
          var RelatedQuestionController = this;
          RelatedQuestionController.count =
          Number(RelatedQuestionController.count);
          var blockArea =
          blockUI.instances.get('blockRelatedQuestionContainer');
          RelatedQuestionController.questions = {
            pageToLoad: 0,
            items: [],
            totalHits: RelatedQuestionController.count,
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
                  if (_.isArray(RelatedQuestionController.methodParams)) {
                    var searchTerms = _.chunk(RelatedQuestionController
                      .methodParams, 5);
                    QuestionSearchResource[RelatedQuestionController.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(questions) {
                        _.pullAllBy(questions
                          .docs, [{'found': false}], 'found');
                        this.items = _.concat(this.items, questions.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    QuestionSearchResource
                    [RelatedQuestionController.methodName](
                    RelatedQuestionController.methodParams,
                    this.pageToLoad * 5, 5)
                    .then(angular.bind(this, function(questions) {
                      this.items = _.concat(this.items, questions.hits.hits);
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
