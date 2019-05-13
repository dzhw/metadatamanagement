/* global _, bowser */

'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, $location, $state, SearchDao,
           VariableUploadService, ProjectUpdateAccessService,
           QuestionUploadService, RelatedPublicationUploadService,
           CleanJSObjectService, CurrentProjectService, $timeout,
           PageTitleService, ToolbarHeaderService, SearchHelperService,
           SearchResultNavigatorService, StudyResource, StudyIdBuilderService,
           $rootScope, ProjectStatusScoringService, DeleteMetadataService,
           SimpleMessageToastService) {

    var queryChangedOnInit = false;
    var tabChangedOnInitFlag = false;
    var locationChanged = false;
    var currentProjectChangeIsBeingHandled = false;
    var selectedTabChangeIsBeingHandled = false;
    var queryChangeIsBeingHandled = false;

    var getSelectedMetadataType = function() {
      return $scope.tabs[$scope.searchParams.selectedTabIndex]
        .elasticSearchType;
    };

    $scope.isSearching = 0;
    $scope.isDropZoneDisabled = true;

    // set the page title in toolbar and window.title
    PageTitleService.setPageTitle('global.menu.search.title');
    //Check the login status
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.hasAnyAuthority = Principal.hasAnyAuthority;
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
      $scope.loadStudyForProject();
      $scope.search();
    };

    $scope.uploadVariables = function(files) {
      VariableUploadService.uploadVariables(files,
        $scope.currentProject.id);
    };

    $scope.uploadQuestions = function(files) {
      QuestionUploadService.uploadQuestions(files,
        $scope.currentProject.id);
    };

    $scope.uploadRelatedPublications = function(file) {
      if (Array.isArray(file)) {
        file = file[0];
      }
      RelatedPublicationUploadService.uploadRelatedPublications(file);
    };

    //Information for the different tabs
    var tabs = [{
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
      uploadFunction: $scope.uploadQuestions,
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
      uploadFunction: $scope.uploadVariables,
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
      uploadFunction: $scope.uploadRelatedPublications,
      disabled: false,
      visibleForPublicUser: true,
      noResultsText: 'search-management.no-results-text.related-publications',
      group: 'publications'
    }];

    //Search function
    $scope.search = function() {
      var projectId = _.get($scope, 'currentProject.id');
      $scope.isSearching++;
      $scope.setDropZoneDisabled();
      SearchResultNavigatorService.setCurrentSearchParams(
        $scope.searchParams, projectId,
        getSelectedMetadataType(),
        $scope.pageObject);
      SearchDao.search($scope.searchParams.query, $scope.pageObject.page,
        projectId, $scope.searchParams.filter,
        getSelectedMetadataType(),
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
        'searchParams': $location.search()
      });
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

    var filterActiveTabs = function(tabs) {
      var project = CurrentProjectService.getCurrentProject();

      if (project) {
        var inactiveStates = [];
        if (!project.configuration.requirements.surveysRequired) {
          inactiveStates.push('surveys');
        }
        if (!project.configuration.requirements.instrumentsRequired) {
          inactiveStates.push('instruments');
        }
        if (!project.configuration.requirements.questionsRequired) {
          inactiveStates.push('questions');
        }
        if (!project.configuration.requirements.dataSetsRequired) {
          inactiveStates.push('dataSets');
        }
        if (!project.configuration.requirements.variablesRequired) {
          inactiveStates.push('variables');
        }
        if (!project.configuration.requirements.publicationsRequired) {
          inactiveStates.push('publications');
        }

        return _.filter(tabs, function(tab) {
          if (tab.group) {
            return inactiveStates.indexOf(tab.group) === -1;
          } else {
            return true;
          }
        });
      } else {
        return tabs;
      }
    };

    $scope.$on('current-project-changed',
      function(event, currentProject) { // jshint ignore:line
        $scope.tabs = filterActiveTabs(tabs);
        currentProjectChangeIsBeingHandled = true;
        //wait for other events (logout, selectedTabIndex)
        $timeout(function() {
          if (!$state.is('search')) {
            return;
          }
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
          $scope.loadStudyForProject();
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
      if (queryChangedOnInit) {
        queryChangedOnInit = false;
        return;
      }
      if (selectedTabChangeIsBeingHandled) {
        return;
      }
      queryChangeIsBeingHandled = true;
      $timeout(function() {
        $scope.pageObject.page = 1;
        delete $scope.searchParams.sortBy;
        writeSearchParamsToLocation();
        $scope.search();
        queryChangeIsBeingHandled = false;
      });
    });

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
            $scope.pageObject.page = 1;
            writeSearchParamsToLocation();
            if (!currentProjectChangeIsBeingHandled) {
              $scope.search();
            }
            $scope.loadStudyForProject();
          }
          tabChangedOnInitFlag = false;
          selectedTabChangeIsBeingHandled = false;
        });
      }
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

    $scope.$on('deletion-completed', function() {
      //wait for 1 seconds until refresh
      //in order to wait for elasticsearch reindex
      $timeout($scope.search, 2000);
    });

    $scope.tabs = filterActiveTabs(tabs);

    $scope.hideMobileKeyboard = function($event) {
      $event.target.querySelector('#query').blur();
    };

    $scope.onFilterChanged = function() {
      $scope.pageObject.page = 1;
      writeSearchParamsToLocation();
      $scope.search();
    };

    $scope.computeSearchResultIndex = function($index) {
      return $index + 1 +
        (($scope.pageObject.page - 1) * $scope.pageObject.size);
    };

    $scope.loadStudyForProject = function() {
      if ($scope.currentProject && !$scope.currentProject.release &&
        $scope.tabs[$scope.searchParams.selectedTabIndex]
          .elasticSearchType === 'studies') {
        $rootScope.$broadcast('start-ignoring-404');
        StudyResource.get({
          id: StudyIdBuilderService.buildStudyId(
            $scope.currentProject.id)
        }).$promise.then(function(study) {
          $scope.currentStudy = study;
        }).catch(function() {
          $scope.currentStudy = undefined;
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      } else {
        $scope.currentStudy = undefined;
      }
    };

    $scope.setDropZoneDisabled = function() {
      if (!$scope.tabs[$scope.searchParams.selectedTabIndex].uploadFunction) {
        $scope.isDropZoneDisabled = true;
        return;
      }

      var type = getSelectedMetadataType();

      if (type !== 'related_publications') {
        if (!$scope.currentProject || $scope.currentProject.release ||
          !Principal.hasAnyAuthority(
            ['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER']) ||
          !ProjectUpdateAccessService
            .isUpdateAllowed($scope.currentProject, type)) {
          $scope.isDropZoneDisabled = true;
          return;
        }
      } else {
        if (!Principal.hasAuthority('ROLE_PUBLISHER') ||
          $scope.currentProject != null) {
          $scope.isDropZoneDisabled = true;
          return;
        }
      }
      $scope.isDropZoneDisabled = false;
    };

    $scope.getSentimentValue = function(tab) {
      return ProjectStatusScoringService
        .scoreProjectStatus($scope.currentProject, tab);
    };

    $scope.isUploadAllowed = function(type) {
      if (bowser.msie) {
        SimpleMessageToastService.openAlertMessageToast('global.error.' +
          'browser-not-supported');
        return false;
      }
      return ProjectUpdateAccessService.isUpdateAllowed($scope.currentProject,
        type, true);
    };
    $scope.deleteAllStudies = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'studies');
    };
    $scope.deleteAllQuestions = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'questions');
    };
    $scope.deleteAllVariables = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'variables');
    };
    $scope.deleteAllInstruments = function() {
      DeleteMetadataService.deleteAllOfType(
        $scope.currentProject, 'instruments');
    };
    $scope.deleteAllSurveys = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'surveys');
    };
    $scope.deleteAllDataSets = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'data_sets');
    };
    $scope.deleteAllPublications = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject,
        'publications');
    };
    $scope.navigateToCreateState = function(createState) {
      var type = getSelectedMetadataType();
      if (type === 'related_publications') {
        type = 'publications';
      }
      if (ProjectUpdateAccessService.isUpdateAllowed($scope.currentProject,
        type, true)) {
        ProjectUpdateAccessService.isPrerequisiteFulfilled(
          $scope.currentProject, type).then(function() {
            $state.go(createState);
          });
      }
    };
    init();
  });
