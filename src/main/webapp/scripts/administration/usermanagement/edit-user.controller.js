'use strict';

angular.module('metadatamanagementApp').controller('EditUserController',
  function($scope, $uibModalInstance,
    user, UserResource, parent, LanguageService) {
    $scope.user = user;
    $scope.authorities = ['ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN'];
    LanguageService.getAll().then(function(languages) {
      $scope.languages = languages;
    });

    $scope.clear = function() {
      $uibModalInstance.dismiss('cancel');
    };

    $scope.save = function() {
      UserResource.update($scope.user, function() {
        parent.refresh();
        $uibModalInstance.dismiss('cancel');
      });
    };
  });
