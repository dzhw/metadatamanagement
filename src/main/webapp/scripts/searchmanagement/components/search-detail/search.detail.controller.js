(function() {
  'use strict';

  angular
    .module('metadatamanagementApp')
    .controller('searchDetailController', searchDetailController);

  function searchDetailController(
    $rootScope, $state, LanguageService, $location
  ) {
    var $ctrl = this; // jshint ignore:line
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
})();
