/* global _  */
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchFilterController', [
    '$scope', 'SearchDao', 'StudySearchService', '$timeout',
    'CurrentProjectService', '$rootScope',
    function($scope, SearchDao, StudySearchService, $timeout,
      CurrentProjectService, $rootScope) {
      // prevent study changed events during init
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
          $scope.currentSearchParams.filter.study) {
          $rootScope.$broadcast('start-ignoring-404');
          StudySearchService.findOneById(
            $scope.currentSearchParams.filter.study).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
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
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentStudy = {
                  _source: {
                    id: $scope.currentSearchParams.filter.study
                  }
                };
                $timeout(function() {
                  $scope.studyFilterForm.studyFilter.$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.studyFilterForm.studyFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(study) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (study) {
          $scope.currentSearchParams.filter.study = study._source.id;
        } else {
          delete $scope.currentSearchParams.filter.study;
        }
        $scope.studyChangedCallback();
      };

      $scope.searchStudies = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'study');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        if (searchText === lastSearchText &&
          _.isEqual(lastFilter, cleanedFilter) &&
          lastProjectId === currentProjectId) {
          return lastSearchResult;
        }
        return SearchDao.search(searchText, 1,
            currentProjectId, cleanedFilter,
            'studies',
            100).then(function(data) {
              lastSearchText = searchText;
              lastFilter = _.cloneDeep(cleanedFilter);
              lastProjectId = currentProjectId;
              lastSearchResult = data.hits.hits;
              return data.hits.hits;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter.study', function() {
        init();
      });
    }
  ]);
