/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationSearchFilterController', [
    '$scope', 'RelatedPublicationSearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function($scope, RelatedPublicationSearchService, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent related-publication changed events during init
      var initializing = true;
      var selectionChanging = false;
      var cache = {
        searchText: null,
        filter: null,
        type: null,
        query: null,
        projectId: null,
        searchResult: null
      };
      var init = function() {
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['related-publication']) {
          $rootScope.$broadcast('start-ignoring-404');
          RelatedPublicationSearchService.findOneById(
            $scope.currentSearchParams.filter['related-publication'],
            ['id', 'title']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentRelatedPublication = result;
              } else {
                $scope.currentRelatedPublication = {
                  id: $scope.currentSearchParams.filter['related-publication']
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentRelatedPublication = {
                  id: $scope.currentSearchParams.filter['related-publication']
                };
                $timeout(function() {
                  $scope.relatedPublicationFilterForm.relatedPublicationFilter
                    .$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.relatedPublicationFilterForm.relatedPublicationFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(relatedPublication) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (relatedPublication) {
          $scope.currentSearchParams.filter['related-publication'] =
          relatedPublication.id;
        } else {
          delete $scope.currentSearchParams.filter['related-publication'];
        }
        $scope.relatedPublicationChangedCallback();
      };

      $scope.searchRelatedPublications = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
        CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'related-publication');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }
        //Search Call to Elasticsearch
        return RelatedPublicationSearchService.findTitles(searchText,
           cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(titles) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = titles;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return titles;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter["related-publication"]',
        function() {
          init();
        });
    }
  ]);
