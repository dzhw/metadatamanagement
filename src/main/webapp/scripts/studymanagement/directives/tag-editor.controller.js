'use strict';

angular.module('metadatamanagementApp')
  .controller('TagEditorController', function($scope) {
    if (angular.isUndefined($scope.tags)) {
      $scope.tags = {
        de: [],
        en: []
      };
    } else if (angular.isUndefined($scope.tags.de)) {
      $scope.tags.de = [];
    } else if (angular.isUndefined($scope.tags.en)) {
      $scope.tags.en = [];
    }
  });
