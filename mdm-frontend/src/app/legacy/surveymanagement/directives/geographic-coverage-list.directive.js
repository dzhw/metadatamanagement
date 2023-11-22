'use strict';

angular.module('metadatamanagementApp')
  .directive('geographicCoverageList', function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/surveymanagement/directives/geographic-coverage' +
        '-list.html.tmpl',
      scope: {
        geographicCoverages: '='
      },
      require: '?ngModel',
      link: function($scope, el, attr, ngModelCtrl) { // jshint ignore:line
        $scope.ngModelCtrl = ngModelCtrl;
        if (ngModelCtrl) {
          ngModelCtrl.$validators['not-empty'] =
            function(modelValue, viewValue) {
              if (ngModelCtrl.$isEmpty(modelValue)) {
                return false;
              }

              return viewValue.length !== 0;
            };
        }
      },
      controller: 'GeographicCoverageListController'
    };
  });
