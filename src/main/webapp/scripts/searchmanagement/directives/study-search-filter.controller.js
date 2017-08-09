/* global _  */
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchFilterController', [
    '$scope', 'SearchDao', 'StudySearchService', '$timeout',
    function($scope, SearchDao, StudySearchService, $timeout) {
      // prevent study changed events during init
      var initializing = true;
      var lastSearchText;
      var lastFilter;
      var lastSearchResult;
      var init = function() {
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter.study) {
          StudySearchService.findOneById(
            $scope.currentSearchParams.filter.study).promise
            .then(function(result) {
              if (result) {
                $scope.currentStudy = {_source: result};
              } else {
                $scope.currentStudy = {
                  _source: {
                    id: $scope.currentSearchParams.filter.study
                  }
                };
              }
            }, function() {
                $scope.currentStudy = {
                  _source: {
                    id: $scope.currentSearchParams.filter.study
                  }
                };
                $timeout(function() {
                  $scope.studyFilterForm.studyFilter.$setValidity(
                    'md-require-match', false);
                });
                $scope.studyFilterForm.studyFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(study) {
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (study) {
          $scope.currentSearchParams.filter.study = study._source.id;
        } else {
          delete $scope.currentSearchParams.filter.study;
        }
        if (!initializing) {
          $scope.studyChangedCallback();
        }
        initializing = false;
      };

      $scope.searchStudies = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'study');
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter)) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            undefined, cleanedFilter,
            'studies',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      init();
    }
  ]);
