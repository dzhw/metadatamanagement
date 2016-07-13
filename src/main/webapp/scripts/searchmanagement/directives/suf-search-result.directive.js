'use strict';

angular.module('metadatamanagementApp').directive('sufSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'suf-search-result.html.tmpl',
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
