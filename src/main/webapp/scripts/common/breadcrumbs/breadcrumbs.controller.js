'use strict';

function CTRL(Principal) {
  var $ctrl = this;
  $ctrl.$onInit = init;

  function init() {
    Principal.identity().then(function() {
      $ctrl.isAuthenticated = Principal.isAuthenticated;
    });
  }
}

angular
  .module('metadatamanagementApp')
  .controller('BreadcrumbController', CTRL);
