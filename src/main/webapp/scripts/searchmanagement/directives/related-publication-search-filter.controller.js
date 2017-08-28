/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationSearchFilterController', [
    '$scope', 'SearchDao', 'RelatedPublicationSearchService', '$timeout',
    'CurrentProjectService',
    function($scope, SearchDao, RelatedPublicationSearchService, $timeout,
      CurrentProjectService) {
      // prevent related-publication changed events during init
      var initializing = true;
      var selectionChanging = false;
      var lastSearchText;
      var lastFilter;
      var lastProjectId;
      var lastSearchResult;
      var init = function() {
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['related-publication']) {
          RelatedPublicationSearchService.findOneById(
            $scope.currentSearchParams.filter['related-publication']).promise
            .then(function(result) {
              if (result) {
                $scope.currentRelatedPublication = {_source: result};
              } else {
                $scope.currentRelatedPublication = {
                  _source: {
                    id: $scope.currentSearchParams.filter['related-publication']
                  }
                };
              }
            }, function() {
                $scope.currentRelatedPublication = {
                  _source: {
                    id: $scope.currentSearchParams.filter['related-publication']
                  }
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
          relatedPublication._source.id;
        } else {
          delete $scope.currentSearchParams.filter['related-publication'];
        }
        $scope.relatedPublicationChangedCallback();
      };

      $scope.searchRelatedPublications = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'related-publication');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
            CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
          currentProjectId, cleanedFilter,
          'related_publications',
          100).then(function(data) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastProjectId = currentProjectId;
            lastSearchResult = data.hits.hits;
            return data.hits.hits;
          });
      };
      $scope.$watch('currentSearchParams.filter["related-publication"]',
        function() {
          init();
        });
    }
  ]);
