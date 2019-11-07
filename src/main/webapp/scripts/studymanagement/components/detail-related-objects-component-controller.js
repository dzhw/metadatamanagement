/* global _ */

'use strict';

var CTRL = function(
    $scope, $rootScope, SearchHelperService, $location, SearchDao, $timeout,
    SearchResultNavigatorService, CleanJSObjectService, ToolbarHeaderService,
    $state
) {

  var queryChangedOnInit = false;
  var tabChangedOnInitFlag = false;
  var locationChanged = false;
  var selectedTabChangeIsBeingHandled = false;
  var queryChangeIsBeingHandled = false;
  var relatedObjects = [];

  //Information for the different tabs
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

  var tabsFilter = function(data) {
    tabs.forEach(function(tab, index) {
      var prop = tab.group + 'Count';
      if (data.hasOwnProperty(prop)) {
        tabs[index].count = data[prop];
        relatedObjects.push(tabs[index]);
      }
    });
  };
  var getSelectedMetadataType = function() {
    return $scope.tabs[$scope.searchParams.selectedTabIndex]
      .elasticSearchType;
  };
  // write the searchParams object to the location with the correct types
  var writeSearchParamsToLocation = function() {
    var locationSearch = {};
    locationSearch.page = '' + $scope.options.pageObject.page;
    locationSearch.size = '' + $scope.options.pageObject.size;
    try {
      locationSearch.type = $scope.tabs[
        $scope.searchParams.selectedTabIndex].elasticSearchType;
    } catch (e) {
      $scope.searchParams.selectedTabIndex = 0;
      locationSearch.type = $scope.tabs[
        $scope.searchParams.selectedTabIndex].elasticSearchType;
    }
    if ($scope.searchParams.query && $scope.searchParams.query !== '') {
      locationSearch.query = $scope.searchParams.query;
    }
    if ($scope.searchParams.sortBy && $scope.searchParams.sortBy !== '') {
      locationSearch['sort-by'] = $scope.searchParams.sortBy;
    } else {
      delete locationSearch['sort-by'];
    }
    _.assign(locationSearch, $scope.searchParams.filter);
    locationChanged = !angular.equals($location.search(),
      locationSearch);
    // Todo: Find solution for breadcrumbs error when selecting a tab
    // Quick fix: The change from $location.search to $location.replace
    // Negative: We can not link to the selected tab in the details page
    $location.replace(locationSearch);
  };

  // read the searchParams object from the location with the correct types
  var readSearchParamsFromLocation = function() {
    var locationSearch = $location.search();
    if (CleanJSObjectService.isNullOrEmpty(locationSearch)) {
      $scope.options.pageObject.page = 1;
      $scope.options.pageObject.size = 10;
      $scope.searchParams = {
        query: '',
        size: $scope.options.pageObject.size,
        selectedTabIndex: 0
      };
    } else {
      if (locationSearch.page != null) {
        $scope.options.pageObject.page = parseInt(locationSearch.page);
      } else {
        $scope.options.pageObject.page = 1;
      }
      if (locationSearch.size != null) {
        $scope.options.pageObject.size = parseInt(locationSearch.size);
        $scope.searchParams.size = $scope.options.pageObject.size;
      } else {
        $scope.options.pageObject.size = 10;
        $scope.searchParams.size = $scope.options.pageObject.size;
      }
      if (locationSearch.query) {
        $scope.searchParams.query = locationSearch.query;
      } else {
        $scope.searchParams.query = '';
      }
      $scope.searchParams.filter = _.omit(locationSearch, ['page', 'size',
        'type', 'query', 'sort-by'
      ]);
      $scope.searchParams.sortBy = locationSearch['sort-by'];
      var indexToSelect = _.findIndex($scope.tabs,
        function(tab) {
          return tab.elasticSearchType === locationSearch.type;
        });
      if (indexToSelect < 0) {
        $scope.searchParams.selectedTabIndex = 0;
      } else {
        $scope.searchParams.selectedTabIndex = indexToSelect;
      }
    }
  };

  $scope.computeSearchResultIndex = function($index) {
    return $index + 1 +
      (($scope.options.pageObject.page - 1) * $scope.options.pageObject.size);
  };

  //Search function
  $scope.search = function() {
    var projectId = _.get($scope, 'currentProject.id');
    $scope.isSearching++;
    SearchResultNavigatorService.setCurrentSearchParams(
      $scope.searchParams, projectId,
      getSelectedMetadataType(),
      $scope.options.pageObject);
    SearchDao.search($scope.searchParams.query,
      $scope.options.pageObject.page, projectId, $scope.searchParams.filter,
      getSelectedMetadataType(),
      $scope.options.pageObject.size, $scope.searchParams.sortBy)
      .then(function(data) {
        $scope.searchResult = data.hits.hits;
        $scope.options.pageObject.totalHits = data.hits.total.value;
        $scope.isSearching--;
        // Safari fix
        $timeout(function() {
          angular.element('body').append('<div id=fdz-safari-fix></div>');
          $timeout(function() {
            angular.element('#fdz-safari-fix').remove();
          });
        }, 100);
      }, function() {
        $scope.options.pageObject.totalHits = 0;
        $scope.searchResult = {};
        $scope.tabs.forEach(function(tab) {
          tab.count = null;
        });
        $scope.tabs[$scope.searchParams.selectedTabIndex].count = 0;
        $scope.isSearching--;
      });
  };

  this.$onInit = function() {
    tabsFilter(this.options);
    tabChangedOnInitFlag = true;
    queryChangedOnInit = true;
    $scope.tabs = relatedObjects;
    $scope.searchResult = {};
    // fdz-paginator options object
    $scope.options = {
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
    $scope.searchParams = {
      query: '',
      size: $scope.options.pageObject.size,
      selectedTabIndex: 0
    };

    readSearchParamsFromLocation();
    writeSearchParamsToLocation();
    $scope.search();
  };

  $scope.onSelectedTabChanged = function() {
    if (!selectedTabChangeIsBeingHandled && !queryChangeIsBeingHandled) {
      //prevent multiple tab change handlers caused by logout
      selectedTabChangeIsBeingHandled = true;
      $timeout(function() {
        if (!tabChangedOnInitFlag) {
          $scope.searchParams.filter = SearchHelperService
            .removeIrrelevantFilters(
              $scope.tabs[$scope.searchParams.selectedTabIndex]
                .elasticSearchType,
              $scope.searchParams.filter);
          $scope.searchParams.sortBy = undefined;
          $scope.options.pageObject.page = 1;
          writeSearchParamsToLocation();
          $scope.search();
        }
        tabChangedOnInitFlag = false;
        selectedTabChangeIsBeingHandled = false;
      });
    }
  };
  $scope.$watch('searchParams.query', function() {
    if (queryChangedOnInit) {
      queryChangedOnInit = false;
      return;
    }
    if (selectedTabChangeIsBeingHandled) {
      return;
    }
    queryChangeIsBeingHandled = true;
    $timeout(function() {
      $scope.options.pageObject.page = 1;
      delete $scope.searchParams.sortBy;
      writeSearchParamsToLocation();
      $scope.search();
      queryChangeIsBeingHandled = false;
    });
  });

  // watch for location changes not triggered by our code
  // $scope.$watchCollection(function() {
  //   return $location.search();
  // }, function(newValue, oldValue) {
  //   ToolbarHeaderService.updateToolbarHeader({
  //     'stateName': $state.current.name,
  //     'tabName': 'study-management.detail.label.study',
  //     'searchUrl': $location.absUrl(),
  //     'searchParams': $location.search()
  //   });
  //   if (newValue !== oldValue && !locationChanged) {
  //     readSearchParamsFromLocation();
  //     // type changes are already handled by $scope.onSelectedTabChanged
  //     if (newValue.type === oldValue.type) {
  //       $scope.search();
  //     }
  //   } else {
  //     locationChanged = false;
  //   }
  // });
};

angular
  .module('metadatamanagementApp')
  .controller('RelatedObjectsController', CTRL);
