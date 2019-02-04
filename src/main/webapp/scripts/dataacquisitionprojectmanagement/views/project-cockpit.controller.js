/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ProjectCockpitController',
  function($scope, $state, $location, $transitions, Principal,
           PageTitleService, LanguageService, ToolbarHeaderService,
           CurrentProjectService, projectDeferred, CommonDialogsService,
           ProjectSaveService) {

    var unregisterTransitionHook;
    var pageTitleKey = 'data-acquisition-project-management.project' +
      '-cockpit.title';

    ToolbarHeaderService.updateToolbarHeader({
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

    var initializing = true;
    $scope.$on('current-project-changed',
      function(event, changedProject) { // jshint ignore:line
        if (changedProject && !initializing) {
          $location.url('/' + LanguageService.getCurrentInstantly() +
            '/projects/' + changedProject.id);
          PageTitleService.setPageTitle(pageTitleKey,
            {projectId: changedProject.id});
        }
        initializing = false;
      });

    $scope.$on('project-saved', function() {
      $scope.changed = false;
    });

    $scope.$watch('project', function(newVal, oldVal) {
      if (oldVal !== undefined && newVal !== oldVal &&
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

        // setProjectRequirementsDisabled(project);

        registerConfirmOnDirtyHook();
      }).finally(function() {
      $scope.loadStarted = false;
    });

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

    $state.loadComplete = true;

  });
