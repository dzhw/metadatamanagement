'use strict';

angular.module('metadatamanagementApp').controller('DisclosureController',
  function(PageTitleService, $state, BreadcrumbService) {
    PageTitleService.setPageTitle('disclosure.title');
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
