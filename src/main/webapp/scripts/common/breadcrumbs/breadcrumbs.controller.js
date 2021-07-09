(function() {
  'use strict';

  function BreadcrumbController(Principal, $state, $scope, $rootScope) {
    var $ctrl = this;
    $ctrl.show = false;
    $scope.bowser = $rootScope.bowser;
    $ctrl.isAuthenticated = Principal.isAuthenticated;

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
