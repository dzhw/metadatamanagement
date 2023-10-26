(function() {
  'use strict';

  var DetailComponent = {
    controller: ['$scope', '$rootScope', function($scope, $rootScope) {
      $scope.bowser = $rootScope.bowser;
      $scope.expandLicenses = {};

      /**
       * Creates a mapping of the available license fields
       * to set if they are expanded or collapsed. This ensures they 
       * can be expanded seperately.
       * @param {*} index the index of the analysis data package
       */
      $scope.toggleLicenses = function(index) {
        if (index in $scope.expandLicenses) {
          $scope.expandLicenses[index] = !$scope.expandLicenses[index]
        } else {
          $scope.expandLicenses[index] = true
        }
      };

      /**
       * Handles the mapping of the keys of data sources
       * to add them as unique values to the expandLicenses map.
       * Custom data packages within an analysis package can have multiple
       * data sources of which each can have a license.
       * @param {*} index the index of the child element
       * @param {*} link the data source object
       */
      $scope.toggleLicensesDataSources = function(index, link) {
        // combine index and the English name of the data source into a unique key
        var key = index + "-" + link.name.en.replaceAll(" ", "");
        $scope.toggleLicenses(key); 
      }

      /**
       * Returns wether or not the license of a data source within
       * a data package should be displayed or not by looking up the key of the child
       * object within the expandLicenses map.
       * @param {*} index the index of the child element
       * @param {*} link the data source object
       * @returns true if the key within the map is set to true else false
       */
      $scope.showLicenseDataSource = function(index, link) {
        if (link === undefined) {
          return false;
        }
        var key = index + "-" + link.name.en.replaceAll(" ", "");
        return $scope.expandLicenses[key] != null && $scope.expandLicenses[key];
      }
    }],
    bindings: {
      lang: '<',
      options: '<',
      collapsed: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzDetail', DetailComponent);
})();
