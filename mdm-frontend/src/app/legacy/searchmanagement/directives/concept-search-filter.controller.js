/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ConceptSearchFilterController', [
    '$scope', 'ConceptSearchService', '$timeout',
    '$rootScope', '$location', '$q',
    function($scope, ConceptSearchService, $timeout, $rootScope, $location,
      $q) {
      // prevent concept changed events during init
      var initializing = true;
      var selectionChanging = false;
      var cache = {
        searchText: null,
        filter: null,
        type: null,
        query: null,
        searchResult: null
      };
      var init = function() {
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter.concept) {
          $rootScope.$broadcast('start-ignoring-404');
          ConceptSearchService.findOneById(
            $scope.currentSearchParams.filter.concept,
            ['id', 'title']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentConcept = result;
              } else {
                $scope.currentConcept = {
                  id: $scope.currentSearchParams.filter.concept
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentConcept = {
                  id: $scope.currentSearchParams.filter.concept
                };
                $timeout(function() {
                  $scope.conceptFilterForm.conceptFilter
                    .$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.conceptFilterForm.conceptFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(concept) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (concept) {
          $scope.currentSearchParams.filter.concept =
          concept.id;
        } else {
          delete $scope.currentSearchParams.filter.concept;
        }
        $scope.conceptChangedCallback();
      };

      $scope.searchConcepts = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'concept');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query
          ) {
          return $q.resolve(cache.searchResult);
        }
        //Search Call to Elasticsearch
        return ConceptSearchService.findTitles(searchText,
           cleanedFilter, $scope.type, $scope.query)
          .then(function(titles) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = titles;
            cache.type = $scope.type;
            cache.query = $scope.query;
            return titles;
          }
        );
      };
      $scope.$watch('currentSearchParams.filter["concept"]',
        function() {
          init();
        });
    }
  ]);
