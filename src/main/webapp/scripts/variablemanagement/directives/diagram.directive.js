/* global Plotly*/
/* global  document */
/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('diagram',
  function($window, $timeout, $filter) {
    var link = function(scope, element) {
      scope.isNotLoaded = true;
      var data = [{
        marker: {
          color: '#006AB2'
        }
      }];

      var drawDiagram = function() {
        $timeout(function() {
          var layout = {
            margin: {
              l: 35,
              r: 35,
              t: 15,
              b: 30
            },
            width: document.getElementById('diagramContainer').offsetWidth -
              10
          };
          if (scope.language === 'de') {
            layout.separators = ',.';
          }
          scope.isNotLoaded = false;
          if (angular.element('diagram').length > 0) {
            Plotly.newPlot('diagram', data, _.cloneDeep(layout), {
              showLink: false,
              displayModeBar: true,
              modeBarButtonsToRemove: ['hoverClosestCartesian',
                'hoverCompareCartesian'
              ]
            });
          }
        }, 1000);
      };
      switch (scope.type) {
        case 'verhältnis':
        case 'ratio':
          if (scope.distribution.histogram) {
            var size = (scope.distribution.histogram.end -
              scope.distribution.histogram.start) /
              scope.distribution.histogram.numberOfBins;
            data[0].x = [];
            data[0].type = 'histogram';
            data[0].autobinx = false;
            data[0].xbins = {
              start: scope.distribution.histogram.start,
              end: scope.distribution.histogram.end,
              size: size
            };
            data[0].hoverinfo = 'text+y';
            data[0].text = [];
            for (var i = 0; i < scope.distribution.histogram.numberOfBins;
              i++) {
              data[0].text.push('[' + $filter('number')(scope.distribution
                .histogram.start + size * i, 2) + '; ' +
                $filter('number')(scope.distribution.histogram.start +
                  size * (i + 1), 2) + ')');
            }
            if (scope.distribution.validResponses) {
              for (var j = 0; j < scope.distribution.validResponses.length;
                j++) {
                for (var k = 0; k < scope.distribution.validResponses[j]
                  .absoluteFrequency; k++) {
                  data[0].x.push(scope.distribution.validResponses[j].value);
                }
              }
            }
          }
        break;
        default:
          data[0].x = [];
          data[0].text = [];
          data[0].y = [];
          data[0].type = 'bar';
          if (scope.distribution.validResponses) {
            scope.distribution.validResponses.forEach(function(obj) {
              var filteredText = $filter('variableDataType')(obj.value,
                scope.datatype);
              if (scope.datatype !== 'numeric') {
                data[0].x.push(filteredText);
              } else {
                data[0].x.push(obj.value);
              }
              try {
                data[0].text.push(obj.label[scope.language]);
              } catch (e) {
                data[0].text.push(filteredText);
              }
              data[0].y.push(obj.absoluteFrequency);
            });
          }
        break;
      }
      angular.element($window).on('resize', function() {
        $timeout(function() {
          var update = {
            width: document.getElementById('diagramContainer').offsetWidth
          };
          Plotly.relayout('diagram', update);
        }, 1000);
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
        distribution: '=',
        language: '=',
        type: '=',
        datatype: '='
      }
    };
  });
