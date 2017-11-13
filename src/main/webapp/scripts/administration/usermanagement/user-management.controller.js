'use strict';

angular.module('metadatamanagementApp').controller('UserManagementController',
  function($scope, UserResource, ParseLinks, $state,
    PageTitleService, ToolbarHeaderService, $mdDialog, $uibModal) {
    PageTitleService.setPageTitle('user-management.home.title');
    $scope.users = [];
    $scope.page = 0;
    $scope.loadAll = function() {
      UserResource.query({
        page: $scope.page,
        //jscs:disable
        per_page: 20
          //jscs:enable
      }, function(result, headers) {
        $scope.links = ParseLinks.parse(headers('link'));
        $scope.users = result;
      });
    };

    $scope.loadPage = function(page) {
      $scope.page = page;
      $scope.loadAll();
    };
    $scope.loadAll();

    $scope.setActive = function(user, isActivated) {
      user.activated = isActivated;
      UserResource.update(user, function() {
        $scope.loadAll();
      });
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

    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
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
  });
