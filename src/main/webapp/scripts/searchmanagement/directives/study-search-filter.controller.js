/* global _  */
'use strict';

angular.module('metadatamanagementApp')
  .controller('StudySearchFilterController', [
    '$scope', 'StudySearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location',
    function($scope, StudySearchService, $timeout,
      CurrentProjectService, $rootScope, $location) {
      // prevent study changed events during init
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
          $scope.currentSearchParams.filter.study) {
          $rootScope.$broadcast('start-ignoring-404');
          StudySearchService.findOneById(
            $scope.currentSearchParams.filter.study).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentStudy = result;
              } else {
                $scope.currentStudy = {
                  id: $scope.currentSearchParams.filter.study
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentStudy = {
                    id: $scope.currentSearchParams.filter.study
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
          $scope.currentSearchParams.filter.study = study.id;
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
        $scope.type = $location.search().type;
        var query = $location.search().query;
        if (searchText === cache.searchText &&
          _.isEqual(cache.filter, cleanedFilter) &&
          cache.projectId === currentProjectId &&
          cache.type === $scope.type &&
          cache.query === query) {
          return cache.searchResult;
        }
        return StudySearchService.findStudyTitles(searchText, cleanedFilter,
            $scope.type, query, currentProjectId).then(function(data) {
              cache.searchText = searchText;
              cache.filter = _.cloneDeep(cleanedFilter);
              cache.projectId = currentProjectId;
              cache.query = query;
              cache.type = $scope.type;
              cache.searchResult = data;
              return data;
            }
          );
      };
      $scope.$watch('currentSearchParams.filter.study', function() {
        init();
      });
    }
  ]);
