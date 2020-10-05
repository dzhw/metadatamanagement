/* global _  */
'use strict';

angular.module('metadatamanagementApp')
  .controller('DataPackageSearchFilterController', [
    '$scope', 'DataPackageSearchService', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function($scope, DataPackageSearchService, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent dataPackage changed events during init
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
          $scope.currentSearchParams.filter['data-package']) {
          $rootScope.$broadcast('start-ignoring-404');
          DataPackageSearchService.findOneById(
            $scope.currentSearchParams.filter['data-package'], ['id',
            'masterId', 'title']).promise
            .then(function(result) {
              $rootScope.$broadcast('stop-ignoring-404');
              if (result) {
                $scope.currentDataPackage = result;
              } else {
                $scope.currentDataPackage = {
                  id: $scope.currentSearchParams.filter['data-package']
                };
              }
            }, function() {
                $rootScope.$broadcast('stop-ignoring-404');
                $scope.currentDataPackage = {
                    id: $scope.currentSearchParams.filter['data-package']
                  };
                $timeout(function() {
                  $scope.dataPackageFilterForm.dataPackageFilter.$setValidity(
                    'md-require-match', false);
                }, 500);
                $scope.dataPackageFilterForm.dataPackageFilter.$setTouched();
              });
        } else {
          initializing = false;
        }
      };
      $scope.onSelectionChanged = function(dataPackage) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (dataPackage) {
          $scope.currentSearchParams.filter['data-package'] = dataPackage.id;
        } else {
          delete $scope.currentSearchParams.filter['data-package'];
        }
        $scope.datapackageChangedCallback();
      };

      $scope.searchDataPackages = function(searchText) {
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'data-package');
        var currentProjectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        $scope.type = $location.search().type;
        var query = $location.search().query;
        if (searchText === cache.searchText &&
          _.isEqual(cache.filter, cleanedFilter) &&
          cache.projectId === currentProjectId &&
          cache.type === $scope.type &&
          cache.query === query) {
          return $q.resolve(cache.searchResult);
        }
        return DataPackageSearchService.findDataPackageTitles(searchText,
            cleanedFilter, $scope.type, query, currentProjectId).then(
              function(data) {
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
      $scope.$watch('currentSearchParams.filter["data-package"]', function() {
        init();
      });
    }
  ]);
