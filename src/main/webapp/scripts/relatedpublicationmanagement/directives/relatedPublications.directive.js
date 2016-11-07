/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('relatedPublications',
    function(RelatedPublicationSearchResource, blockUI) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/relatedpublicationmanagement/directives/' +
          'relatedPublications.html.tmpl',
        scope: {},
        link: function(scope, element, attrs) {
          scope.RelatedPublicationController.style = {};
          if (attrs.count > 3) {
            scope.RelatedPublicationController.style.height = '400';
          } else {
            scope.RelatedPublicationController.style.height = attrs.count * 138;
          }
        },
        controllerAs: 'RelatedPublicationController',
        controller: function() {
          var RelatedPublicationController = this;
          RelatedPublicationController.count =
          Number(RelatedPublicationController.count);
          var blockArea = blockUI.instances.get('blockRelatedSurveyContainer');
          RelatedPublicationController.relatedPublications = {
            pageToLoad: 0,
            items: [],
            totalHits: RelatedPublicationController.count,
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
                  if (_.isArray(RelatedPublicationController.methodParams)) {
                    var searchTerms = _.chunk(RelatedPublicationController
                      .methodParams, 5);
                    RelatedPublicationSearchResource
                    [RelatedPublicationController.methodName]
                    (searchTerms[this.pageToLoad])
                      .then(angular.bind(this, function(relatedPublications) {
                        _.pullAllBy(relatedPublications.docs,
                          [{'found': false}], 'found');
                        this.items = _.concat(this.items,
                          relatedPublications.docs);
                        this.pageToLoad += 1;
                      })).finally(function() {
                        blockArea.stop();
                      });
                  } else {
                    RelatedPublicationSearchResource
                    [RelatedPublicationController.methodName](
                    RelatedPublicationController.methodParams,
                    this.pageToLoad * 5, 5)
                    .then(angular.bind(this, function(relatedPublications) {
                      this.items = _.concat(this.items,
                        relatedPublications.hits.hits);
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
