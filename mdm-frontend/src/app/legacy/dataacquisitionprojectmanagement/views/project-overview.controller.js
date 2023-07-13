'use strict';

angular.module('metadatamanagementApp')
  .controller('ProjectOverviewController', [
  '$stateParams',
  '$state',
  'BreadcrumbService',
  'PageMetadataService',
  'DataAcquisitionProjectRepositoryClient', function($stateParams, $state,
    BreadcrumbService, PageMetadataService,
    DataAcquisitionProjectRepositoryClient) {
    var ctrl = this;
    var limit = $stateParams.limit ? $stateParams.limit : 10;

    ctrl.pagination = {
      selectedPageNumber: $stateParams.page ? $stateParams.page : 1,
      totalItems: null,
      itemsPerPage: 10
    };

    var fetchData = function(page) {
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
    };

    ctrl.onPageChange = function() {
      fetchData(ctrl.pagination.selectedPageNumber - 1);
    };

    ctrl.openProjectCockpit = function(projectId) {
      $state.go('project-cockpit', {id: projectId});
    };

    init();
  }]);

