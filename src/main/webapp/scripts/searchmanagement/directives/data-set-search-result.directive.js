'use strict';

angular.module('metadatamanagementApp').directive('datasetSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'data-set-search-result.html.tmpl',
      scope: {
        searchResult: '='
      },
      controller: ['$scope', 'DataSetReportService', 'Principal',
        function($scope, DataSetReportService, Principal) {
          //Check the login status
          $scope.isAuthenticated = Principal.isAuthenticated;

          $scope.uploadTexTemplate = function(file, dataSetId) {
            DataSetReportService.uploadTexTemplate(file, dataSetId);
          };
        }
      ],
    };
  });
