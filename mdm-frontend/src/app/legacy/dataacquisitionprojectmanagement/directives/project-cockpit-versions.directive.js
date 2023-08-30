/* global _, bowser */

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectCockpitVersions', ['Principal', 'CommonDialogsService', 'DataAcquisitionProjectShadowsResource', 'SimpleMessageToastService', 'DataAcquisitionProjectShadowsHideResource', '$timeout', 'DataAcquisitionProjectShadowsActionResource', 'DataAcquisitionProjectResource',  function(Principal, CommonDialogsService,
    DataAcquisitionProjectShadowsResource, SimpleMessageToastService,
    DataAcquisitionProjectShadowsHideResource, $timeout,
    DataAcquisitionProjectShadowsActionResource,
    DataAcquisitionProjectResource) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/dataacquisitionprojectmanagement/directives/' +
        'project-cockpit-versions.html.tmpl',
      scope: {
        project: '='
      },
      replace: true,
      controllerAs: 'ctrl',
      controller: ['$scope', function($scope) {
        this.project = $scope.project;
        $scope.bowser = bowser;
      }],
      /* jshint -W098 */
      link: function($scope, elem, attrs, ctrl) {
        $scope.$on('$destroy', function() {
          delete ctrl.shadows;
          timers.forEach($timeout.cancel);
        });
        var timers = [];
        var watchAction = function(shadow) {
          if (ctrl.shadows && _.findIndex(ctrl.shadows, function(s) {
            return s.id === shadow.id;}) > -1) {
            timers.push($timeout(function() {
              DataAcquisitionProjectShadowsActionResource.get({
                id: shadow.masterId,
                version: shadow.release.version
              }).$promise.then(function(result) {
                if (result.action) {
                  shadow.inProgress = true;
                  watchAction(shadow);
                } else {
                  DataAcquisitionProjectResource.get({id: shadow.id})
                  .$promise.then(function(result) {
                    angular.copy(result, shadow);
                  });
                }
              });
            }, 10000));
          }
        };

        var loadShadows = function(pageNumber) {
          DataAcquisitionProjectShadowsResource.get({
            id: ctrl.project.id,
            sort: 'release.version,DESC',
            page: pageNumber || 0
          })
          .$promise.then(function(page) {
            ctrl.shadows = page.content;
            ctrl.page = page;
            ctrl.page.number++;
            ctrl.shadows.forEach(function(shadow) {
              DataAcquisitionProjectShadowsActionResource.get({
                id: shadow.masterId,
                version: shadow.release.version
              }).$promise.then(function(result) {
                if (result.action) {
                  shadow.inProgress = true;
                  watchAction(shadow);
                }
              });
            });
          });
        };

        loadShadows();

        ctrl.onPageChanged = function() {
          loadShadows(ctrl.page.number - 1);
        };

        var isNotAssignedPublisher = function() {
          var loginName = Principal.loginName();
          var publishers = _.get(ctrl.project, 'configuration.publishers', []);
          return publishers.indexOf(loginName) === -1;
        };

        var showConfirmDialog = function(action, shadow, event) {
          var translationPrefix = 'data-acquisition-project-management.' +
            'project-cockpit.versions.confirm-';
          return CommonDialogsService.showConfirmDialog(
            translationPrefix + action + '.title', {
              version: shadow.release.version,
              id: shadow.masterId
            },
            translationPrefix + action + '.content', {
              version: shadow.release.version,
              id: shadow.masterId
            },
            event
          );
        };

        ctrl.isShadowStateChangeDisabled = function() {
          return !ctrl.isUserPublisher() || isNotAssignedPublisher();
        };

        ctrl.isUserPublisher = function() {
          return Principal.hasAuthority('ROLE_PUBLISHER');
        };

        ctrl.hide = function(shadow, event) {
          showConfirmDialog('hide', shadow, event).then(function() {
            DataAcquisitionProjectShadowsHideResource.hide({
              id: shadow.masterId,
              version: shadow.release.version
            }).$promise.then(function() {
              SimpleMessageToastService
                .openSimpleMessageToast(
                'data-acquisition-project-management.project-cockpit.' +
                'versions.hiding-toast', {
                    id: shadow.masterId,
                    version: shadow.release.version
                  });
              shadow.inProgress = true;
              watchAction(shadow);
            });
          });
        };

        ctrl.unhide = function(shadow, event) {
          showConfirmDialog('unhide', shadow, event).then(function() {
            DataAcquisitionProjectShadowsHideResource.unhide({
              id: shadow.masterId,
              version: shadow.release.version
            }).$promise.then(function() {
              SimpleMessageToastService
                .openSimpleMessageToast(
                'data-acquisition-project-management.project-cockpit.' +
                'versions.unhiding-toast', {
                    id: shadow.masterId,
                    version: shadow.release.version
                  });
              shadow.inProgress = true;
              watchAction(shadow);
            });
          });
        };
      }
    };
  }]);

