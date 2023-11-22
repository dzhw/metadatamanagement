(function() {
  'use strict';

  function BreadcrumbController(Principal, $state, $scope, $rootScope) {
    var $ctrl = this;
    $ctrl.$onInit = init;
    $ctrl.show = false;
    $scope.bowser = $rootScope.bowser;

    function init() {
      Principal.identity().then(function() {
        $ctrl.isAuthenticated = Principal.isAuthenticated;
        $ctrl.isProviderViewActive = Principal.isProviderActive;
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
    .controller('BreadcrumbController', [
      'Principal',
      '$state',
      '$scope',
      '$rootScope',
      BreadcrumbController
    ]);
})();
