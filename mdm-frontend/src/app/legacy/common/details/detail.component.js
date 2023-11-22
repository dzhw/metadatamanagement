(function() {
  'use strict';

  var DetailComponent = {
    controller: ['$scope', '$rootScope', function($scope, $rootScope) {
      $scope.bowser = $rootScope.bowser;
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
