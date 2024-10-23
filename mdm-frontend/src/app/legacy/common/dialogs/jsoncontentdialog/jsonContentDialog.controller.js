'use strict';

angular.module('metadatamanagementApp')
  .controller('DialogController', [
  '$scope',
  'content',
  '$mdDialog',
  'headerTitle',
    function($scope, content, $mdDialog, headerTitle) {
        $scope.cancel = $mdDialog.hide;
        $scope.content = content;
        $scope.headerTitle = headerTitle;
    }]);

