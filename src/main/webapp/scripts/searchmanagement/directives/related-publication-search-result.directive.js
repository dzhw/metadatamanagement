'use strict';

angular.module('metadatamanagementApp').directive('studySearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'study-search-result.html.tmpl',
      scope: {
        searchResult: '='
      },
      controller: ['$scope', 'Principal',
        function($scope, Principal) {
          //Check the login status
          $scope.isAuthenticated = Principal.isAuthenticated;
        }
      ],
    };
  });
