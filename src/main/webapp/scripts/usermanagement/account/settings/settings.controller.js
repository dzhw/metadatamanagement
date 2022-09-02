'use strict';

angular.module('metadatamanagementApp').controller('SettingsController',
  function($state, BreadcrumbService,
    PageMetadataService) {
    PageMetadataService.setPageTitle('global.menu.account.settings');

    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
