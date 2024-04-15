/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('ApprovedUsageSearchFilterController', [
    'DataPackageSearchService', '$scope', '$timeout',
    'CurrentProjectService', '$rootScope', '$location', '$q',
    function(DataPackageSearchService, $scope, $timeout,
      CurrentProjectService, $rootScope, $location, $q) {
      // prevent datapackage changed events during init
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
        // if the search page is called with an url-param (e.g. "?approved-usage=foobar"),
        // $scope.currentSearchParams.filter is not empty
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter["approved-usage"]) {
          $rootScope.$broadcast('start-ignoring-404');

          DataPackageSearchService.findApprovedUsage(
            $scope.currentSearchParams.filter["approved-usage"],
            $scope.currentSearchParams.filter
          ).then(function(result) {
            $rootScope.$broadcast('stop-ignoring-404');
            if (result.length > 0) {
              // found DataPackage with approvedUsage
              $scope.currentApprovedUsage = result[0].title;
            } else {
              // no DataPackage with approvedUsage found
              console.log("Did not find DataPackge with approvedUsage");
              $scope.currentApprovedUsage = $scope.currentSearchParams.filter["approved-usage"];
              $timeout(function() {
                $scope.approvedUsageFilterForm.approvedUsageFilter.$setValidity(
                  'md-require-match', false);
              }, 500);
              $scope.approvedUsageFilterForm.approvedUsageFilter.$setTouched();
            }
          });
        } else {
          initializing = false;
        }
      };
      
      // triggered whenever the selection of the "approved usage"-dropdown in DataPackage filter is changed
      $scope.onSelectionChanged = function(approvedUsage) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }
        if (approvedUsage) {
          $scope.currentSearchParams.filter["approved-usage"] = approvedUsage.title;
        } else {
          delete $scope.currentSearchParams.filter["approved-usage"];
        }
        $scope.approvedUsageChangedCallback();
      };

      // search datapackage index from ElasticSearch for a provided searchText
      $scope.searchApprovedUsage = function(searchText) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'survey');

        if (searchText === cache.searchText &&
            $scope.type === cache.type &&
            _.isEqual(cache.filter, cleanedFilter) &&
            $scope.query === cache.query &&
            $scope.projectId === cache.projectId
          ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return DataPackageSearchService.findApprovedUsage(searchText, cleanedFilter)
          .then(function(approvedUsage) {
            cache.searchText = searchText;
            cache.filter = _.cloneDeep(cleanedFilter);
            cache.searchResult = approvedUsage;
            cache.type = $scope.type;
            cache.query = $scope.query;
            cache.projectId = $scope.projectId;
            return approvedUsage;
          }
        );
      };

      $scope.$watch('currentSearchParams.filter.approvedUsage', function() {
        init();
      });
    }
  ]);
