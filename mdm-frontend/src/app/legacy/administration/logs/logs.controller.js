'use strict';

angular.module('metadatamanagementApp').controller('LogsController', [
  '$scope',
  '$state',
  'LogsResource',
  'PageMetadataService',
  'BreadcrumbService',
  function($scope, $state, LogsResource, PageMetadataService,
  BreadcrumbService) {
    PageMetadataService.setPageTitle('administration.logs.title');
    $scope.loggers = LogsResource.findAll();

    $scope.changeLevel = function(name, level) {
      LogsResource.changeLevel({
        name: name,
        level: level
      }, function() {
        $scope.loggers = LogsResource.findAll();
      });
    };
    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});
  }]);

