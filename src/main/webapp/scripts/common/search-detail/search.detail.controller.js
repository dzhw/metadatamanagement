'use strict';

function Controller($scope, $state, LanguageService, $location) {
  var $ctrl = this;
  $ctrl.query = '';

  $scope.$watch('$ctrl.query', function(newValue, oldValue) {
    if (newValue !== oldValue) {
      var searchObject = $location.search();

      $state.go('search', {
        lang: LanguageService.getCurrentInstantly(),
        query: $ctrl.query
        // ,
        // size: parseInt(searchObject.pageObject.size)
      });
    }
  });
}
angular
  .module('metadatamanagementApp')
  .controller('searchDetailController', Controller);
