'use strict';

angular.module('metadatamanagementApp')
  .controller('ProjectOverviewController', function($stateParams, $state,
      DataAcquisitionProjectCollectionResource, BreadcrumbService,
      PageTitleService) {
    var ctrl = this;
    var sort = $stateParams.sort ? $stateParams.sort : 'id,asc';
    var limit = $stateParams.limit ? $stateParams.limit : 20;

    ctrl.pagination = {
      selectedPageNumber: $stateParams.page ? $stateParams.page : 1,
      totalItems: null,
      itemsPerPage: 20
    };

    var fetchData = function(page) {
      ctrl.overview = DataAcquisitionProjectCollectionResource.get({
        page: page,
        sort: sort,
        limit: limit
      });

      ctrl.overview.$promise.then(function(data) {
        ctrl.pagination.totalItems = data.page.totalElements;
        ctrl.pagination.itemsPerPage = data.page.size;
        ctrl.pagination.selectedPageNumber = data.page.number + 1;
        ctrl.pagination.itemsPerPage = data.page.size;
      });
    };

    var init = function() {
      PageTitleService.setPageTitle('data-acquisition-project-' +
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
  });
