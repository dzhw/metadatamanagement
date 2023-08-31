/* global _ */
'use strict';
angular.module('metadatamanagementApp')
  .controller('SponsorSearchFilterController', [
  '$scope',
  '$location',
  '$q',
  '$timeout',
  'CurrentProjectService',
  'DataPackageSearchService',
    function($scope, $location, $q, $timeout, CurrentProjectService,
             DataPackageSearchService) {

      // prevent sponsor changed events during init
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
      var currentFilterByLanguage;

      //Search after sponsors, call Elasticsearch
      $scope.searchSponsors = function(searchText, language) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'sponsor-' + language);

        if (searchText === cache.searchText &&
          $scope.type === cache.type &&
          _.isEqual(cache.filter, cleanedFilter) &&
          $scope.query === cache.query &&
          $scope.projectId === cache.projectId
        ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return DataPackageSearchService.findSponsorFilterOptions(searchText,
          cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(sponsors) {
              cache.searchText = searchText;
              cache.filter = _.cloneDeep(cleanedFilter);
              cache.searchResult = sponsors;
              cache.type = $scope.type;
              cache.query = $scope.query;
              cache.projectId = $scope.projectId;
              return sponsors;
            }
          );
      };

      //Init the de and en filter of sponsors
      var init = function(currentLanguage) {
        //Just a change? No Init!
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }

        //Init the Filter
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['sponsor-' +
          currentLanguage]) {

          //Check with of both filter are active, depending on actual language
          currentFilterByLanguage =
            $scope.currentSearchParams.filter['sponsor-' +
            currentLanguage];

          //Search and validate sponsors
          $scope.searchSponsors(currentFilterByLanguage, currentLanguage)
            .then(function(sponsors) {

              //Just one sponsor
              if (sponsors.length === 1) {
                $scope.currentSponsor = sponsors[0];
                return;
              } else if (sponsors.length > 1) {
                //Standard case, there are many sponosrs
                sponsors.forEach(function(sponsor) {
                  if (sponsor[currentLanguage] ===
                    currentFilterByLanguage) {
                    $scope.currentSponsor = sponsor;
                    return;
                  }
                });
              }

              //Sponsor was not found check the language
              if (!$scope.currentSponsor) {
                $scope.currentSponsor =
                  $scope.currentSearchParams.filter['sponsor-' +
                  currentLanguage];
                $timeout(function() {
                  $scope.sponsorFilterForm.sponsorFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.sponsorFilterForm.sponsorFilter
                  .$setTouched();
              }
            });
        } else {
          var i18nActualEnding = $scope.currentLanguage;
          var i18nAnotherEnding;
          if (i18nActualEnding === 'de') {
            i18nAnotherEnding = 'en';
          } else {
            i18nAnotherEnding = 'de';
          }

          if ($scope.currentSearchParams.filter &&
            $scope.currentSearchParams.filter['sponsor-' +
            i18nAnotherEnding]) {
            $scope.searchSponsors(
              $scope.currentSearchParams.filter['sponsor-' +
              i18nAnotherEnding], i18nAnotherEnding)
              .then(function(sponsors) {
                if (sponsors) {
                  $scope.currentSponsor = sponsors[0];

                  $scope.currentSearchParams.filter['sponsor-' +
                  i18nActualEnding] = sponsors[0][i18nActualEnding];
                  $scope.selectedFilters.push('sponsor-' +
                    i18nActualEnding);
                  delete $scope.selectedFilters[_.indexOf(
                    $scope.selectedFilters,
                    'sponsor-' + i18nAnotherEnding)
                    ];
                  delete $scope.currentSearchParams.filter['sponsor-' +
                  i18nAnotherEnding];
                  return;
                }
              });
          }
          initializing = false;
        }
      };

      //Set the filter, if the user changed the sponsor filter
      $scope.onSelectionChanged = function(sponsor) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }

        //Set the sponsor Filter in the URL
        if (sponsor) {
          $scope.currentSearchParams.filter['sponsor-' +
          $scope.currentLanguage] = sponsor[$scope.currentLanguage];
        } else {
          //No sponsor is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['sponsor-' +
          $scope.currentLanguage];
        }
        $scope.sponsorChangedCallback();
      };

      //Initialize and watch the current sponsor filter
      $scope.$watch('currentSearchParams.filter["sponsor-' +
        $scope.currentLanguage + '"]',
        function() {
          init($scope.currentLanguage);
        });
    }]);

