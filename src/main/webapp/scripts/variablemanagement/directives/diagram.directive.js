/* global  document */
/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('diagram',
  function($window, $timeout, $filter, Plotly) {
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
              l: 45,
              r: 30,
              t: 30,
              b: 30
            },
            width: document.getElementById('diagramContainer').offsetWidth -
              10,
            yaxis: {
              tickformat: ',d'
            },
            xaxis: {
            }
          };
          if (scope.variable.dataType.en === 'numeric') {
            if (scope.variable.storageType === 'integer') {
              if (scope.variable.doNotDisplayThousandsSeparator) {
                layout.xaxis.tickformat = 'd';
              } else {
                layout.xaxis.tickformat = ',d';
              }
            } else {
              if (scope.variable.doNotDisplayThousandsSeparator) {
                layout.xaxis.tickformat = '.' +
                  scope.variable.distribution.maxNumberOfDecimalPlaces + 'f';
              } else {
                layout.xaxis.tickformat = ',.' +
                  scope.variable.distribution.maxNumberOfDecimalPlaces + 'f';
              }
            }
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
        });
      };
      switch (scope.variable.scaleLevel.en) {
        case 'ratio':
          data[0].x = [];
          data[0].type = 'histogram';
          data[0].autobinx = true;
          data[0].hoverinfo = 'x+y';
          if (scope.variable.distribution.validResponses) {
            for (var j = 0;
              j < scope.variable.distribution.validResponses.length;
              j++) {
              for (var k = 0;
                k < scope.variable.distribution.validResponses[j]
                .absoluteFrequency; k++) {
                data[0].x.push(
                  scope.variable.distribution.validResponses[j].value);
              }
            }
          }
          break;
        default:
          data[0].x = [];
          data[0].text = [];
          data[0].y = [];
          data[0].type = 'bar';
          if (scope.variable.distribution.validResponses) {
            scope.variable.distribution.validResponses.forEach(function(obj) {
              var filteredText = $filter('formatResponse')(obj.value,
                scope.variable);
              var relativeFrequency =
                $filter('number')(obj.validRelativeFrequency, 2) + ' %';
              if (scope.variable.dataType.en !== 'numeric') {
                data[0].x.push(filteredText);
              } else {
                data[0].x.push(obj.value);
              }
              if (obj.label && obj.label[scope.language]) {
                data[0].text.push(relativeFrequency +
                  '<br>' + obj.label[scope.language]);
              } else {
                data[0].text.push(relativeFrequency +
                  '<br>' + filteredText);
              }
              data[0].y.push(obj.absoluteFrequency);
            });
          }
          break;
      }

      function resizeDiagram() {
        $timeout(function() {
          var update = {
            width: document.getElementById('diagramContainer').offsetWidth
          };
          Plotly.relayout('diagram', update);
        }, 500);
      }

      angular.element($window).on('resize', resizeDiagram);
      element.on('$destroy', function() {
        angular.element($window).off('resize', resizeDiagram);
      });
      drawDiagram();
    };
    return {
      restrict: 'E',
      templateUrl: 'scripts/variablemanagement/directives/diagram.html.tmpl',
      link: link,
      scope: {
        language: '=',
        variable: '='
      }
    };
  });
