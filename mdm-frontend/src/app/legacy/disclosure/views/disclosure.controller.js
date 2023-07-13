'use strict';

angular.module('metadatamanagementApp').controller('DisclosureController', [
  'PageMetadataService',
  '$state',
  'BreadcrumbService',
  function(PageMetadataService, $state, BreadcrumbService) {
    PageMetadataService.setPageTitle('disclosure.title');
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  }]);

