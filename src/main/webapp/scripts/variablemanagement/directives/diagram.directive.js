/* global Plotly */
'use strict';

angular.module('metadatamanagementApp').directive('diagram',
  function($window, $timeout) {
    var link = function(scope, element) {
      scope.isNotLoaded = true;
      scope.isNotAvailable = false;
      var data = [{
        marker: {
          color: '#006AB2',
          opacity: 0.7
        }}];
      var drawDiagram = function() {
        $timeout(function() {
          scope.isNotLoaded = false;
          if (angular.element('diagram').length > 0) {
            Plotly.newPlot('diagram', data);
          }
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
            var tempValue =
              truncate(' - ' + (obj.label[scope.language] || obj.value) + ' - ',
              11);
            data[0].x.push(tempValue);
            data[0].text.push((obj.label[scope.language] || obj.value));
            data[0].y.push(obj.absoluteFrequency);
          });
        } else {
          scope.isNotAvailable = true;
        }
      }
      if ((scope.type === 'kontinuierlich') || (scope.type === 'continous')) {
        var size = ((parseInt(scope.statistics.histogram.end) -
        parseInt(scope.statistics.histogram.start)) / parseInt(scope.statistics
          .histogram.numberOfBins));
        data[0].x = [];
        data[0].type = 'histogram';
        data[0].autobinx = false;
        data[0].xbins = {
          start: scope.statistics.histogram.start,
          end: scope.statistics.histogram.end,
          size: size
        };
        if (scope.statistics.validResponses) {
          for (var j = 0; j < scope.statistics.validResponses.length; j++) {
            for (var k = 0; k < scope.statistics.validResponses[j]
              .absoluteFrequency; k++) {
              data[0].x.push(scope.statistics.validResponses[j].value);
            }
          }
        } else {
          scope.isNotAvailable = true;
        }
      }
      angular.element($window).on('resize', function() {
        drawDiagram();
      });
      element.on('$destroy', function() {
        angular.element($window).off('resize');
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
