/* global _ */

'use strict';

function Controller(
    $scope, $location, SearchDao, $timeout,
    SearchResultNavigatorService, CleanJSObjectService,
    SearchHelperService
) {
  var $ctrl = this;
  $ctrl.computeSearchResultIndex = computeSearchResultIndex;
  $ctrl.onPageChanged = onPageChanged;
  $ctrl.onSelectedTabChanged = onSelectedTabChanged;
  $ctrl.search = search;
  $ctrl.$onInit = init;
  $ctrl.dataPacketFilter = {};

  $ctrl.options = {};
  $ctrl.searchResult = {};
  $ctrl.placeHolder = '';

  var queryChangedOnInit = false;
  var tabChangedOnInitFlag = false;
  var locationChanged = false;
  var selectedTabChangeIsBeingHandled = false;
  var queryChangeIsBeingHandled = false;
  var tabs = [{
    title: 'search-management.tabs.studies',
    inputLabel: 'search-management.input-label.studies',
    icon: 'assets/images/icons/study.svg',
    elasticSearchType: 'studies',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.studies',
    group: 'studies'
  }, {
    title: 'search-management.tabs.surveys',
    inputLabel: 'search-management.input-label.surveys',
    icon: 'assets/images/icons/survey.svg',
    elasticSearchType: 'surveys',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.surveys',
    group: 'surveys'
  }, {
    title: 'search-management.tabs.instruments',
    inputLabel: 'search-management.input-label.instruments',
    icon: 'assets/images/icons/instrument.svg',
    elasticSearchType: 'instruments',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.instruments',
    group: 'instruments'
  }, {
    title: 'search-management.tabs.questions',
    inputLabel: 'search-management.input-label.questions',
    icon: 'assets/images/icons/question.svg',
    elasticSearchType: 'questions',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.questions',
    group: 'questions'
  }, {
    title: 'search-management.tabs.data_sets',
    inputLabel: 'search-management.input-label.data-sets',
    icon: 'assets/images/icons/data-set.svg',
    elasticSearchType: 'data_sets',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.data-sets',
    group: 'dataSets'
  }, {
    title: 'search-management.tabs.variables',
    inputLabel: 'search-management.input-label.variables',
    icon: 'assets/images/icons/variable.svg',
    elasticSearchType: 'variables',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.variables',
    group: 'variables'
  }, {
    title: 'search-management.tabs.related_publications',
    inputLabel: 'search-management.input-label.related-publications',
    icon: 'assets/images/icons/related-publication.svg',
    elasticSearchType: 'related_publications',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.related-publications',
    group: 'publications'
  }, {
    title: 'search-management.tabs.concepts',
    inputLabel: 'search-management.input-label.concepts',
    icon: 'assets/images/icons/concept.svg',
    elasticSearchType: 'concepts',
    count: null,
    uploadFunction: null,
    disabled: false,
    visibleForPublicUser: true,
    noResultsText: 'search-management.no-results-text.concepts',
    group: 'concepts'
  }];

  function init() {
    tabChangedOnInitFlag = true;
    queryChangedOnInit = true;
    $ctrl.tabs = tabsFilter($ctrl.options);

    // fdz-paginator options object
    $ctrl.options = {
      sortObject: {
        selected: 'relevance',
        options: ['relevance']
      },
      pageObject: {
        options: [10, 20, 50],
        totalHits: 0,
        size: 10,
        page: 1
      }
    };
    $ctrl.placeHolder = $ctrl.tabs[0].title;
    $ctrl.searchParams = {
      query: '',
      size: $ctrl.options.pageObject.size,
      selectedTabIndex: 0
    };
    readSearchParamsFromLocation();
    writeSearchParamsToLocation();
    $ctrl.search();
  }

  // TODO: Fix this ugly function
  // The use of a temp object is a few lines less than using the delete command
  // to delete the unnecessary object properties.
  function getCurrentObjectFilter() {
    var obj = {};
    var urlPath = _.takeRight($location.path().split('/'), 2);
    switch (urlPath[0]) {
      case 'studies':
        obj.study = $ctrl.id;
        break;
      case 'surveys':
        obj.survey = $ctrl.id;
        break;
      case 'instruments':
        obj.instrument = $ctrl.id;
        break;
      case 'questions':
        obj.question = $ctrl.id;
        break;
      case 'data-sets':
        obj['data-set'] = $ctrl.id;
        break;
      case 'variables':
        obj.variable = $ctrl.id;
        break;
      case 'publications':
        obj['related-publication'] = $ctrl.id;
        break;
      case 'concepts':
        obj.concept = $ctrl.id;
        break;
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
    try {
      locationSearch.type = $ctrl.tabs[
        $ctrl.searchParams.selectedTabIndex].elasticSearchType;
    } catch (e) {
      $ctrl.searchParams.selectedTabIndex = 0;
      locationSearch.type = $ctrl.tabs[
        $ctrl.searchParams.selectedTabIndex].elasticSearchType;
    }
    if ($ctrl.searchParams.query && $ctrl.searchParams.query !== '') {
      locationSearch.query = $ctrl.searchParams.query;
    }
    if ($ctrl.searchParams.sortBy && $ctrl.searchParams.sortBy !== '') {
      locationSearch['sort-by'] = $ctrl.searchParams.sortBy;
    } else {
      delete locationSearch['sort-by'];
    }
    _.assign(locationSearch, $ctrl.searchParams.filter);
    locationChanged = !angular.equals($location.search(),
      locationSearch);
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

  // Paginator method
  function computeSearchResultIndex($index) {
    return $index + 1 +
      (($ctrl.options.pageObject.page - 1) * $ctrl.options.pageObject.size);
  }

  //Search function
  function search() {
    var projectId = _.get($scope, 'currentProject.id');
    $ctrl.isSearching++;
    console.log($ctrl.searchParams);
    SearchResultNavigatorService.setCurrentSearchParams(
      $ctrl.searchParams, projectId,
      getSelectedMetadataType(),
      $ctrl.options.pageObject);
    var filter = getCurrentObjectFilter();
    SearchDao.search($ctrl.searchParams.query,
      $ctrl.options.pageObject.page, projectId, filter,
      getSelectedMetadataType(),
      $ctrl.options.pageObject.size, $ctrl.searchParams.sortBy)
      .then(function(data) {
        console.log(data);
        // $ctrl.searchParams.filter
        $ctrl.searchResult = data.hits.hits;
        $ctrl.options.pageObject.totalHits = data.hits.total.value;
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

  $scope.$watch('$ctrl.searchParams.query', function() {
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
      writeSearchParamsToLocation();
      $ctrl.search();
      queryChangeIsBeingHandled = false;
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
}

angular
  .module('metadatamanagementApp')
  .controller('RelatedObjectsController', Controller);
