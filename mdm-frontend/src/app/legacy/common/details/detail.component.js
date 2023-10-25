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
