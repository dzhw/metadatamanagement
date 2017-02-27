'use strict';

angular.module('metadatamanagementApp').controller('DisclosureController',
  function(PageTitleService, $state, ToolbarHeaderService) {
    PageTitleService.setPageTitle('disclosure.title');
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
