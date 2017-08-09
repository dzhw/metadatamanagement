/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationSearchFilterController', [
    '$scope', 'SearchDao', 'RelatedPublicationSearchService', '$timeout',
    function($scope, SearchDao, RelatedPublicationSearchService, $timeout) {
      // prevent related-publication changed events during init
      var initializing = true;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var init = function() {
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
                });
                $scope.relatedPublicationFilterForm.relatedPublicationFilter
                  .$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(relatedPublication) {
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (relatedPublication) {
          $scope.currentSearchParams.filter['related-publication'] =
          relatedPublication._source.id;
        } else {
          delete $scope.currentSearchParams.filter['related-publication'];
        }
        if (!initializing) {
          $scope.relatedPublicationChangedCallback();
        }
        initializing = false;
      };

      $scope.searchRelatedPublications = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'related-publication');
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
          undefined, cleanedFilter,
          'related_publications',
          100).then(function(data) {
            lastSearchText = searchText;
            lastFilter = _.cloneDeep(cleanedFilter);
            lastSearchResult = data.hits.hits;
            return data.hits.hits;
          });
      };
      init();
    }
  ]);
