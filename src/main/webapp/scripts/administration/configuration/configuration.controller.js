'use strict';

angular.module('metadatamanagementApp').controller('ConfigurationController',
  function($scope, $state, ToolbarHeaderService, ConfigurationService,
    PageTitleService) {
    PageTitleService.setPageTitle('administration.configuration.title');
    ConfigurationService.get().then(function(configuration) {
      $scope.configuration = configuration;
    });
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
