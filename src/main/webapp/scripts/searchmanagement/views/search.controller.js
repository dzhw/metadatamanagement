/* Author: Daniel Katzberg */
/* global _ */

'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, ElasticSearchProperties, $location,
    SearchDao, $translate, VariableUploadService,
    RelatedPublicationsPostValidationService,
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

      var filter = {};

      // write the searchParams object to the location with the correct types
      var writeSearchParamsToLocation = function() {
        var locationSearch = {};
        locationSearch.page = '' + $scope.pageObject.page;
        locationSearch.project = $scope.searchParams.projectId;
        locationSearch.type = $scope.tabs[
          $scope.searchParams.selectedTabIndex].elasticSearchType;
        if ($scope.searchParams.query && $scope.searchParams.query !== '') {
          locationSearch.query = $scope.searchParams.query;
        }
        _.assign(locationSearch, filter);
        $location.search(locationSearch);
      };

      // read the searchParams object from the location with the correct types
      var readSearchParamsFromLocation = function() {
        var locationSearch = $location.search();
        if (CleanJSObjectService.isNullOrEmpty(locationSearch)) {
          CurrentProjectService.setCurrentProject(null);
          $scope.pageObject.page  = 1;
          $scope.searchParams = {
            projectId: undefined,
            query: '',
            selectedTabIndex: 0
          };
        } else {
          if (locationSearch.project) {
            CurrentProjectService.setCurrentProject({
              id: locationSearch.project});
          } else {
            CurrentProjectService.setCurrentProject(null);
          }
          $scope.searchParams.projectId = locationSearch.project;
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
          filter =  _.omit(locationSearch, ['page', 'project', 'type']);
          $scope.searchParams.selectedTabIndex = _.findIndex($scope.tabs,
            function(tab) {
              return tab.elasticSearchType === locationSearch.type;
            });
        }
      };

      // init the controller and its scope objects
      var init = function() {
        $scope.searchResult = {};
        $scope.pageObject = {
          totalHits: 0,
          size: 5,
          page: 1
        };
        $scope.searchParams = {
          projectId: undefined,
          query: '',
          selectedTabIndex: 0
        };

        readSearchParamsFromLocation();
        writeSearchParamsToLocation();
        $scope.search();
      };

      // watch for location changes
      $scope.$watchCollection(function() {
          return $location.search();
        }, function(newValue, oldValue) {
        if (newValue !== oldValue) {
          readSearchParamsFromLocation();
          //do not search if page has not been set yet
          if (newValue.page != null) {
            $scope.search();
          }
        }
      });

      // watch for searchParams changes
      $scope.$watchCollection(function() {
          return $scope.searchParams;
        }, function(newValue, oldValue) {
        if (newValue !== oldValue) {
          filter = {};
          $scope.pageObject.page = 1;
          writeSearchParamsToLocation();
        }
      });

      //Search function
      $scope.search = function() {
        $scope.isSearching = true;
        SearchDao.search($scope.searchParams.query, $scope.pageObject.page,
          $scope.searchParams.projectId, filter,
          $scope.tabs[$scope.searchParams.selectedTabIndex].elasticSearchType,
          $scope.pageObject.size)
        .then(function(data) {
          $scope.searchResult = data.hits.hits;
          $scope.pageObject.totalHits = data.hits.total;
          //Count information by aggregations
          $scope.tabs.forEach(function(tab) {
            if ($scope.tabs[$scope.searchParams.selectedTabIndex].
              elasticSearchType === undefined) {
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

      $scope.$on('current-project-changed', function(event, currentProject) {
        if (currentProject) {
          $scope.searchParams.projectId = currentProject.id;
        } else {
          $scope.searchParams.projectId = undefined;
        }
      });

      $scope.onPageChanged = function() {
        writeSearchParamsToLocation();
      };

      $scope.uploadVariables = function(files) {
        if (!files || files.length === 0) {
          return;
        }
        VariableUploadService.uploadVariables(files,
          $scope.searchParams.projectId);
      };

      $scope.uploadQuestions = function(files) {
        if (!files || files.length === 0) {
          return;
        }
        QuestionUploadService.uploadQuestions(files,
          $scope.searchParams.projectId);
      };

      $scope.uploadSurveys = function(files) {
        if (!files || files.length === 0) {
          return;
        }
        SurveyUploadService.uploadSurveys(files,
          $scope.searchParams.projectId);
      };

      $scope.uploadDataSets = function(files) {
        if (!files || files.length === 0) {
          return;
        }
        DataSetUploadService.uploadDataSets(files,
          $scope.searchParams.projectId);
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
        StudyUploadService.uploadStudy(files, $scope.searchParams.projectId);
      };

      $scope.uploadInstruments = function(files) {
        if (!files || files.length === 0) {
          return;
        }
        InstrumentUploadService.uploadInstruments(files,
          $scope.searchParams.projectId);
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

      init();
    });
