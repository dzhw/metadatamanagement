'use strict';

angular.module('metadatamanagementApp').controller('SearchController',
    function($rootScope, $scope, $location, $window, client, BookmarkableUrl,
        VariablesearchQuerybuilder) {
      $scope.$on('$locationChangeSuccess', function() {
        $scope.query = $location.search().query;
        $scope.search();
      });
      $scope.initsearch = function() {
        if ($location.search().query) {
          $scope.query = $location.search().query;
        }
        $scope.search();
      };

      $scope.search = function() {
        BookmarkableUrl.setUrlLanguage($location, $rootScope);
        $location.search('query', $scope.query);
        client.search(VariablesearchQuerybuilder.Query($scope.query)).then(
          function(response) { $scope.searchResult = response.hits.hits;
          }).catch(function(error) { $scope.error = error; });
      };
    });
