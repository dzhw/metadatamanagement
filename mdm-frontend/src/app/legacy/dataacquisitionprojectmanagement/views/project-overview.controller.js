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
  'ElasticSearchClient', 
  'Principal',
  'dataAcquisitionProjectSearchService', 
  '$location',
  'CleanJSObjectService', function($stateParams, $state, $scope, $mdSelect,
    BreadcrumbService, PageMetadataService,
    ElasticSearchClient, Principal, dataAcquisitionProjectSearchService, $location, CleanJSObjectService) {
    var ctrl = this;
    var stateParamsLimit = $stateParams.limit ? $stateParams.limit : 10;
    ctrl.userHasProjects = false;
    ctrl.isSearching = false;

    // Configuration for tabs
    var tabs = [{
      title: 'search-management.tabs.all',
      elasticSearchType: undefined,
      count: null
    }, {
      title: 'search-management.tabs.data_packages',
      elasticSearchType: 'data_packages',
      count: null,
      group: 'dataPackages'
    }, {
      title: 'search-management.tabs.analysis_packages',
      elasticSearchType: 'analysis_packages',
      count: null,
      group: 'analysisPackages'
    }];
    $scope.tabs = tabs; 
    ctrl.currentTab = tabs[0];

    ctrl.pagination = {
      selectedPageNumber: $stateParams.page ? $stateParams.page : 1,
      totalItems: null,
      itemsPerPage: 10
    };

    /**
     * Method to transform data from Elasticsearch into usable result entries.
     */
    var readESData = function(result) {
      var resultList = []
      for (var entry of result) {
        resultList.push(entry._source);
      }
      ctrl.overview = {};
      ctrl.overview.data = resultList;
    }

    /**
     * Method for searching across available projects. Query is sensitive to currently selected
     * tab and active user.
     * @param {string} searchTerm term by which the projects are searched
     * @returns a list of available projects
     */
    var searchProjectsById = function(searchTerm) {
      var query = dataAcquisitionProjectSearchService.createSearchQueryForProjectsById(
        ctrl.currentTab.group ? ctrl.currentTab.group : null,
        Principal.isDataprovider() && !Principal.isPublisher() ? true : false,
        searchTerm,
        Principal.loginName())
      return ElasticSearchClient.search(query);
    }

    

     /**
     * Method to write search filter values as URL query params.
     */
     var writeSearchParamsToLocation = function() {
      var locationSearch = {};
      locationSearch.page = ctrl.pagination.selectedPageNumber ? ctrl.pagination.selectedPageNumber : 1;
      locationSearch.size = $stateParams.size ? $stateParams.size : 10;
      locationSearch.tab = ctrl.currentTab && ctrl.currentTab.group ? ctrl.currentTab.group : null;
      locationSearch.assignee = ctrl.selectedAssignedGroup ? ctrl.selectedAssignedGroup : null;
      locationSearch.release = ctrl.selectedReleaseState ? ctrl.selectedReleaseState : null;
      locationSearch.packageFilter = ctrl.selectedFiltersDataPackage && ctrl.selectedFiltersDataPackage.length > 0 ? ctrl.selectedFiltersDataPackage : null;
      locationSearch.additional = ctrl.selectedAdditionalInfo ? ctrl.selectedAdditionalInfo : null;
      locationSearch.project = ctrl.selectedProject ? ctrl.selectedProject.id : null;
      
      locationChanged = !angular.equals($location.search(), locationSearch);
      $location.search(locationSearch);
    };

    /**
     * Method to read search filter values from URL query params and apply them.
     */ 
    var readSearchParamsFromLocation = function() {
      var locationSearch = $location.search();
      if (CleanJSObjectService.isNullOrEmpty(locationSearch)) {
        ctrl.pagination.selectedPageNumber = 1;
        ctrl.pagination.itemsPerPage = 10;
        ctrl.currentTab = tabs[0];
        ctrl.selectedAssignedGroup = null;
        ctrl.selectedReleaseState = null;
        ctrl.selectedFiltersDataPackage = null;
        ctrl.selectedAdditionalInfo = null;
        ctrl.selectedProject = null;
      } else {
        if (locationSearch.page != null) {
          ctrl.pagination.selectedPageNumber = parseInt(locationSearch.page);
        } else {
          ctrl.pagination.selectedPageNumber = 1;
        }
        if (locationSearch.size != null) {
          ctrl.pagination.itemsPerPage = parseInt(locationSearch.size);
        } else {
          ctrl.pagination.itemsPerPage = 10;
        }
        if (locationSearch.tab) {
          ctrl.currentTab = tabs.find(tab => tab.group === locationSearch.tab);
          ctrl.selectedTabIndex = tabs.findIndex(tab => tab.group === locationSearch.tab);
        } else {
          ctrl.currentTab = tabs[0];
          ctrl.selectedTabIndex = 0;
        }
        if (locationSearch.project) {
          // var list = searchProjectsById(locationSearch.project);
          ctrl.searchProjects(locationSearch.project).then(function(list) {
            ctrl.selectedProject = list.length > 0 ? list[0] : null;
          });
          
        }
        ctrl.selectedAssignedGroup = locationSearch.assignee ? locationSearch.assignee : null;
        ctrl.selectedReleaseState = locationSearch.release ? locationSearch.release : null;
        ctrl.selectedFiltersDataPackage = locationSearch.packageFilter ? locationSearch.packageFilter : null;
        ctrl.selectedAdditionalInfo = locationSearch.additional ? locationSearch.additional : null;
      }
    };

    /**
     * Watcher for recognizing changes in URL query params.
     */
    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue, oldValue) {
      if (!angular.equals(oldValue, newValue)) {
        ctrl.isSearching = true;
        // call search function from new ProjectSearchService
        var query = dataAcquisitionProjectSearchService.createFilterQueryForProjects(
          ctrl.pagination.selectedPageNumber ? ctrl.pagination.selectedPageNumber - 1 : 0,  // pageNumber to Index
          stateParamsLimit,
          ctrl.currentTab.group ? ctrl.currentTab.group : null,
          Principal.isDataprovider() && !Principal.isPublisher() ? true : false,
          ctrl.selectedProject ? ctrl.selectedProject.id : null,
          ctrl.selectedAssignedGroup,
          ctrl.selectedReleaseState,
          ctrl.selectedFiltersDataPackage,
          ctrl.selectedAdditionalInfo,
          Principal.loginName()
        )
        ElasticSearchClient.search(query).then(function(result) {
          ctrl.totalHits = result.hits.total.value;
          ctrl.pagination.totalItems = result.hits.total.value;
          ctrl.pagination.itemsPerPage = stateParamsLimit;
          readESData(result.hits.hits);
          if (ctrl.overview.data.length > 0) {
            ctrl.userHasProjects = true;
          }
          ctrl.currentTab.count = ctrl.pagination.totalItems;
          ctrl.isSearching = false;
        })
      }
    });

    var init = function() {
      PageMetadataService.setPageTitle('data-acquisition-project-' +
        'management.project-overview.header');
      BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
          name});

      ctrl.isSearching = true;
      readSearchParamsFromLocation();
      writeSearchParamsToLocation();
      
      // get query and perform initial search 
      var query = dataAcquisitionProjectSearchService.createFilterQueryForProjects(
        $stateParams.page ? $stateParams.page - 1 : 0, 
        stateParamsLimit,
        ctrl.currentTab.group ? ctrl.currentTab.group : null,
        Principal.isDataprovider() && !Principal.isPublisher() ? true : false,
        ctrl.selectedProject,
        ctrl.selectedAssignedGroup,
        ctrl.selectedReleaseState,
        ctrl.selectedFiltersDataPackage,
        ctrl.selectedAdditionalInfo,
        Principal.loginName()
      )
      
      ElasticSearchClient.search(query).then(function(result) {
        ctrl.totalHits = result.hits.total.value;
        ctrl.pagination.selectedPageNumber = $stateParams.page ? $stateParams.page : 1;
        ctrl.pagination.totalItems = result.hits.total.value;
        ctrl.pagination.itemsPerPage = stateParamsLimit;
        readESData(result.hits.hits);
        if (ctrl.overview.data.length > 0) {
          ctrl.userHasProjects = true;
        }
        ctrl.currentTab.count = ctrl.pagination.totalItems;
        ctrl.isSearching = false;
      })      
    };

    /**
     * Method for collecting available data acquisition projects filtered by ID.
     * @param {string} searchTerm the search term to be used in the query
     * @returns a list of available projects
     */
    ctrl.searchProjects = function(searchTerm) {
      return searchProjectsById(searchTerm).then(function(result) {
        var projectList = [];
        for (var res of result.hits.hits) {
          projectList.push(res._source)
        }
        return projectList;
      });
    }; 

    /**
     * Method for handling resetting of all filters. All filter values are set
     * as unselected (null). Page is reset to the first page. The tab count is reset 
     * before resetting the tab to the 'all data' tab. URL query params are updated.
     */
    ctrl.onClearFilters = function() {
      ctrl.selectedProject = null;
      ctrl.selectedAssignedGroup = null;
      ctrl.selectedReleaseState = null;
      ctrl.selectedFiltersDataPackage = null;
      ctrl.selectedAdditionalInfo = null;
      ctrl.pagination.selectedPageNumber = 1;
      if (ctrl.currentTab) {
        ctrl.currentTab.count = null;
      }
      ctrl.currentTab = tabs[0];
      ctrl.selectedTabIndex = 0;
      writeSearchParamsToLocation();
    }

    /**
     * Method for handling filter changes. Every time a filter is set the query params in the URL
     * are updated. The page is reset to the first page.
     */
    ctrl.onFilterValueChanged = function() {
      ctrl.pagination.selectedPageNumber = 1;
      writeSearchParamsToLocation();
    };   

    /**
     * Method for handling page changes. Every time a page is changed the query params in the URL
     * are updated.
     */
    ctrl.onPageChange = function() {
      writeSearchParamsToLocation();
    };

    /**
     * Method for handling tab changes. Every time the tab is switched previously active filters are reset.
     * The page is set to the first page.
     * @param {*} tab the configuration object of the selected tab
     */
    ctrl.onSelectedTabChanged = function(tab) {
      if (ctrl.currentTab) {
        ctrl.currentTab.count = null;
      }

      if (!angular.equals(ctrl.currentTab, tab)) {
        ctrl.selectedProject = null;
        ctrl.selectedAssignedGroup = null;
        ctrl.selectedReleaseState = null;
        ctrl.selectedFiltersDataPackage = null;
        ctrl.selectedAdditionalInfo = null;
        ctrl.pagination.selectedPageNumber = 1;
      }
      ctrl.currentTab = tab;
      $scope.tabs = tabs;
      writeSearchParamsToLocation();
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

    init();
  }]);

