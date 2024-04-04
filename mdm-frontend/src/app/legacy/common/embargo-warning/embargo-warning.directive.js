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
            //$scope.isOrderView = localStorage.getItem('currentView') === 'orderView';
            this.project = $scope.project;
            this.dateString = this.project && this.project.embargoDate ? new Date(this.project.embargoDate).toLocaleDateString('de-DE', {day:'2-digit', month:'2-digit', year:'numeric'}) : '';
          }],

        /* jshint -W098 */
        link: function ($scope, elem, attrs, ctrl) {
          /* jshint +W098 */

          /**
           * Whether the embargo date has expired or not.
           * @returns true if it has expired else false
           */
          ctrl.isEmbargoDateExpired = function () {
            if (ctrl.project.embargoDate) {
              var current = new Date();
              return new Date(ctrl.project.embargoDate) < current;
            }
            return true;
          }
        }
      };
    }]);

