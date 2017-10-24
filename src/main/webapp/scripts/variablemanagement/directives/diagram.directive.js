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
              l: 35,
              r: 35,
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
                layout.xaxis.tickformat = '.2f';
              } else {
                layout.xaxis.tickformat = ',.2f';
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
        }, 1000);
      };
      switch (scope.variable.scaleLevel.en) {
        case 'verhältnis':
        case 'ratio':
          if (scope.variable.distribution.histogram) {
            var size = Math.abs(scope.variable.distribution.histogram.end -
              scope.variable.distribution.histogram.start) /
              scope.variable.distribution.histogram.numberOfBins;
            data[0].x = [];
            data[0].type = 'histogram';
            data[0].autobinx = false;
            data[0].xbins = {
              start: scope.variable.distribution.histogram.start,
              end: scope.variable.distribution.histogram.end,
              size: size
            };
            data[0].hoverinfo = 'text+y';
            data[0].text = [];
            var relativeFrequencies = new Array(
              scope.variable.distribution.histogram.numberOfBins);
            if (scope.variable.distribution.validResponses) {
              for (var j = 0;
                j < scope.variable.distribution.validResponses.length;
                j++) {
                var binIndex =
                  Math.floor(
                    (scope.variable.distribution.validResponses[j].value -
                    scope.variable.distribution.histogram.start) / size);
                relativeFrequencies[binIndex] =
                  relativeFrequencies[binIndex] ?
                  relativeFrequencies[binIndex] + scope.variable.distribution
                    .validResponses[j].validRelativeFrequency :
                  scope.variable.distribution
                      .validResponses[j].validRelativeFrequency;
                for (var k = 0;
                  k < scope.variable.distribution.validResponses[j]
                  .absoluteFrequency; k++) {
                  data[0].x.push(
                    scope.variable.distribution.validResponses[j].value);
                }
              }
              for (var i = 0; i < relativeFrequencies.length; i++) {
                var left =
                  scope.variable.distribution.histogram.start + size * i;
                var right = scope.variable.distribution.histogram.start +
                  size * (i + 1);
                var relativeFrequency =
                    $filter('number')(relativeFrequencies[i] || 0, 2) + ' %';
                data[0].text.push(relativeFrequency +
                  '<br>[' + $filter('number')(left, 2) +
                  '; ' +
                  $filter('number')(right, 2) + ')');
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
              if (obj.label[scope.language]) {
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
        language: '=',
        variable: '='
      }
    };
  });
