'use strict';

angular.module('metadatamanagementApp').directive('studySearchResult',
  function(CurrentProjectService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/searchmanagement/directives/' +
        'study-search-result.html.tmpl',
      link: function($scope) {
        $scope.currentProject = CurrentProjectService.getCurrentProject();
      },
      scope: {
        searchResult: '=',
        currentLanguage: '=',
        bowser: '=',
        searchResultIndex: '='
      }
    };
  });
