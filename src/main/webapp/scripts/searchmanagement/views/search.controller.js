/* global _ */

'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, $location,
    SearchDao, VariableUploadService,
    RelatedPublicationsPostValidationService,
    QuestionUploadService, RelatedPublicationUploadService,
    DataSetUploadService, StudyUploadService, SurveyUploadService,
    CleanJSObjectService, InstrumentUploadService,
    CurrentProjectService, $timeout, PageTitleService, BreadCrumbService) {

    var tabChangedOnInitFlag = false;
    var locationChanged = false;
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
      }
      _.assign(locationSearch, $scope.searchParams.filter);
      locationChanged = !angular.equals($location.search(),
        locationSearch);
      $location.search(locationSearch);
      BreadCrumbService.addToBreadCrumb($location.absUrl(), $location.search(),
        $location.url());
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
        $scope.searchParams.selectedTabIndex = _.findIndex($scope.tabs,
          function(tab) {
            return tab.elasticSearchType === locationSearch.type;
          });
      }
    };

    // init the controller and its scope objects
    var init = function() {
      tabChangedOnInitFlag = true;
      $scope.searchResult = {};
      var project = CurrentProjectService.getCurrentProject();
      if (project) {
        $scope.projectId = project.id;
      } else {
        $scope.projectId = undefined;
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
      $scope.isSearching = true;
      SearchDao.search($scope.searchParams.query, $scope.pageObject.page,
          $scope.projectId, $scope.searchParams.filter,
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
          $scope.isSearching = false;
        }, function() {
          $scope.isSearching = false;
        });
    };

    // watch for location changes not triggered by our code
    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue, oldValue) {
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
        $scope.searchParams.filter = undefined;
        if (currentProject) {
          $scope.projectId = currentProject.id;
        } else {
          $scope.projectId = undefined;
        }
        $scope.pageObject.page = 1;
        writeSearchParamsToLocation();
        $scope.search();
      });

    $scope.onPageChanged = function() {
      writeSearchParamsToLocation();
      $scope.search();
    };
    $scope.onQueryChanged = function() {
      $scope.pageObject.page = 1;
      writeSearchParamsToLocation();
      $scope.search();
    };

    $scope.onSelectedTabChanged = function() {
      if (!tabChangedOnInitFlag) {
        $scope.searchParams.filter = undefined;
        $scope.searchParams.sortBy = undefined;
        $scope.pageObject.page = 1;
        writeSearchParamsToLocation();
        $scope.search();
      }
      tabChangedOnInitFlag = false;
    };

    $scope.uploadVariables = function(files) {
      if (!files || files.length === 0 || !$scope.projectId) {
        return;
      }
      VariableUploadService.uploadVariables(files,
        $scope.projectId);
    };

    $scope.uploadQuestions = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      QuestionUploadService.uploadQuestions(files,
        $scope.projectId);
    };

    $scope.uploadSurveys = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      SurveyUploadService.uploadSurveys(files,
        $scope.projectId);
    };

    $scope.uploadDataSets = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      DataSetUploadService.uploadDataSets(files,
        $scope.projectId);
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
      StudyUploadService.uploadStudy(files, $scope.projectId);
    };

    $scope.uploadInstruments = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      InstrumentUploadService.uploadInstruments(files,
        $scope.projectId);
    };

    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };

    $scope.$on('upload-completed', function() {
      //wait for 1 seconds until refresh
      //in order to wait for elasticsearch reindex
      $timeout($scope.search, 1000);
    });

    //Information for the different tabs
    $scope.tabs = [{
      title: 'search-management.tabs.all',
      inputLabel: 'search-management.input-label.all',
      elasticSearchType: undefined,
      count: null,
      acceptedFileUploadType: null,
      uploadFunction: null
    }, {
      title: 'search-management.tabs.studies',
      inputLabel: 'search-management.input-label.studies',
      icon: 'assets/images/icons/study.svg',
      elasticSearchType: 'studies',
      count: null,
      uploadFunction: $scope.uploadStudy
    }, {
      title: 'search-management.tabs.questions',
      inputLabel: 'search-management.input-label.questions',
      icon: 'assets/images/icons/question.svg',
      elasticSearchType: 'questions',
      count: null,
      uploadFunction: $scope.uploadQuestions
    }, {
      title: 'search-management.tabs.variables',
      inputLabel: 'search-management.input-label.variables',
      icon: 'assets/images/icons/variable.svg',
      elasticSearchType: 'variables',
      count: null,
      uploadFunction: $scope.uploadVariables
    }, {
      title: 'search-management.tabs.surveys',
      inputLabel: 'search-management.input-label.surveys',
      icon: 'assets/images/icons/survey.svg',
      elasticSearchType: 'surveys',
      count: null,
      uploadFunction: $scope.uploadSurveys
    }, {
      title: 'search-management.tabs.data_sets',
      inputLabel: 'search-management.input-label.data-sets',
      icon: 'assets/images/icons/data-set.svg',
      elasticSearchType: 'data_sets',
      count: null,
      uploadFunction: $scope.uploadDataSets
    }, {
      title: 'search-management.tabs.instruments',
      inputLabel: 'search-management.input-label.instruments',
      icon: 'assets/images/icons/instrument.svg',
      elasticSearchType: 'instruments',
      count: null,
      uploadFunction: $scope.uploadInstruments
    }, {
      title: 'search-management.tabs.related_publications',
      inputLabel: 'search-management.input-label.related-publications',
      icon: 'assets/images/icons/related-publication.svg',
      elasticSearchType: 'related_publications',
      count: null,
      uploadFunction: $scope.uploadRelatedPublications
    }];
    init();
  });
