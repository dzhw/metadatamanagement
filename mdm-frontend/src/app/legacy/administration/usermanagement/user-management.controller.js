/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('UserManagementController', [
  '$scope',
  'UserResource',
  'ParseLinks',
  '$state',
  'PageMetadataService',
  'BreadcrumbService',
  '$mdDialog',
  '$uibModal',
  'DataAcquisitionProjectRepositoryClient',
  'CommonDialogsService',
  function($scope, UserResource, ParseLinks, $state,
    PageMetadataService, BreadcrumbService, $mdDialog, $uibModal,
    DataAcquisitionProjectRepositoryClient, CommonDialogsService) {
    PageMetadataService.setPageTitle('user-management.home.title');
    $scope.users = [];
    $scope.page = 0;
    $scope.searchFilter = null;
    $scope.loadAll = function () {
      UserResource.searchFilter({
        searchFilter: $scope.searchFilter,
        page: $scope.page,
        //jscs:disable
        per_page: 20
        //jscs:enable
      }, function (result, headers) {
        $scope.links = ParseLinks.parse(headers('link'));
        $scope.users = result;
      });
    };

    $scope.loadPage = function(page) {
      $scope.page = page;
      $scope.loadAll();
    };
    $scope.loadAll();

    var joinProjectIds = function(projects) {
      return _.join(_.map(projects, function(project) {
        return project.id;
      }), ', ');
    };

    $scope.setActive = function(user, isActivated) {
      var userHasBeenActivated = user.activated;

      if (userHasBeenActivated && !isActivated) {
        DataAcquisitionProjectRepositoryClient.findAssignedProjects(
            user.login).then(function(response) {
                var projects = response.data;
                if (projects.length === 0) {
                  CommonDialogsService.showConfirmDialog(
                    'global.common-dialogs' +
                    '.confirm-deactivate-user-with-assigned-projects.title',
                    {},
                    'global.common-dialogs' +
                    '.confirm-deactivate-user-with-assigned-projects' +
                    '.content-without-assigned-projects',
                    {},
                    null
                  ).then(function success() {
                    user.activated = isActivated;
                    UserResource.update(user, function() {
                      $scope.loadAll();
                    });
                  }, function error() {
                    user.activated = true;
                  });
                } else {
                  var projectIds = joinProjectIds(projects);
                  CommonDialogsService.showConfirmDialog(
                    'global.common-dialogs' +
                    '.confirm-deactivate-user-with-assigned-projects.title',
                    {},
                    'global.common-dialogs' +
                    '.confirm-deactivate-user-with-assigned-projects.content',
                    {projects: projectIds},
                    null
                  ).then(function success() {
                      user.activated = isActivated;
                      UserResource.update(user, function() {
                        $scope.loadAll();
                      });
                    }, function error() {
                      user.activated = true;
                    }
                  );
                }
              }).catch(function() {
                user.activated = true;
              });
      } else {
        CommonDialogsService.showConfirmDialog(
          'global.common-dialogs' +
          '.confirm-activate-user.title',
          {},
          'global.common-dialogs' +
          '.confirm-activate-user.content',
          {},
          null
        ).then(function success() {
          user.activated = isActivated;
          UserResource.update(user, function() {
            $scope.loadAll();
          });
        }, function error() {
          user.activated = false;
        });
      }
    };

    $scope.showUpdate = function(login) {
      UserResource.get({
        login: login
      }, function(result) {
        $uibModal.open({
            templateUrl: 'scripts/administration/usermanagement/' +
              'edit-user.html.tmpl',
            controller: 'EditUserController',
            size: 'lg',
            resolve: {
              user: result,
              parent: $scope
            }
          });
      });
    };

    $scope.refresh = function() {
      $scope.loadAll();
    };

    BreadcrumbService.updateToolbarHeader({'stateName': $state.current.
    name});

    $scope.openUserMessageDialog = function(event) {
      $mdDialog.show({
          controller: 'UserMessageDialogController',
          templateUrl: 'scripts/administration/' +
            'usermanagement/user-message-dialog.html.tmpl',
          clickOutsideToClose: false,
          targetEvent: event,
          fullscreen: true
        });
    };

    $scope.hideMobileKeyboardAndSearch = function ($event) {
      // reset page index
      $scope.page = 0;
      // restart search with given filter 
      $scope.loadAll();
      $event.target.querySelector('#query').blur();
    };
  }]);

