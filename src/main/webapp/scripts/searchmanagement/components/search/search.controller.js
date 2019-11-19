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
    var searchObject = $location.search();
    var paramsObject = {
      query: $ctrl.query
    };
    _.assign(searchObject, paramsObject);
    $rootScope.searchQuery = $ctrl.query;
    $location.search(paramsObject);
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
