'use strict';

angular.module('metadatamanagementApp')
  .controller('ProjectOverviewController', [
  '$stateParams',
  '$state',
  '$scope',
  '$mdSelect',
  'BreadcrumbService',
  'PageMetadataService',
  'DataAcquisitionProjectRepositoryClient', 'SearchDao', '$timeout', function($stateParams, $state, $scope, $mdSelect,
    BreadcrumbService, PageMetadataService,
    DataAcquisitionProjectRepositoryClient, SearchDao, $timeout) {
    var ctrl = this;
    var limit = $stateParams.limit ? $stateParams.limit : 10;

    ctrl.pagination = {
      selectedPageNumber: $stateParams.page ? $stateParams.page : 1,
      totalItems: null,
      itemsPerPage: 100
    };

    var fetchData = function(page) {
      console.log("fetching ...")
      DataAcquisitionProjectRepositoryClient
      .findByIdLikeOrderByIdAsc('', page, limit).then(function(result) {
        ctrl.overview = {};
        ctrl.overview.data = result.data.dataAcquisitionProjects;
        ctrl.pagination.totalItems = result.data.page.totalElements;
        ctrl.pagination.itemsPerPage = result.data.page.size;
        ctrl.pagination.selectedPageNumber = result.data.page.number + 1;
      });
    };

    var init = function() {
      PageMetadataService.setPageTitle('data-acquisition-project-' +
        'management.project-overview.header');
      BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
          name});
      var page = $stateParams.page ? $stateParams.page - 1 : 0;
      fetchData(page);
      ctrl.selectedAssignedGroup = null;
      ctrl.selectedReleaseState = null;
      ctrl.selectedFiltersDataPackage = null;
      ctrl.selectedAdditionalInfo = null;
    };

    ctrl.onPageChange = function() {
      console.log("Page change")
      fetchData(ctrl.pagination.selectedPageNumber - 1);
    };

    ctrl.openProjectCockpit = function(projectId) {
      $state.go('project-cockpit', {id: projectId});
    };

    $scope.closeSelectMenu = function() {
      $mdSelect.hide();
    };

    $scope.selectedAssignedGroup = null;

    /**
     * Mehtod for handling changes of assignee filter
     */
    ctrl.onAssignedGroupFilterChange = function() {
      console.log("CHANGE", ctrl.selectedAssignedGroup);
      console.log(ctrl.overview.data)
      ctrl.search();
    }

    // tolle Suchfunktion
    ctrl.search = function() {
      DataAcquisitionProjectRepositoryClient
        .findByIdLikeOrderByIdAsc("").then(function(result) {
        var projectCache = result.data.dataAcquisitionProjects;
        // todo- CACHE
        projectCache = filterPackageType(projectCache);

        if (ctrl.selectedProject) {
          projectCache = [ctrl.selectedProject];
        }
 
        if (ctrl.selectedAssignedGroup) {
          projectCache = projectCache.filter(project => project.assigneeGroup === ctrl.selectedAssignedGroup);
        }

        if (ctrl.selectedReleaseState) {
          projectCache = ctrl.selectedReleaseState === "released" ?
            projectCache.filter((project) => project.release != undefined) :
            projectCache = projectCache.filter((project) => project.release === undefined);
        }

        if (ctrl.selectedFiltersDataPackage.length > 0) {
          console.log(ctrl.selectedFiltersDataPackage)
          for (filter of ctrl.selectedFiltersDataPackage) {
            console.log(filter)
              projectCache = projectCache.filter(project => project.configuration
              .requirements[filter + 'Required'] === true)
          }
        }

        if (ctrl.selectedAdditionalInfo) {
          // todo
          projectCache = ctrl.selectedAdditionalInfo === "true" ?
            projectCache.filter((project) => project.hasAdditionalInfo === true) :
            projectCache.filter((project) => project.hasAdditionalInfo === false);
        }

        
        
        ctrl.overview.data = projectCache;
      }) 
    }

    //Query for searching in project list
    ctrl.searchProjects = function(query) {
      return DataAcquisitionProjectRepositoryClient
        .findByIdLikeOrderByIdAsc(query).then(function(result) {
          return filterPackageType(result.data.dataAcquisitionProjects);
        });
    };

    //Update the state for the current project
    ctrl.onSelectedProjectChanged = function(project) {
      ctrl.search();
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
    }];
    $scope.tabs = tabs;

    $scope.onSelectedTabChanged = function(tab) {
      ctrl.currentTab = tab;
      $scope.tabs = tabs;
      // reset filters
      ctrl.searchTerm = null;
      ctrl.selectedAssignedGroup = null;
      ctrl.selectedReleaseState = null;
      ctrl.selectedFiltersDataPackage = null;
      ctrl.selectedAdditionalInfo = null;
      DataAcquisitionProjectRepositoryClient
        .findByIdLikeOrderByIdAsc("").then(function(result) {
          ctrl.overview.data = filterPackageType(result.data.dataAcquisitionProjects);
      });
    };

    /**
     * Method to filter relevant projects according to selected tab
     */
    var filterPackageType = function(results) {
      if (ctrl.currentTab.group === "dataPackages") {
        return results.filter(project => project.configuration.requirements.dataPackagesRequired === true);
      } else if (ctrl.currentTab.group === "analysisPackages") {
        return results.filter(project => project.configuration.requirements.analysisPackagesRequired === true);
      } else {
        return results;
      }
    }

    init();
  }]);

