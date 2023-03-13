/* global _, bowser */

'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
  function($scope, Principal, $location, $state, SearchDao, MessageBus,
           VariableUploadService, ProjectUpdateAccessService,
           QuestionUploadService, RelatedPublicationUploadService,
           CleanJSObjectService, CurrentProjectService, $timeout,
           PageMetadataService, BreadcrumbService, SearchHelperService,
           SearchResultNavigatorService, DataPackageResource,
           AnalysisPackageResource, DataPackageIdBuilderService,
           AnalysisPackageIdBuilderService,
           $rootScope, ProjectStatusScoringService, DeleteMetadataService,
           SimpleMessageToastService, $mdSidenav, $analytics,
           DataAcquisitionProjectRepositoryClient) {

    var queryChangedOnInit = true;
    var tabChangedOnInitFlag = true;
    var locationChanged = false;
    var currentProjectChangeIsBeingHandled = false;
    var selectedTabChangeIsBeingHandled = false;
    var queryChangeIsBeingHandled = false;

    var searchFilterAggregations = {
      data_packages: [
        'study-series',
        'survey-data-types',
        'tags',
        'sponsors',
        'institutions',
        'access-ways',
        'concepts'
      ],
      analysis_packages: [
        'tags',
        'sponsors',
        'institutions'
      ],
      related_publications: [
        'year',
        'language'
      ]
    };
    var getSelectedMetadataType = function() {
      return $scope.tabs[$scope.searchParams.selectedTabIndex]
        .elasticSearchType;
    };

    $scope.isSearching = 0;
    $scope.isDropZoneDisabled = true;
    // set the page title in toolbar and window.title
    PageMetadataService.setPageTitle('global.menu.search.title');
    PageMetadataService.setPageDescription('global.menu.search.description');
    //Check the login status
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.hasAnyAuthority = Principal.hasAnyAuthority;
    });
    // check for projects
    $scope.findProjects = function() {
      if (Principal.isAuthenticated() && !Principal.showAllData()) {
        DataAcquisitionProjectRepositoryClient.findAssignedProjects(
          Principal.loginName()).then(function(response) {
            var projects = response.data;
            if (projects.length === 0) {
              $scope.showNoProjectsCard = true;
            }
          }).catch(function() {
            $scope.showNoProjectsCard = false;
          });
      } else {
        $scope.showNoProjectsCard = false;
      }
    };
    $scope.findProjects();
    var writeSearchParamsToLocation = function() {
      var locationSearch = {};
      locationSearch.page = '' + $scope.options.pageObject.page;
      locationSearch.size = '' + $scope.options.pageObject.size;
      if (Principal.isAuthenticated()) {
        try {
          locationSearch.type = $scope.tabs[
            $scope.searchParams.selectedTabIndex].elasticSearchType;
        } catch (e) {
          $scope.searchParams.selectedTabIndex = 0;
          locationSearch.type = $scope.tabs[
            $scope.searchParams.selectedTabIndex].elasticSearchType;
        }
      } else {
        try {
          locationSearch.type = $scope.searchParams.type;
        } catch (e) {
          locationSearch.type = 'data_packages';
        }
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
        $scope.options.pageObject.page = 1;
        $scope.options.pageObject.size = 10;
        $scope.searchParams = {
          query: '',
          type: 'data_packages',
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
        if (locationSearch.type) {
          $scope.searchParams.type = locationSearch.type;
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

    // init the controller and its scope objects
    var init = function() {
      MessageBus.set('searchInit', {});
      $scope.tabs = _.filter($scope.tabs, function(tab) {
        return tab.visibleForPublicUser || Principal.isAuthenticated();
      });
      $scope.searchResult = {};
      $scope.currentProject = CurrentProjectService.getCurrentProject();
      if (Principal.isAuthenticated()) {
        searchFilterAggregations = null;
        $scope.searchFilterMapping = {};
      }
      if (!$scope.currentProject) {
        $scope.currentProject = undefined;
      }
      // fdz-paginator options object
      $scope.options = {
        sortObject: {
          selected: 'relevance',
          options: $scope.tabs[0].sortOptions
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
        type: 'data_packages',
        size: $scope.options.pageObject.size,
        selectedTabIndex: 0
      };
      readSearchParamsFromLocation();
      writeSearchParamsToLocation();
      $scope.loadDataPackageForProject();
      $scope.loadAnalysisPackageForProject();
      $scope.search();
      MessageBus.set('onDetailViewLoaded', {type: 'search'});
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
      noResultsText: 'search-management.no-results-text.all',
      sortOptions: ['relevance']
    }, {
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
        'first-release-date']
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
      sortOptions: ['relevance', 'alphabetically', 'first-release-date']
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
      uploadFunction: $scope.uploadQuestions,
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
      uploadFunction: $scope.uploadVariables,
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
      uploadFunction: $scope.uploadRelatedPublications,
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

    function createDataPackageFilterContent(data, prop) {
      _.map(data.all.filtered[prop].buckets, function(val1, i1) {
        data.all.filtered[prop].buckets[i1].doc_count = 0;
        _.find(data[prop].buckets, function(val2, i2) {
          if (val1.key === val2.key) {
            data.all.filtered[prop].buckets[i1].doc_count = data[prop]
              .buckets[i2].doc_count;
          }
        });
      });
      return data.all.filtered[prop].buckets;
    }

    function createDataPackageFilterObject(data) {
      if (Principal.isAuthenticated()) { return null; }
      var dataPackageFilter = {
        'study-series': createDataPackageFilterContent(data,
          'study-series'),
        'survey-data-types': createDataPackageFilterContent(data,
          'survey-data-types'),
        'tags': createDataPackageFilterContent(data,
          'tags'),
        'access-ways': createDataPackageFilterContent(data,
          'access-ways'),
        'concepts': createDataPackageFilterContent(data,
          'concepts'),
        'sponsors': createDataPackageFilterContent(data,
          'sponsors'),
        'institutions': createDataPackageFilterContent(data,
          'institutions')
      };
      MessageBus.set('onDataPackageFilterChange', dataPackageFilter);
    }

    function createAnalysisPackageFilterObject(data) {
      if (Principal.isAuthenticated()) { return null; }
      var dataPackageFilter = {
        'tags': createDataPackageFilterContent(data,
          'tags'),
        'sponsors': createDataPackageFilterContent(data,
          'sponsors'),
        'institutions': createDataPackageFilterContent(data,
          'institutions')
      };
      MessageBus.set('onDataPackageFilterChange', dataPackageFilter);
    }

    function createPublicationsFilterObject(data) {
      if (Principal.isAuthenticated()) { return null; }
      var dataPackageFilter = {
        'year': data.year.buckets,
        'language': data.language.buckets
      };
      MessageBus.set('onDataPackageFilterChange', dataPackageFilter);
    }

    function createTotalHitsObject(totalHitsInCurrentIndex,
      totalHitsInAdditionalIndex) {
      var totalHits = {};
      totalHits[$scope.searchParams.type] = totalHitsInCurrentIndex;
      if ($scope.searchParams.additionalSearchIndex) {
        for (var index = 0;
          index < $scope.searchParams.additionalSearchIndex.length; index++) {
          totalHits[$scope.searchParams.additionalSearchIndex[index]] =
            totalHitsInAdditionalIndex[index];
        }
      }
      MessageBus.set('onTotalHitsChange', totalHits);
    }

    $scope.setCurrentSearchParams = function(projectId) {
      if (!projectId) {
        projectId = _.get($scope, 'currentProject.id');
      }
      SearchResultNavigatorService.setCurrentSearchParams(
        $scope.searchParams, projectId,
        getSelectedMetadataType(),
        $scope.options.pageObject);
    };

    //Search function
    /**
     * Searches and processes the results.
     * The authentification status determines the type of search.
     */
    $scope.search = function() {
      // Public users search in multiple indices at the same time
      // additional indices need to be set according to the currently
      // selected search type
      // aggregations need to be read from mapping
      var aggregation = null;
      $scope.searchParams.additionalSearchIndex = null;
      if (!Principal.isAuthenticated()) {
        $scope.searchFilterMapping = $scope.searchParams.filter;
        aggregation = searchFilterAggregations[$scope.searchParams.type];
        if ($scope.searchParams.type === 'data_packages') {
          $scope.searchParams.additionalSearchIndex = [
            'analysis_packages','related_publications'];
        }
        if ($scope.searchParams.type === 'analysis_packages') {
          $scope.searchParams.additionalSearchIndex = [
            'data_packages','related_publications'];
        }
        if ($scope.searchParams.type === 'related_publications') {
          $scope.searchParams.additionalSearchIndex = [
            'data_packages','analysis_packages'];
        }
      }
      var projectId = _.get($scope, 'currentProject.id');
      $scope.isSearching++;
      MessageBus.set('onStartSearch', {});
      $scope.setDropZoneDisabled();
      if (Principal.isAuthenticated()) {
        $scope.setCurrentSearchParams(projectId);
      }
      SearchDao.search($scope.searchParams.query,
        $scope.options.pageObject.page, projectId, $scope.searchParams.filter,
        getSelectedMetadataType(),
        $scope.options.pageObject.size,
        null,
        // Aggregations Usage: ['study-series', ...]
        aggregation,
        // Usage:
        // {
        //   'study-series': ['DZHW-Absolventenstudien','adf','asd'],
        //   'sponsors': ['Bundesministerium für Bildung und Forschung (BMBF)']
        // })
        $scope.searchFilterMapping, $scope.options.sortObject.selected, false,
        $scope.searchParams.additionalSearchIndex)
        .then(function(data) {
          var totalHitsInAdditionalIndex = [];
          if ($scope.searchParams.additionalSearchIndex) {
            for (var index = 0;
              index < $scope.searchParams.additionalSearchIndex.length;
              index++) {
              totalHitsInAdditionalIndex
                .push(data.responses[+index + 1].hits.total.value);
            }

            data = data.responses[0];
          }
          if ($scope.searchParams.type === 'data_packages') {
            createDataPackageFilterObject(data.aggregations);
          } else if ($scope.searchParams.type === 'analysis_packages') {
            createAnalysisPackageFilterObject(data.aggregations);
          } else {
            createPublicationsFilterObject(data.aggregations);
          }
          $scope.searchResult = data.hits.hits;
          $scope.options.pageObject.totalHits = data.hits.total.value;
          createTotalHitsObject(data.hits.total.value,
            totalHitsInAdditionalIndex);
          $analytics.trackSiteSearch(
            $scope.searchParams.query ? $scope.searchParams.query : '<null>',
            $scope.tabs[$scope.searchParams.selectedTabIndex]
              .elasticSearchType ?
              $scope.tabs[$scope.searchParams.selectedTabIndex]
                .elasticSearchType : 'all',
            $scope.options.pageObject.totalHits);
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
              data.hits.total.value;
          });
          $scope.isSearching--;
          MessageBus.set('onStopSearch', {});

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
          MessageBus.set('onStopSearch', {});
        });
    };

    var filterActiveTabs = function(tabs) {
      var project = CurrentProjectService.getCurrentProject();

      if (project) {
        var inactiveStates = [];
        if (!project.configuration.requirements.dataPackagesRequired) {
          inactiveStates.push('dataPackages');
        }
        if (!project.configuration.requirements.analysisPackagesRequired) {
          inactiveStates.push('analysisPackages');
        }
        if (!project.configuration.requirements.conceptsRequired) {
          inactiveStates.push('concepts');
        }
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

    // watch for location changes not triggered by our code
    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue, oldValue) {
      BreadcrumbService.updateToolbarHeader({
        'stateName': $state.current.name,
        'tabName': $scope.tabs[$scope.searchParams.selectedTabIndex].title,
        'searchUrl': $location.absUrl(),
        'searchParams': $location.search()
      });
      if (newValue !== oldValue && !locationChanged) {
        readSearchParamsFromLocation();
        // type changes are already handled by $scope.onSelectedTabChanged
        // if (newValue.type === oldValue.type) {
        if (!$scope.isSearching) {
          $scope.options.sortObject.options = $scope.tabs[
            $scope.searchParams.selectedTabIndex].sortOptions;
          $scope.options.sortObject.selected = 'relevance';
          $scope.search();
        }
      } else {
        locationChanged = false;
      }
    });

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
          $scope.options.pageObject.page = 1;
          writeSearchParamsToLocation();
          if (!selectedTabChangeIsBeingHandled) {
            $scope.search();
          }
          $scope.loadDataPackageForProject();
          $scope.loadAnalysisPackageForProject();
          currentProjectChangeIsBeingHandled = false;
        });
      });

    $scope.onPageChanged = function() {
      writeSearchParamsToLocation();
      $scope.search();
    };

    if (Principal.isAuthenticated()) {
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
    }

    $scope.onSelectedTabChanged = function() {
      $scope.tabs = filterActiveTabs(tabs);
      $scope.options.sortObject.options = $scope.tabs[
        $scope.searchParams.selectedTabIndex].sortOptions;
      $scope.options.sortObject.selected = 'relevance';
      if (!selectedTabChangeIsBeingHandled) {
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
            if (!currentProjectChangeIsBeingHandled) {
              $scope.search();
            }
            $scope.loadDataPackageForProject();
            $scope.loadAnalysisPackageForProject();
            selectedTabChangeIsBeingHandled = false;
          } else {
            tabChangedOnInitFlag = false;
            selectedTabChangeIsBeingHandled = false;
          }
        });
      }
    };
    //Refresh function for the refresh button
    $scope.refresh = function() {
      $scope.search();
    };

    $scope.$on('upload-completed', $scope.search);

    $scope.$on('deletion-completed', $scope.search);

    $scope.tabs = filterActiveTabs(tabs);

    $scope.hideMobileKeyboard = function($event) {
      $event.target.querySelector('#query').blur();
    };

    $scope.onFilterChanged = function() {
      $scope.options.pageObject.page = 1;
      writeSearchParamsToLocation();
      $scope.search();
    };

    $scope.computeSearchResultIndex = function($index) {
      return $index + 1 +
        (($scope.options.pageObject.page - 1) * $scope.options.pageObject.size);
    };

    $scope.loadDataPackageForProject = function() {
      if ($scope.currentProject && !$scope.currentProject.release &&
        $scope.tabs[$scope.searchParams.selectedTabIndex]
          .elasticSearchType === 'data_packages') {
        $rootScope.$broadcast('start-ignoring-404');
        DataPackageResource.get({
          id: DataPackageIdBuilderService.buildDataPackageId(
            $scope.currentProject.id)
        }).$promise.then(function(dataPackage) {
          $scope.currentDataPackage = dataPackage;
        }).catch(function() {
          $scope.currentDataPackage = undefined;
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      } else {
        $scope.currentDataPackage = undefined;
      }
    };
    $scope.loadAnalysisPackageForProject = function() {
      if ($scope.currentProject && !$scope.currentProject.release &&
        $scope.tabs[$scope.searchParams.selectedTabIndex]
          .elasticSearchType === 'analysis_packages') {
        $rootScope.$broadcast('start-ignoring-404');
        AnalysisPackageResource.get({
          id: AnalysisPackageIdBuilderService.buildAnalysisPackageId(
            $scope.currentProject.id)
        }).$promise.then(function(analysisPackage) {
          $scope.currentAnalysisPackage = analysisPackage;
        }).catch(function() {
          $scope.currentAnalysisPackage = undefined;
        }).finally(function() {
          $rootScope.$broadcast('stop-ignoring-404');
        });
      } else {
        $scope.currentAnalysisPackage = undefined;
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
    $scope.deleteAllDataPackages = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject,
        'data_packages');
    };
    $scope.deleteAllAnalysisPackages = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject,
        'analysis_packages');
    };
    $scope.deleteAllQuestions = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'questions');
    };
    $scope.deleteAllVariables = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'variables');
    };
    $scope.deleteAllInstruments = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject,
        'instruments');
    };
    $scope.deleteAllSurveys = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'surveys');
    };
    $scope.deleteAllDataSets = function() {
      DeleteMetadataService.deleteAllOfType($scope.currentProject, 'data_sets');
    };
    $scope.navigateToCreateState = function(createState) {
      var type = getSelectedMetadataType();
      if (type === 'related_publications') {
        type = 'publications';
      }
      if (type === 'concepts') {
        $state.go(createState);
        return;
      }
      if (ProjectUpdateAccessService.isUpdateAllowed($scope.currentProject,
        type, true)) {
        ProjectUpdateAccessService.isPrerequisiteFulfilled(
          $scope.currentProject, type).then(function() {
          $state.go(createState);
        });
      }
    };

    $scope.toggleSidenav = function() {
      $mdSidenav('SideNavBar').toggle();
    };

    init();
  });
