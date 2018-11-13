'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function(PageTitleService, $state, ToolbarHeaderService) {
    PageTitleService.setPageTitle('projectcockpit.title');
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
