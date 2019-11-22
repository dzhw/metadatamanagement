'use strict';

angular.module('metadatamanagementApp').directive('questionSearchResult',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'question-search-result.html.tmpl',
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '=',
        setParams: '&'
      },
      controller: function($scope, Principal) {
        $scope.isLoggedIn = Principal.loginName();
        $scope.setCSRParams = function() {
          $scope.setCurrentSearchParams();
        };
      }
    };
  });
