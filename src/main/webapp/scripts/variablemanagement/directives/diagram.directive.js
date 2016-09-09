/* global Plotly */
'use strict';

angular.module('metadatamanagementApp').directive('diagram',
  function($window, $timeout) {
    var link = function(scope) {
      scope.isNotLoaded = true;
      scope.isNotAvailable = false;
      var data = [{
        marker: {
          color: 'rgb(49,130,189)',
          opacity: 0.7
        }}];
      var drawDiagram = function() {
        $timeout(function() {
          scope.isNotLoaded = false;
          Plotly.newPlot('diagram', data);
        }, 1000);
      };
      if ((scope.type === 'nominal') || (scope.type === 'ordinal')) {
        data[0].x = [];
        data[0].y = [];
        data[0].type = 'bar';
        if (scope.statistics.validResponses) {
          scope.statistics.validResponses.forEach(function(obj) {
            data[0].x.push('-' + obj.label[scope.language] + '-');
            data[0].y.push(obj.absoluteFrequency);
          });
        } else {
          scope.isNotAvailable = true;
        }
      }
      if ((scope.type === 'kontinuierlich') || (scope.type === 'continous')) {
        /* should be discussed....
        data[0].x = [];
        data[0].type = 'histogram';
        */
        scope.isNotAvailable = true;
      }
      angular.element($window).bind('resize', function() {
        drawDiagram();
      });
      drawDiagram();
    };
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/diagram.html.tmpl',
      link: link,
      scope: {
        statistics: '=',
        language: '=',
        type: '='
      }
    };
  });
