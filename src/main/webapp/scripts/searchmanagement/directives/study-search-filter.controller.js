'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchFilterController', [
    '$scope', 'SearchDao', 'StudySearchService',
    function($scope, SearchDao, StudySearchService) {
      // prevent study changed events during init
      var initializing = true;
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
        return SearchDao.search(searchText, 1,
            undefined, undefined,
            'studies',
            100).then(function(data) {
              return data.hits.hits;
            }
          );
      };
      init();
    }
  ]);
