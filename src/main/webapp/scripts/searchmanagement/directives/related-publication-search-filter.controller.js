'use strict';

angular.module('metadatamanagementApp')
  .controller('RelatedPublicationSearchFilterController', [
    '$scope', 'SearchDao', 'RelatedPublicationSearchService', '$q',
    function($scope, SearchDao, RelatedPublicationSearchService, $q) {
      // prevent related-publication changed events during init
      var initializing = true;
      // prevent duplicate searches caused by md-autocomplete
      var activeSearches = {};
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
        if (!activeSearches[searchText]) {
          activeSearches[searchText] = true;
          return SearchDao.search(searchText, 1,
            undefined, $scope.currentSearchParams.filter,
            'related_publications',
            100).then(function(data) {
              delete activeSearches[searchText];
              return data.hits.hits;
            }
          );
        } else {
          return $q.defer().promise;
        }
      };
      init();
    }
  ]);
