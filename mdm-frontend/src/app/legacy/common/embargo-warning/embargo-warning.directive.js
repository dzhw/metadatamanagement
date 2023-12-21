/* global _, bowser */

'use strict';

angular.module('metadatamanagementApp')
  .directive('embargoWarning', ['$state',
    function ($state) {
      return {
        restrict: 'E',
        templateUrl: 'scripts/common/embargo-warning/' +
          'embargo-warning.html.tmpl',
        scope: {
          //isEmbargoDateExpired: '&',
          // dateString: '&',
          project: '='
        },
        replace: true,
        controllerAs: 'ctrl',

        controller: [
          '$scope', '$rootScope',
          function ($scope, $rootScope) {
            $scope.isOrderView = localStorage.getItem('currentView') === 'orderView';
            console.log($scope.project)
            this.project = $scope.project;
            this.dateString = this.project.embargoDate ? new Date(this.project.embargoDate).toLocaleDateString('de-DE', {day:'2-digit', month:'2-digit', year:'numeric'}) : '';
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

