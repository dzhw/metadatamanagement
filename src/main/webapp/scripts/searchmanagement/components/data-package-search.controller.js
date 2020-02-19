/* global _ */

(function() {
  'use strict';

  function DataPackageSearchController(
    $scope,
    $location,
    $rootScope,
    MessageBus
  ) {
    var $ctrl = this;

    $ctrl.searchEvent = MessageBus;
    $ctrl.isSearching = false;
    $ctrl.$onInit = init;
    $ctrl.change = change;

    function init() {
      var searchObject = $location.search();

      $ctrl.query = searchObject.query;
      $rootScope.searchQuery = $ctrl.query;
    }

    // $location.search() returns an empty object when called in a
    // ng-change function. Therefore we use the queryString function to retrieve
    // the URL parameter
    function change() {
      var params = queryString($location.url());
      if ($ctrl.query === '' && params.hasOwnProperty('query')) {
        delete params.query;
      } else {
        _.assign(params, {query: $ctrl.query, page: 1});
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

    $scope.$watch(function() {
        return $ctrl.searchEvent;
      },
      function() {
        if ($ctrl.searchEvent.get('onStartSearch', true)) {
          $ctrl.isSearching = true;
        }
        if ($ctrl.searchEvent.get('onStopSearch', true)) {
          $ctrl.isSearching = false;
        }
      }, true);

    $scope.$watch(function() { return $location.search().query; },
      function(newVal, oldVal) {
        if (newVal !== oldVal) {
          $ctrl.query = $location.search().query;
          $rootScope.searchQuery = $ctrl.query;
        }
      });
  }

  angular
    .module('metadatamanagementApp')
    .controller('DataPackageSearchController', DataPackageSearchController);
})();
