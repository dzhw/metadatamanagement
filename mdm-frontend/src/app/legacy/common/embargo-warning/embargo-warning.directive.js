/* global _, bowser */

'use strict';

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
          '$scope', '$rootScope', 'Principal',
          function ($scope, $rootScope, Principal) {
            if (localStorage.getItem('currentView') === 'orderView' || !Principal.isAuthenticated()) {
              $scope.isOrderView = true;
            }
            this.project = $scope.project;
            this.type = this.project && this.project.configuration.requirements.dataPackageRequired ? 'data-package' : 'analysis-package';
          }],

        /* jshint -W098 */
        link: function ($scope, elem, attrs, ctrl) {
          /* jshint +W098 */

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
        }
      };
    }]);

