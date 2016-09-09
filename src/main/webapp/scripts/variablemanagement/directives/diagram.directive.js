/* global Plotly */
'use strict';

angular.module('metadatamanagementApp').directive('diagram',
  function($window, $timeout) {
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/diagram.html.tmpl',
      scope: {
        statistics: '=',
        language: '=',
        type: '='
      },
      controller: ['$scope',
        function($scope) {
          var data = [{
            marker: {
              color: 'rgb(49,130,189)',
              opacity: 0.7
            }}];
          if (($scope.type === 'nominal') || ($scope.type === 'ordinal')) {
            data[0].x = [];
            data[0].y = [];
            data[0].type = 'bar';
            if ($scope.statistics.validResponses) {
              $scope.statistics.validResponses.forEach(function(obj) {
                data[0].x.push('-' + obj.label[$scope.language] + '-');
                data[0].y.push(obj.absoluteFrequency);
              });
            }
          }
          if (($scope.type === 'kontinuierlich') ||
          ($scope.type === 'continous')) {
            data[0].x = [];
            data[0].type = 'histogram';
          }
          var drawDiagram = function() {
            $timeout(function() {
              Plotly.newPlot('diagram', data);
            }, 1000);
          };
          angular.element($window).bind('resize', function() {
            drawDiagram();
          });
          drawDiagram();
        }]
    };
  });
