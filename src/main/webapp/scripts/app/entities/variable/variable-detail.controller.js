/* global d3 */
/* @Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp')
  .controller('VariableDetailController', ['$scope', 'entity', '$translate',
    function($scope, entity, $translate) {
      $scope.variable = entity;

      var usedLanguage = $translate.use();

      //options for charts
      $scope.options = {
        chart: {

          //CSS information
          type: 'discreteBarChart',
          height: 350,
          showValues: true,
          valueFormat: function(d) {
            return d3.format(',.2f')(d); //.2f means two numbers after a commata
          },
          transitionDuration: 500,

          //Point for data extraction to visualisation
          x: function(d) {

            //if en, return en lables
            if (usedLanguage === 'en') {
              return d.label.en;
            }

            //de is default. return de labels.
            return d.label.de;
          },
          y: function(d) {
            return d.absoluteFrequency;
          }
        },

        // title options
        title: {
          enable: true,
          text: $scope.variable.name
        },

        // subtitle options
        subtitle: {
          enable: true,
          text: 'Absolute Häufigkeit von Werten',
          css: {
            'text-align': 'center',
            'margin': '10px 13px 0px 7px'
          }
        },

        // caption options
        caption: {
          enable: true,
          html: 'Figure 1: ' + $scope.variable.name +
            ', Absolute Häufigkeit von Werten',
          css: {
            'text-align': 'center',
            'margin': '-30px 0px 0px 0px'
          }
        }
      };

      //The data object is for the display
      $scope.data = [{
        values: []
      }];

      //TODO problem after language change ... no direct set...
      console.log('Test');
      console.log($scope);
      $scope.data[0].values = $scope.variable.values;
      console.log($scope);
    }
  ]);
