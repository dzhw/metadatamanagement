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

      $scope.project.configuration.publishers =
          $scope.activeUsers.publishers.map(function(identity) {
        return identity.login;
      });
      $scope.project.configuration.dataProviders =
          $scope.activeUsers.dataProviders.map(function(identity) {
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

    $scope.searchText = {
      publishers: '',
      dataProviders: ''
    };

    $scope.selectedUser = {
      publishers: null,
      dataProviders: null
    };

    $scope.selectedUserChanged = function(user, role) {
      if (user) {
        $scope.activeUsers[role].push(user);
        $scope.changed = true;
        $scope.searchText[role] = '';
        $scope.selectedUser[role] = null;
      }
    };

    $scope.activeUsers = {
      'publishers': [],
      'dataProviders': []
    };

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
                    if (!_.includes($scope.activeUsers[key].map(function(u) {
                      return u.login;
                    }), userResult.login)) {
                      $scope.activeUsers[key].push(userResult);
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
    $scope.canDeleteUser = function(user, role) {

      function isOnlyRole(roleInternal) {
        return _.includes(user.authorities, roleInternal) &&
          $scope.activeUsers[role].filter(function(activeUser) {
            return _.includes(activeUser.authorities, roleInternal);
          }).length > 1;
      }

      var isOnly = {
        publishers: isOnlyRole('ROLE_PUBLISHER'),
        dataProviders: isOnlyRole('ROLE_DATA_PROVIDER')
      };

      if (isOnly.publishers && role === 'publishers') {
        return false;
      }
      if (isOnly.dataProviders && role === 'dateProviders') {
        return false;
      }
      if (!$scope.advancedPrivilege && role === 'publishers') {
        return false;
      }
      return true;
    };

    $state.currentPromise = null;
    $scope.searchUsers = function(search, role, roleInternal) {
      if (!search) {
        return [];
      }
      if (!$state.currentPromise) {
        $state.currentPromise = UserResource.search({
          login: search,
          role: roleInternal
        }).$promise.then(function(result) {
          $state.currentPromise = null;

          return result.filter(function(x) {
            // filter out already added users
            return $scope.activeUsers[role].map(function(u) {
              return u.login;
            }).indexOf(x.login) < 0 && _.includes(x.authorities, roleInternal);
          });
        });
      }
      return $state.currentPromise;
    };

    $scope.removeUser = function(user, role) {
      $scope.changed = true;
      $scope.activeUsers[role] = $scope.activeUsers[role]
          .filter(function(item) {
        return item.login !== user.login;
      });
    };
  });
