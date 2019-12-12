(function() {
  'use strict';

  function BreadcrumbController(Principal, $state, $scope) {
    var $ctrl = this;
    $ctrl.$onInit = init;
    $ctrl.show = false;

    function init() {
      Principal.identity().then(function() {
        $ctrl.isAuthenticated = Principal.isAuthenticated;
      });
    }

    $scope.$watch(function() {
      return $state.current.name;
    }, function() {
      $ctrl.show = $state.current.name !== 'start';
    }, true);
  }

  angular
    .module('metadatamanagementApp')
    .controller('BreadcrumbController', BreadcrumbController);
})();
