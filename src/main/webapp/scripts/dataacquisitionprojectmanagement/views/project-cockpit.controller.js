/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($q, $scope, $state, $location, $transitions, UserResource, Principal,
           PageTitleService, LanguageService, ToolbarHeaderService,
           DataAcquisitionProjectResource, SimpleMessageToastService,
           CurrentProjectService, projectDeferred, CommonDialogsService) {

    PageTitleService.setPageTitle(
      'data-acquisition-project-management.project-cockpit.title');
    ToolbarHeaderService.updateToolbarHeader({
      stateName: $state.current.name
    });

    var registerConfirmOnDirtyHook = function() {
      var unregisterTransitionHook = $transitions.onBefore({}, function() {
        if ($scope.changed) {
          return CommonDialogsService.showConfirmOnDirtyDialog();
        }
      });

      $scope.$on('$destroy', unregisterTransitionHook);
    };

    var setProjectRequirementsDisabled = function(project) {
      var loginName = Principal.loginName();
      var publishers = _.get(project, 'configuration.publishers');
      var result;
      if (_.isArray(publishers)) {
        result = publishers.indexOf(loginName) === -1;
      } else {
        result = false;
      }
      $scope.isProjectRequirementsDisabled = result;
      return result;
    };

    var requiredTypesWatch;

    $state.loadStarted = true;

    $scope.$on('current-project-changed',
      function(event, changedProject) { // jshint ignore:line
        if (changedProject) {
          $location.url('/' + LanguageService.getCurrentInstantly() +
            '/projects/' + changedProject.id);
        }
      });

    $scope.saveChanges = function(origin) {
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
          $scope.changed = false;
          SimpleMessageToastService
            .openSimpleMessageToast(
              'data-acquisition-project-management.log-messages.' +
              'data-acquisition-project.saved', {
                id: $scope.project.id
              });
          if (origin !== 'requirements') {
            $state.reload();
          }
        },
        //Server Error
        function() {
          SimpleMessageToastService
            .openAlertMessageToast(
              'data-acquisition-project-management.log-messages.' +
              'data-acquisition-project.server-error'
            );
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
        $state.searchCache[role] = {};
      }
    };

    $scope.activeUsers = {
      publishers: [],
      dataProviders: []
    };

    $scope.fetching = false;

    // load all users assigned to the currrent project
    projectDeferred.promise.then(
      function(project) {
        $scope.project = project;
        $scope.fetching = true;
        var isProjectRequirementsDisabled =
          setProjectRequirementsDisabled(project);
        CurrentProjectService.setCurrentProject(project);

        if (requiredTypesWatch) {
          requiredTypesWatch();
        }

        if (!isProjectRequirementsDisabled &&
          project.configuration.requirements) {
          $scope.$watch(function() {
            return $scope.project.configuration.requirements;
          }, function(newVal, oldVal) {
            if (newVal !== oldVal && !$scope.changed) {
              $scope.changed = true;
            }
          }, true);
        }

        function getAndAddUsers(key) {
          // get users of type {key} asynchronously
          // and return promise resolving when all are fetched
          if (project.configuration[key]) {
            return $q.all(
              project.configuration[key].map(function(userLogin) {
                return (
                  UserResource.getPublic({
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
          $scope.fetching = false;
        }).catch(function(error) {
          SimpleMessageToastService
            .openAlertMessageToast(
              'global.error.server-error.internal-server-error', {
                status: error.data.error_description
              });
        });
        registerConfirmOnDirtyHook();
      }).finally(function() {
      $scope.loadStarted = false;
    });

    $scope.advancedPrivileges = Principal.hasAnyAuthority(['ROLE_PUBLISHER',
      'ROLE_ADMIN']);

    $scope.canDeleteUser = function(user, role) {
      if (user.restricted) {
        // cannot modify user whose details we can't read
        return false;
      }
      if (!$scope.advancedPrivileges && role === 'publishers') {
        // cannot remove publishers without advanced privilege
        return false;
      }
      if ($scope.activeUsers[role].length <= 1) {
        // cannot remove the last user in this list
        return false;
      }
      return true;
    };

    $state.currentPromise = null;
    $state.searchCache = {
      publishers: {},
      dataProviders: {}
    };
    $scope.searchUsers = function(search, role, roleInternal) {
      if (!$state.loadComplete) {
        return [];
      }
      if ($state.searchCache[role][search]) {
        return $state.searchCache[role][search];
      }
      if (!$state.currentPromise) {
        $state.currentPromise = UserResource.search({
          login: search,
          role: roleInternal
        }).$promise.then(function(result) {
          $state.currentPromise = null;
          var results = result.filter(function(x) {
            // filter out already added users
            return $scope.activeUsers[role].map(function(u) {
              return u.login;
            }).indexOf(x.login) < 0 && _.includes(x.authorities, roleInternal);
          });
          $state.searchCache[role][search] = results;
          return results;
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
      $state.searchCache[role] = {};
    };

    $state.loadComplete = true;
  }).directive('projectCockpitConfig', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
        'project-cockpit-config.html.tmpl'
    };
  }).directive('projectCockpitStatus', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
        'project-cockpit-status.html.tmpl'
    };
  });
