/* global _ */
(function() {

  'use strict';

  function RelatedObjectsController(
    $scope, $location, SearchDao, $timeout,
    SearchResultNavigatorService, CleanJSObjectService, $analytics,
    SearchHelperService, $transitions, $rootScope, PageMetadataService
  ) {
    var $ctrl = this;
    $scope.bowser = $rootScope.bowser;
    $ctrl.computeSearchResultIndex = computeSearchResultIndex;
    $ctrl.onPageChanged = onPageChanged;
    $ctrl.onSelectedTabChanged = onSelectedTabChanged;
    $ctrl.setCurrentSearchParams = setCurrentSearchParams;
    $ctrl.search = search;
    $ctrl.$onInit = init;
    $ctrl.dataPackageFilter = {};

    $ctrl.options = {};
    $ctrl.searchResult = {};
    $ctrl.placeHolder = '';
    $ctrl.disabled = false;

    var queryChangedOnInit = false;
    var tabChangedOnInitFlag = false;
    var locationChanged = false;
    var selectedTabChangeIsBeingHandled = false;
    var queryChangeIsBeingHandled = false;
    var tabs = [{
      title: 'search-management.tabs.data_packages',
      inputLabel: 'search-management.input-label.data-packages',
      elasticSearchType: 'data_packages',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.data-packages',
      group: 'dataPackages',
      sortOptions: ['relevance', 'alphabetically', 'survey-period',
        'first-release-date', 'last-release-date']
    }, {
      title: 'search-management.tabs.analysis_packages',
      inputLabel: 'search-management.input-label.analysis-packages',
      elasticSearchType: 'analysis_packages',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.analysis-packages',
      group: 'analysisPackages',
      sortOptions: ['relevance', 'alphabetically', 'first-release-date',
        'last-release-date']
    }, {
      title: 'search-management.tabs.surveys',
      inputLabel: 'search-management.input-label.surveys',
      elasticSearchType: 'surveys',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.surveys',
      group: 'surveys',
      sortOptions: ['relevance', 'alphabetically']
    }, {
      title: 'search-management.tabs.instruments',
      inputLabel: 'search-management.input-label.instruments',
      elasticSearchType: 'instruments',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.instruments',
      group: 'instruments',
      sortOptions: ['relevance', 'alphabetically']
    }, {
      title: 'search-management.tabs.questions',
      inputLabel: 'search-management.input-label.questions',
      elasticSearchType: 'questions',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.questions',
      group: 'questions',
      sortOptions: ['relevance', 'alphabetically']
    }, {
      title: 'search-management.tabs.data_sets',
      inputLabel: 'search-management.input-label.data-sets',
      elasticSearchType: 'data_sets',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.data-sets',
      group: 'dataSets',
      sortOptions: ['relevance', 'alphabetically']
    }, {
      title: 'search-management.tabs.variables',
      inputLabel: 'search-management.input-label.variables',
      elasticSearchType: 'variables',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.variables',
      group: 'variables',
      sortOptions: ['relevance', 'alphabetically']
    }, {
      title: 'search-management.tabs.related_publications',
      inputLabel: 'search-management.input-label.related-publications',
      elasticSearchType: 'related_publications',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.related-publications',
      group: 'publications',
      sortOptions: ['relevance', 'alphabetically']
    }, {
      title: 'search-management.tabs.concepts',
      inputLabel: 'search-management.input-label.concepts',
      elasticSearchType: 'concepts',
      count: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.concepts',
      group: 'concepts',
      sortOptions: ['relevance', 'alphabetically']
    }];

    function init() {
      tabChangedOnInitFlag = true;
      queryChangedOnInit = true;
      $ctrl.tabs = tabsFilter($ctrl.options);

      // fdz-paginator options object
      $ctrl.options = {
        sortObject: {
          selected: null,
          options: null
        },
        pageObject: {
          options: [10, 20, 50],
          totalHits: 0,
          size: 10,
          page: 1
        }
      };
      $ctrl.searchParams = {
        query: '',
        size: $ctrl.options.pageObject.size,
        selectedTabIndex: 0,
        filter: {}
      };

      readSearchParamsFromLocation();
      writeSearchParamsToLocation();
      $ctrl.placeHolder = $ctrl.tabs[$ctrl.searchParams.selectedTabIndex].title;
      $ctrl.search();
    }

    function getCurrentObjectFilter() {
      var objMapper = {
        'data-packages': 'data-package',
        'analysis-packages': 'analysis-package',
        'studies': 'data-package',
        'surveys': 'survey',
        'instruments': 'instrument',
        'questions': 'question',
        'data-sets': 'data-set',
        'variables': 'variable',
        'publications': 'related-publication',
        'concepts': 'concept'
      };
      var obj = {};
      var urlPath = _.takeRight($location.path().split('/'), 2);

      if (objMapper.hasOwnProperty(urlPath[0])) {
        obj[objMapper[urlPath[0]]] = $ctrl.id;
      }
      return obj;
    }

    function tabsFilter(data) {
      var relatedObjects = [];
      tabs.forEach(function(tab, index) {
        var prop = tab.group + 'Count';
        if (data.hasOwnProperty(prop)) {
          // tabs[index].count = data[prop];
          relatedObjects.push(tabs[index]);
        }
      });
      return relatedObjects;
    }

    function getSelectedMetadataType() {
      return $ctrl.tabs[$ctrl.searchParams.selectedTabIndex]
        .elasticSearchType;
    }

    // write the searchParams object to the location with the correct types
    function writeSearchParamsToLocation() {
      var locationSearch = {};
      locationSearch.page = '' + $ctrl.options.pageObject.page;
      locationSearch.size = '' + $ctrl.options.pageObject.size;
      locationSearch.type = $ctrl.tabs[
        $ctrl.searchParams.selectedTabIndex].elasticSearchType;
      if ($ctrl.searchParams.query && $ctrl.searchParams.query !== '') {
        locationSearch.query = $ctrl.searchParams.query;
      }
      if ($ctrl.searchParams['access-way'] &&
        $ctrl.searchParams['access-way'] !== '') {
        locationSearch['access-way'] = $ctrl.searchParams['access-way'];
      }
      if ($ctrl.searchParams.version && $ctrl.searchParams.version !== '') {
        locationSearch.version = $ctrl.searchParams.version;
      }
      if ($ctrl.searchParams.sortBy && $ctrl.searchParams.sortBy !== '') {
        locationSearch['sort-by'] = $ctrl.searchParams.sortBy;
      } else {
        delete locationSearch['sort-by'];
      }
      _.assign(locationSearch, _.omit($ctrl.searchParams.filter,
        'data-package', 'analysis-package', 'survey',
        'concept', 'data-set', 'instrument',
        'question', 'related-publication', 'variable'));
      locationChanged = !angular.equals($location.search(),
        locationSearch);
      $location.search(locationSearch).replace();
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
        if (locationSearch['access-way']) {
          $ctrl.searchParams['access-way'] = locationSearch['access-way'];
        }
        if (locationSearch.version) {
          $ctrl.searchParams.version = locationSearch.version;
        } else {
          $ctrl.searchParams.version = '';
        }
        $ctrl.searchParams.filter = _.omit(locationSearch, ['page', 'size',
          'type', 'query', 'sort-by', 'version'
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

    // Paginator method
    function computeSearchResultIndex($index) {
      return $index + 1 +
        (($ctrl.options.pageObject.page - 1) * $ctrl.options.pageObject.size);
    }

    function setCurrentSearchParams() {
      SearchResultNavigatorService.setCurrentSearchParams(
        $ctrl.searchParams, null,
        getSelectedMetadataType(),
        $ctrl.options.pageObject);
    }

    //Search function
    function search() {
      $ctrl.isSearching++;
      if (!$ctrl.searchParams.filter) {
        $ctrl.searchParams.filter = {};
      }
      var filter = getCurrentObjectFilter();
      _.assign($ctrl.searchParams.filter, filter);
      SearchDao.search($ctrl.searchParams.query,
        $ctrl.options.pageObject.page, null, $ctrl.searchParams.filter,
        getSelectedMetadataType(),
        $ctrl.options.pageObject.size, $ctrl.searchParams.sortBy)
        .then(function(data) {
          $ctrl.searchResult = data.hits.hits;
          $ctrl.options.pageObject.totalHits = data.hits.total.value;
          $analytics.trackSiteSearch(
            $ctrl.searchParams.query ? $ctrl.searchParams.query : '<null>',
            $ctrl.tabs[
                $ctrl.searchParams.selectedTabIndex].elasticSearchType ?
            $ctrl.tabs[
                $ctrl.searchParams.selectedTabIndex].elasticSearchType : 'all',
            $ctrl.options.pageObject.totalHits);
          $ctrl.tabs.forEach(function(tab) {
            if ($ctrl.tabs[
                $ctrl.searchParams.selectedTabIndex].elasticSearchType ===
              undefined) {
              tab.count = 0;
              data.aggregations.countByType.buckets.forEach(
                function(bucket) {
                  if (bucket.key === tab.elasticSearchType) {
                    // jscs:disable
                    tab.count = bucket.doc_count;
                    // jscs:enable
                  }
                });
            } else {
              tab.count = null;
            }
          });
          $ctrl.tabs[$ctrl.searchParams.selectedTabIndex].count =
            data.hits.total.value;
          $ctrl.isSearching--;

          // Safari fix
          $timeout(function() {
            angular.element('body')
              .append('<div id=fdz-safari-fix></div>');
            $timeout(function() {
              angular.element('#fdz-safari-fix').remove();
            });
          }, 100);
        }, function() {
          $ctrl.options.pageObject.totalHits = 0;
          $ctrl.searchResult = {};
          $ctrl.tabs.forEach(function(tab) {
            tab.count = null;
          });
          $ctrl.tabs[$ctrl.searchParams.selectedTabIndex].count = 0;
          $ctrl.isSearching--;
        });
    }

    function onSelectedTabChanged() {
      $ctrl.options.sortObject.options = $ctrl.tabs[
        $ctrl.searchParams.selectedTabIndex].sortOptions;
      $ctrl.options.sortObject.selected = 'relevance';
      var nextTabIndex = ($ctrl.searchParams.selectedTabIndex + 1) %
        $ctrl.tabs.length;
      var previousTabIndex = ($ctrl.tabs.length +
        $ctrl.searchParams.selectedTabIndex - 1) % $ctrl.tabs.length;
      PageMetadataService.setPreviousLink($ctrl.tabs[
        previousTabIndex].elasticSearchType);
      PageMetadataService.setNextLink($ctrl.tabs[
        nextTabIndex].elasticSearchType);
      if (!selectedTabChangeIsBeingHandled && !queryChangeIsBeingHandled) {
        //prevent multiple tab change handlers caused by logout
        selectedTabChangeIsBeingHandled = true;
        $timeout(function() {
          if (!tabChangedOnInitFlag) {
            $ctrl.placeHolder = $ctrl.tabs[$ctrl.searchParams.selectedTabIndex]
              .title;
            $ctrl.searchParams.filter = SearchHelperService
              .removeIrrelevantFilters(
                $ctrl.tabs[$ctrl.searchParams.selectedTabIndex]
                  .elasticSearchType,
                $ctrl.searchParams.filter);
            $ctrl.searchParams.sortBy = undefined;
            $ctrl.options.pageObject.page = 1;
            // $ctrl.searchParams.filter.version = version;
            writeSearchParamsToLocation();
            $ctrl.search();
          }
          tabChangedOnInitFlag = false;
          selectedTabChangeIsBeingHandled = false;
        });
      }
    }

    function onPageChanged() {
      writeSearchParamsToLocation();
      $ctrl.search();
    }

    $scope.$watch('$ctrl.searchParams.query', function(newVal, oldVal) {

      if (queryChangedOnInit) {
        queryChangedOnInit = false;
        return;
      }
      if (selectedTabChangeIsBeingHandled) {
        return;
      }
      queryChangeIsBeingHandled = true;
      $timeout(function() {
        $ctrl.options.pageObject.page = 1;
        delete $ctrl.searchParams.sortBy;
        if (newVal !== oldVal) {
          writeSearchParamsToLocation();
          $ctrl.search();
          queryChangeIsBeingHandled = false;
        }
      });
    });

    // watch for location changes not triggered by our code
    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue, oldValue) {
      if (newValue !== oldValue && !locationChanged) {
        readSearchParamsFromLocation();
        // type changes are already handled by $scope.onSelectedTabChanged
        if (newValue.type === oldValue.type) {
          $ctrl.search();
        }
      } else {
        locationChanged = false;
      }
    });

    var unregisterTransitionHook = $transitions.onStart({}, function(trans) {
      $ctrl.disabled = trans.$to().name === 'relatedPublicationDetail' ||
        trans.$to().name === 'conceptDetail';
    });

    $scope.$on('$destroy', unregisterTransitionHook);
  }

  angular
    .module('metadatamanagementApp')
    .controller('RelatedObjectsController', RelatedObjectsController);

})();
