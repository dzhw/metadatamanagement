/* global Plotly */
'use strict';

angular.module('metadatamanagementApp').directive('diagram', ['$window',
  function() {
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
            x: [],
            y: [],
            type: '',
            marker: {
              color: 'rgb(49,130,189)',
              opacity: 0.7
            }}];
          if (($scope.type === 'nominal') || ($scope.type === 'ordinal')) {
            $scope.statistics.validResponses.forEach(function(obj) {
              data[0].type = 'bar';
              data[0].x.push('-' + obj.label[$scope.language] + '-');
              data[0].y.push(obj.absoluteFrequency);
            });
          }
          if (($scope.type === 'kontinuierlich') ||
          ($scope.type === 'continous')) {
            //$scope.statistics.validResponses.forEach(function(obj) {
            data[0].type = 'histogram';
            //data[0].x.push('-' + obj.label[$scope.language] + '-');
            //data[0].y.push(obj.absoluteFrequency);
            //});
          }
          //var w = angular.element($window);
          var drowDiagram = function() {
            Plotly.newPlot('diagram', data);
          };
          /*$scope.$watch(function() {
            return $window.innerWidth;
          }, function(value) {
            $scope.windowWidth = value;
          }, true);
          w.bind('resize', function() {
          $scope.$apply();
            drowDiagramm();
          });*/
          drowDiagram();
        }]
    };
  }]);
