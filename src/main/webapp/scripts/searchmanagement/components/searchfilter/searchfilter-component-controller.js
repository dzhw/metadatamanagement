/* global _ */

'use strict';

var CTRL = function($rootScope, $location) {
  var $ctrl = this;
  var registerScope = null;
  $ctrl.dataPacketFilter = {};
  $ctrl.searchFilterMapping = {};
  $ctrl.searchParams = {};
  $ctrl.filterValues = [
    {
      name: 'global.filter.survey-data-types',
      property: 'survey-data-types',
      collapsed: false
    },
    {
      name: 'global.filter.study-series',
      property: 'study-series',
      collapsed: false
    },
    {
      name: 'global.filter.tags',
      property: 'tags', collapsed: true
    },
    {
      name: 'global.filter.sponsors',
      property: 'sponsor', collapsed: true
    },
    {
      name: 'global.filter.institutions',
      property: 'institutions', collapsed: true
    }
  ];
  $ctrl.toggleFilterItem = toggleFilterItem;
  $ctrl.exists = exists;
  $ctrl.clearFilter = clearFilter;
  $ctrl.$onInit = init;

  function init() {
    readSearchParamsFromLocation();
    _.assign($ctrl.searchFilterMapping, $ctrl.searchParams.filter);
  }

  function clearFilter() {
    readSearchParamsFromLocation();
    $ctrl.searchParams.filter = {};
    $ctrl.searchFilterMapping = {};
    writeSearchParamsToLocation();
    $rootScope.$emit('onSearchFilterChange');
  }

  function toggleFilterItem(item, prop) {
    readSearchParamsFromLocation();
    if ($ctrl.searchFilterMapping &&
      $ctrl.searchFilterMapping.hasOwnProperty(prop) &&
      $ctrl.searchFilterMapping[prop].includes(item)) {
      $ctrl.searchFilterMapping[prop] = _.without(
        $ctrl.searchFilterMapping[prop], item
      );
      if ($ctrl.searchFilterMapping[prop].length === 0) {
        delete $ctrl.searchFilterMapping[prop];
      }
    } else {
      if ($ctrl.searchFilterMapping &&
        $ctrl.searchFilterMapping.hasOwnProperty(prop)) {
        $ctrl.searchFilterMapping[prop].push(item);
      } else {
        $ctrl.searchFilterMapping[prop] = [];
        $ctrl.searchFilterMapping[prop].push(item);
      }
    }
    $ctrl.searchParams.filter = $ctrl.searchFilterMapping;
    writeSearchParamsToLocation();
    // $rootScope.$emit('onSearchFilterChange');
  }

  function exists(item, prop) {
    if ($ctrl.searchFilterMapping &&
      $ctrl.searchFilterMapping.hasOwnProperty(prop)) {
      return $ctrl.searchFilterMapping[prop].indexOf(item) > -1;}
  }

  // write the searchParams object to the location with the correct types
  function writeSearchParamsToLocation() {
    var locationSearch = {};
    locationSearch.page = '' + $ctrl.searchParams.page;
    locationSearch.size = '' + $ctrl.searchParams.size;
    if ($ctrl.searchParams.query) {
      locationSearch.query = $ctrl.searchParams.query;
    }
    if ($ctrl.searchParams.type) {
      locationSearch.type = $ctrl.searchParams.type;
    }

    if ($ctrl.searchParams.sortBy) {
      locationSearch['sort-by'] = $ctrl.searchParams.sortBy;
    }
    _.assign(locationSearch, $ctrl.searchParams.filter);

    $location.search(locationSearch);
  }

  // read the searchParams object from the location with the correct types
  function readSearchParamsFromLocation() {
    $ctrl.searchParams = {};
    var locationSearch = $location.search();
    if (locationSearch.page != null) {
      $ctrl.searchParams.page = parseInt(locationSearch.page);
    }
    if (locationSearch.size != null) {
      $ctrl.searchParams.size = parseInt(locationSearch.size);
    }
    if (locationSearch.query) {
      if (locationSearch.query !== '') {
        $ctrl.searchParams.query = locationSearch.query;
      }
      if ($ctrl.searchParams.query === '') {
        delete $ctrl.searchParams.query;
      }
    }
    if (locationSearch.type) {
      $ctrl.searchParams.type = locationSearch.type;
    }
    if (locationSearch.selectedTabIndex) {
      $ctrl.searchParams.selectedTabIndex = locationSearch.selectedTabIndex;
    }
    $ctrl.searchParams.filter = _.omit(locationSearch, ['page', 'size',
      'type', 'query', 'sort-by'
    ]);
  }

  registerScope = $rootScope.$on('onDataPacketFilterChange',
    function(event, data) { // jshint ignore:line
      $ctrl.dataPacketFilter = data;
    });

  $ctrl.$onDestroy = function() {
    //unregister rootScope event by calling the return function
    registerScope();
  };
};

angular
  .module('metadatamanagementApp')
  .controller('SearchFilterController', CTRL);
