/* global _ */

'use strict';

var CTRL = function($location, $rootScope) {
  var $ctrl = this;
  $ctrl.change = change;
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
};

angular
  .module('metadatamanagementApp')
  .controller('DataPacketSearchController', CTRL);
