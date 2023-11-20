'use strict';

/**
 * This view implements the project overview table. The project
 * overview is accessible for authenticated users with roles PUBLISHER or
 * DATAPROVIDER. PUBLISHERs will get insights into
 * all available data acquisition projects. DATAPROVIDERs can only see
 * their assigned projects.
 */
angular.module('metadatamanagementApp')
  .controller('ProjectOverviewController', [
  '$stateParams',
  '$state',
  '$scope',
  '$mdSelect',
  'BreadcrumbService',
  'PageMetadataService', 
  'ElasticSearchClient', 'clientId', 'Principal', function($stateParams, $state, $scope, $mdSelect,
    BreadcrumbService, PageMetadataService,
    ElasticSearchClient, clientId, Principal) {
    var ctrl = this;
    var stateParamsLimit = $stateParams.limit ? $stateParams.limit : 10;

    ctrl.pagination = {
      selectedPageNumber: $stateParams.page ? $stateParams.page : 1,
      totalItems: null,
      itemsPerPage: 10
    };

    /**
     * Method for searching data acquisition projects with selected filters
     * @param {number} pageNumber current page index
     * @param {number} limit maximum entries per page
     */
    var searchWithFilter = function(pageNumber, pageSize) {
      var query = {};
      query.preference = clientId;
      query.index = "data_acquisition_projects";
      query.body = {};
      query.body.track_total_hits = true;
      query.body._source = ['id', 'release', 'configuration', 'assigneeGroup'];
      query.body.sort = [
        "_score",
        {
            "id": {
                "order": "asc"
            }
        }
      ]
      query.body.query = {
        "bool": {
          "must": [
              {
                  "match_all": {}
              }
          ],
          "filter": [
              {
                  "bool": {
                      "must": [
                          {
                              "term": {
                                  "shadow": false
                              }
                          }
                      ]
                  }
              }
          ]
        }
      }
      //define from
      query.body.from = (pageNumber) * pageSize;
      //define size
      query.body.size = pageSize;

      // dataproviders should only see assigned projects
      if (Principal.isDataprovider() && !Principal.isPublisher()) {
        var loginName = Principal.loginName();
        var filter = {
          "bool": {
              "must": [
                  {
                    "term": {
                      "configuration.dataProviders": loginName
                    }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }

      // tab filter
      if (ctrl.currentTab && ctrl.currentTab.group) {
        var requirement = "configuration.requirements.is" + ctrl.currentTab.group.charAt(0).toUpperCase() + ctrl.currentTab.group.slice(1)  + "Required"
        var filter = {
          "bool": {
              "must": [
                  {
                      "term": {
                          [requirement]: true
                      }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }

      // Project filter
      if (ctrl.selectedProject) {
        var filter = {
          "bool": {
              "must": [
                  {
                      "term": {
                          "id": ctrl.selectedProject.id
                      }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }

      // zugewiesen an
      if (ctrl.selectedAssignedGroup) {
        var filter = {
          "bool": {
              "must": [
                  {
                    "term": {
                        "assigneeGroup": ctrl.selectedAssignedGroup
                    }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }

      // Release status
      if (ctrl.selectedReleaseState) {
        var filterReleased = {
          'bool': {
              'must': [
                  {
                    'exists': {
                      'field': 'release'
                    }
                  }
              ]
          }
        }
        var filterUnreleased = {
          "bool": {
              "must_not": [
                {
                  'exists': {
                    'field': 'release'
                  }
                }
              ]
          }
        }
        ctrl.selectedReleaseState === "true" ? query.body.query.bool.filter.push(filterReleased) : query.body.query.bool.filter.push(filterUnreleased);
      }

      // Data package component filters
      if (ctrl.selectedFiltersDataPackage && ctrl.selectedFiltersDataPackage.length > 0) {
        for (var selectedFilter of ctrl.selectedFiltersDataPackage) {
          var fieldName = 'configuration.requirements.is' + selectedFilter + 'Required';
          var filter = {
            'bool': {
                'must': [
                    {
                      'term': {
                        [fieldName]: true
                      }
                    }
                ]
            }
          }
          query.body.query.bool.filter.push(filter);
        }
      }

      // additional user service remarks
      if (ctrl.selectedAdditionalInfo) {
        var filter = {
          "bool": {
              "must": [
                  {
                    "term": {
                        "hasUserServiceRemarks": ctrl.selectedAdditionalInfo === "true" ? true : false
                    }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }

      return ElasticSearchClient.search(query);
    }

    /**
     * Method to transform data from Elasticsearch into usable result entries
     */
    var readESData = function() {
      var resultList = []
      for (var entry of ctrl.esData) {
        resultList.push(entry._source);
      }
      ctrl.overview = {};
      ctrl.overview.data = resultList;
    }

    /**
     * Method for querying available projects. Query is sensitive to currently selected
     * tab and active user.
     * @param {string} searchTerm search term the projects are searched by
     * @returns a list of available projects
     */
    var searchProjectsById = function(searchTerm) {
      var query = {};
      query.preference = clientId;
      query.index = "data_acquisition_projects";
      query.body = {};
      query.body.track_total_hits = true;
      query.body._source = ['id', 'release', 'configuration', 'assigneeGroup'];
      query.body.sort = [
        "_score",
        {
            "id": {
                "order": "asc"
            }
        }
      ];
      query.body.query = {
        "bool": {
          "must": [
              {
                  "match": {
                    "id.ngrams": {
                      "query": searchTerm || '',
                      "operator": "AND",
                      "minimum_should_match": "100%",
                      "zero_terms_query": "ALL"
                  }
                  }
              }
          ],
          "filter": [
              {
                  "bool": {
                      "must": [
                          {
                              "term": {
                                  "shadow": false
                              }
                          }
                      ]
                  }
              }
          ]
        }
      }

      // dataproviders should only see assigned projects
      if (Principal.isDataprovider() && !Principal.isPublisher()) {
        var loginName = Principal.loginName();
        var filter = {
          "bool": {
              "must": [
                  {
                    "term": {
                      "configuration.dataProviders": loginName
                    }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }

      // only projects belonging to the currently selected tab are included
      if (ctrl.currentTab && ctrl.currentTab.group) {
        var requirement = "configuration.requirements.is" + ctrl.currentTab.group.charAt(0).toUpperCase() + ctrl.currentTab.group.slice(1)  + "Required"
        var filter = {
          "bool": {
              "must": [
                  {
                      "term": {
                          [requirement]: true
                      }
                  }
              ]
          }
        }
        query.body.query.bool.filter.push(filter);
      }
      //define from
      query.body.from = 0;
      //define size
      query.body.size = 10000;
      return ElasticSearchClient.search(query);
    }

    

    var init = function() {
      ctrl.userHasProjects = false;
      PageMetadataService.setPageTitle('data-acquisition-project-' +
        'management.project-overview.header');
      BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
          name});
      var page = $stateParams.page ? $stateParams.page - 1 : 0;
      
      searchWithFilter(page, stateParamsLimit).then(function(result) {
        ctrl.esData = result.hits.hits;
        ctrl.totalHits = result.hits.total.value;
        ctrl.pagination.selectedPageNumber = page;
        ctrl.pagination.totalItems = result.hits.total.value;
        ctrl.pagination.itemsPerPage = stateParamsLimit;
        readESData();
        if (ctrl.overview.data.length > 0) {
          ctrl.userHasProjects = true;
        }
      });
      
      ctrl.currentTab = tabs[0];
      ctrl.selectedAssignedGroup = null;
      ctrl.selectedReleaseState = null;
      ctrl.selectedFiltersDataPackage = null;
      ctrl.selectedAdditionalInfo = null;
    };

    /**
     * Method for handling page changes. Every time a page is changed Elasticsearch is queried
     * for the results of the current page.
     */
    ctrl.onPageChange = function() {
      searchWithFilter(ctrl.pagination.selectedPageNumber - 1, stateParamsLimit).then(function(result) {
        ctrl.esData = result.hits.hits;
        ctrl.totalHits = result.hits.total.value;
        ctrl.pagination.totalItems = result.hits.total.value;
        ctrl.pagination.itemsPerPage = stateParamsLimit;
        readESData();
      });
    };

    /**
     * Method for handling tab changes. Every time the tab is switched Elasticsearch is queried
     * for selected tab. Previously active filters are reset.
     * @param {*} tab the configuration object of the selected tab
     */
    ctrl.onSelectedTabChanged = function(tab) {
      ctrl.currentTab = tab;
      $scope.tabs = tabs;
      // reset filters
      ctrl.selectedProject = null;
      ctrl.selectedAssignedGroup = null;
      ctrl.selectedReleaseState = null;
      ctrl.selectedFiltersDataPackage = null;
      ctrl.selectedAdditionalInfo = null;
      searchWithFilter(0, stateParamsLimit).then(function(result) {
        ctrl.esData = result.hits.hits;
        ctrl.totalHits = result.hits.total.value;
        ctrl.pagination.selectedPageNumber = $stateParams.page ? $stateParams.page - 1 : 0;
        ctrl.pagination.totalItems = result.hits.total.value;
        ctrl.pagination.itemsPerPage = stateParamsLimit;
        readESData();
      });
    };

    /**
     * Opens the project cockpit of the seleted project.
     * @param {*} projectId 
     */
    ctrl.openProjectCockpit = function(projectId) {
      $state.go('project-cockpit', {id: projectId});
    };

    /**
     * Closes the select menu.
     */
    $scope.closeSelectMenu = function() {
      $mdSelect.hide();
    };

    /**
     * Method for filtering the list of available data acquisition projects 
     * according to the filter set. Available filters are:
     *    - project id
     *    - project type (all, data package or analysis package)
     *    - assigned user group
     *    - release state
     *    - if variables, questions, publications an/or concepts are required (data packages only)
     *    - if additional remarks for the user service are given (data packages only)
     * Filtering is performed on cached data to avoid repeatedly querying the database.
     */
    ctrl.search = function() {
      searchWithFilter(0, stateParamsLimit).then(function(result) {
        ctrl.esData = result.hits.hits;
        ctrl.totalHits = result.hits.total.value;
        ctrl.pagination.selectedPageNumber = $stateParams.page ? $stateParams.page - 1 : 0;
        ctrl.pagination.totalItems = result.hits.total.value;
        ctrl.pagination.itemsPerPage = stateParamsLimit;
        readESData();
      });
    };

    // Query for collecting available data acquisition projects
    ctrl.searchProjects = function(searchTerm) {
      return searchProjectsById(searchTerm).then(function(result) {
        var projectList = [];
        for (var res of result.hits.hits) {
          projectList.push(res._source)
        }
        return projectList;
      });
    };

    // Information for the different tabs
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
    }];

    $scope.tabs = tabs;    

    init();
  }]);

