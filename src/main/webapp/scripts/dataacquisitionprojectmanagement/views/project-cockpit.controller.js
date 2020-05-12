/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($scope, $state, $location, $transitions, Principal,
           PageTitleService, LanguageService, BreadcrumbService,
           CurrentProjectService, projectDeferred, CommonDialogsService,
           ProjectSaveService, blockUI) {
    blockUI.start();
    var unregisterTransitionHook;
    var pageTitleKey = 'data-acquisition-project-management.project' +
      '-cockpit.title';

    BreadcrumbService.updateToolbarHeader({
      stateName: $state.current.name
    });

    var registerConfirmOnDirtyHook = function() {
      if (unregisterTransitionHook) {
        unregisterTransitionHook();
      }

      unregisterTransitionHook = $transitions.onBefore({}, function(trans) {
        if ($scope.changed &&
            trans.to().name !== trans.from().name) {
          return CommonDialogsService.showConfirmOnDirtyDialog();
        }
      });

      $scope.$on('$destroy', unregisterTransitionHook);
    };

    var saveProject = function(project) {
      return ProjectSaveService.saveProject(project).then(function() {
        $scope.$broadcast('project-saved');
      });
    };

    $state.loadStarted = true;

    $scope.initializing = true;
    var projectId = $state.params.id;
    $scope.$on('current-project-changed',
      function(event, changedProject) { // jshint ignore:line
        if (changedProject && !$scope.initializing) {
          $location.url('/' + LanguageService.getCurrentInstantly() +
            '/projects/' + changedProject.id);
          PageTitleService.setPageTitle(pageTitleKey,
            {projectId: changedProject.id});
          if (changedProject.id === projectId) {
            $scope.project = _.assignIn({}, changedProject);
          }
        } else if (!changedProject) {
          $scope.project = null;
        }
        $scope.initializing = false;
      });

    $scope.$on('project-saved', function() {
      $scope.changed = false;
    });

    $scope.$watch('project', function(newVal, oldVal) {
      if (oldVal != null && newVal !== oldVal &&
          !ProjectSaveService.getSaving()) {
        $scope.changed = true;
        $scope.$broadcast('project-changed');
      }
    }, true);

    $scope.$on('project-deleted', function() {
        $state.go('search');
      });

    $scope.onSaveChanges = function() {
      var project = ProjectSaveService.prepareProjectForSave($scope.project);
      saveProject(project);
    };

    $scope.$on('project-changed', function() {
        $scope.onSaveChanges();
      });

    $scope.changed = false;

    $scope.isPublisher = Principal.hasAnyAuthority(['ROLE_PUBLISHER',
      'ROLE_ADMIN']);
    $scope.isPublisher = Principal.hasAnyAuthority(['ROLE_PUBLISHER']);
    $scope.isDataProvider = Principal.hasAnyAuthority(['ROLE_DATA_PROVIDER']);
    $scope.isAssignedToProject = false;

    projectDeferred.promise.then(
      function(project) {
        $scope.project = _.assignIn({}, project);

        PageTitleService.setPageTitle(pageTitleKey,
          {projectId: project.id});

        CurrentProjectService.setCurrentProject(project);

        registerConfirmOnDirtyHook();
      }).finally(function() {
      $state.loadStarted = false;
      $scope.initializing = false;
      blockUI.stop();
    });

    $scope.shareButtonShown = false;

    $scope.$watchCollection(function() {
      return $location.search();
    }, function(newValue) {
      if (newValue && newValue.tab) {
        $scope.selectedTab.index = (function() {
          switch (newValue.tab) {
            case 'status': return 0;
            case 'config': return 1;
            case 'versions': return 2;
            default: return 0;
          }
        })();
      }
    });

    $scope.onTabSelect = function(tab) {
      $state.go('project-cockpit', {tab: tab});
      $scope.shareButtonShown = false;
      if (tab === 'status') {
        $scope.shareButtonShown = true;
      }
    };
    $scope.selectedTab = {};
    $scope.selectedTab.index = (function() {
      switch ($state.params.tab) {
        case 'status': return 0;
        case 'config': return 1;
        case 'versions': return 2;
        default: return 0;
      }
    })();

    $state.loadComplete = true;

  });
