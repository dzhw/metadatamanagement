/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('EditUserController', [
  '$scope',
  '$uibModalInstance',
  'user',
  'UserResource',
  'parent',
  'LanguageService',
  'SimpleMessageToastService',
  'DataAcquisitionProjectRepositoryClient',
  '$q',
  function($scope, $uibModalInstance,
    user, UserResource, parent, LanguageService, SimpleMessageToastService,
    DataAcquisitionProjectRepositoryClient, $q) {
    $scope.user = user;
    $scope.authorities = ['ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER',
      'ROLE_ADMIN', 'ROLE_RELEASE_MANAGER'];
    LanguageService.getAll().then(function(languages) {
      $scope.languages = languages;
    });

    $scope.showUserStatusConfirmation = false;

    $scope.userAssignedProjects = [];
    DataAcquisitionProjectRepositoryClient.findAssignedProjects(
      $scope.user.login).then(function(response) {
          $scope.userAssignedProjects = response.data;
        });

    /**
     * Switches the user status to active if it has been deactivated before and vice versa
     */
    $scope.switchUserStatus = function() {
      user.activated = !user.activated;
      $scope.showUserStatusConfirmation = false;
    };

    $scope.hideUserStatusConfirmation = function() {
      $scope.showUserStatusConfirmation = false;
    };

    $scope.clear = function() {
      $uibModalInstance.dismiss('cancel');
    };

    // var userHasBeenActivated = user.activated;
    var userHasBeenPublisher =
      _.includes(user.authorities, 'ROLE_PUBLISHER');
    var userHasBeenDataProvider =
      _.includes(user.authorities, 'ROLE_DATA_PROVIDER');

    var joinProjectIds = function(projects) {
      return _.join(_.map(projects, function(project) {
        return project.id;
      }), ', ');
    };

    /**
     * Handles display of confirm dialog for user status changes
     */
    $scope.changeUserStatus = function() {
      $scope.showUserStatusConfirmation = true;
    };

    var checkIfUserCanBeLoosePublisherRole = function() {
      var deferred = $q.defer();
      if (userHasBeenPublisher &&
        !_.includes(user.authorities, 'ROLE_PUBLISHER')) {
        DataAcquisitionProjectRepositoryClient.findAssignedProjectsAsPublisher(
            $scope.user.login).then(function(response) {
                var projects = response.data;
                if (projects.length === 0) {
                  deferred.resolve();
                } else {
                  var projectIds = joinProjectIds(projects);
                  SimpleMessageToastService.openAlertMessageToast(
                    'user-management.error.user.must-not-loose-publisher-role',
                    {projectIds: projectIds});
                  deferred.reject();
                }
              }).catch(deferred.reject);
      } else {
        deferred.resolve();
      }
      return deferred.promise;
    };

    var checkIfUserCanBeLooseDataProviderRole = function() {
      var deferred = $q.defer();
      if (userHasBeenDataProvider &&
        !_.includes(user.authorities, 'ROLE_DATA_PROVIDER')) {
        DataAcquisitionProjectRepositoryClient
          .findAssignedProjectsAsDataProvider(
            $scope.user.login).then(function(response) {
                var projects = response.data;
                if (projects.length === 0) {
                  deferred.resolve();
                } else {
                  var projectIds = joinProjectIds(projects);
                  SimpleMessageToastService.openAlertMessageToast(
                    'user-management.error.' +
                      'user.must-not-loose-data-provider-role',
                    {projectIds: projectIds});
                  deferred.reject();
                }
              }).catch(deferred.reject);
      } else {
        deferred.resolve();
      }
      return deferred.promise;
    };

    $scope.save = function() {
      var promises = [];
      promises.push(checkIfUserCanBeLoosePublisherRole());
      promises.push(checkIfUserCanBeLooseDataProviderRole());
      $q.all(promises).then(function() {
        UserResource.update($scope.user, function() {
          parent.refresh();
          $uibModalInstance.dismiss('cancel');
        });
      });
    };
  }]);

