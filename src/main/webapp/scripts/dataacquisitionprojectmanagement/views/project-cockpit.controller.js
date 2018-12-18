/* global _, $ */
'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($q, $scope, $state, $location, $transitions, UserResource, Principal,
           PageTitleService, LanguageService, ToolbarHeaderService,
           DataAcquisitionProjectResource, SimpleMessageToastService,
           CurrentProjectService, projectDeferred, CommonDialogsService,
           SearchDao, QuestionUploadService, VariableUploadService, $translate,
           $mdDialog, ProjectReleaseService, $timeout,
           ProjectUpdateAccessService,
           DataAcquisitionProjectPostValidationService) {

    var unregisterTransitionHook;
    var pageTitleKey = 'data-acquisition-project-management.project' +
      '-cockpit.title';

    ToolbarHeaderService.updateToolbarHeader({
      stateName: $state.current.name
    });

    var setTypeCounts = function(projectId) {
      SearchDao.search('', 1, projectId, {}, undefined, 0, undefined)
        .then(function(data) {
          $scope.counts = {};
          ['variables', 'questions', 'data_sets', 'surveys', 'instruments',
            'studies'].forEach(function(type) {
            var bucket  = _.find(data.aggregations.countByType.buckets,
              {key: type});
            $scope.counts[type] = _.get(bucket, 'doc_count', 0);
          });
        });
    };

    var registerConfirmOnDirtyHook = function() {
      if (unregisterTransitionHook) {
        unregisterTransitionHook();
      }

      unregisterTransitionHook = $transitions.onBefore({}, function(trans) {
        if ($scope.changed && trans.to().name !== trans.from().name) {
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

    $state.loadStarted = true;

    $scope.$watch('project', function(newVal, oldVal) {
      if (oldVal !== undefined && newVal !== oldVal && !$scope.saving) {
        $scope.changed = true;
      }
      $scope.saving = false;
    }, true);

    $scope.$on('project-deleted', function() {
        $state.go('search');
      });

    var initializing = true;
    $scope.$on('current-project-changed',
      function(event, changedProject) { // jshint ignore:line
        if (changedProject && !initializing) {
          $location.url('/' + LanguageService.getCurrentInstantly() +
            '/projects/' + changedProject.id);
          PageTitleService.setPageTitle(pageTitleKey,
            {projectId: changedProject.id});
          setTypeCounts(changedProject.id);
          setProjectRequirementsDisabled(changedProject);
        }
        initializing = false;
      });

    $scope.isUpdateAllowed = function(type) {
      return !_.get($scope, 'project.configuration.requirements.' +
        type + 'Required');
    };

    $scope.isUploadAllowed = function(type) {
      return ProjectUpdateAccessService.isUpdateAllowed($scope.project, type,
        true);
    };

    var saveProject = function(project) {
      return DataAcquisitionProjectResource.save(
        project,
        //Success
        function() {
          $scope.changed = false;
          $scope.saving = true;
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

    var getNextAssigneeGroup = function(project) {
      switch (_.get(project, 'assigneeGroup')) {
        case 'DATA_PROVIDER':
          return 'PUBLISHER';
        case 'PUBLISHER':
          return 'DATA_PROVIDER';
        default:
          return '';
      }
    };

    var showAssigneeGroupMessageDialog = function(recipient) {
      var currentProject = CurrentProjectService.getCurrentProject();
      var assigneeGroup = _.get(currentProject, 'assigneeGroup');
      recipient = recipient || assigneeGroup;

      switch (recipient) {
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

    $scope.onSaveChangesAndTakeBack = function() {
      var confirm = $mdDialog.confirm()
      .title($translate.instant('data-acquisition' +
        '-project-management.project-cockpit.takeback-dialog.title')
      ).textContent($translate.instant('data-acquisition' +
      '-project-management.project-cockpit.takeback-dialog.text')
      ).ok($translate.instant('global.common-dialogs.yes'))
      .cancel($translate.instant('global.common-dialogs.no'));
      $mdDialog.show(confirm).then(function() {
        showAssigneeGroupMessageDialog('PUBLISHER').then(function(message) {
          var project = prepareProjectForSave(message, 'PUBLISHER');
          saveProject(project);
        });
      });
    };

    $scope.onSaveChangesAndAssign = function() {
      if (!_.get($scope.project, 'configuration.dataProviders.length')) {
        SimpleMessageToastService.openAlertMessageToast('data-acquisition' +
          '-project-management.project-cockpit.no-data-providers' +
          '-dialog.text');
        return;
      }

      var postValidationStep = $q.defer();
      var newAssigneeGroup = getNextAssigneeGroup($scope.project);
      var isPublisher = Principal.hasAuthority('ROLE_PUBLISHER');

      if (newAssigneeGroup === 'PUBLISHER' && !isPublisher) {
        DataAcquisitionProjectPostValidationService
          .postValidate($scope.project.id)
          .then(postValidationStep.resolve, postValidationStep.reject);
      } else {
        postValidationStep.resolve();
      }

      postValidationStep.promise.then(function() {
        showAssigneeGroupMessageDialog().then(function(message) {
          var project = prepareProjectForSave(message, newAssigneeGroup);
          saveProject(project);
        });
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

    $scope.preventSearching = {
      publishers: false,
      dataProviders: false
    };

    $scope.selectedUserChanged = function(user, role) {
      if (user) {
        $scope.activeUsers[role].push(user);
        $scope.changed = true;
        $scope.searchText[role] = '';
        $scope.selectedUser[role] = null;
        $scope.preventSearching[role] = true;
        $state.searchCache[role] = {};
        $(':focus').blur();
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
        setTypeCounts(project.id);
        $scope.project = project;
        $scope.fetching = true;

        PageTitleService.setPageTitle(pageTitleKey,
          {projectId: project.id});

        CurrentProjectService.setCurrentProject(project);

        setProjectRequirementsDisabled(project);

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
      var deferred = $q.defer();
      if (!$state.loadComplete || $scope.preventSearching[role]) {
        $scope.preventSearching[role] = false;
        deferred.resolve([]);
        return deferred.promise;
      }
      if ($state.searchCache[role]['text_' + search]) {
        deferred.resolve($state.searchCache[role]['text_' + search]);
        return deferred.promise;
      }
      if (!$state.currentPromise) {
        $state.currentPromise = deferred.promise;
        UserResource.search({
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
          $state.searchCache[role]['text_' + search] = results;
          deferred.resolve($state.searchCache[role]['text_' + search]);
        });
        return deferred.promise;
      } else {
        return $state.currentPromise;
      }
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

    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue) {
      $scope.selectedTab.index = (function() {
        switch (newValue.tab) {
          case 'status': return 0;
          case 'config': return 1;
          default: return 0;
        }
      })();
    });

    $scope.onTabSelect = function(tab) {
      $state.go('project-cockpit', {tab: tab});
      if (tab === 'config') {
        $scope.shareButtonShown = false;
      } else if (tab === 'status') {
        $scope.shareButtonShown = true;
      }
    };
    $scope.selectedTab = {};
    $scope.selectedTab.index = (function() {
      switch ($state.params.tab) {
        case 'status': return 0;
        case 'config': return 1;
        default: return 0;
      }
    })();

    $scope.setChanged = function(changed) {
      if (changed === undefined) {
        changed = true;
      }
      $scope.changed = changed;
    };

    $scope.toggleReleaseProject = function() {
      if ($scope.project.release) {
        ProjectReleaseService.unreleaseProject($scope.project);
      } else {
        ProjectReleaseService.releaseProject($scope.project);
      }
    };

    $scope.$on('upload-completed', function() {
      $timeout(function() {
        setTypeCounts($scope.project.id);
      }, 2000);
    });

    $scope.uploadQuestions = function(files) {
      QuestionUploadService.uploadQuestions(files, $scope.project.id);
    };

    $scope.uploadVariables = function(files) {
      VariableUploadService.uploadVariables(files, $scope.project.id);
    };

    $state.loadComplete = true;
  });
