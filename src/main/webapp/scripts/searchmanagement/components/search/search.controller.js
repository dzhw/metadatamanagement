/* global _ */

'use strict';

var CTRL = function($scope, $location, $rootScope) {
  var $ctrl = this;
  var registerScope1;
  var registerScope2;
  $ctrl.isSearching = false;
  $ctrl.$onInit = init;
  $ctrl.change = change;

  function init() {
    var searchObject = $location.search();
    $ctrl.query = searchObject.query;
  }

  // $location.search() return an empty object when called in a
  // ng-change function. Therefore we use the queryString function to retrieve
  // the URL parameter
  function change() {
    console.log('change');
    var params = queryString($location.url());
    if ($ctrl.query === '' && params.hasOwnProperty('query')) {
      delete params.query;
    } else {
      _.assign(params, {query: $ctrl.query});
    }
    $rootScope.searchQuery = $ctrl.query;
    $location.search(params);
  }
  function queryString(URL) {
    var queryString = {};
    var usefulParam = URL.split('?')[1] || '';
    var query = usefulParam || '';
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
      var pair = vars[i].split('=');
      if (typeof queryString[pair[0]] === 'undefined') {
        queryString[pair[0]] = decodeURIComponent(pair[1]);
      } else if (typeof queryString[pair[0]] === 'string') {
        var arr = [queryString[pair[0]], decodeURIComponent(pair[1])];
        queryString[pair[0]] = arr;
      } else {
        queryString[pair[0]].push(decodeURIComponent(pair[1]));
      }
    }

    return queryString;
  }
  registerScope1 = $rootScope.$on('onStartSearch',
    function(event) { // jshint ignore:line
    $ctrl.isSearching = true;
  });
  registerScope2 = $rootScope.$on('onStopSearch',
    function(event) { // jshint ignore:line
    $ctrl.isSearching = false;
  });

  $scope.$onDestroy = function() {
    //unregister rootScope event by calling the return function
    registerScope1();
    registerScope2();
  };
};

angular
  .module('metadatamanagementApp')
  .controller('DataPacketSearchController', CTRL);
