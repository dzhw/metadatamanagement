'use strict';

angular.module('metadatamanagementApp').controller('DisclosureController',
  function(PageMetadataService, $state, BreadcrumbService) {
    PageMetadataService.setPageTitle('disclosure.title');
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
