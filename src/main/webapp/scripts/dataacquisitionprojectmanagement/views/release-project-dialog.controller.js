'use strict';

angular.module('metadatamanagementApp')
  .controller('ReleaseProjectDialogController', function($scope, $mdDialog,
    project, SimpleMessageToastService, DataAcquisitionProjectResource,
    DaraReleaseResource, $rootScope, CurrentProjectService,
    StudyIdBuilderService, StudyResource, $q,
    DataAcquisitionProjectLastReleasedVersionResource) {
    $scope.bowser = $rootScope.bowser;
    $scope.project = project;
    var ctrl = this;
    var i18nPrefix = 'data-acquisition-project-management.log-messages.' +
      'data-acquisition-project.';
    $scope.cancel = function() {
      $mdDialog.cancel();
    };

    ctrl.getLastReleasedVersion = function() {
      var deferred = $q.defer();
      $rootScope.$broadcast('start-ignoring-404');
      DataAcquisitionProjectLastReleasedVersionResource
      .lastReleasedVersion({
        id: project.id
      },
      function(result) {
        var resultStr = '';
        Object.values(result).forEach(function(value) {
          if (typeof value === 'string') {
            resultStr = resultStr.concat(value);
          }
        });
        $rootScope.$broadcast('stop-ignoring-404');
        deferred.resolve(resultStr);
      },
      function() {
        $rootScope.$broadcast('stop-ignoring-404');
        deferred.resolve('0.0.0');
      });
      return deferred.promise;
    };

    ctrl.saveProject = function(project) {
      DataAcquisitionProjectResource.save(project).$promise
        .then(function() {
          SimpleMessageToastService.openSimpleMessageToast(
            i18nPrefix + 'released-beta-successfully', {
              id: project.id
            });
          CurrentProjectService.setCurrentProject(project);
        });
    };

    ctrl.saveStudy = function(project) {
      var studyId = StudyIdBuilderService.buildStudyId(project.id);
      StudyResource.get({id: studyId})
        .$promise
        .then(function(study) {
          study.doi = StudyIdBuilderService.buildDoi(project);
          StudyResource.save(study);
        });
    };

    $scope.ok = function(release) {
      ctrl.getLastReleasedVersion()
      .then(function(lastReleasedVersion) {
        var compareForBeta = $scope.bowser
          .compareVersions(['1.0.0', release.version]);
        var compareForHigherVersion = $scope.bowser
          .compareVersions([lastReleasedVersion, release.version]);

        //No higher version -> always break up
        //1 = old Version is higher, 0 = Same Version Number
        if (compareForHigherVersion !== -1) {
          console.log('No Higher Version!');
          SimpleMessageToastService.openSimpleMessageToast(
            i18nPrefix + 'no-higher-version', {
              lastReleasedVersion: lastReleasedVersion
            });
        } else {
          if (compareForBeta === 1) {
            //BETA RELEASE
            release.date = new Date().toISOString();
            project.release = release;
            ctrl.saveProject(project);
            $mdDialog.hide();
          } else {
            //REGULAR RELEASE
            release.date = new Date().toISOString();
            project.release = release;
            DaraReleaseResource.release(project)
              .$promise.then(function() {
              project.hasBeenReleasedBefore = true;
              ctrl.saveProject(project);
              ctrl.saveStudy(project);
            }).catch(function() {
              delete project.release;
              SimpleMessageToastService.openSimpleMessageToast(
                i18nPrefix + 'dara-released-not-successfully', {
                  id: project.id
                });
            });
            $mdDialog.hide();
          }
        }
      });
    };
  });
