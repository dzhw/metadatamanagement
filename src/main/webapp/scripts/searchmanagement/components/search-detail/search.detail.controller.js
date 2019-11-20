'use strict';

function Controller($rootScope, $state, LanguageService, $location) {
  var $ctrl = this;
  var registerScope = null;
  $ctrl.query = '';
  $ctrl.submit = submit;
  $ctrl.$onInit = init;

  function init() {
    if ($rootScope.searchQuery) {
      $ctrl.query = $rootScope.searchQuery;
    }
  }

  function submit() {
    var searchObject = $location.search();
    var paramsObject = {
      lang: LanguageService.getCurrentInstantly(),
      query: $ctrl.query
    };
    if (searchObject.hasOwnProperty('size')) {
      paramsObject.size = parseInt(searchObject.size);
    }
    $state.go('search', paramsObject);
  }

  registerScope = $rootScope.$watch(function() {
    return $rootScope.searchQuery;
  }, function() {
    $ctrl.query = $rootScope.searchQuery;
  });

  $ctrl.$onDestroy = function() {
    //unregister rootScope event by calling the return function
    registerScope();
  };
}
angular
  .module('metadatamanagementApp')
  .controller('searchDetailController', Controller);
