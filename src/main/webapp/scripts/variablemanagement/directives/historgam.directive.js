/* global Plotly */
'use strict';

angular.module('metadatamanagementApp').directive('histogram', ['$window',
  function($window) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/histogram.html.tmpl',
      scope: {
        statistics: '=',
        language: '='
      },
      controller: ['$scope',
        function($scope) {
          var data = [{
            x: [],
            y: [],
            type: 'bar',
            marker: {
              color: 'rgb(49,130,189)',
              opacity: 0.7
            }}];
          var w = angular.element($window);
          var drowDiagramm = function() {
            Plotly.newPlot('histogram', data);
          };
          $scope.statistics.validResponses.forEach(function(obj) {
            data[0].x.push('-' + obj.label[$scope.language] + '-');
            data[0].y.push(obj.absoluteFrequency);
          });
          $scope.$watch(function() {
            return $window.innerWidth;
          }, function(value) {
            $scope.windowWidth = value;
          }, true);
          w.bind('resize', function() {
            $scope.$apply();
            drowDiagramm();
          });
          drowDiagramm();
        }]
    };
  }]);
