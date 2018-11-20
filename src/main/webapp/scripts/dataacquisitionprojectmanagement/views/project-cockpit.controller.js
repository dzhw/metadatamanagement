/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($q, $scope, $state, UserResource, Principal, PageTitleService,
           ToolbarHeaderService, CurrentProjectService,
           DataAcquisitionProjectResource, SimpleMessageToastService) {

    PageTitleService.setPageTitle('project-cockpit.title');
    ToolbarHeaderService.updateToolbarHeader({
      stateName: $state.current.name
    });

    $scope.$on('current-project-changed', function() {
      $state.reload();
    });

    var selectedProject = CurrentProjectService.getCurrentProject();
    if (!selectedProject) {
      return;
    }

    $scope.saveChanges = function() {
      if (!$scope.project.configuration) {
        $scope.project.configuration = {};
      }

      $scope.project.configuration.publishers = $scope.activeUsers.filter(
        function(user) {
          return _.includes(user.authorities, 'ROLE_PUBLISHER');
        }).map(function(identity) {
        return identity.login;
      });
      $scope.project.configuration.dataProviders = $scope.activeUsers.filter(
        function(user) {
          return _.includes(user.authorities, 'ROLE_DATA_PROVIDER');
        }).map(function(identity) {
        return identity.login;
      });

      DataAcquisitionProjectResource.save(
        $scope.project,
        //Success
        function() {
          SimpleMessageToastService
            .openSimpleMessageToast(
              'saved', {
                id: $scope.project.id
              });
        },
        //Server Error
        function(error) {
          SimpleMessageToastService
            .openAlertMessageToast(
              'server-error' + error);
        }
      );
    };

    $scope.changed = false;
    $scope.searchText = '';
    $scope.selectedUser = null;
    $scope.selectedUserChanged = function(user) {
      if (user) {
        $scope.activeUsers.push(user);
        $scope.changed = true;
        $scope.searchText = '';
        $scope.selectedUser = null;
      }
    };
    $scope.activeUsers = [];

    $scope.usersFetched = false;

    // load all users assigned to the currrent project
    DataAcquisitionProjectResource.get({id: selectedProject.id}).$promise.then(
      function(project) {
        $scope.project = project;

        function getAndAddUsers(key) {
          // get users of type {key} asynchronously
          // and return promise resolving when all are fetched
          if (project.configuration[key]) {
            return $q.all(
              project.configuration[key].map(function(userLogin) {
                return (
                  UserResource.get({
                    login: userLogin
                  }).$promise.then(function(userResult) {
                    if (!_.includes($scope.activeUsers.map(function(u) {
                      return u.login;
                    }), userResult.login)) {
                      $scope.activeUsers.push(userResult);
                    }
                  })
                );
              })
            );
          } else {
            return $q.resolve([]);
          }
        }

        $q.resolve($q.all([
          getAndAddUsers('publishers'),
          getAndAddUsers('dataProviders')
        ])).then(function() {
          $scope.usersFetched = true;
        }).catch(function(error) {
          SimpleMessageToastService
            .openAlertMessageToast(
              'server-error' + error);
        });
      });

    $scope.advancedPrivileges = Principal.hasAnyAuthority(['ROLE_PUBLISHER',
      'ROLE_ADMIN']);
    $scope.canDeleteUser = function(user) {
      function isOnlyRole(role) {
        return _.includes(user.authorities, role) &&
          $scope.activeUsers.filter(function(activeUser) {
            return _.includes(activeUser.authorities, role);
          }).length > 1;
      }

      var isOnlyPublisher = isOnlyRole('ROLE_PUBLISHER');
      var isOnlyDataProvider = isOnlyRole('ROLE_DATA_PROVIDER');
      return !((!$scope.advancedPrivileges && isOnlyDataProvider) ||
        isOnlyPublisher);
    };

    $state.currentPromise = null;
    $scope.searchUsers = function(search) {
      if (!search) {
        return [];
      }
      if (!$state.currentPromise) {
        $state.currentPromise = UserResource.search({
          login: search
        }).$promise.then(function(result) {
          $state.currentPromise = null;
          return result.filter(function(x) {
            // filter out already added users
            return $scope.activeUsers.map(function(u) {
              return u.login;
            }).indexOf(x.login) < 0;
          });
        });
      }
      return $state.currentPromise;
    };

    $scope.removeUser = function(user) {
      $scope.changed = true;
      $scope.activeUsers = $scope.activeUsers.filter(function(item) {
        return item.login !== user.login;
      });
    };
  });
