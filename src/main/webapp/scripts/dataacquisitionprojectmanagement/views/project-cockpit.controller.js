/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($q, $scope, $state, $location, $transitions, UserResource, Principal,
           PageTitleService, LanguageService, ToolbarHeaderService,
           DataAcquisitionProjectResource, SimpleMessageToastService,
           CurrentProjectService, projectDeferred, CommonDialogsService,
           SearchDao, QuestionUploadService, VariableUploadService, $translate,
           $mdDialog) {

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

    var setAssignedToProject = function() {
      var name = Principal.loginName();
      $scope.isAssignedPublisher =
        !!_.find($scope.activeUsers.publishers, function(o) {
        return o.login === name;
      });
      $scope.isAssignedDataProvider =
      !!_.find($scope.activeUsers.dataProviders, function(o) {
        return o.login === name;
      });
      $scope.isAssignedToProject =
        $scope.isAssignedPublisher || $scope.isAssignedDataProvider;
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

    var saveProject = function(project) {
      return DataAcquisitionProjectResource.save(
        project,
        //Success
        function() {
          $scope.changed = false;
          $scope.project = project;
          setAssignedToProject();
          setProjectRequirementsDisabled($scope.project);
          CurrentProjectService.setCurrentProject($scope.project);
          SimpleMessageToastService
            .openSimpleMessageToast(
              'data-acquisition-project-management.log-messages.' +
              'data-acquisition-project.saved', {
                id: $scope.project.id
              });
        },
        //Server Error
        function(response) {
          var errors = _.get(response, 'data.errors');

          if (errors) {
            errors.forEach(function(error) {
              SimpleMessageToastService.openAlertMessageToast(error.message);
            });
          }
        }
      );
    };

    var showAssigneeGroupMessageDialog = function() {
      var currentProject = CurrentProjectService.getCurrentProject();
      var assigneeGroup = _.get(currentProject, 'assigneeGroup');
      var recipient;

      switch (assigneeGroup) {
        case 'PUBLISHER':
          recipient = $translate.instant('data-acquisition' +
            '-project-management.project-cockpit.label.ROLE_DATA_PROVIDER');
          break;
        case 'DATA_PROVIDER':
          recipient = $translate.instant('data-acquisition' +
            '-project-management.project-cockpit.label.ROLE_PUBLISHER');
          break;
        default:
          recipient = '';
      }

      return $mdDialog.show({
        controller: 'AssigneeMessageDialogController',
        templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
          'assignee-message-dialog.html.tmpl',
        fullscreen: true,
        locals: {
          recipient: recipient
        }
      });
    };

    var prepareProjectForSave = function(assigneeGroupMessage,
                                         newAssigneeGroup) {

      var project = _.assignIn({}, $scope.project);

      if (!project.configuration) {
        $scope.project.configuration = {};
      }

      var configuredPublishers = $scope.activeUsers.publishers
        .map(function(identity) {
          return identity.login;
        });

      var configuredDataProviders = $scope.activeUsers.dataProviders
        .map(function(identity) {
          return identity.login;
        });

      if (project.configuration.publishers) {
        project.configuration.publishers =
          _.filter(_.union(project.configuration.publishers,
            configuredPublishers), function(val) {
            return _.includes(configuredPublishers, val);
          });
      } else {
        project.configuration.publishers = configuredPublishers;
      }

      if (project.configuration.dataProviders) {
        project.configuration.dataProviders =
          _.filter(_.union(project.configuration.dataProviders,
            configuredDataProviders), function(val) {
            return _.includes(configuredDataProviders, val);
          });
      } else {
        project.configuration.dataProviders = configuredDataProviders;
      }

      if (assigneeGroupMessage) {
        project.lastAssigneeGroupMessage = assigneeGroupMessage;
      }

      if (newAssigneeGroup) {
        project.assigneeGroup = newAssigneeGroup;
      }

      return project;
    };

    $scope.onSaveChangesAndAssign = function() {
      showAssigneeGroupMessageDialog().then(function(message) {
        var newAssigneeGroup;

        switch ($scope.project.assigneeGroup) {
          case 'DATA_PROVIDER':
            newAssigneeGroup = 'PUBLISHER';
            break;
          case 'PUBLISHER':
            newAssigneeGroup = 'DATA_PROVIDER';
            break;
          default:
            newAssigneeGroup = $scope.project.assigneeGroup;
        }

        var project = prepareProjectForSave(message, newAssigneeGroup);
        saveProject(project);
      });
    };

    $scope.onSaveChanges = function(origin) {
      var project = prepareProjectForSave();
      saveProject(project).$promise.then(function() {
        if (origin !== 'requirements') {
          $state.reload();
        }
      });
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

    $scope.advancedPrivileges = Principal.hasAnyAuthority(['ROLE_PUBLISHER',
      'ROLE_ADMIN']);
    $scope.isPublisher = Principal.hasAnyAuthority(['ROLE_PUBLISHER']);
    $scope.isDataProvider = Principal.hasAnyAuthority(['ROLE_DATA_PROVIDER']);
    $scope.isAssignedToProject = false;
    $scope.isAssignedPublisher = false;
    $scope.isAssignedDataProvider = false;

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
          setAssignedToProject();
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
    var internalRoles = {
      'publishers': 'ROLE_PUBLISHER',
      'dataProviders': 'ROLE_DATA_PROVIDER'
    };
    $scope.searchUsers = function(search, role) {
      var roleInternal = internalRoles[role];
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
    $scope.searchProjectData = function(group) {
      return SearchDao.search('', 0, $scope.project.id, '', group, 1, '');
    };

    $scope.removeUser = function(user, role) {
      $scope.changed = true;
      $scope.activeUsers[role] = $scope.activeUsers[role]
        .filter(function(item) {
          return item.login !== user.login;
        });
      $state.searchCache[role] = {};
    };

    $scope.shareButtonShown = false;
    $scope.onTabSelect = function(tab) {
      if (tab === 'config') {
        $scope.shareButtonShown = false;
      } else if (tab === 'status') {
        $scope.shareButtonShown = true;
      }
    };

    $scope.setChanged = function(changed) {
      if (changed === undefined) {
        changed = true;
      }
      $scope.changed = changed;
    };

    $scope.uploadQuestions = function(files) {
      QuestionUploadService.uploadQuestions(files, $scope.project.id);
    };

    $scope.uploadVariables = function(files) {
      VariableUploadService.uploadVariables(files, $scope.project.id);
    };

    $state.loadComplete = true;
  }).directive('projectCockpitConfig', function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
      'project-cockpit-config.html.tmpl'
  };
}).directive('projectCockpitStatus', function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
      'project-cockpit-status.html.tmpl'
  };
}).directive('projectCockpitUserlist', function() {
  return {
    restrict: 'E',
    templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
      'project-cockpit-userlist.html.tmpl',
    scope: true,
    link: function(scope, elem, attrs) { // jshint ignore:line
      scope.group = attrs.group;
    }
  };
}).directive('projectCockpitAssignment', function($state) {
  return {
    restrict: 'E',
    templateUrl: 'scripts/dataacquisitionprojectmanagement/views/' +
      'project-cockpit-assignment.html.tmpl',
    scope: true,
    replace: true,
    transclude: true,
    /* jshint -W098 */
    link: function(scope, elem, attrs, ctrl, $transclude) {
      var elasticSearchType = {
        studies: 'studies',
        surveys: 'surveys',
        instruments: 'instruments',
        questions: 'questions',
        dataSets: 'data_sets',
        variables: 'variables'
      };
      scope.group = attrs.group;
      scope.icon = attrs.icon;
      scope.create = function() {
        $state.go(attrs.createstate, {});
      };
      scope.count = null;
      scope.$watch(function() {
        return scope.project &&
        scope.project.configuration[attrs.group + 'State'] ?
          scope.project.configuration[attrs.group + 'State']
            .dataProviderReady : null;
      }, function(newVal, oldVal) {
        if (newVal !== oldVal) {
          scope.setChanged(true);
        }
      });
      scope.$watch(function() {
        return scope.project &&
        scope.project.configuration[attrs.group + 'State'] ?
          scope.project.configuration[attrs.group + 'State']
            .publisherReady : null;
      }, function(newVal, oldVal) {
        if (newVal !== oldVal) {
          scope.setChanged(true);
        }
      });
      $transclude(function(transclusion) {
        scope.hasTranscludedContent = transclusion.length > 0;
      });

      scope.searchProjectData(
        elasticSearchType[attrs.group]
      ).then(function(data) {
        scope.count = data.hits.total;
      }).catch(function() {
        scope.count = 0;
      });

    }
  };
});
