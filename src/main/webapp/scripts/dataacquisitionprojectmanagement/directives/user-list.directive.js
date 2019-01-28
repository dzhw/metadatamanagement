/* global _, $ */

'use strict';

angular.module('metadatamanagementApp')
  .directive('userList', function(
    $q, UserResource, SimpleMessageToastService) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'user-list.html.tmpl',
      scope: {
        group: '@',
        advancedPrivileges: '@',
        project: '=',
      },
      replace: true,
      transclude: true,
      controllerAs: 'ctrl',

      controller: function($scope) {
        this.group = $scope.group;
        this.advancedPrivileges = $scope.advancedPrivileges;
        this.project = $scope.project;
      },
      link: function($scope, elem, attrs, ctrl) { // jshint ignore:line
        ctrl.disabled = !ctrl.advancedPrivileges &&
          ctrl.group === 'publishers';

        ctrl.searchText = '';
        ctrl.selectedUser = null;
        ctrl.preventSearching = false;

        ctrl.updateProject = function() {
          if (!ctrl.project.configuration) {
            ctrl.project.configuration = {};
          }
          var configuredUsers = ctrl.activeUsers.map(function(identity) {
              return identity.login;
            });
          if (ctrl.group === 'publishers') {
            if (ctrl.project.configuration.publishers) {
              ctrl.project.configuration.publishers =
                _.filter(_.union(ctrl.project.configuration.publishers,
                  configuredUsers), function(val) {
                  return _.includes(configuredUsers, val);
                });
            } else {
              ctrl.project.configuration.publishers = configuredUsers;
            }
          }
          if (ctrl.group === 'dataProviders') {
            if (ctrl.project.configuration.dataProviders) {
              ctrl.project.configuration.dataProviders =
                _.filter(_.union(ctrl.project.configuration.dataProviders,
                  configuredUsers), function(val) {
                  return _.includes(configuredUsers, val);
                });
            } else {
              ctrl.project.configuration.dataProviders = configuredUsers;
            }
          }
        };

        ctrl.selectedUserChanged = function(user) {
          if (user) {
            ctrl.activeUsers.push(user);
            ctrl.changed = true;
            ctrl.searchText = '';
            ctrl.selectedUser = null;
            ctrl.preventSearching = true;
            ctrl.searchCache = {};
            $(':focus').blur();

            ctrl.updateProject();
          }
        };

        ctrl.activeUsers = [];

        ctrl.currentPromise = null;
        ctrl.searchCache = {};
        var internalRoles = {
          'publishers': 'ROLE_PUBLISHER',
          'dataProviders': 'ROLE_DATA_PROVIDER'
        };

        ctrl.searchUsers = function(search) {
          var roleInternal = internalRoles[ctrl.group];
          var deferred = $q.defer();
          if (ctrl.preventSearching) {
            ctrl.preventSearching = false;
            deferred.resolve([]);
            return deferred.promise;
          }
          if (ctrl.searchCache['text_' + search]) {
            deferred.resolve(ctrl.searchCache['text_' + search]);
            return deferred.promise;
          }
          if (!ctrl.currentPromise) {
            ctrl.currentPromise = deferred.promise;
            UserResource.search({
              login: search,
              role: roleInternal
            }).$promise.then(function(result) {
              ctrl.currentPromise = null;
              var results = result.filter(function(x) {
                // filter out already added users
                return ctrl.activeUsers.map(function(u) {
                  return u.login;
                }).indexOf(x.login) < 0 &&
                  _.includes(x.authorities, roleInternal);
              });
              ctrl.searchCache['text_' + search] = results;
              deferred.resolve(ctrl.searchCache['text_' + search]);
            });
            return deferred.promise;
          } else {
            return ctrl.currentPromise;
          }
        };

        ctrl.removeUser = function(user) {
          ctrl.changed = true;
          ctrl.activeUsers = ctrl.activeUsers
            .filter(function(item) {
              return item.login !== user.login;
            });
          ctrl.searchCache = {};
          ctrl.updateProject();
        };

        ctrl.canDeleteUser = function(user) {
          if (user.restricted) {
            // cannot modify user whose details we can't read
            return false;
          }
          if (!ctrl.advancedPrivileges && ctrl.group === 'publishers') {
            // cannot remove publishers without advanced privilege
            return false;
          }
          if (ctrl.activeUsers.length <= 1) {
            // cannot remove the last user in this list
            return false;
          }
          return true;
        };

        function getAndAddUsers(key) {
          // get users of type {key} asynchronously
          // and return promise resolving when all are fetched
          if (ctrl.project.configuration[key]) {
            return $q.all(
              ctrl.project.configuration[key].map(function(userLogin) {
                return (
                  UserResource.getPublic({
                    login: userLogin
                  }).$promise.then(function(userResult) {
                    if (!_.includes(ctrl.activeUsers.map(function(u) {
                      return u.login;
                    }), userResult.login)) {
                      ctrl.activeUsers.push(userResult);
                    }
                  })
                );
              })
            );
          } else {
            return $q.resolve([]);
          }
        }

        $q.resolve(getAndAddUsers(ctrl.group)).catch(function(error) {
          SimpleMessageToastService
            .openAlertMessageToast(
              'global.error.server-error.internal-server-error', {
                status: error.data.error_description
              });
        });

      }
    };
  });
