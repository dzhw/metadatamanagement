'use strict';

angular.module('metadatamanagementApp').controller('ConfigurationController',
  function($scope, ConfigurationService, $translate, PageTitleService) {
    $translate('administration.configuration.title').then(
      PageTitleService.setPageTitle);
    ConfigurationService.get().then(function(configuration) {
      $scope.configuration = configuration;
    });
  });
