/* global Plotly */
'use strict';

angular.module('metadatamanagementApp').directive('diagram',
  function($window, $timeout) {
    var link = function(scope) {
      scope.isNotLoaded = true;
      scope.isNotAvailable = false;
      var data = [{
        marker: {
          color: '#006AB2',
          opacity: 0.7
        }}];
      var layout = {
        xaxis: {
          type: 'category',
          autotick: true,
          showticklabels: true,
          tickangle: 45,
          tickcolor: '#000'
        },
        yaxis: {
          tickangle: 45,
        }
      };
      var drawDiagram = function() {
        $timeout(function() {
          scope.isNotLoaded = false;
          Plotly.newPlot('diagram', data, layout);
        }, 1000);
      };
      function createString(object) {
        if (object === null) {
          return '';
        }
        return String(object);
      }
      function truncate(str, length) {
        str = createString(str);
        return str.length > length ? str.slice(0, length) + '...' : str;
      }
      if ((scope.type === 'nominal') || (scope.type === 'ordinal')) {
        data[0].x = [];
        data[0].text = [];
        data[0].y = [];
        data[0].type = 'bar';
        if (scope.statistics.validResponses) {
          scope.statistics.validResponses.forEach(function(obj) {
            var tempValue = truncate(obj.label[scope.language], 10);
            data[0].x.push(tempValue);
            data[0].text.push(obj.label[scope.language]);
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
