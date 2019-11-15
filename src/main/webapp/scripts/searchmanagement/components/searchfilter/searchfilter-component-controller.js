/* global _ */

'use strict';

var CTRL = function($rootScope, $location, CleanJSObjectService) {
  var $ctrl = this;

  $ctrl.toggleFilterItem = toggleFilterItem;
  $ctrl.exists = exists;
  $ctrl.clearFilter = clearFilter;
  $ctrl.dataPacketFilter = {};
  $ctrl.searchFilterMapping = {};
  $ctrl.searchParams = {};
  $ctrl.options = {
    pageObject: {}
  };
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
  $ctrl.$onInit = init;

  function init() {
    readSearchParamsFromLocation();
    _.assign($ctrl.searchFilterMapping, $ctrl.searchParams.filter);
  }

  function clearFilter() {
    $ctrl.searchParams.filter = {};
    writeSearchParamsToLocation();
    $rootScope.$emit('onSearchFilterChange');
  }

  function toggleFilterItem(item, prop) {
    if ($ctrl.searchFilterMapping &&
      $ctrl.searchFilterMapping.hasOwnProperty(prop) &&
      $ctrl.searchFilterMapping[prop].includes(item)) {

      $ctrl.searchFilterMapping[prop] = _.without(
        $ctrl.searchFilterMapping[prop], item
      );
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
    $rootScope.$emit('onSearchFilterChange');
  }

  function exists(item, prop) {
    if ($ctrl.searchFilterMapping &&
      $ctrl.searchFilterMapping.hasOwnProperty(prop)) {
      return $ctrl.searchFilterMapping[prop].indexOf(item) > -1;}
  }

  // write the searchParams object to the location with the correct types
  function writeSearchParamsToLocation() {
    var locationSearch = {};
    locationSearch.page = '' + $ctrl.options.pageObject.page;
    locationSearch.size = '' + $ctrl.options.pageObject.size;
    if ($ctrl.searchParams.query && $ctrl.searchParams.query !== '') {
      locationSearch.query = $ctrl.searchParams.query;
    }
    if ($ctrl.searchParams.sortBy && $ctrl.searchParams.sortBy !== '') {
      locationSearch['sort-by'] = $ctrl.searchParams.sortBy;
    } else {
      delete locationSearch['sort-by'];
    }
    _.assign(locationSearch, $ctrl.searchParams.filter);

    $location.search(locationSearch);
  }

  // read the searchParams object from the location with the correct types
  function readSearchParamsFromLocation() {
    var locationSearch = $location.search();
    if (CleanJSObjectService.isNullOrEmpty(locationSearch)) {
      $ctrl.options.pageObject.page = 1;
      $ctrl.options.pageObject.size = 10;
      $ctrl.searchParams = {
        query: '',
        size: $ctrl.options.pageObject.size,
        selectedTabIndex: 0
      };
    } else {
      if (locationSearch.page != null) {
        $ctrl.options.pageObject.page = parseInt(locationSearch.page);
      } else {
        $ctrl.options.pageObject.page = 1;
      }
      if (locationSearch.size != null) {
        $ctrl.options.pageObject.size = parseInt(locationSearch.size);
        $ctrl.searchParams.size = $ctrl.options.pageObject.size;
      } else {
        $ctrl.options.pageObject.size = 10;
        $ctrl.searchParams.size = $ctrl.options.pageObject.size;
      }
      if (locationSearch.query) {
        $ctrl.searchParams.query = locationSearch.query;
      } else {
        $ctrl.searchParams.query = '';
      }
      $ctrl.searchParams.filter = _.omit(locationSearch, ['page', 'size',
        'type', 'query', 'sort-by'
      ]);
      $ctrl.searchParams.sortBy = locationSearch['sort-by'];
      var indexToSelect = _.findIndex($ctrl.tabs,
        function(tab) {
          return tab.elasticSearchType === locationSearch.type;
        });
      if (indexToSelect < 0) {
        $ctrl.searchParams.selectedTabIndex = 0;
      } else {
        $ctrl.searchParams.selectedTabIndex = indexToSelect;
      }
    }
  }

  $rootScope.$on('onDataPacketFilterChange',
    function(event, data) { // jshint ignore:line
    $ctrl.dataPacketFilter = data;
  });
};

angular
  .module('metadatamanagementApp')
  .controller('SearchFilterController', CTRL);
