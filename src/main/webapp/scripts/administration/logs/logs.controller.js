'use strict';

angular.module('metadatamanagementApp').controller('LogsController',
  function($scope, LogsResource, PageTitleService, $translate) {
    $translate('administration.logs.title').then(PageTitleService.setPageTitle);
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
