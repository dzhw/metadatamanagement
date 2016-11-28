'use strict';

angular.module('metadatamanagementApp').controller('LogsController',
  function($scope, LogsResource, PageTitleService) {
    PageTitleService.setPageTitle('administration.logs.title');
    $scope.loggers = LogsResource.findAll();

    $scope.changeLevel = function(name, level) {
      LogsResource.changeLevel({
        name: name,
        level: level
      }, function() {
        $scope.loggers = LogsResource.findAll();
      });
    };
  });
