/* Author: Daniel Katzberg */
/* global _ */
'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    SearchDao, $translate, VariableUploadService,
    QuestionUploadService, RelatedPublicationUploadService,
    DataSetUploadService, StudyUploadService, SurveyUploadService, $mdDialog,
    CleanJSObjectService, InstrumentUploadService,
    CurrentProjectService, $timeout, PageTitleService) {

    // set the page title in toolbar and window.title
    PageTitleService.setPageTitle('global.menu.search.title');

    //Check the login status
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
    });

    $scope.currentProject = CurrentProjectService.getCurrentProject();
    $scope.page = {
      currentPageNumber: $location.search().page || 1,
      totalHits: 0,
      size: 5
    };

    $scope.searchResult = {};
    $scope.isInitializing = true;

    //Need for interpretation of the query element in the url.
    $scope.query = $location.search().query;

    //The current index of the active tab
    $scope.selectedTabIndex = 0;
    //Set active tab
    $scope.$watch(function() {
      return $location.search().type;
    }, function(value) {
      for (var i = 0; i < $scope.tabs.length; i++) {
        if ($scope.tabs[i].elasticSearchType === value) {
          $scope.selectedTabIndex = i;
        }
      }
    });
    //Search function
    $scope.search = function() {
      $scope.isSearching = true;
      var selectedTab = $scope.tabs[$scope.selectedTabIndex];

      $scope.tabs.forEach(function(tab) {
        tab.count = null;
      });

      //Search with different types, binded on every tab
      if ($scope.currentProject) {
        $location.search('rdc-project', $scope.currentProject.id);
      } else {
        $location.search('rdc-project', '');
      }
      $location.search('query', $scope.query);
      $location.search('page', $scope.page.currentPageNumber);
      $location.search('type', selectedTab.elasticSearchType);

      SearchDao.search($scope.query, $scope.page.currentPageNumber,
          $scope.currentProject,
          selectedTab.elasticSearchType, $scope.page.size)
        .then(function(data) {
          $scope.searchResult = data.hits.hits;
          $scope.page.totalHits = data.hits.total;
          selectedTab.count = data.hits.total;
          //Count information by aggregations
          if (selectedTab.elasticSearchType === '') {
            $scope.tabs.forEach(function(tab) {
              if (tab.elasticSearchType !== '') {
                tab.count = 0;
              }
              data.aggregations.countByType.buckets.forEach(
                function(bucket) {
                  if (bucket.key === tab.elasticSearchType) {
                    // jscs:disable
                    tab.count = bucket.doc_count;
                    // jscs:enable
                  }
                });
            });
          }
          $scope.isSearching = false;
          //If something going wrong: send an alert
        }, function(error) {
          //TODO DKatzberg replace with toast and remove alert line
          //AlertService.error(error.message);
          console.trace(error);
          $scope.isSearching = false;
        });
    };

    $scope.onTabSelected = function() {
      //this funtion is called even when the dialog is initialized
      if (!$scope.isInitializing) {
        $scope.page.currentPageNumber = 1;
      }
      $scope.isInitializing = false;
      $scope.search();
    };

    $scope.onQueryChanged = function() {
      $scope.page.currentPageNumber = 1;
      $scope.throttledSearch();
    };

    $scope.$on('current-project-changed', function(event, currentProject) {
      $scope.currentProject = currentProject;
      $scope.page.currentPageNumber = 1;
      $scope.search();
    });

    $scope.throttledSearch = _.throttle($scope.search, 500);

    $scope.uploadVariables = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      VariableUploadService.uploadVariables(files, $scope.currentProject);
    };

    $scope.uploadQuestions = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      QuestionUploadService.uploadQuestions(files, $scope.currentProject);
    };

    $scope.uploadSurveys = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      SurveyUploadService.uploadSurveys(files, $scope.currentProject);
    };

    $scope.uploadDataSets = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      DataSetUploadService.uploadDataSets(files, $scope.currentProject);
    };

    $scope.uploadRelatedPublications = function(file) {
      if (Array.isArray(file)) {
        file = file[0];
      }
      RelatedPublicationUploadService.uploadRelatedPublications(file);
    };

    $scope.uploadStudy = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      StudyUploadService.uploadStudy(files, $scope.currentProject);
    };

    $scope.uploadInstruments = function(files) {
      if (!files || files.length === 0) {
        return;
      }
      InstrumentUploadService.uploadInstruments(files, $scope.currentProject);
    };

    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };

    $scope.$on('upload-completed', function() {
      //wait for 2 seconds until refresh
      //in order to wait for elasticsearch reindex
      $timeout($scope.refresh, 1000);
    });

    //Information for the different tabs
    $scope.tabs = [{
      title: 'search-management.tabs.all',
      inputLabel: 'search-management.input-label.all',
      elasticSearchType: '',
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
      title: 'search-management.tabs.data-sets',
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
      title: 'search-management.tabs.related-publications',
      inputLabel: 'search-management.input-label.related-publications',
      icon: 'assets/images/icons/related-publication.svg',
      elasticSearchType: 'related_publications',
      count: null,
      uploadFunction: $scope.uploadRelatedPublications
    }];
  });
