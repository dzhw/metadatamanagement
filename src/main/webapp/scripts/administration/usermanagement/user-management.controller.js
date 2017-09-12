/*globals $ */
'use strict';

angular.module('metadatamanagementApp').controller('UserManagementController',
  function($scope, UserResource, ParseLinks, LanguageService, $state,
    PageTitleService, ToolbarHeaderService, WebSocketService) {
    PageTitleService.setPageTitle('user-management.home.title');
    $scope.users = [];
    $scope.authorities = ['ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN'];
    LanguageService.getAll().then(function(languages) {
      $scope.languages = languages;
    });

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
        $scope.clear();
      });
    };

    $scope.showUpdate = function(login) {
      UserResource.get({
        login: login
      }, function(result) {
        $scope.user = result;
        $('#saveUserModal').modal('show');
      });
    };

    $scope.save = function() {
      UserResource.update($scope.user, function() {
        $scope.refresh();
      });
    };

    $scope.refresh = function() {
      $scope.loadAll();
      $('#saveUserModal').modal('hide');
      $scope.clear();
    };

    $scope.clear = function() {
      $scope.user = {
        id: null,
        login: null,
        firstName: null,
        lastName: null,
        email: null,
        activated: null,
        langKey: null,
        createdBy: null,
        createdDate: null,
        lastModifiedBy: null,
        lastModifiedDate: null,
        resetDate: null,
        resetKey: null,
        authorities: null
      };
      $scope.editForm.$setPristine();
      $scope.editForm.$setUntouched();
    };
    ToolbarHeaderService.updateToolbarHeader({'stateName': $state.current.
    name});

    $scope.sendMessageToAllUsers = function() {
      WebSocketService.sendMessageToAllUsers($scope.userMessage);
    };
  });
