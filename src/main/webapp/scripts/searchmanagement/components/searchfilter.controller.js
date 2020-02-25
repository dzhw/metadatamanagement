/* global _ */
(function() {
  'use strict';

  function SearchFilterController($scope, $location, MessageBus) {
    var $ctrl = this;
    $ctrl.onDataPackageFilterChange = MessageBus;
    $ctrl.dataPackageFilter = {};
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
      $ctrl.searchParams.filter = {};
      $ctrl.searchFilterMapping = {};
      readSearchParamsFromLocation();
      checkCollapsible();
      _.assign($ctrl.searchFilterMapping, $ctrl.searchParams.filter);
    }

    function clearFilter() {
      readSearchParamsFromLocation();
      $ctrl.searchParams.page = 1;
      $ctrl.searchParams.filter = {};
      $ctrl.searchFilterMapping = {};
      writeSearchParamsToLocation();
    }

    function checkCollapsible() {
      var props = Object.keys($ctrl.searchParams.filter);
      $ctrl.filterValues.forEach(function(item) {
        if (_.includes(props, item.property)) {
          item.collapsed = false;
        }
      });
    }

    function toggleFilterItem(item, prop) {
      readSearchParamsFromLocation();
      if ($ctrl.searchFilterMapping &&
        $ctrl.searchFilterMapping.hasOwnProperty(prop) &&
        _.includes($ctrl.searchFilterMapping[prop], item)) {
        $ctrl.searchFilterMapping[prop] = _.without(
          $ctrl.searchFilterMapping[prop], item
        );
        if ($ctrl.searchFilterMapping[prop].length === 0) {
          delete $ctrl.searchFilterMapping[prop];
        }
      } else {
        if ($ctrl.searchFilterMapping &&
          $ctrl.searchFilterMapping.hasOwnProperty(prop)) {
          if (!angular.isArray($ctrl.searchFilterMapping[prop])) {
            var tmp = $ctrl.searchFilterMapping[prop];
            $ctrl.searchFilterMapping[prop] = [];
            $ctrl.searchFilterMapping[prop].push(tmp);
          }
          $ctrl.searchFilterMapping[prop].push(item);
        } else {
          $ctrl.searchFilterMapping[prop] = [];
          $ctrl.searchFilterMapping[prop].push(item);
        }
      }
      $ctrl.searchParams.filter = $ctrl.searchFilterMapping;
      $ctrl.searchParams.page = 1;
      writeSearchParamsToLocation();
    }

    function exists(item, prop) {
      if ($ctrl.searchFilterMapping &&
        $ctrl.searchFilterMapping.hasOwnProperty(prop)) {
        return $ctrl.searchFilterMapping[prop].indexOf(item) > -1;
      }
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
      MessageBus.set('searchFilter', $ctrl.searchParams.filter);
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

    $scope.$watch(function() {
        return $ctrl.onDataPackageFilterChange;
      },
      function() {
        if ($ctrl.onDataPackageFilterChange.get('onDataPackageFilterChange')) {
          $ctrl.dataPackageFilter = $ctrl.onDataPackageFilterChange
            .get('onDataPackageFilterChange', true);
        }
        if ($ctrl.onDataPackageFilterChange.get('searchInit', true)) {
          init();
        }
      }, true);

    $scope.$watch(function() {
        return $location.search();
      },
      function() {
        $ctrl.searchParams.filter = _.omit($location.search(), ['page', 'size',
        'type', 'query', 'sort-by'
        ]);
        init();
      });
  }

  angular
    .module('metadatamanagementApp')
    .controller('SearchFilterController', SearchFilterController);
})();
