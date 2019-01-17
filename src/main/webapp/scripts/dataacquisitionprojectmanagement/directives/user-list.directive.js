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

        ctrl.searchText = {
          publishers: '',
          dataProviders: ''
        };

        ctrl.selectedUser = {
          publishers: null,
          dataProviders: null
        };

        ctrl.preventSearching = {
          publishers: false,
          dataProviders: false
        };

        ctrl.updateProject = function() {
          if (!ctrl.project.configuration) {
            ctrl.project.configuration = {};
          }
          var configuredPublishers = ctrl.activeUsers.publishers
            .map(function(identity) {
              return identity.login;
            });
          var configuredDataProviders = ctrl.activeUsers.dataProviders
            .map(function(identity) {
              return identity.login;
            });
          if (ctrl.project.configuration.publishers) {
            ctrl.project.configuration.publishers =
              _.filter(_.union(ctrl.project.configuration.publishers,
                configuredPublishers), function(val) {
                return _.includes(configuredPublishers, val);
              });
          } else {
            ctrl.project.configuration.publishers = configuredPublishers;
          }
          if (ctrl.project.configuration.dataProviders) {
            ctrl.project.configuration.dataProviders =
              _.filter(_.union(ctrl.project.configuration.dataProviders,
                configuredDataProviders), function(val) {
                return _.includes(configuredDataProviders, val);
              });
          } else {
            ctrl.project.configuration.dataProviders = configuredDataProviders;
          }
        };

        ctrl.selectedUserChanged = function(user, role) {
          if (user) {
            ctrl.activeUsers[role].push(user);
            ctrl.changed = true;
            ctrl.searchText[role] = '';
            ctrl.selectedUser[role] = null;
            ctrl.preventSearching[role] = true;
            ctrl.searchCache[role] = {};
            $(':focus').blur();

            ctrl.updateProject();
          }
        };

        ctrl.activeUsers = {
          publishers: [],
          dataProviders: []
        };

        ctrl.currentPromise = null;
        ctrl.searchCache = {
          publishers: {},
          dataProviders: {}
        };
        var internalRoles = {
          'publishers': 'ROLE_PUBLISHER',
          'dataProviders': 'ROLE_DATA_PROVIDER'
        };

        ctrl.searchUsers = function(search, role) {
          var roleInternal = internalRoles[role];
          var deferred = $q.defer();
          if (ctrl.preventSearching[role]) {
            ctrl.preventSearching[role] = false;
            deferred.resolve([]);
            return deferred.promise;
          }
          if (ctrl.searchCache[role]['text_' + search]) {
            deferred.resolve(ctrl.searchCache[role]['text_' + search]);
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
                return ctrl.activeUsers[role].map(function(u) {
                  return u.login;
                }).indexOf(x.login) < 0 &&
                  _.includes(x.authorities, roleInternal);
              });
              ctrl.searchCache[role]['text_' + search] = results;
              deferred.resolve(ctrl.searchCache[role]['text_' + search]);
            });
            return deferred.promise;
          } else {
            return ctrl.currentPromise;
          }
        };

        ctrl.removeUser = function(user, role) {
          ctrl.changed = true;
          ctrl.activeUsers[role] = ctrl.activeUsers[role]
            .filter(function(item) {
              return item.login !== user.login;
            });
          ctrl.searchCache[role] = {};
          ctrl.updateProject();
        };

        ctrl.canDeleteUser = function(user, role) {
          if (user.restricted) {
            // cannot modify user whose details we can't read
            return false;
          }
          if (!ctrl.advancedPrivileges && role === 'publishers') {
            // cannot remove publishers without advanced privilege
            return false;
          }
          if (ctrl.activeUsers[role].length <= 1) {
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
                    if (!_.includes(ctrl.activeUsers[key].map(function(u) {
                      return u.login;
                    }), userResult.login)) {
                      ctrl.activeUsers[key].push(userResult);
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
          // setAssignedToProject();
        }).catch(function(error) {
          SimpleMessageToastService
            .openAlertMessageToast(
              'global.error.server-error.internal-server-error', {
                status: error.data.error_description
              });
        });

      }
    };
  });
