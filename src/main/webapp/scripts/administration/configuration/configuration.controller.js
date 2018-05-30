/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ConfigurationController',
  function($scope, $state, ToolbarHeaderService, ConfigurationService,
    PageTitleService) {
    PageTitleService.setPageTitle('administration.configuration.title');
    ConfigurationService.get().then(function(configuration) {
      $scope.configuration = _.values(configuration[0].application.beans);
    });
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
    name});
  });
