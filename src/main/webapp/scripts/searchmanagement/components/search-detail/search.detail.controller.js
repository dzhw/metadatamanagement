'use strict';

function Controller($state, LanguageService, $location) {
  var $ctrl = this;
  $ctrl.query = '';
  $ctrl.submit = submit;

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
}
angular
  .module('metadatamanagementApp')
  .controller('searchDetailController', Controller);
