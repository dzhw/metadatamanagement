/* global _ */

'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, $location, $state,
    SearchDao, VariableUploadService,
    RelatedPublicationsPostValidationService,
    QuestionUploadService, RelatedPublicationUploadService,
    DataSetUploadService, StudyUploadService, SurveyUploadService,
    CleanJSObjectService, InstrumentUploadService,
    CurrentProjectService, $timeout, PageTitleService, ToolbarHeaderService,
    SearchFilterHelperService) {

    var queryChangedOnInit = false;
    var tabChangedOnInitFlag = false;
    var locationChanged = false;
    var currentProjectChangeIsBeingHandled = false;
    var selectedTabChangeIsBeingHandled = false;
    $scope.isSearching = 0;

    // set the page title in toolbar and window.title
    PageTitleService.setPageTitle('global.menu.search.title');
    //Check the login status
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.hasAuthority = Principal.hasAuthority;
    });
    // write the searchParams object to the location with the correct types
    var writeSearchParamsToLocation = function() {
      var locationSearch = {};
      locationSearch.page = '' + $scope.pageObject.page;
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
      $location.search(locationSearch);
    };

    // read the searchParams object from the location with the correct types
    var readSearchParamsFromLocation = function() {
      var locationSearch = $location.search();
      if (CleanJSObjectService.isNullOrEmpty(locationSearch)) {
        $scope.pageObject.page = 1;
        $scope.searchParams = {
          query: '',
          selectedTabIndex: 0
        };
      } else {
        if (locationSearch.page != null) {
          $scope.pageObject.page = parseInt(locationSearch.page);
        } else {
          $scope.pageObject.page = 1;
        }
        if (locationSearch.query) {
          $scope.searchParams.query = locationSearch.query;
        } else {
          $scope.searchParams.query = '';
        }
        $scope.searchParams.filter = _.omit(locationSearch, ['page', 'type',
          'query', 'sort-by'
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

    // init the controller and its scope objects
    var init = function() {
      tabChangedOnInitFlag = true;
      queryChangedOnInit = true;
      $scope.tabs = _.filter($scope.tabs, function(tab) {
        return tab.visibleForPublicUser || Principal.isAuthenticated();
      });
      $scope.searchResult = {};
      $scope.currentProject = CurrentProjectService.getCurrentProject();
      if (!$scope.currentProject) {
        $scope.currentProject = undefined;
      }
      $scope.pageObject = {
        totalHits: 0,
        size: 5,
        page: 1
      };
      $scope.searchParams = {
        query: '',
        selectedTabIndex: 0
      };
      readSearchParamsFromLocation();
      writeSearchParamsToLocation();
      $scope.search();
    };

    //Search function
    $scope.search = function() {
      var projectId = $scope.currentProject ?
        $scope.currentProject.id : undefined;
      $scope.isSearching++;
      SearchDao.search($scope.searchParams.query, $scope.pageObject.page,
          projectId, $scope.searchParams.filter,
          $scope.tabs[$scope.searchParams.selectedTabIndex].elasticSearchType,
          $scope.pageObject.size, $scope.searchParams.sortBy)
        .then(function(data) {
          $scope.searchResult = data.hits.hits;
          $scope.pageObject.totalHits = data.hits.total;
          //Count information by aggregations
          $scope.tabs.forEach(function(tab) {
            if ($scope.tabs[
                $scope.searchParams.selectedTabIndex].elasticSearchType ===
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
            $scope.tabs[$scope.searchParams.selectedTabIndex].count =
              data.hits.total;
          });
          $scope.isSearching--;
        }, function() {
          $scope.pageObject.totalHits = 0;
          $scope.searchResult = {};
          $scope.tabs.forEach(function(tab) {
            tab.count = null;
          });
          $scope.tabs[$scope.searchParams.selectedTabIndex].count = 0;
          $scope.isSearching--;
        });
    };

    // watch for location changes not triggered by our code
    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue, oldValue) {
      ToolbarHeaderService.updateToolbarHeader({
        'stateName': $state.current.name,
        'tabName': $scope.tabs[$scope.searchParams.selectedTabIndex].title,
        'searchUrl': $location.absUrl(),
        'searchParams': $location.search()});
      if (newValue !== oldValue && !locationChanged) {
        readSearchParamsFromLocation();
        // type changes are already handled by $scope.onSelectedTabChanged
        if (newValue.type === oldValue.type) {
          $scope.search();
        }
      } else {
        locationChanged = false;
      }
    });

    $scope.$on('current-project-changed',
      function(event, currentProject) { // jshint ignore:line
        currentProjectChangeIsBeingHandled = true;
        //wait for other events (logout, selectedTabIndex)
        $timeout(function() {
          if (currentProject) {
            $scope.currentProject = currentProject;
          } else {
            $scope.currentProject = undefined;
          }
          $scope.pageObject.page = 1;
          writeSearchParamsToLocation();
          if (!selectedTabChangeIsBeingHandled) {
            $scope.search();
          }

          currentProjectChangeIsBeingHandled = false;
        });
      });

    $scope.$on('user-logged-out', function() {
      var currentType = $scope.tabs[$scope.searchParams.selectedTabIndex]
        .elasticSearchType;
      $scope.tabs = _.filter($scope.tabs, function(tab) {
        return tab.visibleForPublicUser || Principal.isAuthenticated();
      });
      var indexToSelect = _.findIndex($scope.tabs,
        function(tab) {
        return tab.elasticSearchType === currentType;
      });
      if (indexToSelect < 0) {
        $scope.searchParams.selectedTabIndex = 0;
      } else {
        $scope.searchParams.selectedTabIndex = indexToSelect;
      }
    });

    $scope.onPageChanged = function() {
        writeSearchParamsToLocation();
        $scope.search();
      };

    $scope.$watch('searchParams.query', function() {
      if (queryChangedOnInit)  {
        queryChangedOnInit = false;
        return;
      }
      $scope.pageObject.page = 1;
      delete $scope.searchParams.sortBy;
      writeSearchParamsToLocation();
      $scope.search();
    });

    $scope.onSelectedTabChanged = function() {
      if (!selectedTabChangeIsBeingHandled) {
        //prevent multiple tab change handlers caused by logout
        selectedTabChangeIsBeingHandled = true;
        $timeout(function() {
          if (!tabChangedOnInitFlag) {
            $scope.searchParams.filter = SearchFilterHelperService
              .removeIrrelevantFilters(
                $scope.tabs[$scope.searchParams.selectedTabIndex]
                  .elasticSearchType,
                $scope.searchParams.filter);
            $scope.searchParams.sortBy = undefined;
            $scope.pageObject.page = 1;
            writeSearchParamsToLocation();
            if (!currentProjectChangeIsBeingHandled) {
              $scope.search();
            }
          }
          tabChangedOnInitFlag = false;
          selectedTabChangeIsBeingHandled = false;
        });
      }
    };

    $scope.uploadVariables = function(files) {
      if (!files || files.length === 0 || !$scope.currentProject) {
        return;
      }
      VariableUploadService.uploadVariables(files,
        $scope.currentProject.id);
    };

    $scope.uploadQuestions = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      QuestionUploadService.uploadQuestions(files,
        $scope.currentProject.id);
    };

    $scope.uploadSurveys = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      SurveyUploadService.uploadSurveys(files,
        $scope.currentProject.id);
    };

    $scope.uploadDataSets = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      DataSetUploadService.uploadDataSets(files,
        $scope.currentProject.id);
    };

    $scope.uploadRelatedPublications = function(file) {
      if (Array.isArray(file)) {
        file = file[0];
      }
      RelatedPublicationUploadService.uploadRelatedPublications(file);
    };

    $scope.postValidateRelatedPublications = function() {
      RelatedPublicationsPostValidationService.postValidate();
    };

    $scope.uploadStudy = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      StudyUploadService.uploadStudy(files, $scope.currentProject.id);
    };

    $scope.uploadInstruments = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      InstrumentUploadService.uploadInstruments(files,
        $scope.currentProject.id);
    };

    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };

    $scope.$on('upload-completed', function() {
      //wait for 1 seconds until refresh
      //in order to wait for elasticsearch reindex
      $timeout($scope.search, 2000);
    });

    //Information for the different tabs
    $scope.tabs = [{
      title: 'search-management.tabs.all',
      inputLabel: 'search-management.input-label.all',
      elasticSearchType: undefined,
      count: null,
      acceptedFileUploadType: null,
      uploadFunction: null,
      disabled: false,
      visibleForPublicUser: false,
      noResultsText: 'search-management.no-results-text.all'
    }, {
      title: 'search-management.tabs.studies',
      inputLabel: 'search-management.input-label.studies',
      icon: 'assets/images/icons/study.svg',
      elasticSearchType: 'studies',
      count: null,
      uploadFunction: $scope.uploadStudy,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.studies'
    }, {
      title: 'search-management.tabs.surveys',
      inputLabel: 'search-management.input-label.surveys',
      icon: 'assets/images/icons/survey.svg',
      elasticSearchType: 'surveys',
      count: null,
      uploadFunction: $scope.uploadSurveys,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.surveys'
    }, {
      title: 'search-management.tabs.instruments',
      inputLabel: 'search-management.input-label.instruments',
      icon: 'assets/images/icons/instrument.svg',
      elasticSearchType: 'instruments',
      count: null,
      uploadFunction: $scope.uploadInstruments,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.instruments'
    }, {
      title: 'search-management.tabs.questions',
      inputLabel: 'search-management.input-label.questions',
      icon: 'assets/images/icons/question.svg',
      elasticSearchType: 'questions',
      count: null,
      uploadFunction: $scope.uploadQuestions,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.questions'
    }, {
      title: 'search-management.tabs.data_sets',
      inputLabel: 'search-management.input-label.data-sets',
      icon: 'assets/images/icons/data-set.svg',
      elasticSearchType: 'data_sets',
      count: null,
      uploadFunction: $scope.uploadDataSets,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.data-sets'
    }, {
      title: 'search-management.tabs.variables',
      inputLabel: 'search-management.input-label.variables',
      icon: 'assets/images/icons/variable.svg',
      elasticSearchType: 'variables',
      count: null,
      uploadFunction: $scope.uploadVariables,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.variables'
    }, {
      title: 'search-management.tabs.related_publications',
      inputLabel: 'search-management.input-label.related-publications',
      icon: 'assets/images/icons/related-publication.svg',
      elasticSearchType: 'related_publications',
      count: null,
      uploadFunction: $scope.uploadRelatedPublications,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.related-publications'
    }];

    $scope.hideMobileKeyboard = function($event) {
      $event.target.querySelector('#query').blur();
    };

    $scope.onFilterChanged = function() {
      $scope.pageObject.page = 1;
      writeSearchParamsToLocation();
      $scope.search();
    };

    init();
  });
