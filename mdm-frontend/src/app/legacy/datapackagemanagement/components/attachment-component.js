(function() {
  'use strict';

  var Component = {
    controller: ['$scope', '$rootScope', function($scope, $rootScope) {
      $scope.bowser = $rootScope.bowser;
    }],
    bindings: {
      collapsed: '<',
      headline: '<',
      options: '<',
      lang: '<'
    },
    templateUrl: ['$attrs', function($attrs) {
      return $attrs.templateUrl;
    }]
  };

  angular
    .module('metadatamanagementApp')
    .component('fdzAttachment', Component);

})();
