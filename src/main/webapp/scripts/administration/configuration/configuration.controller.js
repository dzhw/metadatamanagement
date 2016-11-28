'use strict';

angular.module('metadatamanagementApp').controller('ConfigurationController',
  function($scope, ConfigurationService, PageTitleService) {
    PageTitleService.setPageTitle('administration.configuration.title');
    ConfigurationService.get().then(function(configuration) {
      $scope.configuration = configuration;
    });
  });
