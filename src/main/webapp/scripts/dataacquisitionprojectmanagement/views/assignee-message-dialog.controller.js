'use strict';
angular.module('metadatamanagementApp')
  .controller('AssigneeMessageDialogController', function($scope, $mdDialog,
                                                          locals) {
    $scope.input = '';
    $scope.hide = $mdDialog.hide;
    $scope.recipient = locals.recipient;

    $scope.cancel = $mdDialog.cancel;

    $scope.confirm = function() {
      $mdDialog.hide($scope.input);
    };
  });
