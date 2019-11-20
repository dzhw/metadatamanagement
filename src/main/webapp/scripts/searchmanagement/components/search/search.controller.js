/* global _ */

'use strict';

var CTRL = function($location, $rootScope) {
  var $ctrl = this;
  $ctrl.change = change;
  $ctrl.isSearching = false;
  $ctrl.$onInit = init;

  function init() {
    var searchObject = $location.search();
    $ctrl.query = searchObject.query;
  }

  function change() {
    console.log('change');
    var searchObject = $location.search();
    if ($ctrl.query === '' && searchObject.hasOwnProperty('query')) {
      delete searchObject.query;
    } else {
      var paramsObject = {
        query: $ctrl.query
      };
      _.assign(searchObject, paramsObject);
      $rootScope.searchQuery = $ctrl.query;
      $location.search(paramsObject);
    }
  }
  $rootScope.$on('onStartSearch', function(event) { // jshint ignore:line
    $ctrl.isSearching = true;
  });
  $rootScope.$on('onStopSearch', function(event) { // jshint ignore:line
    $ctrl.isSearching = false;
  });
};

angular
  .module('metadatamanagementApp')
  .controller('DataPacketSearchController', CTRL);
