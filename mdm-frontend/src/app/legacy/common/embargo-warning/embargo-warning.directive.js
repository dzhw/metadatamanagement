/* global _, bowser */

'use strict';

/**
 * Directive rendering a warning hint on the embargo date in place.
 * Displayed messages depend on user role and current view.
 */
angular.module('metadatamanagementApp')
  .directive('embargoWarning', ['$state', 'Principal',
    function ($state, Principal) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/common/embargo-warning/' +
          'embargo-warning.html.tmpl',
        scope: {
          project: '='
        },
        replace: true,
        controllerAs: 'ctrl',

        controller: [
          '$scope', 'Principal',
          function ($scope, Principal) {
            var ctrl = this;
            ctrl.project = $scope.project;

            if (ctrl.project) {
              ctrl.type = ctrl.project 
                && ctrl.project.configuration.requirements.isDataPackagesRequired ? 'data-package' : 'analysis-package';
            } else {
              console.error("Project is undefined.")
            }

            if (localStorage.getItem('currentView') === 'orderView' || !Principal.isAuthenticated()) {
              $scope.isOrderView = true;
            }

            /**
             * Whether the embargo date has expired or not.
             * @returns true if it has expired else false
             */
            ctrl.isEmbargoDateExpired = function () {
              if (ctrl.project && ctrl.project.embargoDate) {
                var current = new Date();
                return new Date(ctrl.project.embargoDate) < current;
              }
              return true;
            }

          }]
        };
    }]);

