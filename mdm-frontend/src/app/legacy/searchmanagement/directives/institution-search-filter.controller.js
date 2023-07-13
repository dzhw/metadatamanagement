/* global _ */
'use strict';
angular.module('metadatamanagementApp')
  .controller('InstitutionSearchFilterController', [
  '$scope',
  '$location',
  '$q',
  '$timeout',
  'CurrentProjectService',
  'DataPackageSearchService',
    function($scope, $location, $q, $timeout, CurrentProjectService,
             DataPackageSearchService) {
      // prevent institution changed events during init
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

      //Search after institutes, call Elasticsearch
      $scope.searchInstitutions = function(searchText, language) {
        $scope.type = $location.search().type;
        $scope.query = $location.search().query;
        $scope.projectId = CurrentProjectService.getCurrentProject() ?
          CurrentProjectService.getCurrentProject().id : null;
        var cleanedFilter = _.omit($scope.currentSearchParams.filter,
          'institution-' + language);

        if (searchText === cache.searchText &&
          $scope.type === cache.type &&
          _.isEqual(cache.filter, cleanedFilter) &&
          $scope.query === cache.query &&
          $scope.projectId === cache.projectId
        ) {
          return $q.resolve(cache.searchResult);
        }

        //Search Call to Elasticsearch
        return DataPackageSearchService.findInstitutionFilterOptions(searchText,
          cleanedFilter, $scope.type, $scope.query, $scope.projectId)
          .then(function(institutes) {
              cache.searchText = searchText;
              cache.filter = _.cloneDeep(cleanedFilter);
              cache.searchResult = institutes;
              cache.type = $scope.type;
              cache.query = $scope.query;
              cache.projectId = $scope.projectId;
              return institutes;
            }
          );
      };

      //Init the de and en filter of institutes
      var init = function(currentLanguage) {
        //Just a change? No Init!
        if (selectionChanging) {
          selectionChanging = false;
          return;
        }

        //Init the Filter
        initializing = true;
        if ($scope.currentSearchParams.filter &&
          $scope.currentSearchParams.filter['institution-' +
          currentLanguage]) {

          //Check with of both filter are active, depending on actual language
          currentFilterByLanguage =
            $scope.currentSearchParams.filter['institution-' +
            currentLanguage];

          //Search and validate institutions
          $scope.searchInstitutions(currentFilterByLanguage, currentLanguage)
            .then(function(institutions) {

              //Just one Institution exists
              if (institutions.length === 1) {
                $scope.currentInstitution = institutions[0];
                return;
              } else if (institutions.length > 1) {
                //Standard case, there are many institutions
                institutions.forEach(function(institution) {
                  if (institution[currentLanguage] ===
                    currentFilterByLanguage) {
                    $scope.currentInstitution = institution;
                    return;
                  }
                });
              }

              //Institution was not found check the language
              if (!$scope.currentInstitution) {
                $scope.currentInstitution =
                  $scope.currentSearchParams.filter['institution-' +
                  currentLanguage];
                $timeout(function() {
                  $scope.institutionFilterForm.institutionFilter
                    .$setValidity('md-require-match', false);
                }, 500);
                $scope.institutionFilterForm.institutionFilter
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
            $scope.currentSearchParams.filter['institution-' +
            i18nAnotherEnding]) {
            $scope.searchInstitutes(
              $scope.currentSearchParams.filter['institution-' +
              i18nAnotherEnding], i18nAnotherEnding)
              .then(function(institutions) {
                if (institutions) {
                  $scope.currentInstitution = institutions[0];

                  $scope.currentSearchParams.filter['institution-' +
                  i18nActualEnding] = institutions[0][i18nActualEnding];
                  $scope.selectedFilters.push('institution-' +
                    i18nActualEnding);
                  delete $scope.selectedFilters[_.indexOf(
                    $scope.selectedFilters,
                    'institution-' + i18nAnotherEnding)
                    ];
                  delete $scope.currentSearchParams.filter['institution-' +
                  i18nAnotherEnding];
                  return;
                }
              });
          }
          initializing = false;
        }
      };

      //Set the filter, if the user changed the institution Filter
      $scope.onSelectionChanged = function(institution) {
        if (initializing) {
          initializing = false;
          return;
        }
        selectionChanging = true;
        if (!$scope.currentSearchParams.filter) {
          $scope.currentSearchParams.filter = {};
        }

        //Set the institution filter in the URL
        if (institution) {
          $scope.currentSearchParams.filter['institution-' +
          $scope.currentLanguage] = institution[$scope.currentLanguage];
        } else {
          //No Institution is chosen, delete the Parameter in the URL
          delete $scope.currentSearchParams.filter['institution-' +
          $scope.currentLanguage];
        }
        $scope.institutionChangedCallback();
      };

      //Initialize and watch the current Institution Filter
      $scope.$watch('currentSearchParams.filter["institution-' +
        $scope.currentLanguage + '"]',
        function() {
          init($scope.currentLanguage);
        });
    }]);

